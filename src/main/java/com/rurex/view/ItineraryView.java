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

        setTitle("Transporte UCV - Gestion de Itinerarios");
        setSize(1100, 720);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(241, 243, 247));

        JPanel formCard = new JPanel(new GridLayout(4, 4, 15, 12));
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 224, 230)),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        JTextField txtRuta = new JTextField("Plaza Venezuela - UCV");
        txtRuta.setPreferredSize(new Dimension(180, 34));
        JTextField txtFecha = new JTextField(LocalDate.now().toString());
        txtFecha.setPreferredSize(new Dimension(180, 34));
        JTextField txtSalida = new JTextField("07:30");
        txtSalida.setPreferredSize(new Dimension(180, 34));
        JTextField txtLlegada = new JTextField("08:15");
        txtLlegada.setPreferredSize(new Dimension(180, 34));
        JTextField txtUnidad = new JTextField("UCV-234");
        txtUnidad.setPreferredSize(new Dimension(180, 34));
        JTextField txtChofer = new JTextField("Jose Martinez");
        txtChofer.setPreferredSize(new Dimension(180, 34));
        JTextField txtCupos = new JTextField("32");
        txtCupos.setPreferredSize(new Dimension(180, 34));

        formCard.add(new JLabel("Ruta:"));
        formCard.add(txtRuta);
        formCard.add(new JLabel("Fecha (YYYY-MM-DD):"));
        formCard.add(txtFecha);

        formCard.add(new JLabel("Hora Salida (HH:MM):"));
        formCard.add(txtSalida);
        formCard.add(new JLabel("Hora Llegada (HH:MM):"));
        formCard.add(txtLlegada);

        formCard.add(new JLabel("Placa Bus:"));
        formCard.add(txtUnidad);
        formCard.add(new JLabel("Chofer / Conductor:"));
        formCard.add(txtChofer);

        formCard.add(new JLabel("Cupos:"));
        formCard.add(txtCupos);

        JButton btnGuardar = new JButton("Guardar Itinerario");
        btnGuardar.setBackground(new Color(27, 135, 84));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnGuardar.setPreferredSize(new Dimension(180, 36));
        btnGuardar.setFocusPainted(false);
        formCard.add(btnGuardar);

        mainPanel.add(formCard, BorderLayout.NORTH);

        String[] cols = {"ID", "Ruta", "Fecha", "Salida", "Llegada", "Unidad", "Conductor", "Cupos"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(34);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setPreferredSize(new Dimension(0, 36));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        scroll.setBackground(new Color(241, 243, 247));
        mainPanel.add(scroll, BorderLayout.CENTER);

        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 16));
        bottomBar.setBackground(Color.WHITE);
        bottomBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 224, 230)));

        JButton btnDelete = new JButton("Eliminar Itinerario Seleccionado");
        btnDelete.setBackground(new Color(180, 40, 40));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnDelete.setPreferredSize(new Dimension(240, 38));
        btnDelete.setFocusPainted(false);
        bottomBar.add(btnDelete);
        mainPanel.add(bottomBar, BorderLayout.SOUTH);

        add(mainPanel);

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
                JOptionPane.showMessageDialog(this, "Formato invalido (YYYY-MM-DD y HH:MM)", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Cupos debe ser un numero.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un itinerario.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String id = (String) tableModel.getValueAt(row, 0);
            controller.eliminarItinerario(id);
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
                    it.getHoraLlegada().toString(),
                    it.getUnidad().getPlaca(),
                    it.getConductor(),
                    it.getCuposDisponibles()
            });
        }
    }
}
