package es.cic.curso.curso06.ejercicio028.frontend.principal;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.web.context.ContextLoader;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinService;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import es.cic.curso.curso06.ejercicio028.backend.dominio.Categoria;
import es.cic.curso.curso06.ejercicio028.backend.dominio.Genero;
import es.cic.curso.curso06.ejercicio028.backend.dominio.Programa;
import es.cic.curso.curso06.ejercicio028.backend.service.ServicioGestorPrograma;




public class VistaCanales extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7445902879676064023L;

	private TextField buscador;
	private TextField nombre, duracion, anio;
	private Label label;
	private Grid gridProgramas;
	private Button crear, borrar, actualizar, aceptar, cancelar;
	private ComboBox genero, categoria;
	private List<String> lisCategorias = new ArrayList<>();
	List <Genero> listaGeneros = new ArrayList<>();
	List <Categoria> listaCategorias = new ArrayList<>();
	private List<String> lisGeneros = new ArrayList<>();
	private Programa programa, programaSeleccionado;
	private ServicioGestorPrograma servicioGestorPrograma;
	private Collection<Programa> listaProgramas;
	private Genero generoElegido;
	private Categoria categoriaElegida;
	
	
	@SuppressWarnings("serial")
	public VistaCanales(){
		
		programa = new Programa();
		
		servicioGestorPrograma = ContextLoader.getCurrentWebApplicationContext().getBean(ServicioGestorPrograma.class);
		

		HorizontalLayout layoutEncabezado = inicializaLayoutEncabezado();
		
		HorizontalLayout layoutUno = label_buscador();
		
		HorizontalLayout layoutDos = layoutDos();
		
		HorizontalLayout layoutTres = layoutTres();

		addComponents(layoutEncabezado, layoutUno, layoutDos, layoutTres);
		
		cargaGrid();
		
	}


	private HorizontalLayout inicializaLayoutEncabezado() {
		String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		FileResource resource = new FileResource(new File(basepath + "/WEB-INF/images/cic_logo.png"));
		Image imagen = new Image(null, resource);
		imagen.setHeight(60.0F, Unit.PIXELS);
		
		Label titulo = new Label("<span style=\"font-size: 175%;\">Programación Televisiva</span>");
		titulo.setContentMode(ContentMode.HTML);

		HorizontalLayout layoutEncabezado = new HorizontalLayout();
		layoutEncabezado.setMargin(new MarginInfo(true, true, true, true));
		layoutEncabezado.setSpacing(false);
		layoutEncabezado.addComponents(imagen, titulo);
		layoutEncabezado.setComponentAlignment(titulo, Alignment.MIDDLE_LEFT);
		return layoutEncabezado;
	}

	private HorizontalLayout layoutTres() {
		HorizontalLayout layoutTres = new HorizontalLayout();
		layoutTres.setMargin(true);
		layoutTres.setSpacing(true);
		crear = new Button("Crear");
		crear.setVisible(true);
		crear.setEnabled(true);
		crear.setIcon(FontAwesome.PLUS);
		crear.addClickListener(c-> {
			crearPrograma();
			
		});
		
		borrar = new Button("Borrar");
		borrar.setVisible(true);
		borrar.setEnabled(false);
		borrar.setIcon(FontAwesome.ERASER);
		borrar.addClickListener(b -> this.getUI().getUI()
				.addWindow(creaVentanaConfirmacionBorradoProgramas(programaSeleccionado.getNombre())));
		
		actualizar = new Button("Actualizar");
		actualizar.setVisible(true);
		actualizar.setEnabled(false);
		actualizar.setIcon(FontAwesome.REFRESH);
		actualizar.addClickListener(a -> {
			nombre.setValue(programaSeleccionado.getNombre());
			duracion.setValue(String.valueOf(programaSeleccionado.getDuracion()));
			anio.setValue(String.valueOf(programaSeleccionado.getAnio()));
			categoria.setValue(programaSeleccionado.getCategoria());
			//genero.setValue(programaSeleccionado.getGenero());
			activarMenu();
		});
		
		layoutTres.addComponents(crear, borrar, actualizar);
		return layoutTres;
	}

	private HorizontalLayout layoutDos() {
		HorizontalLayout layoutDos = new HorizontalLayout();
		layoutDos.setMargin(true);
		layoutDos.setSpacing(true);
		VerticalLayout grid = new VerticalLayout();
		gridProgramas = new Grid();
		gridProgramas.setVisible(true);
		gridProgramas.setColumns("nombre", "duracion", "anio", "categoria" ,"genero");
		gridProgramas.setSizeFull();
		gridProgramas.setSelectionMode(SelectionMode.SINGLE);	
		gridProgramas.addSelectionListener(e -> {
			programaSeleccionado = null;
			if (!e.getSelected().isEmpty()) {
				programaSeleccionado = (Programa) e.getSelected().iterator().next();
				crear.setEnabled(false);
				borrar.setEnabled(true);
				actualizar.setEnabled(true);
				} else {
					crear.setEnabled(true);
					borrar.setEnabled(false);
					actualizar.setEnabled(false);
				}
		});
		grid.addComponent(gridProgramas);
		

		
		VerticalLayout menu = new VerticalLayout();
		menu.setMargin(true);
		menu.setSpacing(true);
		
			nombre = new TextField("Nombre");
			nombre.setInputPrompt("Nombre");
			nombre.setVisible(true);
			nombre.setEnabled(false);
			duracion = new TextField("Duración");
			duracion.setInputPrompt("Duración");
			duracion.setVisible(true);
			duracion.setEnabled(false);
			anio = new TextField("Año");
			anio.setInputPrompt("Año");
			anio.setVisible(true);
			anio.setEnabled(false);
			

		HorizontalLayout ok = new HorizontalLayout();
		ok.setSpacing(true);
		
		aceptar = new Button("Aceptar");
		aceptar.setVisible(true);
		aceptar.setEnabled(false);
		aceptar.setIcon(FontAwesome.CHECK);
		aceptar.addClickListener(e -> {
			if ("".equals(nombre.getValue()) || "".equals(duracion.getValue()) || "".equals(anio.getValue())|| "".equals(categoria.getValue()) || "".equals(genero.getValue())) {
				Notification.show("Debes rellenar todos los campos.");
			} else {
				try{
					//Notification.show(genero.getValue().toString());
//					generoElegido = servicioGestorPrograma.obtenerGenero((Long)genero.getValue());
//					Notification.show(generoElegido.getNombre());
					Programa nuevoPrograma = new Programa(nombre.getValue(), Integer.parseInt(duracion.getValue()), 
							Integer.parseInt(anio.getValue()), categoriaElegida, generoElegido);
					if (programaSeleccionado.getId() > 0) {
						programaSeleccionado.setNombre(nombre.getValue());
						programaSeleccionado.setDuracion(Integer.parseInt(duracion.getValue()));
						programaSeleccionado.setAnio(Integer.parseInt(anio.getValue()));
						//programaSeleccionado.setCategoria(categoriaElegida);
						Notification.show("aDSDSAFDGh fh"+ genero.getValue().toString());
						generoElegido = servicioGestorPrograma.obtenerGenero(Long.parseLong(genero.getValue().toString()));
						programaSeleccionado.setGenero(generoElegido);
						servicioGestorPrograma.modificarPrograma(programaSeleccionado);
						//Notification.show("Programa \"" + nuevoPrograma.getNombre() + "\" editado con éxito.");
					} else {
						servicioGestorPrograma.aniadirPrograma(nuevoPrograma);
					}
					cargaGrid();
					menu.setEnabled(false);
					crear.setEnabled(true);
					limpiarMenu();
					//Notification.show("Programa \"" + nuevoPrograma.getNombre() + "\" añadido con éxito.");
					//
					
				
				}catch(NumberFormatException ex){
					Notification.show("En numeros pon numeros");
				
					
				
				}	
			}
		});
		
		cancelar = new Button("Cancelar");
		cancelar.setIcon(FontAwesome.CLOSE);
		cancelar.setEnabled(false);
		cancelar.addClickListener(e-> {
			menu.setEnabled(false);
			cargaGrid();
			
		});
		categoria = new ComboBox();
		genero = new ComboBox();
		actualizarGenero();
		ok.addComponents(aceptar, cancelar);
		menu.addComponents(nombre, duracion, anio, genero, categoria, ok);

		layoutDos.addComponents(grid, menu);
		return layoutDos;
	}
	private void limpiarMenu() {
		
					duracion.clear();
					nombre.clear();
					anio.clear();
					genero.clear();
					categoria.clear();
	}

	private void activarMenu(){
		
		duracion.setEnabled(true);
		nombre.setEnabled(true);
		anio.setEnabled(true);
		genero.setEnabled(true);
		categoria.setEnabled(true);
		aceptar.setEnabled(true);
		cancelar.setEnabled(true);
		
	}

	private void desactivarMenu(){
		
		nombre.setEnabled(false);
		duracion.setEnabled(false);
		anio.setEnabled(false);
		genero.setEnabled(false);
		categoria.setEnabled(false);
		aceptar.setEnabled(false);
		cancelar.setEnabled(false);
	}
	
	private void actualizarCategoria() {
		
		List <Categoria> listaCategorias = new ArrayList<>();
		
		listaCategorias = servicioGestorPrograma.listarCategoria();
		
		categoria = new ComboBox("Categoría");
		categoria.setInputPrompt("Categoría");
		categoria.setNullSelectionAllowed(false);
		for(int i = 0; i < listaCategorias.size(); i++){
			categoria.addItem(listaCategorias.get(i).getNombre());
		}
		categoria.select(1);
		categoria.setImmediate(true);
		categoria.setVisible(true);
		categoria.setEnabled(false);
		
	}


	private void actualizarGenero() {
		
		List <Genero> listaGeneros = new ArrayList<>();
		
		listaGeneros = servicioGestorPrograma.listarGenero();
		
	//	genero.removeAllItems();
		genero.setInputPrompt("Género");
		genero.setNullSelectionAllowed(false);
		for(int i = 0; i < listaGeneros.size(); i++){
			genero.addItem(listaGeneros.get(i).getId());
			genero.setItemCaption(listaGeneros.get(i).getId(), listaGeneros.get(i).getNombre());
		}
		genero.select(1);
		genero.setImmediate(true);
		genero.setVisible(true);
		
		//genero.setEnabled(false);
		

		Notification.show(Integer.toString(listaGeneros.size()));

		
	}

	public void crearPrograma() {

		nombre.setEnabled(true);
		duracion.setEnabled(true);
		anio.setEnabled(true);
		genero.setEnabled(true);
		categoria.setEnabled(true);
		aceptar.setEnabled(true);
		cancelar.setEnabled(true);
		crear.setEnabled(false);
		borrar.setEnabled(false);
		actualizar.setEnabled(false);
		actualizarGenero();
		programaSeleccionado = new Programa();
		programaSeleccionado.setId((long) 0);

	}
	
	public void enter(ViewChangeEvent event) {
		cargaGrid();
	}

	public void cargaGrid() {
		
		Collection<Programa> listaProgramas = servicioGestorPrograma.listarPrograma();
		gridProgramas.setContainerDataSource(new BeanItemContainer<>(Programa.class, listaProgramas));
		crear.setEnabled(true);

	}
	
	private HorizontalLayout label_buscador() {
		HorizontalLayout label_buscador = new HorizontalLayout();
		label_buscador.setMargin(true);
		label = new Label("Lista de Canales");
		label.setVisible(true);
		buscador = new TextField();
		buscador.setInputPrompt("Buscador");
		label_buscador.addComponents(label, buscador);
		label_buscador.setWidth(100.0F, Unit.PERCENTAGE);
		label_buscador.setComponentAlignment(buscador, Alignment.TOP_RIGHT);
		return label_buscador;
	}

	private void recorrerGeneros() {
		for(Genero gen :listaGeneros){
			if(genero.getValue() == gen.getNombre()){
				generoElegido.setNombre(gen.getNombre());
				
			}			
		}
	}
	
	private void recorrerCategorias() {
		for(Categoria cat :listaCategorias){
			if(categoria.getValue() == cat.getNombre()){
				categoriaElegida.setNombre(cat.getNombre());
				
			}			
		}
	}
	
	public void setPrograma(Programa programa) {
		this.setVisible(programa != null);
		this.programa = programa;

		if (programa != null) {
			BeanFieldGroup.bindFieldsUnbuffered(programa, this);
		} else {
			BeanFieldGroup.bindFieldsUnbuffered(new Programa(), this);
		}
	}

	private Window creaVentanaConfirmacionBorradoProgramas(String nombre) {
		Window resultado = new Window();
		resultado.setWidth(350.0F, Unit.PIXELS);
		resultado.setModal(true);
		resultado.setClosable(false);
		resultado.setResizable(false);
		resultado.setDraggable(false);

		Label label = new Label("¿Está seguro de que desea borrar este Programa: <strong>\"" + nombre + "\"</strong>?");
		label.setContentMode(ContentMode.HTML);

		Button botonAceptar = new Button("Aceptar");
		botonAceptar.addClickListener(e -> {
			servicioGestorPrograma.borrarPrograma(programaSeleccionado.getId());
			cargaGrid();
			resultado.close();
		});

		Button botonCancelar = new Button("Cancelar");
		botonCancelar.addClickListener(e -> resultado.close());

		HorizontalLayout layoutBotones = new HorizontalLayout();
		layoutBotones.setMargin(true);
		layoutBotones.setSpacing(true);
		layoutBotones.setWidth(100.0F, Unit.PERCENTAGE);
		layoutBotones.addComponents(botonAceptar, botonCancelar);

		final FormLayout content = new FormLayout();
		content.setMargin(true);
		content.addComponents(label, layoutBotones);
		resultado.setContent(content);
		resultado.center();
		return resultado;
	
	}

	public Programa getProgramaSeleccionado() {
		return programaSeleccionado;
	}


	public void setProgramaSeleccionado(Programa programaSeleccionado) {
		this.programaSeleccionado = programaSeleccionado;
	}
	
}
