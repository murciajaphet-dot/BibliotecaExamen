package hn.uth.poo.biblioteca.servicio;

import hn.uth.poo.biblioteca.dao.AutorDAO;
import hn.uth.poo.biblioteca.dao.LibroDAO;
import hn.uth.poo.biblioteca.excepciones.DatoInvalidoException;
import hn.uth.poo.biblioteca.modelo.Libro;
import java.time.LocalDate;
import java.util.List;

public class ServicioLibros {
    private final LibroDAO libroDAO;
    private final AutorDAO autorDAO;

    public ServicioLibros(LibroDAO libroDAO, AutorDAO autorDAO) {
        this.libroDAO = libroDAO;
        this.autorDAO = autorDAO;
    }

    public void registrar(Libro libro) {
        if (libro.getTitulo() == null || libro.getTitulo().trim().isEmpty()) {
            throw new DatoInvalidoException("El título del libro es obligatorio.");
        }
        if (libro.getIsbn() == null || libro.getIsbn().trim().isEmpty()) {
            throw new DatoInvalidoException("El ISBN es obligatorio.");
        }
        for (Libro l : libroDAO.listar()) {
            if (l.getIsbn().equalsIgnoreCase(libro.getIsbn())) {
                throw new DatoInvalidoException("El ISBN ya está registrado.");
            }
        }
        int anioActual = LocalDate.now().getYear();
        if (libro.getAnio() < 1450 || libro.getAnio() > anioActual) {
            throw new DatoInvalidoException("El año debe estar entre 1450 y " + anioActual + ".");
        }
        if (libro.getPrecio() < 0) {
            throw new DatoInvalidoException("El precio no puede ser negativo.");
        }
        if (autorDAO.buscarPorId(libro.getAutorId()) == null) {
            throw new DatoInvalidoException("El autor especificado no existe.");
        }
        libroDAO.insertar(libro);
    }

    public List<Libro> obtenerTodos() {
        return libroDAO.listar();
    }

    public List<Libro> obtenerPorAutor(int autorId) {
        return libroDAO.listarPorAutor(autorId);
    }

    public void actualizar(Libro libro) {
        if (libro.getTitulo() == null || libro.getTitulo().trim().isEmpty()) {
            throw new DatoInvalidoException("El título del libro es obligatorio.");
        }
        int anioActual = LocalDate.now().getYear();
        if (libro.getAnio() < 1450 || libro.getAnio() > anioActual) {
            throw new DatoInvalidoException("El año debe estar entre 1450 y " + anioActual + ".");
        }
        if (libro.getPrecio() < 0) {
            throw new DatoInvalidoException("El precio no puede ser negativo.");
        }
        libroDAO.actualizar(libro);
    }

    public void eliminar(int id) {
        libroDAO.eliminar(id);
    }
}