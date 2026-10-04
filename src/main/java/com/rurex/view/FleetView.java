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

        setTitle("Transporte UCV - Gestión de Unidades");
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contenido = new JPanel(new BorderLayout(0, 12));
        contenido.setBackground(Estilos.FONDO);

        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(Estilos.FONDO);

        JButton btnNewUnit = Estilos.botonPrimario("+ Nueva Unidad");
        barra.add(btnNewUnit, BorderLayout.WEST);

        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        filtro.setBackground(Estilos.FONDO);
        filtro.add(Estilos.etiqueta("Filtrar por estado operativo:", Font.PLAIN, 13, Estilos.NAVY));
        comboFilter = new JComboBox<>(new String[]{"Todas", "Activa", "En Mantenimiento", "Fuera de Servicio"});
        Estilos.estilizarCombo(comboFilter);
        filtro.add(comboFilter);
        barra.add(filtro, BorderLayout.EAST);

        contenido.add(barra, BorderLayout.NORTH);

        String[] cols = {"Placa", "Modelo", "Capacidad", "Estado", "Acciones"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return col == 4; }
        };
        JTable table = new JTable(tableModel);
        JScrollPane scroll = Estilos.estilizarTabla(table);

        table.getColumn("Modelo").setCellRenderer(Estilos.rendererTexto(Estilos.GRIS));
        table.getColumn("Estado").setCellRenderer(Estilos.rendererBadge());

        Estilos.AccionesCelda acciones = new Estilos.AccionesCelda(new String[]{"Editar", "Cambiar Estado"}, false, (fila, boton) -> {
            String placa = (String) tableModel.getValueAt(fila, 0);
            if (boton == 0) {
                mostrarDialogoEditar(placa, (String) tableModel.getValueAt(fila, 1), (String) tableModel.getValueAt(fila, 2));
            } else {
                mostrarDialogoEstado(placa, (String) tableModel.getValueAt(fila, 3));
            }
        });
        table.getColumn("Acciones").setCellRenderer(acciones);
        table.getColumn("Acciones").setCellEditor(acciones);
        table.getColumn("Acciones").setMinWidth(250);
        table.getColumn("Acciones").setMaxWidth(250);
        table.getColumn("Placa").setMinWidth(80);
        table.getColumn("Modelo").setPreferredWidth(200);
        table.getColumn("Capacidad").setMinWidth(100);
        table.getColumn("Estado").setMinWidth(140);

        contenido.add(Estilos.cardConTitulo("Lista de Unidades", scroll), BorderLayout.CENTER);

        Estilos.crearShell(this, "Unidades", "Gestión de Unidades", contenido);

        comboFilter.addActionListener(e -> actualizarTabla());
        btnNewUnit.addActionListener(e -> mostrarDialogoNueva());
    }

    private void mostrarDialogoNueva() {
        JTextField txtPlaca = Estilos.campo("UCV-234");
        JTextField txtModelo = Estilos.campo("Mercedes-Benz OF-1721");
        JTextField txtCapacidad = Estilos.campo("30");
        txtCapacidad.setText("30");
        JComboBox<OperationalStatus> comboStatus = new JComboBox<>(OperationalStatus.values());
        Estilos.estilizarCombo(comboStatus);

        JPanel form = Estilos.crearFormulario();
        Estilos.agregarCampo(form, "Placa:", txtPlaca);
        Estilos.agregarCampo(form, "Modelo:", txtModelo);
        Estilos.agregarCampo(form, "Capacidad de Pasajeros:", txtCapacidad);
        Estilos.agregarCampo(form, "Estado Inicial:", comboStatus);

        JDialog dialogo = Estilos.crearDialogo(this, "Nueva Unidad", form, "Guardar",
                () -> Estilos.validarEntero(form, txtCapacidad.getText(), "La capacidad debe ser un número entero."),
                () -> controller.registrarNuevaUnidad(
                        txtPlaca.getText(),
                        txtModelo.getText(),
                        Integer.parseInt(txtCapacidad.getText().trim()),
                        (OperationalStatus) comboStatus.getSelectedItem()));
        dialogo.setVisible(true);
    }

    private void mostrarDialogoEditar(String placa, String modelo, String capacidadTexto) {
        JTextField txtPlaca = Estilos.campo("");
        txtPlaca.setText(placa);
        txtPlaca.setEditable(false);
        txtPlaca.setBackground(Estilos.GRIS_CLARO);
        JTextField txtModelo = Estilos.campo("");
        txtModelo.setText(modelo);
        JTextField txtCapacidad = Estilos.campo("");
        txtCapacidad.setText(capacidadTexto.replace(" pasajeros", "").trim());

        JPanel form = Estilos.crearFormulario();
        Estilos.agregarCampo(form, "Placa (no editable):", txtPlaca);
        Estilos.agregarCampo(form, "Modelo:", txtModelo);
        Estilos.agregarCampo(form, "Capacidad de Pasajeros:", txtCapacidad);

        JDialog dialogo = Estilos.crearDialogo(this, "Editar Unidad " + placa, form, "Guardar",
                () -> Estilos.validarEntero(form, txtCapacidad.getText(), "La capacidad debe ser un número entero."),
                () -> controller.editarUnidad(placa, txtModelo.getText(), Integer.parseInt(txtCapacidad.getText().trim()), null));
        dialogo.setVisible(true);
    }

    private void mostrarDialogoEstado(String placa, String estadoActual) {
        JComboBox<OperationalStatus> comboStatus = new JComboBox<>(OperationalStatus.values());
        Estilos.estilizarCombo(comboStatus);
        comboStatus.setSelectedItem(OperationalStatus.fromString(estadoActual));

        JPanel form = Estilos.crearFormulario();
        Estilos.agregarCampo(form, "Nuevo estado para " + placa + ":", comboStatus);

        JDialog dialogo = Estilos.crearDialogo(this, "Cambiar Estado", form, "Guardar",
                () -> true,
                () -> controller.cambiarEstadoUnidad(placa, (OperationalStatus) comboStatus.getSelectedItem()));
        dialogo.setVisible(true);
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
                    u.getCapacidad() + " pasajeros",
                    u.getEstado().getLabel(),
                    ""
            });
        }
    }
}
