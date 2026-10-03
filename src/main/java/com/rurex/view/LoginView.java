package com.rurex.view;

import com.rurex.controller.AuthController;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    private final AuthController controller;
    private final JTextField txtUser;
    private final JPasswordField txtPass;

    public LoginView(AuthController controller) {
        this.controller = controller;
        this.controller.setLoginView(this);

        setTitle("Transporte UCV - Inicio de Sesion");
        setSize(1000, 650);
        setMinimumSize(new Dimension(800, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(241, 243, 247));
        add(mainPanel);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(460, 480));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 224, 230), 1),
                BorderFactory.createEmptyBorder(35, 40, 35, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblTitle = new JLabel("Transporte UCV", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitle.setForeground(new Color(24, 32, 56));
        card.add(lblTitle, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(6, 0, 0, 0);
        JLabel lblSub = new JLabel("Sistema de gestion de transporte universitario", SwingConstants.CENTER);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSub.setForeground(new Color(110, 118, 135));
        card.add(lblSub, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(18, 0, 18, 0);
        JSeparator sep = new JSeparator();
        card.add(sep, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 6, 0);
        JLabel lblUser = new JLabel("Correo institucional o Cedula:");
        lblUser.setFont(new Font("SansSerif", Font.PLAIN, 13));
        card.add(lblUser, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 16, 0);
        txtUser = new JTextField();
        txtUser.setPreferredSize(new Dimension(380, 40));
        txtUser.setFont(new Font("SansSerif", Font.PLAIN, 14));
        card.add(txtUser, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 6, 0);
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("SansSerif", Font.PLAIN, 13));
        card.add(lblPassword, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 24, 0);
        txtPass = new JPasswordField();
        txtPass.setPreferredSize(new Dimension(380, 40));
        txtPass.setFont(new Font("SansSerif", Font.PLAIN, 14));
        card.add(txtPass, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 12, 0);
        JButton btnLogin = new JButton("Iniciar Sesion");
        btnLogin.setPreferredSize(new Dimension(380, 44));
        btnLogin.setBackground(new Color(27, 135, 84));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        card.add(btnLogin, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        JButton btnRegister = new JButton("Crear Cuenta");
        btnRegister.setPreferredSize(new Dimension(380, 40));
        btnRegister.setBackground(Color.WHITE);
        btnRegister.setForeground(new Color(40, 45, 55));
        btnRegister.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnRegister.setFocusPainted(false);
        card.add(btnRegister, gbc);

        btnLogin.addActionListener(e -> controller.login(txtUser.getText(), new String(txtPass.getPassword())));
        btnRegister.addActionListener(e -> controller.mostrarRegistro());

        mainPanel.add(card);
    }
}
