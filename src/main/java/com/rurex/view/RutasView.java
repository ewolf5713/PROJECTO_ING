package com.rurex.view;

import com.rurex.controller.RutaController;
import com.rurex.model.Ruta;
import com.rurex.model.TipoRuta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RutasView extends JFrame {

    private final RutaController controller;
    private final DefaultTableModel tableModel;
    private final List<Ruta> rutasEnTabla = new ArrayList<>();

    private final JTextField txtNombre = Estilos.campo("Plaza Venezuela - UCV");
    private final JTextField txtOrigen = Estilos.campo("Plaza Venezuela");
    private final JTextField txtDestino = Estilos.campo("Ciudad Universitaria");
    private final JComboBox<TipoRuta> comboTipo = new JComboBox<>(TipoRuta.values());
    private final PanelParadas panelParadas = new PanelParadas(3);
    private final JButton btnGuardarCambios = Estilos.botonPrimario("Guardar Cambios");
    private final JLabel lblModo = Estilos.etiqueta("Creando una ruta nueva", Font.PLAIN, 12, Estilos.GRIS);

    private Ruta rutaEnEdicion; // null = el formulario crea una ruta nueva

    public RutasView(RutaController controller) {
        this.controller = controller;

        setTitle("Transporte UCV - Gestión de Rutas");
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        Estilos.cerrarVolviendoAlInicio(this);
        setLocationRelativeTo(null);

        Estilos.estilizarCombo(comboTipo);

        JButton btnCrear = Estilos.botonPrimario("Crear Ruta");
        JButton btnLimpiar = Estilos.botonOutline("Limpiar");
        btnGuardarCambios.setEnabled(false);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botones.setBackground(Color.WHITE);
        botones.add(btnLimpiar);
        botones.add(btnGuardarCambios);
        botones.add(btnCrear);

        JPanel pie = new JPanel(new BorderLayout());
        pie.setBackground(Color.WHITE);
        pie.add(lblModo, BorderLayout.WEST);
        pie.add(botones, BorderLayout.EAST);

        JPanel formulario = Estilos.crearFormulario();
        formulario.add(Estilos.filaColumnas(
                Estilos.etiquetaCampo("Nombre:", txtNombre),
                Estilos.etiquetaCampo("Origen:", txtOrigen),
                Estilos.etiquetaCampo("Destino:", txtDestino),
                Estilos.etiquetaCampo("Tipo:", comboTipo)));
        formulario.add(Box.createVerticalStrut(12));
        formulario.add(panelParadas);
        formulario.add(Box.createVerticalStrut(12));
        formulario.add(pie);
        panelParadas.setAlCambiar(() -> {
            formulario.revalidate();
            formulario.repaint();
        });

        String[] cols = {"Nombre", "Origen", "Destino", "Tipo", "Paradas", "Estado", "Acciones"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return col == 6; }
        };
        JTable table = new JTable(tableModel);
        JScrollPane scroll = Estilos.estilizarTabla(table);

        table.getColumn("Paradas").setCellRenderer(Estilos.rendererTexto(Estilos.GRIS));

        Estilos.AccionesCelda acciones = new Estilos.AccionesCelda(new String[]{"Editar", "Activar/Desactivar"}, false, (fila, boton) -> {
            Ruta ruta = rutasEnTabla.get(fila);
            if (boton == 0) {
                cargarEnFormulario(ruta);
            } else {
                controller.alternarEstado(ruta);
                actualizarTabla();
            }
        });
        table.getColumn("Acciones").setCellRenderer(acciones);
        table.getColumn("Acciones").setCellEditor(acciones);
        table.getColumn("Acciones").setMinWidth(230);
        table.getColumn("Acciones").setMaxWidth(230);
        table.getColumn("Nombre").setPreferredWidth(180);
        table.getColumn("Paradas").setPreferredWidth(200);
        table.getColumn("Tipo").setMinWidth(90);
        table.getColumn("Estado").setMinWidth(80);

        JPanel contenido = new JPanel(new BorderLayout(0, 16));
        contenido.setBackground(Estilos.FONDO);
        contenido.add(Estilos.cardConTitulo("Datos de la Ruta", formulario), BorderLayout.NORTH);
        contenido.add(Estilos.cardConTitulo("Rutas Registradas", scroll), BorderLayout.CENTER);

        Estilos.crearShell(this, "Rutas", "Gestión de Rutas", contenido);

        btnCrear.addActionListener(e -> ejecutar(() -> controller.crearRuta(
                txtNombre.getText(), txtOrigen.getText(), txtDestino.getText(),
                (TipoRuta) comboTipo.getSelectedItem(), panelParadas.getParadas())));
        btnGuardarCambios.addActionListener(e -> {
            if (rutaEnEdicion != null) {
                ejecutar(() -> controller.actualizarRuta(rutaEnEdicion,
                        txtNombre.getText(), txtOrigen.getText(), txtDestino.getText(),
                        (TipoRuta) comboTipo.getSelectedItem(), panelParadas.getParadas()));
            }
        });
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        actualizarTabla();
    }

    /** Ejecuta la acción; si es válida limpia el formulario, si no muestra el error. */
    private void ejecutar(Runnable accion) {
        try {
            accion.run();
            limpiarFormulario();
            actualizarTabla();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarEnFormulario(Ruta ruta) {
        rutaEnEdicion = ruta;
        txtNombre.setText(ruta.getNombre());
        txtOrigen.setText(ruta.getOrigen());
        txtDestino.setText(ruta.getDestino());
        comboTipo.setSelectedItem(ruta.getTipo());
        panelParadas.limpiar();
        for (String parada : ruta.getParadas()) {
            panelParadas.agregarParada(parada);
        }
        btnGuardarCambios.setEnabled(true);
        lblModo.setText("Editando: " + ruta.getNombre());
    }

    private void limpiarFormulario() {
        rutaEnEdicion = null;
        txtNombre.setText("");
        txtOrigen.setText("");
        txtDestino.setText("");
        comboTipo.setSelectedIndex(0);
        panelParadas.limpiar();
        btnGuardarCambios.setEnabled(false);
        lblModo.setText("Creando una ruta nueva");
    }

    public void actualizarTabla() {
        tableModel.setRowCount(0);
        rutasEnTabla.clear();
        for (Ruta r : controller.getRutas()) {
            rutasEnTabla.add(r);
            tableModel.addRow(new Object[]{
                    r.getNombre(),
                    r.getOrigen(),
                    r.getDestino(),
                    r.getTipo().toString(),
                    r.getParadas().isEmpty() ? "-" : String.join(", ", r.getParadas()),
                    r.isActivada() ? "Activa" : "Inactiva",
                    ""
            });
        }
    }
}
