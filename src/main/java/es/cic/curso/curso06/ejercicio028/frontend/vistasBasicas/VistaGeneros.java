package es.cic.curso.curso06.ejercicio028.frontend.vistasBasicas;

import java.io.File;
import java.util.Collection;

import org.springframework.web.context.ContextLoader;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import es.cic.curso.curso06.ejercicio028.backend.dominio.Genero;
import es.cic.curso.curso06.ejercicio028.backend.service.ServicioGestorPrograma;

public class VistaGeneros extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5366004381410718812L;

	private TextField nombre, descripcion;
	private Label label;
	private Grid gridGeneros;
	private Button crear, borrar, actualizar, aceptar, cancelar;
	private Genero nuevoGenero, generoSeleccionado;
	private ServicioGestorPrograma servicioGestorPrograma;
	private Collection<Genero> listaGeneros;

	@SuppressWarnings("serial")
	public VistaGeneros() {

		servicioGestorPrograma = ContextLoader.getCurrentWebApplicationContext().getBean(ServicioGestorPrograma.class);
		nuevoGenero = new Genero();

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
		layoutEncabezado.setMargin(new MarginInfo(true, true, false, true));
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
		crear.addClickListener(e -> {
			crearGenero();
		});
		borrar = new Button("Borrar");
		borrar.setVisible(true);
		borrar.setEnabled(false);
		borrar.setIcon(FontAwesome.ERASER);
		borrar.addClickListener(e -> this.getUI().getUI()
				.addWindow(creaVentanaConfirmacionBorradoGeneros(generoSeleccionado.getNombre())));
				

		actualizar = new Button("Actualizar");
		actualizar.setVisible(true);
		actualizar.setEnabled(false);
		actualizar.setIcon(FontAwesome.REFRESH);
		actualizar.addClickListener(e -> {
			nombre.setValue(generoSeleccionado.getNombre());
			descripcion.setValue(generoSeleccionado.getDescripcion());
			nombre.setEnabled(true);
			descripcion.setEnabled(true);
			aceptar.setEnabled(true);
			cancelar.setEnabled(true);
			crear.setEnabled(false);
			borrar.setEnabled(false);
			actualizar.setEnabled(false);
		});
		
		layoutTres.addComponents(crear, borrar, actualizar);
		return layoutTres;
	}

	private HorizontalLayout layoutDos() {
		HorizontalLayout layoutDos = new HorizontalLayout();
		layoutDos.setMargin(true);
		layoutDos.setSpacing(true);
		VerticalLayout grid = new VerticalLayout();
		grid.setSpacing(true);

		gridGeneros = new Grid();
		gridGeneros.setVisible(true);
		gridGeneros.setColumns("nombre", "descripcion");
		gridGeneros.setSizeFull();
		gridGeneros.setSelectionMode(SelectionMode.SINGLE);
		gridGeneros.addSelectionListener(e -> {
			generoSeleccionado = null;
			if (!e.getSelected().isEmpty()) {
				generoSeleccionado = (Genero) e.getSelected().iterator().next();
				System.out.println(generoSeleccionado.getId());
				borrar.setEnabled(true);
				actualizar.setEnabled(true);
			} else {
				ocultarElementos();

			}
		});

		grid.addComponent(gridGeneros);
		VerticalLayout menu = new VerticalLayout();
		menu.setMargin(true);
		menu.setSpacing(true);
		nombre = new TextField("Nombre");
		nombre.setInputPrompt("Nombre");
		nombre.setNullRepresentation("");
		nombre.setNullSettingAllowed(false);
		nombre.setRequired(true);
		nombre.setRequiredError("Debes introducir un nombre.");
		nombre.setWidth(250.0F, Unit.PIXELS);
		nombre.setVisible(true);
		nombre.setEnabled(false);
		descripcion = new TextField("Descripción");
		descripcion.setInputPrompt("Descripción");
		descripcion.setNullRepresentation("");
		descripcion.setNullSettingAllowed(false);
		descripcion.setRequired(true);
		descripcion.setRequiredError("Debes introducir una descripción.");
		descripcion.setWidth(250.0F, Unit.PIXELS);
		descripcion.setVisible(true);
		descripcion.setEnabled(false);

		HorizontalLayout ok = new HorizontalLayout();
		ok.setSpacing(true);
		aceptar = new Button("Aceptar");
		aceptar.setVisible(true);
		aceptar.setEnabled(false);
		aceptar.setIcon(FontAwesome.CHECK);
		aceptar.addClickListener(e -> {
			if ("".equals(nombre.getValue()) || "".equals(descripcion.getValue())) {
				Notification.show("Debes indicar un nombre y una descripción para crear un Género nuevo.",Type.WARNING_MESSAGE);
			} else {
				Genero nuevoGenero = new Genero(nombre.getValue(), descripcion.getValue());
				if (generoSeleccionado.getId() > 0) {
					generoSeleccionado.setNombre(nombre.getValue());
					generoSeleccionado.setDescripcion(descripcion.getValue());
					servicioGestorPrograma.modificarGenero(generoSeleccionado);
					Notification.show("Género \"" + nuevoGenero.getNombre() + "\" editado con éxito.");
					
				} else {
					servicioGestorPrograma.aniadirGenero(nuevoGenero);
					Notification.show("Género \"" + nuevoGenero.getNombre() + "\" añadido con éxito.");
				}
				cargaGrid();
				ocultarElementos();
				descripcion.clear();
				nombre.clear();
				
			}
		});
		cancelar = new Button("Cancelar");
		cancelar.setIcon(FontAwesome.CLOSE);
		cancelar.addClickListener(e -> {
			descripcion.clear();
			nombre.clear();
			cargaGrid();
			
			

		});

		ok.addComponents(aceptar, cancelar);
		menu.addComponents(nombre, descripcion, ok);

		layoutDos.addComponents(grid, menu);
		return layoutDos;
	}

	public void ocultarElementos() {
		cancelar.setEnabled(false);
		aceptar.setEnabled(false);
		nombre.setEnabled(false);
		descripcion.setEnabled(false);
		crear.setEnabled(true);

	}

	public void crearGenero() {

		nombre.setEnabled(true);
		descripcion.setEnabled(true);
		aceptar.setEnabled(true);
		cancelar.setEnabled(true);
		crear.setEnabled(false);
		borrar.setEnabled(false);
		actualizar.setEnabled(false);
		generoSeleccionado = new Genero();
		generoSeleccionado.setId((long) 0);

	}

	private HorizontalLayout label_buscador() {
		HorizontalLayout label_buscador = new HorizontalLayout();
		label_buscador.setMargin(true);
		label_buscador.setSpacing(true);
		label = new Label("Lista de Generos");
		label.setVisible(true);
		label_buscador.addComponents(label);
		return label_buscador;
	}

	public void enter(ViewChangeEvent event) {
		if (servicioGestorPrograma.listarGenero().isEmpty()) {

			for (int i = 1; i <= 5; i++) {
				Genero genero = new Genero();
				genero.setNombre("Nombre" + i);
				genero.setDescripcion("Descripción" + i);
				servicioGestorPrograma.aniadirGenero(genero);
			}
			Notification.show("Cargados generos de DEMOSTRACIÓN");
		}
		cargaGrid();
	}

	public void cargaGrid() {
		
		Collection<Genero> listaGeneros = servicioGestorPrograma.listarGenero();
		gridGeneros.setContainerDataSource(new BeanItemContainer<>(Genero.class, listaGeneros));
		ocultarElementos();
	}
	
	private Window creaVentanaConfirmacionBorradoGeneros(String nombre) {
		Window resultado = new Window();
		resultado.setWidth(350.0F, Unit.PIXELS);
		resultado.setModal(true);
		resultado.setClosable(false);
		resultado.setResizable(false);
		resultado.setDraggable(false);

		Label label = new Label("¿Está seguro de que desea borrar este Género: <strong>\"" + nombre + "\"</strong>?");
		label.setContentMode(ContentMode.HTML);

		Button botonAceptar = new Button("Aceptar");
		botonAceptar.addClickListener(e -> {
			servicioGestorPrograma.borrarGenero(generoSeleccionado.getId());
			cargaGrid();
			resultado.close();
			borrar.setEnabled(false);
			actualizar.setEnabled(false);
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

	
	public Genero getGeneroSeleccionado() {
		return generoSeleccionado;
	}

	public void setGeneroSeleccionado(Genero generoSeleccionado) {
		this.generoSeleccionado = generoSeleccionado;
	}
}