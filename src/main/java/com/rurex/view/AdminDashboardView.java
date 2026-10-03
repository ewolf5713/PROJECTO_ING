package com.rurex.view;

import com.rurex.controller.AuthController;
import com.rurex.controller.FleetController;
import com.rurex.controller.ItineraryController;
import com.rurex.model.User;

import javax.swing.*;
import java.awt.*;

public class AdminDashboardView extends JFrame {

    public AdminDashboardView(User user, AuthController authController, FleetController fleetController, ItineraryController itineraryController) {
        setTitle("Transporte UCV - Panel Administrador");
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(241, 243, 247));

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(240, 700));
        sidebar.setBackground(Color.WHITE);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 224, 230)));

        JLabel lblLogo = new JLabel("Transporte UCV");
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblLogo.setForeground(new Color(24, 32, 56));
        lblLogo.setBorder(BorderFactory.createEmptyBorder(25, 25, 5, 25));

        JLabel lblSub = new JLabel("Sistema de Gestion");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSub.setForeground(new Color(110, 118, 135));
        lblSub.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));

        sidebar.add(lblLogo);
        sidebar.add(lblSub);

        JButton btnInicio = createSidebarButton("Inicio", true);
        JButton btnUnidades = createSidebarButton("Gestion de Unidades", false);
        JButton btnItinerarios = createSidebarButton("Gestion de Itinerarios", false);
        JButton btnLogout = new JButton("Cerrar Sesion");

        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogout.setMaximumSize(new Dimension(190, 40));
        btnLogout.setPreferredSize(new Dimension(190, 40));
        btnLogout.setForeground(new Color(190, 40, 40));
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnLogout.setFocusPainted(false);

        btnUnidades.addActionListener(e -> fleetController.mostrarVista());
        btnItinerarios.addActionListener(e -> itineraryController.mostrarVista());
        btnLogout.addActionListener(e -> {
            dispose();
            authController.volverLogin();
        });

        sidebar.add(btnInicio);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnUnidades);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnItinerarios);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnLogout);
        sidebar.add(Box.createVerticalStrut(25));

        mainPanel.add(sidebar, BorderLayout.WEST);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));
        content.setBackground(new Color(241, 243, 247));

        JLabel lblWelcome = new JLabel("Panel de Administracion — " + user.getNombreCompleto());
        lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblWelcome.setForeground(new Color(24, 32, 56));
        content.add(lblWelcome);

        content.add(Box.createVerticalStrut(25));

        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        cardsPanel.setBackground(new Color(241, 243, 247));
        cardsPanel.setMaximumSize(new Dimension(1200, 130));

        cardsPanel.add(createCard("Unidades Activas", "24", "En ruta operativa"));
        cardsPanel.add(createCard("En Mantenimiento", "3", "Revision programada"));
        cardsPanel.add(createCard("Reservas de Hoy", "187", "Confirmadas"));

        content.add(cardsPanel);

        content.add(Box.createVerticalStrut(35));

        JPanel actionsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        actionsPanel.setBackground(new Color(241, 243, 247));
        actionsPanel.setMaximumSize(new Dimension(1200, 50));

        JButton btnOpenFleet = new JButton("Gestion de Unidades (Flota)");
        btnOpenFleet.setBackground(new Color(27, 135, 84));
        btnOpenFleet.setForeground(Color.WHITE);
        btnOpenFleet.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnOpenFleet.setFocusPainted(false);
        btnOpenFleet.addActionListener(e -> fleetController.mostrarVista());
        actionsPanel.add(btnOpenFleet);

        JButton btnOpenItin = new JButton("Gestion de Itinerarios");
        btnOpenItin.setBackground(new Color(24, 32, 56));
        btnOpenItin.setForeground(Color.WHITE);
        btnOpenItin.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnOpenItin.setFocusPainted(false);
        btnOpenItin.addActionListener(e -> itineraryController.mostrarVista());
        actionsPanel.add(btnOpenItin);

        content.add(actionsPanel);

        mainPanel.add(content, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JButton createSidebarButton(String text, boolean active) {
        JButton b = new JButton(text);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(190, 40));
        b.setPreferredSize(new Dimension(190, 40));
        b.setFont(new Font("SansSerif", Font.PLAIN, 13));
        b.setFocusPainted(false);
        if (active) {
            b.setBackground(new Color(27, 135, 84));
            b.setForeground(Color.WHITE);
        } else {
            b.setBackground(Color.WHITE);
            b.setForeground(new Color(40, 45, 55));
        }
        return b;
    }

    private JPanel createCard(String title, String value, String sub) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 224, 230), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel t = new JLabel(title);
        t.setFont(new Font("SansSerif", Font.PLAIN, 13));
        t.setForeground(new Color(110, 118, 135));
        p.add(t);

        p.add(Box.createVerticalStrut(8));

        JLabel v = new JLabel(value);
        v.setFont(new Font("SansSerif", Font.BOLD, 32));
        v.setForeground(new Color(24, 32, 56));
        p.add(v);

        p.add(Box.createVerticalStrut(4));

        JLabel s = new JLabel(sub);
        s.setFont(new Font("SansSerif", Font.PLAIN, 12));
        s.setForeground(new Color(27, 135, 84));
        p.add(s);

        return p;
    }
}
