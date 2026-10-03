package com.rurex.view;

import com.rurex.controller.FleetController;
import com.rurex.model.OperationalStatus;
import com.rurex.model.TransportUnit;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FleetView extends JFrame {

    private final FleetController controller;
    private final DefaultTableModel tableModel;
    private final JComboBox<String> comboFilter;

    public FleetView(FleetController controller) {
        this.controller = controller;
        this.controller.setFleetView(this);

        setTitle("Transporte UCV - Gestion de Unidades");
        setSize(1000, 680);
        setMinimumSize(new Dimension(850, 550));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(241, 243, 247));

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 16));
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 224, 230)));

        JButton btnNewUnit = new JButton("+ Nueva Unidad");
        btnNewUnit.setBackground(new Color(27, 135, 84));
        btnNewUnit.setForeground(Color.WHITE);
        btnNewUnit.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnNewUnit.setPreferredSize(new Dimension(160, 38));
        btnNewUnit.setFocusPainted(false);
        topBar.add(btnNewUnit);

        JLabel lblFiltro = new JLabel("Filtrar por estado operativo:");
        lblFiltro.setFont(new Font("SansSerif", Font.PLAIN, 13));
        topBar.add(lblFiltro);

        comboFilter = new JComboBox<>(new String[]{"Todas", "Activa", "En Mantenimiento", "Fuera de Servicio"});
        comboFilter.setPreferredSize(new Dimension(200, 36));
        comboFilter.setFont(new Font("SansSerif", Font.PLAIN, 13));
        topBar.add(comboFilter);

        mainPanel.add(topBar, BorderLayout.NORTH);

        String[] cols = {"Placa", "Modelo", "Capacidad", "Estado Operativo"};
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

        JButton btnChangeStatus = new JButton("Cambiar Estado de Unidad");
        btnChangeStatus.setBackground(new Color(24, 32, 56));
        btnChangeStatus.setForeground(Color.WHITE);
        btnChangeStatus.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnChangeStatus.setPreferredSize(new Dimension(220, 38));
        btnChangeStatus.setFocusPainted(false);
        bottomBar.add(btnChangeStatus);
        mainPanel.add(bottomBar, BorderLayout.SOUTH);

        add(mainPanel);

        comboFilter.addActionListener(e -> actualizarTabla());

        btnNewUnit.addActionListener(e -> {
            JTextField txtPlaca = new JTextField();
            JTextField txtModelo = new JTextField();
            JTextField txtCapacidad = new JTextField("30");
            JComboBox<OperationalStatus> comboStatus = new JComboBox<>(OperationalStatus.values());

            Object[] fields = {
                    "Placa:", txtPlaca,
                    "Modelo:", txtModelo,
                    "Capacidad de Pasajeros:", txtCapacidad,
                    "Estado Inicial:", comboStatus
            };

            int option = JOptionPane.showConfirmDialog(this, fields, "Registrar Nueva Unidad", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    int cap = Integer.parseInt(txtCapacidad.getText().trim());
                    controller.registrarNuevaUnidad(txtPlaca.getText(), txtModelo.getText(), cap, (OperationalStatus) comboStatus.getSelectedItem());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "La capacidad debe ser un numero entero.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnChangeStatus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione una unidad de la lista.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String plate = (String) tableModel.getValueAt(row, 0);
            OperationalStatus newStatus = (OperationalStatus) JOptionPane.showInputDialog(
                    this,
                    "Seleccione nuevo estado para " + plate + ":",
                    "Cambiar Estado",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    OperationalStatus.values(),
                    OperationalStatus.ACTIVA
            );
            if (newStatus != null) {
                controller.cambiarEstadoUnidad(plate, newStatus);
            }
        });
    }

    public void actualizarTabla() {
        tableModel.setRowCount(0);
        String sel = (String) comboFilter.getSelectedItem();
        OperationalStatus filter = null;
        if (sel != null && !sel.equals("Todas")) {
            filter = OperationalStatus.fromString(sel);
        }

        List<TransportUnit> list = controller.cargarUnidades(filter);
        for (TransportUnit u : list) {
            tableModel.addRow(new Object[]{
                    u.getPlaca(),
                    u.getModelo(),
                    u.getCapacidad() + " puestos",
                    u.getEstado().getLabel()
            });
        }
    }
}
