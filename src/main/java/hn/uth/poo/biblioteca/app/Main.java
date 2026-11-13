package hn.uth.poo.biblioteca.app;

import hn.uth.poo.biblioteca.dao.AutorDAO;
import hn.uth.poo.biblioteca.dao.LibroDAO;
import hn.uth.poo.biblioteca.daoimpl.AutorDAOImpl;
import hn.uth.poo.biblioteca.daoimpl.LibroDAOImpl;
import hn.uth.poo.biblioteca.servicio.ServicioAutores;
import hn.uth.poo.biblioteca.servicio.ServicioLibros;
import hn.uth.poo.biblioteca.excepciones.DatoInvalidoException;
import hn.uth.poo.biblioteca.excepciones.DatosException;
import hn.uth.poo.biblioteca.modelo.Autor;
import hn.uth.poo.biblioteca.modelo.Libro;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Main extends JFrame {
    private final ServicioAutores servicioAutores;
    private final ServicioLibros servicioLibros;

    private JTextArea txtAreaLog;
    private JComboBox<Autor> cmbAutoresLibro, cmbAutoresFiltro;
    private JTextField txtNombreAutor, txtNacAutor, txtIdAutorEliminar;
    private JTextField txtIsbnLibro, txtTituloLibro, txtAnioLibro, txtPrecioLibro, txtIdLibroEliminar;

    public Main() {
        AutorDAO autorDAO = new AutorDAOImpl();
        LibroDAO libroDAO = new LibroDAOImpl();
        this.servicioAutores = new ServicioAutores(autorDAO, libroDAO);
        this.servicioLibros = new ServicioLibros(libroDAO, autorDAO);

        setTitle("Sistema de Gestión de Biblioteca - Examen");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        cargarCombos();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // --- PESTAÑA AUTORES ---
        JPanel panelAutores = new JPanel(new GridLayout(1, 2, 10, 10));
        panelAutores.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelFormAutor = new JPanel(new GridLayout(5, 2, 5, 5));
        panelFormAutor.setBorder(BorderFactory.createTitledBorder("Gestión de Autores"));
        txtNombreAutor = new JTextField();
        txtNacAutor = new JTextField();
        txtIdAutorEliminar = new JTextField();

        JButton btnGuardarAutor = new JButton("Guardar Autor");
        btnGuardarAutor.addActionListener(e -> guardarAutor());
        JButton btnListarAutores = new JButton("Listar Autores");
        btnListarAutores.addActionListener(e -> listarAutores());
        JButton btnEliminarAutor = new JButton("Eliminar Autor (por ID)");
        btnEliminarAutor.addActionListener(e -> eliminarAutor());

        panelFormAutor.add(new JLabel("Nombre:")); panelFormAutor.add(txtNombreAutor);
        panelFormAutor.add(new JLabel("Nacionalidad:")); panelFormAutor.add(txtNacAutor);
        panelFormAutor.add(btnGuardarAutor); panelFormAutor.add(btnListarAutores);
        panelFormAutor.add(new JLabel("ID para Eliminar:")); panelFormAutor.add(txtIdAutorEliminar);
        panelFormAutor.add(btnEliminarAutor); panelFormAutor.add(new JLabel(""));

        panelAutores.add(panelFormAutor);

        // --- PESTAÑA LIBROS ---
        JPanel panelLibros = new JPanel(new GridLayout(1, 2, 10, 10));
        panelLibros.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelFormLibro = new JPanel(new GridLayout(8, 2, 5, 5));
        panelFormLibro.setBorder(BorderFactory.createTitledBorder("Gestión de Libros"));
        txtIsbnLibro = new JTextField();
        txtTituloLibro = new JTextField();
        txtAnioLibro = new JTextField();
        txtPrecioLibro = new JTextField();
        cmbAutoresLibro = new JComboBox<>();
        txtIdLibroEliminar = new JTextField();
        cmbAutoresFiltro = new JComboBox<>();

        JButton btnGuardarLibro = new JButton("Guardar Libro");
        btnGuardarLibro.addActionListener(e -> guardarLibro());
        JButton btnListarLibros = new JButton("Listar Todos Libros");
        btnListarLibros.addActionListener(e -> listarLibros());
        JButton btnListarPorAutor = new JButton("Listar Libros de Autor");
        btnListarPorAutor.addActionListener(e -> listarLibrosPorAutor());
        JButton btnEliminarLibro = new JButton("Eliminar Libro (por ID)");
        btnEliminarLibro.addActionListener(e -> eliminarLibro());
        JButton btnRefrescar = new JButton("Actualizar Combos");
        btnRefrescar.addActionListener(e -> cargarCombos());

        panelFormLibro.add(new JLabel("ISBN:")); panelFormLibro.add(txtIsbnLibro);
        panelFormLibro.add(new JLabel("Título:")); panelFormLibro.add(txtTituloLibro);
        panelFormLibro.add(new JLabel("Año:")); panelFormLibro.add(txtAnioLibro);
        panelFormLibro.add(new JLabel("Precio:")); panelFormLibro.add(txtPrecioLibro);
        panelFormLibro.add(new JLabel("Autor:")); panelFormLibro.add(cmbAutoresLibro);
        panelFormLibro.add(btnGuardarLibro); panelFormLibro.add(btnListarLibros);
        panelFormLibro.add(new JLabel("Filtro Autor:")); panelFormLibro.add(cmbAutoresFiltro);
        panelFormLibro.add(btnListarPorAutor); panelFormLibro.add(btnRefrescar);

        panelLibros.add(panelFormLibro);

        tabbedPane.add("Autores", panelAutores);
        tabbedPane.add("Libros", panelLibros);
        add(tabbedPane, BorderLayout.CENTER);

        // Área de salida de texto y logs
        txtAreaLog = new JTextArea(10, 40);
        txtAreaLog.setEditable(false);
        add(new JScrollPane(txtAreaLog), BorderLayout.SOUTH);
    }

    private void cargarCombos() {
        cmbAutoresLibro.removeAllItems();
        cmbAutoresFiltro.removeAllItems();
        try {
            List<Autor> autores = servicioAutores.obtenerTodos();
            for (Autor a : autores) {
                cmbAutoresLibro.addItem(a);
                cmbAutoresFiltro.addItem(a);
            }
        } catch (Exception e) {
            txtAreaLog.setText("Error cargando combos: " + e.getMessage());
        }
    }

    private void guardarAutor() {
        try {
            Autor a = new Autor();
            a.setNombre(txtNombreAutor.getText());
            a.setNacionalidad(txtNacAutor.getText());
            servicioAutores.registrar(a);
            txtAreaLog.setText("¡Autor registrado con éxito! ID asignado: " + a.getId());
            cargarCombos();
            txtNombreAutor.setText("");
            txtNacAutor.setText("");
        } catch (DatoInvalidoException | DatosException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Negocio", JOptionPane.WARNING_MESSAGE);
            txtAreaLog.setText("Error controlado: " + ex.getMessage());
        }
    }

    private void listarAutores() {
        try {
            List<Autor> lista = servicioAutores.obtenerTodos();
            StringBuilder sb = new StringBuilder("--- LISTA DE AUTORES ---\n");
            for (Autor a : lista) {
                sb.append(a.toString()).append("\n");
            }
            txtAreaLog.setText(sb.toString());
        } catch (Exception e) {
            txtAreaLog.setText("Error: " + e.getMessage());
        }
    }

    private void eliminarAutor() {
        try {
            int id = Integer.parseInt(txtIdAutorEliminar.getText().trim());
            servicioAutores.eliminar(id);
            txtAreaLog.setText("¡Autor eliminado correctamente!");
            cargarCombos();
            txtIdAutorEliminar.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID numérico válido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (DatoInvalidoException | DatosException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Regla de Negocio Incumplida", JOptionPane.ERROR_MESSAGE);
            txtAreaLog.setText("Error controlado: " + ex.getMessage());
        }
    }

    private void guardarLibro() {
        try {
            Autor autorSel = (Autor) cmbAutoresLibro.getSelectedItem();
            if (autorSel == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un autor.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Libro l = new Libro();
            l.setIsbn(txtIsbnLibro.getText());
            l.setTitulo(txtTituloLibro.getText());
            l.setAnio(Integer.parseInt(txtAnioLibro.getText().trim()));
            l.setPrecio(Double.parseDouble(txtPrecioLibro.getText().trim()));
            l.setAutorId(autorSel.getId());

            servicioLibros.registrar(l);
            txtAreaLog.setText("¡Libro registrado con éxito! ID: " + l.getId());
            txtIsbnLibro.setText("");
            txtTituloLibro.setText("");
            txtAnioLibro.setText("");
            txtPrecioLibro.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Año o precio con formato numérico inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (DatoInvalidoException | DatosException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Regla de Negocio Incumplida", JOptionPane.WARNING_MESSAGE);
            txtAreaLog.setText("Error controlado: " + ex.getMessage());
        }
    }

    private void listarLibros() {
        try {
            List<Libro> lista = servicioLibros.obtenerTodos();
            StringBuilder sb = new StringBuilder("--- LISTA DE LIBROS ---\n");
            for (Libro l : lista) {
                sb.append(l.toString()).append(" [Autor ID: ").append(l.getAutorId()).append("]\n");
            }
            txtAreaLog.setText(sb.toString());
        } catch (Exception e) {
            txtAreaLog.setText("Error: " + e.getMessage());
        }
    }

    private void listarLibrosPorAutor() {
        try {
            Autor autorSel = (Autor) cmbAutoresFiltro.getSelectedItem();
            if (autorSel == null) return;
            List<Libro> lista = servicioLibros.obtenerPorAutor(autorSel.getId());
            StringBuilder sb = new StringBuilder("--- LIBROS DEL AUTOR: " + autorSel.getNombre() + " ---\n");
            for (Libro l : lista) {
                sb.append(l.toString()).append("\n");
            }
            txtAreaLog.setText(sb.toString());
        } catch (Exception e) {
            txtAreaLog.setText("Error: " + e.getMessage());
        }
    }

    private void eliminarLibro() {
        try {
            String input = JOptionPane.showInputDialog(this, "Ingrese el ID del libro a eliminar:");
            if (input != null && !input.trim().isEmpty()) {
                int id = Integer.parseInt(input.trim());
                servicioLibros.eliminar(id);
                txtAreaLog.setText("¡Libro eliminado con éxito!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar libro: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}