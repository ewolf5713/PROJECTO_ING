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

        setTitle("Transporte UCV - Inicio de Sesión");
        setSize(1000, 650);
        setMinimumSize(new Dimension(800, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Estilos.FONDO);
        setContentPane(mainPanel);

        JPanel card = Estilos.crearCard(24);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        card.add(Estilos.etiqueta("Transporte UCV", Font.BOLD, 20, Estilos.NAVY));
        card.add(Box.createVerticalStrut(4));
        card.add(Estilos.etiqueta("Sistema de gestión de transporte universitario", Font.PLAIN, 12, Estilos.GRIS));
        card.add(Box.createVerticalStrut(14));
        card.add(Estilos.separador());
        card.add(Box.createVerticalStrut(16));

        txtUser = Estilos.campo("ejemplo@ucv.ve o V-12345678");
        txtPass = Estilos.campoClave("Ingrese su contraseña");

        card.add(Estilos.etiquetaCampo("Correo o Cédula:", txtUser));
        card.add(Box.createVerticalStrut(12));
        card.add(Estilos.etiquetaCampo("Contraseña:", txtPass));
        card.add(Box.createVerticalStrut(20));

        JButton btnLogin = Estilos.botonPrimario("Iniciar Sesión");
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        Estilos.anchoCompleto(btnLogin);
        card.add(btnLogin);
        getRootPane().setDefaultButton(btnLogin);
        card.add(Box.createVerticalStrut(8));

        JButton btnRegister = Estilos.botonOutline("Crear Cuenta");
        btnRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        Estilos.anchoCompleto(btnRegister);
        card.add(btnRegister);

        card.setPreferredSize(new Dimension(360, card.getPreferredSize().height));

        btnLogin.addActionListener(e -> controller.login(txtUser.getText(), new String(txtPass.getPassword())));
        btnRegister.addActionListener(e -> controller.mostrarRegistro());

        mainPanel.add(card);
    }
}
