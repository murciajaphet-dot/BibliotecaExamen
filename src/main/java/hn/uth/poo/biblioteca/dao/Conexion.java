package hn.uth.poo.biblioteca.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexion {
    private static final String URL = "jdbc:sqlite:biblioteca.db";

    public static Connection getConexion() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
            // Crear tablas automáticamente si no existen para evitar errores al insertar
            stmt.execute("CREATE TABLE IF NOT EXISTS autores (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "nombre TEXT NOT NULL, " +
                         "nacionalidad TEXT);");
            stmt.execute("CREATE TABLE IF NOT EXISTS libros (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         "isbn TEXT NOT NULL UNIQUE, " +
                         "titulo TEXT NOT NULL, " +
                         "anio INTEGER, " +
                         "precio REAL NOT NULL, " +
                         "autor_id INTEGER NOT NULL, " +
                         "FOREIGN KEY (autor_id) REFERENCES autores(id));");
        }
        return conn;
    }
}