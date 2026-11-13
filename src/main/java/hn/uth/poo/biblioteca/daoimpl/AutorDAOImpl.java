package hn.uth.poo.biblioteca.daoimpl;

import hn.uth.poo.biblioteca.dao.AutorDAO;
import hn.uth.poo.biblioteca.dao.Conexion;
import hn.uth.poo.biblioteca.excepciones.DatosException;
import hn.uth.poo.biblioteca.modelo.Autor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutorDAOImpl implements AutorDAO {

    private Autor mapear(ResultSet rs) throws SQLException {
        return new Autor(rs.getInt("id"), rs.getString("nombre"), rs.getString("nacionalidad"));
    }

    @Override
    public void insertar(Autor autor) {
        String sql = "INSERT INTO autores(nombre, nacionalidad) VALUES(?, ?)";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, autor.getNombre());
            pstmt.setString(2, autor.getNacionalidad());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    autor.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DatosException("Error al insertar autor", e);
        }
    }

    @Override
    public Autor buscarPorId(int id) {
        String sql = "SELECT * FROM autores WHERE id = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatosException("Error al buscar autor por ID", e);
        }
        return null;
    }

    @Override
    public List<Autor> listar() {
        List<Autor> lista = new ArrayList<>();
        String sql = "SELECT * FROM autores";
        try (Connection conn = Conexion.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new DatosException("Error al listar autores", e);
        }
        return lista;
    }

    @Override
    public void actualizar(Autor autor) {
        String sql = "UPDATE autores SET nombre = ?, nacionalidad = ? WHERE id = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, autor.getNombre());
            pstmt.setString(2, autor.getNacionalidad());
            pstmt.setInt(3, autor.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatosException("Error al actualizar autor", e);
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM autores WHERE id = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatosException("Error al eliminar autor", e);
        }
    }
}