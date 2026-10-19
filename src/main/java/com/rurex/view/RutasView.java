package com.rurex.view;

import com.rurex.controller.RutaController;
import com.rurex.model.Ruta;
import com.rurex.model.TipoRuta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RutasView extends JFrame {

    private final RutaController controller;
    private final DefaultTableModel tableModel;

    public RutasView(RutaController controller) {
        this.controller = controller;

        setTitle("Transporte UCV - Gestión de Rutas");
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        Estilos.cerrarVolviendoAlInicio(this);
        setLocationRelativeTo(null);

        JPanel contenido = new JPanel(new BorderLayout(0, 12));
        contenido.setBackground(Estilos.FONDO);

        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(Estilos.FONDO);

        JButton btnNuevaRuta = Estilos.botonPrimario("+ Nueva Ruta");
        barra.add(btnNuevaRuta, BorderLayout.WEST);
        contenido.add(barra, BorderLayout.NORTH);

        String[] cols = {"Nombre", "Origen", "Destino", "Tipo", "Paradas", "Estado", "Acciones"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 6;
            }
        };

        JTable table = new JTable(tableModel);
        JScrollPane scroll = Estilos.estilizarTabla(table);

        table.getColumn("Origen").setCellRenderer(Estilos.rendererTexto(Estilos.GRIS));
        table.getColumn("Destino").setCellRenderer(Estilos.rendererTexto(Estilos.GRIS));
        table.getColumn("Paradas").setCellRenderer(Estilos.rendererTexto(Estilos.GRIS));
        table.getColumn("Estado").setCellRenderer(Estilos.rendererBadge());

        Estilos.AccionesCelda acciones = new Estilos.AccionesCelda(new String[]{"Editar", "Cambiar Estado"}, false, (fila, boton) -> {
            List<Ruta> lista = controller.getRutas();
            if (fila >= 0 && fila < lista.size()) {
                Ruta ruta = lista.get(fila);
                if (boton == 0) {
                    mostrarDialogoEditar(ruta);
                } else {
                    controller.alternarEstado(ruta);
                    actualizarTabla();
                }
            }
        });

        table.getColumn("Acciones").setCellRenderer(acciones);
        table.getColumn("Acciones").setCellEditor(acciones);
        table.getColumn("Acciones").setMinWidth(250);
        table.getColumn("Acciones").setMaxWidth(250);
        table.getColumn("Nombre").setPreferredWidth(160);
        table.getColumn("Origen").setPreferredWidth(120);
        table.getColumn("Destino").setPreferredWidth(120);
        table.getColumn("Tipo").setMinWidth(100);
        table.getColumn("Paradas").setPreferredWidth(200);
        table.getColumn("Estado").setMinWidth(110);

        contenido.add(Estilos.cardConTitulo("Lista de Rutas", scroll), BorderLayout.CENTER);

        Estilos.crearShell(this, "Rutas", "Gestión de Rutas", contenido);

        btnNuevaRuta.addActionListener(e -> mostrarDialogoNueva());

        actualizarTabla();
    }

    private void mostrarDialogoNueva() {
        JTextField txtNombre = Estilos.campo("Ej: Plaza Venezuela - UCV");
        JTextField txtOrigen = Estilos.campo("Ej: Plaza Venezuela");
        JTextField txtDestino = Estilos.campo("Ej: Ciudad Universitaria");
        JComboBox<TipoRuta> comboTipo = new JComboBox<>(TipoRuta.values());
        Estilos.estilizarCombo(comboTipo);
        PanelParadas panelParadas = new PanelParadas(1);

        JPanel form = Estilos.crearFormulario();
        Estilos.agregarCampo(form, "Nombre de la Ruta:", txtNombre);
        Estilos.agregarCampo(form, "Origen:", txtOrigen);
        Estilos.agregarCampo(form, "Destino:", txtDestino);
        Estilos.agregarCampo(form, "Tipo de Ruta:", comboTipo);
        form.add(panelParadas);
        form.add(Box.createVerticalStrut(12));

        JDialog dialogo = Estilos.crearDialogo(this, "Nueva Ruta", form, "Guardar",
                () -> {
                    if (txtNombre.getText().trim().isEmpty() || txtOrigen.getText().trim().isEmpty() || txtDestino.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    return true;
                },
                () -> {
                    try {
                        controller.crearRuta(
                                txtNombre.getText(),
                                txtOrigen.getText(),
                                txtDestino.getText(),
                                (TipoRuta) comboTipo.getSelectedItem(),
                                panelParadas.getParadas()
                        );
                        actualizarTabla();
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
        dialogo.setVisible(true);
    }

    private void mostrarDialogoEditar(Ruta ruta) {
        JTextField txtNombre = Estilos.campo("");
        txtNombre.setText(ruta.getNombre());
        JTextField txtOrigen = Estilos.campo("");
        txtOrigen.setText(ruta.getOrigen());
        JTextField txtDestino = Estilos.campo("");
        txtDestino.setText(ruta.getDestino());
        JComboBox<TipoRuta> comboTipo = new JComboBox<>(TipoRuta.values());
        Estilos.estilizarCombo(comboTipo);
        comboTipo.setSelectedItem(ruta.getTipo());
        PanelParadas panelParadas = new PanelParadas(1);
        for (String parada : ruta.getParadas()) {
            panelParadas.agregarParada(parada);
        }

        JPanel form = Estilos.crearFormulario();
        Estilos.agregarCampo(form, "Nombre de la Ruta:", txtNombre);
        Estilos.agregarCampo(form, "Origen:", txtOrigen);
        Estilos.agregarCampo(form, "Destino:", txtDestino);
        Estilos.agregarCampo(form, "Tipo de Ruta:", comboTipo);
        form.add(panelParadas);
        form.add(Box.createVerticalStrut(12));

        JDialog dialogo = Estilos.crearDialogo(this, "Editar Ruta", form, "Guardar",
                () -> {
                    if (txtNombre.getText().trim().isEmpty() || txtOrigen.getText().trim().isEmpty() || txtDestino.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    return true;
                },
                () -> {
                    try {
                        controller.actualizarRuta(
                                ruta,
                                txtNombre.getText(),
                                txtOrigen.getText(),
                                txtDestino.getText(),
                                (TipoRuta) comboTipo.getSelectedItem(),
                                panelParadas.getParadas()
                        );
                        actualizarTabla();
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
        dialogo.setVisible(true);
    }

    public void actualizarTabla() {
        tableModel.setRowCount(0);
        List<Ruta> list = controller.getRutas();
        for (Ruta r : list) {
            String paradasTexto = r.getParadas().isEmpty() ? "Sin paradas intermedias" : String.join(", ", r.getParadas());
            tableModel.addRow(new Object[]{
                    r.getNombre(),
                    r.getOrigen(),
                    r.getDestino(),
                    r.getTipo().toString(),
                    paradasTexto,
                    r.isActivada() ? "Activa" : "Fuera de Servicio",
                    ""
            });
        }
    }
}
