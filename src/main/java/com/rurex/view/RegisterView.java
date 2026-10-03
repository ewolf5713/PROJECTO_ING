package com.rurex.view;

import com.rurex.controller.AuthController;
import com.rurex.model.UserRole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RegisterView extends JFrame {

    private final AuthController controller;

    public RegisterView(AuthController controller) {
        this.controller = controller;
        this.controller.setRegisterView(this);

        setTitle("Transporte UCV - Registro de Usuario");
        setSize(1000, 750);
        setMinimumSize(new Dimension(850, 650));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.volverLogin();
            }
        });

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(241, 243, 247));
        add(mainPanel);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(600, 640));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 224, 230), 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        JLabel lblTitle = new JLabel("Registro de Nuevo Usuario");
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(new Color(24, 32, 56));
        card.add(lblTitle);

        card.add(Box.createVerticalStrut(4));

        JLabel lblSub = new JLabel("Complete el formulario para crear su cuenta institucional");
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSub.setForeground(new Color(110, 118, 135));
        card.add(lblSub);

        card.add(Box.createVerticalStrut(16));

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(520, 2));
        card.add(sep);

        card.add(Box.createVerticalStrut(16));

        JPanel row1 = new JPanel(new GridLayout(1, 2, 20, 0));
        row1.setBackground(Color.WHITE);
        row1.setMaximumSize(new Dimension(520, 60));

        JPanel colNombre = new JPanel(new BorderLayout(0, 4));
        colNombre.setBackground(Color.WHITE);
        colNombre.add(new JLabel("Nombre y Apellido:"), BorderLayout.NORTH);
        JTextField txtNombre = new JTextField();
        txtNombre.setPreferredSize(new Dimension(240, 36));
        txtNombre.setFont(new Font("SansSerif", Font.PLAIN, 13));
        colNombre.add(txtNombre, BorderLayout.CENTER);

        JPanel colCedula = new JPanel(new BorderLayout(0, 4));
        colCedula.setBackground(Color.WHITE);
        colCedula.add(new JLabel("Cedula:"), BorderLayout.NORTH);
        JTextField txtCedula = new JTextField();
        txtCedula.setPreferredSize(new Dimension(240, 36));
        txtCedula.setFont(new Font("SansSerif", Font.PLAIN, 13));
        colCedula.add(txtCedula, BorderLayout.CENTER);

        row1.add(colNombre);
        row1.add(colCedula);
        card.add(row1);

        card.add(Box.createVerticalStrut(12));

        JPanel colEmail = new JPanel(new BorderLayout(0, 4));
        colEmail.setBackground(Color.WHITE);
        colEmail.setMaximumSize(new Dimension(520, 60));
        colEmail.add(new JLabel("Correo institucional (@ucv.ve):"), BorderLayout.NORTH);
        JTextField txtEmail = new JTextField();
        txtEmail.setPreferredSize(new Dimension(520, 36));
        txtEmail.setFont(new Font("SansSerif", Font.PLAIN, 13));
        colEmail.add(txtEmail, BorderLayout.CENTER);
        card.add(colEmail);

        card.add(Box.createVerticalStrut(12));

        JPanel colRol = new JPanel(new BorderLayout(0, 4));
        colRol.setBackground(Color.WHITE);
        colRol.setMaximumSize(new Dimension(520, 60));
        colRol.add(new JLabel("Tipo de Usuario:"), BorderLayout.NORTH);
        // Excluimos ADMINISTRADOR del autorregistro publico por seguridad
        UserRole[] rolesPermitidos = new UserRole[]{UserRole.ESTUDIANTE, UserRole.EMPLEADO, UserRole.CONDUCTOR};
        JComboBox<UserRole> comboRol = new JComboBox<>(rolesPermitidos);
        comboRol.setPreferredSize(new Dimension(520, 36));
        comboRol.setFont(new Font("SansSerif", Font.PLAIN, 13));
        colRol.add(comboRol, BorderLayout.CENTER);
        card.add(colRol);

        card.add(Box.createVerticalStrut(12));

        JPanel colCarnet = new JPanel(new BorderLayout(0, 4));
        colCarnet.setBackground(Color.WHITE);
        colCarnet.setMaximumSize(new Dimension(520, 60));
        colCarnet.add(new JLabel("Carnet Estudiantil (si aplica):"), BorderLayout.NORTH);
        JTextField txtCarnet = new JTextField();
        txtCarnet.setPreferredSize(new Dimension(520, 36));
        txtCarnet.setFont(new Font("SansSerif", Font.PLAIN, 13));
        colCarnet.add(txtCarnet, BorderLayout.CENTER);
        card.add(colCarnet);

        card.add(Box.createVerticalStrut(12));

        JPanel rowPass = new JPanel(new GridLayout(1, 2, 20, 0));
        rowPass.setBackground(Color.WHITE);
        rowPass.setMaximumSize(new Dimension(520, 60));

        JPanel colPass = new JPanel(new BorderLayout(0, 4));
        colPass.setBackground(Color.WHITE);
        colPass.add(new JLabel("Contraseña:"), BorderLayout.NORTH);
        JPasswordField txtPass = new JPasswordField();
        txtPass.setPreferredSize(new Dimension(240, 36));
        txtPass.setFont(new Font("SansSerif", Font.PLAIN, 13));
        colPass.add(txtPass, BorderLayout.CENTER);

        JPanel colPassConfirm = new JPanel(new BorderLayout(0, 4));
        colPassConfirm.setBackground(Color.WHITE);
        colPassConfirm.add(new JLabel("Confirmar Contraseña:"), BorderLayout.NORTH);
        JPasswordField txtPassConfirm = new JPasswordField();
        txtPassConfirm.setPreferredSize(new Dimension(240, 36));
        txtPassConfirm.setFont(new Font("SansSerif", Font.PLAIN, 13));
        colPassConfirm.add(txtPassConfirm, BorderLayout.CENTER);

        rowPass.add(colPass);
        rowPass.add(colPassConfirm);
        card.add(rowPass);

        card.add(Box.createVerticalStrut(24));

        JPanel rowButtons = new JPanel(new GridLayout(1, 2, 20, 0));
        rowButtons.setBackground(Color.WHITE);
        rowButtons.setMaximumSize(new Dimension(520, 42));

        JButton btnRegistrar = new JButton("Registrarse");
        btnRegistrar.setBackground(new Color(27, 135, 84));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnRegistrar.setFocusPainted(false);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(Color.WHITE);
        btnCancelar.setForeground(new Color(80, 85, 95));
        btnCancelar.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnCancelar.setFocusPainted(false);

        rowButtons.add(btnRegistrar);
        rowButtons.add(btnCancelar);
        card.add(rowButtons);

        btnCancelar.addActionListener(e -> controller.volverLogin());

        btnRegistrar.addActionListener(e -> controller.registrar(
                txtNombre.getText(),
                txtEmail.getText(),
                txtCedula.getText(),
                (UserRole) comboRol.getSelectedItem(),
                txtCarnet.getText(),
                new String(txtPass.getPassword()),
                new String(txtPassConfirm.getPassword())
        ));

        mainPanel.add(card);
    }
}
