package com.rurex.view;

import com.rurex.controller.ItineraryController;
import com.rurex.model.Itinerary;
import com.rurex.model.Ruta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
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
        Estilos.cerrarVolviendoAlInicio(this);
        setLocationRelativeTo(null);

        List<Ruta> rutas = controller.getRutasActivas();
        JComboBox<String> comboRuta = new JComboBox<>(rutas.stream().map(Ruta::getNombre).toArray(String[]::new));
        JComboBox<String> comboUnidad = new JComboBox<>(controller.getPlacasActivas().toArray(String[]::new));
        JComboBox<String> comboChofer = new JComboBox<>(controller.getConductores().toArray(String[]::new));
        Estilos.estilizarCombo(comboRuta);
        Estilos.estilizarCombo(comboUnidad);
        Estilos.estilizarCombo(comboChofer);
        JSpinner spFecha = spinner("yyyy-MM-dd", new Date());
        JSpinner spSalida = spinner("HH:mm", hora(7, 30));
        JSpinner spLlegada = spinner("HH:mm", hora(8, 15));
        JTextField txtCupos = Estilos.campo("");
        Runnable cuposDeUnidad = () -> txtCupos.setText(comboUnidad.getSelectedItem() == null ? "" : String.valueOf(controller.getCapacidad((String) comboUnidad.getSelectedItem())));
        comboUnidad.addActionListener(e -> cuposDeUnidad.run());
        cuposDeUnidad.run();

        JButton btnGuardar = Estilos.botonPrimario("Guardar Itinerario");
        JPanel panelBoton = new JPanel(new BorderLayout());
        panelBoton.setBackground(Color.WHITE);
        panelBoton.add(btnGuardar, BorderLayout.SOUTH);

        JPanel formulario = Estilos.crearFormulario();
        formulario.add(Estilos.filaColumnas(
                Estilos.etiquetaCampo("Ruta:", comboRuta),
                Estilos.etiquetaCampo("Fecha:", spFecha),
                Estilos.etiquetaCampo("Hora Salida:", spSalida),
                Estilos.etiquetaCampo("Hora Llegada:", spLlegada)));
        formulario.add(Box.createVerticalStrut(12));
        formulario.add(Estilos.filaColumnas(
                Estilos.etiquetaCampo("Unidad:", comboUnidad),
                Estilos.etiquetaCampo("Conductor:", comboChofer),
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
                int seats = Integer.parseInt(txtCupos.getText().trim());
                int i = comboRuta.getSelectedIndex();
                controller.guardarItinerario(
                        i < 0 ? null : rutas.get(i),
                        aLocal(spFecha).toLocalDate(),
                        aLocal(spSalida).toLocalTime().withSecond(0).withNano(0),
                        aLocal(spLlegada).toLocalTime().withSecond(0).withNano(0),
                        (String) comboUnidad.getSelectedItem(),
                        (String) comboChofer.getSelectedItem(),
                        seats
                );
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Cupos debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private static JSpinner spinner(String formato, Date valor) {
        JSpinner s = new JSpinner(new SpinnerDateModel(valor, null, null, Calendar.MINUTE));
        s.setEditor(new JSpinner.DateEditor(s, formato));
        s.setFont(Estilos.fuente(Font.PLAIN, 13));
        s.setPreferredSize(new Dimension(180, 34));
        return s;
    }

    private static Date hora(int h, int m) {
        return Date.from(LocalDate.now().atTime(h, m).atZone(ZoneId.systemDefault()).toInstant());
    }

    private static LocalDateTime aLocal(JSpinner s) {
        return LocalDateTime.ofInstant(((Date) s.getValue()).toInstant(), ZoneId.systemDefault());
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
