package hn.uth.poo.biblioteca.daoimpl;

import hn.uth.poo.biblioteca.dao.Conexion;
import hn.uth.poo.biblioteca.dao.LibroDAO;
import hn.uth.poo.biblioteca.excepciones.DatosException;
import hn.uth.poo.biblioteca.modelo.Libro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAOImpl implements LibroDAO {

    private Libro mapear(ResultSet rs) throws SQLException {
        return new Libro(
                rs.getInt("id"),
                rs.getString("isbn"),
                rs.getString("titulo"),
                rs.getInt("anio"),
                rs.getDouble("precio"),
                rs.getInt("autor_id")
        );
    }

    @Override
    public void insertar(Libro libro) {
        String sql = "INSERT INTO libros(isbn, titulo, anio, precio, autor_id) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, libro.getIsbn());
            pstmt.setString(2, libro.getTitulo());
            pstmt.setInt(3, libro.getAnio());
            pstmt.setDouble(4, libro.getPrecio());
            pstmt.setInt(5, libro.getAutorId());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    libro.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DatosException("Error al insertar libro", e);
        }
    }

    @Override
    public Libro buscarPorId(int id) {
        String sql = "SELECT * FROM libros WHERE id = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatosException("Error al buscar libro por ID", e);
        }
        return null;
    }

    @Override
    public List<Libro> listar() {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT * FROM libros";
        try (Connection conn = Conexion.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new DatosException("Error al listar libros", e);
        }
        return lista;
    }

    @Override
    public void actualizar(Libro libro) {
        String sql = "UPDATE libros SET isbn = ?, titulo = ?, anio = ?, precio = ?, autor_id = ? WHERE id = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, libro.getIsbn());
            pstmt.setString(2, libro.getTitulo());
            pstmt.setInt(3, libro.getAnio());
            pstmt.setDouble(4, libro.getPrecio());
            pstmt.setInt(5, libro.getAutorId());
            pstmt.setInt(6, libro.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatosException("Error al actualizar libro", e);
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM libros WHERE id = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatosException("Error al eliminar libro", e);
        }
    }

    @Override
    public List<Libro> listarPorAutor(int autorId) {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT * FROM libros WHERE autor_id = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, autorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatosException("Error al listar libros por autor", e);
        }
        return lista;
    }
}