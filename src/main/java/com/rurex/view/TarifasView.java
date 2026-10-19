package com.rurex.view;

import com.rurex.controller.AuthController;
import com.rurex.model.UserRole;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;

public class TarifasView extends JFrame {

    private final AuthController controller;
    private final DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Tipo de usuario", "Porcentaje", "Precio (Bs.)"}, 0) {
        @Override public boolean isCellEditable(int row, int col) { return false; }
    };

    public TarifasView(AuthController controller) {
        this.controller = controller;

        setTitle("Transporte UCV - Gestión de Tarifas");
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        Estilos.cerrarVolviendoAlInicio(this);
        setLocationRelativeTo(null);

        JTextField txtBase = Estilos.campo("10.00");
        txtBase.setText(controller.getTarifaBase().toPlainString());
        JSpinner spEstudiante = new JSpinner(new SpinnerNumberModel(controller.getPorcentajeEstudiante(), 10, 20, 1));
        JSpinner spEmpleado = new JSpinner(new SpinnerNumberModel(controller.getPorcentajeEmpleado(), 30, 60, 1));

        JButton btnGuardar = Estilos.botonPrimario("Guardar Tarifas");
        btnGuardar.addActionListener(e -> {
            try {
                controller.configurarTarifas(new BigDecimal(txtBase.getText().trim().replace(',', '.')),
                        (Integer) spEstudiante.getValue(), (Integer) spEmpleado.getValue());
                cargar();
                JOptionPane.showMessageDialog(this, "Tarifas guardadas.", "Listo", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese una tarifa base válida.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel pie = new JPanel(new BorderLayout());
        pie.setBackground(Color.WHITE);
        pie.add(Estilos.etiqueta("Público General: 100%", Font.PLAIN, 12, Estilos.GRIS), BorderLayout.WEST);
        pie.add(btnGuardar, BorderLayout.EAST);

        JPanel formulario = Estilos.crearFormulario();
        formulario.add(Estilos.filaColumnas(
                Estilos.etiquetaCampo("Tarifa base (Bs.):", txtBase),
                Estilos.etiquetaCampo("% Estudiante (10-20):", spEstudiante),
                Estilos.etiquetaCampo("% Empleado/Profesor (30-60):", spEmpleado)));
        formulario.add(Box.createVerticalStrut(12));
        formulario.add(pie);

        JPanel contenido = new JPanel(new BorderLayout(0, 16));
        contenido.setBackground(Estilos.FONDO);
        contenido.add(Estilos.cardConTitulo("Tarifa Base Municipal", formulario), BorderLayout.NORTH);
        contenido.add(Estilos.cardConTitulo("Precio por Tipo de Usuario", Estilos.estilizarTabla(new JTable(tableModel))), BorderLayout.CENTER);

        cargar();
        Estilos.crearShell(this, "Tarifas", "Gestión de Tarifas", contenido);
    }

    private void cargar() {
        tableModel.setRowCount(0);
        tableModel.addRow(new Object[]{UserRole.ESTUDIANTE, controller.getPorcentajeEstudiante() + "%", controller.precioTarifa(UserRole.ESTUDIANTE)});
        tableModel.addRow(new Object[]{"Empleado / Profesor", controller.getPorcentajeEmpleado() + "%", controller.precioTarifa(UserRole.EMPLEADO)});
        tableModel.addRow(new Object[]{UserRole.PUBLICO_GENERAL, "100%", controller.precioTarifa(UserRole.PUBLICO_GENERAL)});
    }
}
