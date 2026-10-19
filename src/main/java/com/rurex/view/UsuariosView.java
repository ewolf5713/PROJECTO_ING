package com.rurex.view;

import com.rurex.controller.AuthController;
import com.rurex.model.User;
import com.rurex.model.UserRole;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UsuariosView extends JFrame {

    private final AuthController controller;
    private final DefaultTableModel tableModel;
    private List<User> usuarios;

    public UsuariosView(AuthController controller) {
        this.controller = controller;

        setTitle("Transporte UCV - Gestión de Usuarios");
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        Estilos.cerrarVolviendoAlInicio(this);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel(new String[]{"Nombre", "Cédula", "Correo", "Rol"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JComboBox<UserRole> comboRol = new JComboBox<>(new UserRole[]{UserRole.ESTUDIANTE, UserRole.EMPLEADO, UserRole.PUBLICO_GENERAL, UserRole.ADMINISTRADOR, UserRole.CONDUCTOR});
        Estilos.estilizarCombo(comboRol);
        JButton btnAsignar = Estilos.botonPrimario("Asignar");
        btnAsignar.addActionListener(e -> {
            int fila = table.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                controller.cambiarRol(usuarios.get(fila).getCedula(), (UserRole) comboRol.getSelectedItem());
                cargar();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        barra.setBackground(Color.WHITE);
        barra.add(Estilos.etiqueta("Nuevo rol para el usuario seleccionado:", Font.PLAIN, 12, Estilos.NAVY));
        barra.add(comboRol);
        barra.add(btnAsignar);

        JPanel contenido = new JPanel(new BorderLayout(0, 12));
        contenido.setBackground(Color.WHITE);
        contenido.add(barra, BorderLayout.NORTH);
        contenido.add(Estilos.estilizarTabla(table), BorderLayout.CENTER);

        cargar();
        Estilos.crearShell(this, "Usuarios", "Gestión de Usuarios", Estilos.cardConTitulo("Usuarios Registrados", contenido));
    }

    private void cargar() {
        usuarios = controller.getUsuarios();
        tableModel.setRowCount(0);
        for (User u : usuarios) {
            tableModel.addRow(new Object[]{u.getNombreCompleto(), u.getCedula(), u.getEmail(), u.getRol()});
        }
    }
}
