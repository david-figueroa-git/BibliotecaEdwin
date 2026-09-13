

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ventanaPrincipal extends JFrame {

    private Biblioteca biblioteca;

    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtCodigo;
    private JComboBox<String> cmbGenero;
    private JTextField txtAnio;
    private JTextField txtCopias;

    private JTextField txtBuscarAutor;

    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;

    private JButton btnAgregar;
    private JButton btnEliminar;
    private JButton btnBuscarAutor;
    private JButton btnMostrarTodos;

    private String[] generos = {
            "Novela",
            "Ciencia",
            "Historia",
            "Infantil",
            "Técnico"
    };

    public ventanaPrincipal(Biblioteca biblioteca) {

        this.biblioteca = biblioteca;

        configurarVentana();
        crearComponentes();
        configurarEventos();
        actualizarTabla(biblioteca.obtenerTodos());
    }

    private void configurarVentana() {

        setTitle("Sistema de Gestión de Biblioteca");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void crearComponentes() {

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridLayout(3, 4, 10, 10));

        txtTitulo = new JTextField();
        txtAutor = new JTextField();
        txtCodigo = new JTextField();
        cmbGenero = new JComboBox<>(generos);
        txtAnio = new JTextField();
        txtCopias = new JTextField();

        panelFormulario.add(new JLabel("Título:"));
        panelFormulario.add(txtTitulo);

        panelFormulario.add(new JLabel("Autor:"));
        panelFormulario.add(txtAutor);

        panelFormulario.add(new JLabel("Código / ISBN:"));
        panelFormulario.add(txtCodigo);

        panelFormulario.add(new JLabel("Género:"));
        panelFormulario.add(cmbGenero);

        panelFormulario.add(new JLabel("Año:"));
        panelFormulario.add(txtAnio);

        panelFormulario.add(new JLabel("Copias:"));
        panelFormulario.add(txtCopias);

        JPanel panelBotones = new JPanel();

        btnAgregar = new JButton("Agregar libro");
        btnEliminar = new JButton("Eliminar seleccionado");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);

        JPanel panelBusqueda = new JPanel(new FlowLayout());

        txtBuscarAutor = new JTextField(20);

        btnBuscarAutor = new JButton("Filtrar por autor");
        btnMostrarTodos = new JButton("Mostrar todos");

        panelBusqueda.add(new JLabel("Autor:"));
        panelBusqueda.add(txtBuscarAutor);
        panelBusqueda.add(btnBuscarAutor);
        panelBusqueda.add(btnMostrarTodos);

        JPanel panelSuperior = new JPanel(new BorderLayout(5, 5));

        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel();

        modeloTabla.addColumn("Título");
        modeloTabla.addColumn("Autor");
        modeloTabla.addColumn("Código");
        modeloTabla.addColumn("Género");
        modeloTabla.addColumn("Año");
        modeloTabla.addColumn("Copias");

        tablaLibros = new JTable(modeloTabla);

        JScrollPane scrollTabla = new JScrollPane(tablaLibros);

        JPanel panelCentro = new JPanel(new BorderLayout());

        panelCentro.add(panelBusqueda, BorderLayout.NORTH);
        panelCentro.add(scrollTabla, BorderLayout.CENTER);

        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        add(panelPrincipal);
    }

    private void configurarEventos() {

        btnAgregar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                agregarLibro();
            }
        });

        btnEliminar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarLibro();
            }
        });

        btnBuscarAutor.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                filtrarPorAutor();
            }
        });

        btnMostrarTodos.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarTodos();
            }
        });
    }

    private void agregarLibro() {

        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        String codigo = txtCodigo.getText().trim();
        String genero = cmbGenero.getSelectedItem().toString();
        String anioTexto = txtAnio.getText().trim();
        String copiasTexto = txtCopias.getText().trim();

        if (titulo.isEmpty()
                || autor.isEmpty()
                || codigo.isEmpty()
                || anioTexto.isEmpty()
                || copiasTexto.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Todos los campos son obligatorios.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        int anio;
        int copias;

        try {

            anio = Integer.parseInt(anioTexto);
            copias = Integer.parseInt(copiasTexto);

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "El año y las copias deben ser números enteros.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        int anioActual = java.time.Year.now().getValue();

        if (anio > anioActual) {

            JOptionPane.showMessageDialog(
                    this,
                    "El año no puede ser mayor al año actual.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (copias < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Las copias no pueden ser negativas.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (biblioteca.existeCodigo(codigo)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ya existe un libro con ese código.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        Libro libro = new Libro(
                titulo,
                autor,
                codigo,
                genero,
                anio,
                copias
        );

        boolean agregado = biblioteca.agregarLibro(libro);

        if (agregado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Libro agregado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarFormulario();

            actualizarTabla(biblioteca.obtenerTodos());
        }
    }

    private void eliminarLibro() {

        int filaSeleccionada = tablaLibros.getSelectedRow();

        if (filaSeleccionada == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un libro de la tabla.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String codigo = modeloTabla
                .getValueAt(filaSeleccionada, 2)
                .toString();

        Libro libroSeleccionado = buscarLibroPorCodigo(codigo);

        if (libroSeleccionado == null) {
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar el libro seleccionado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta == JOptionPane.YES_OPTION) {

            biblioteca.eliminarLibro(libroSeleccionado);

            actualizarTabla(biblioteca.obtenerTodos());

            JOptionPane.showMessageDialog(
                    this,
                    "Libro eliminado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private Libro buscarLibroPorCodigo(String codigo) {

        for (Libro libro : biblioteca.obtenerTodos()) {

            if (libro.getCodigo().equals(codigo)) {
                return libro;
            }
        }

        return null;
    }

    private void filtrarPorAutor() {

        String autor = txtBuscarAutor.getText().trim();

        if (autor.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un autor para realizar la búsqueda.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        java.util.ArrayList<Libro> resultados =
                biblioteca.filtrarPorAutor(autor);

        actualizarTabla(resultados);

        if (resultados.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se encontraron libros de ese autor.",
                    "Resultado",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void mostrarTodos() {

        txtBuscarAutor.setText("");

        actualizarTabla(biblioteca.obtenerTodos());
    }

    private void actualizarTabla(java.util.ArrayList<Libro> libros) {

        modeloTabla.setRowCount(0);

        for (Libro libro : libros) {

            Object[] fila = {
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getCodigo(),
                    libro.getGenero(),
                    libro.getAnioPublicacion(),
                    libro.getCopiasDisponibles()
            };

            modeloTabla.addRow(fila);
        }
    }

    private void limpiarFormulario() {

        txtTitulo.setText("");
        txtAutor.setText("");
        txtCodigo.setText("");
        txtAnio.setText("");
        txtCopias.setText("");

        cmbGenero.setSelectedIndex(0);
    }
}