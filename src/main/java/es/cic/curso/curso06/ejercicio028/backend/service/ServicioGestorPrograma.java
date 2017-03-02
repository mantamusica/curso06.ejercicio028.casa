package es.cic.curso.curso06.ejercicio028.backend.service;

import java.util.List;

import es.cic.curso.curso06.ejercicio028.backend.dominio.Categoria;
import es.cic.curso.curso06.ejercicio028.backend.dominio.Genero;
import es.cic.curso.curso06.ejercicio028.backend.dominio.Programa;

public interface ServicioGestorPrograma {

	Categoria aniadirCategoria(Categoria categoria);

	List<Categoria> listarCategoria();

	Categoria obtenerCategoria(Long id);

	void borrarCategoria(Long id);

	Categoria modificarCategoria(Categoria categoria);
	
	Genero aniadirGenero(Genero genero);

	List<Genero> listarGenero();

	Genero obtenerGenero(Long id);

	void borrarGenero(Long id);

	Genero modificarGenero(Genero genero);
	
	Programa aniadirPrograma(Programa programa);

	List<Programa> listarPrograma();

	Programa obtenerPrograma(Long id);

	void borrarPrograma(Long id);

	Programa modificarPrograma(Programa programa);

	

}