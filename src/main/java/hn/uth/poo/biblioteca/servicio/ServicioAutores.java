package hn.uth.poo.biblioteca.servicio;

import hn.uth.poo.biblioteca.dao.AutorDAO;
import hn.uth.poo.biblioteca.dao.LibroDAO;
import hn.uth.poo.biblioteca.excepciones.DatoInvalidoException;
import hn.uth.poo.biblioteca.modelo.Autor;
import java.util.List;

public class ServicioAutores {
    private final AutorDAO autorDAO;
    private final LibroDAO libroDAO;

    public ServicioAutores(AutorDAO autorDAO, LibroDAO libroDAO) {
        this.autorDAO = autorDAO;
        this.libroDAO = libroDAO;
    }

    public void registrar(Autor autor) {
        if (autor.getNombre() == null || autor.getNombre().trim().isEmpty()) {
            throw new DatoInvalidoException("El nombre del autor es obligatorio.");
        }
        autorDAO.insertar(autor);
    }

    public List<Autor> obtenerTodos() {
        return autorDAO.listar();
    }

    public Autor buscarPorId(int id) {
        return autorDAO.buscarPorId(id);
    }

    public void actualizar(Autor autor) {
        if (autor.getNombre() == null || autor.getNombre().trim().isEmpty()) {
            throw new DatoInvalidoException("El nombre del autor es obligatorio.");
        }
        autorDAO.actualizar(autor);
    }

    public void eliminar(int id) {
        List<hn.uth.poo.biblioteca.modelo.Libro> libros = libroDAO.listarPorAutor(id);
        if (!libros.isEmpty()) {
            throw new DatoInvalidoException("No se puede eliminar un autor que tiene libros registrados.");
        }
        autorDAO.eliminar(id);
    }
}