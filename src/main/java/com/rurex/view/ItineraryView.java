package com.rurex.view;

import com.rurex.controller.ItineraryController;
import com.rurex.model.Itinerary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ItineraryView extends JFrame {

    private final ItineraryController controller;
    private final DefaultTableModel tableModel;

    public ItineraryView(ItineraryController controller) {
        this.controller = controller;
        this.controller.setItineraryView(this);

        setTitle("Transporte UCV - Gestión de Itinerarios");
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTextField txtRuta = Estilos.campo("");
        txtRuta.setText("Plaza Venezuela - UCV");
        JTextField txtFecha = Estilos.campo("");
        txtFecha.setText(LocalDate.now().toString());
        JTextField txtSalida = Estilos.campo("");
        txtSalida.setText("07:30");
        JTextField txtLlegada = Estilos.campo("");
        txtLlegada.setText("08:15");
        JTextField txtUnidad = Estilos.campo("");
        txtUnidad.setText("UCV-234");
        JTextField txtChofer = Estilos.campo("");
        txtChofer.setText("Jose Martinez");
        JTextField txtCupos = Estilos.campo("");
        txtCupos.setText("32");

        JButton btnGuardar = Estilos.botonPrimario("Guardar Itinerario");
        JPanel panelBoton = new JPanel(new BorderLayout());
        panelBoton.setBackground(Color.WHITE);
        panelBoton.add(btnGuardar, BorderLayout.SOUTH);

        JPanel formulario = Estilos.crearFormulario();
        formulario.add(Estilos.filaColumnas(
                Estilos.etiquetaCampo("Ruta:", txtRuta),
                Estilos.etiquetaCampo("Fecha (AAAA-MM-DD):", txtFecha),
                Estilos.etiquetaCampo("Hora Salida (HH:MM):", txtSalida),
                Estilos.etiquetaCampo("Hora Llegada (HH:MM):", txtLlegada)));
        formulario.add(Box.createVerticalStrut(12));
        formulario.add(Estilos.filaColumnas(
                Estilos.etiquetaCampo("Unidad (Placa):", txtUnidad),
                Estilos.etiquetaCampo("Conductor:", txtChofer),
                Estilos.etiquetaCampo("Cupos:", txtCupos),
                panelBoton));

        String[] cols = {"ID", "Ruta", "Fecha", "Salida", "Llegada", "Unidad", "Conductor", "Cupos", "Acciones"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return col == 8; }
        };
        JTable table = new JTable(tableModel);
        JScrollPane scroll = Estilos.estilizarTabla(table);
        table.removeColumn(table.getColumn("ID"));

        Estilos.AccionesCelda acciones = new Estilos.AccionesCelda(new String[]{"Eliminar"}, true, (fila, boton) -> {
            String id = (String) tableModel.getValueAt(fila, 0);
            controller.eliminarItinerario(id);
        });
        table.getColumn("Acciones").setCellRenderer(acciones);
        table.getColumn("Acciones").setCellEditor(acciones);
        table.getColumn("Acciones").setMinWidth(110);
        table.getColumn("Acciones").setMaxWidth(110);
        table.getColumn("Ruta").setPreferredWidth(200);
        table.getColumn("Fecha").setMinWidth(95);
        table.getColumn("Salida").setMinWidth(65);
        table.getColumn("Llegada").setMinWidth(70);
        table.getColumn("Unidad").setMinWidth(80);
        table.getColumn("Conductor").setPreferredWidth(140);
        table.getColumn("Cupos").setMinWidth(60);

        JPanel contenido = new JPanel(new BorderLayout(0, 16));
        contenido.setBackground(Estilos.FONDO);
        contenido.add(Estilos.cardConTitulo("Crear Nuevo Itinerario", formulario), BorderLayout.NORTH);
        contenido.add(Estilos.cardConTitulo("Itinerarios Programados", scroll), BorderLayout.CENTER);

        Estilos.crearShell(this, "Itinerarios", "Gestión de Itinerarios", contenido);

        btnGuardar.addActionListener(e -> {
            try {
                LocalDate date = LocalDate.parse(txtFecha.getText().trim());
                LocalTime dep = LocalTime.parse(txtSalida.getText().trim());
                LocalTime arr = txtLlegada.getText().isBlank() ? null : LocalTime.parse(txtLlegada.getText().trim());
                int seats = Integer.parseInt(txtCupos.getText().trim());

                controller.guardarItinerario(
                        txtRuta.getText(),
                        date,
                        dep,
                        arr,
                        txtUnidad.getText(),
                        txtChofer.getText(),
                        seats
                );
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Formato inválido (AAAA-MM-DD y HH:MM)", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Cupos debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void actualizarTabla() {
        tableModel.setRowCount(0);
        List<Itinerary> list = controller.cargarItinerarios();
        for (Itinerary it : list) {
            tableModel.addRow(new Object[]{
                    it.getId(),
                    it.getRutaNombre(),
                    it.getFecha().toString(),
                    it.getHoraSalida().toString(),
                    it.getHoraLlegada() != null ? it.getHoraLlegada().toString() : "-",
                    it.getUnidad().getPlaca(),
                    it.getConductor(),
                    it.getCuposDisponibles(),
                    ""
            });
        }
    }
}
