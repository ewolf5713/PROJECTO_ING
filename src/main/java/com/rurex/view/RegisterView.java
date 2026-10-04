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
        mainPanel.setBackground(Estilos.FONDO);
        setContentPane(mainPanel);

        JPanel card = Estilos.crearCard(16);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel lblTitle = Estilos.etiqueta("Registro de Nuevo Usuario", Font.BOLD, 18, Estilos.NAVY);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
        JLabel lblSub = Estilos.etiqueta("Complete el formulario para crear su cuenta", Font.PLAIN, 12, Estilos.GRIS);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSub.setHorizontalAlignment(SwingConstants.LEFT);
        lblTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, lblTitle.getPreferredSize().height));
        lblSub.setMaximumSize(new Dimension(Integer.MAX_VALUE, lblSub.getPreferredSize().height));
        card.add(Box.createHorizontalStrut(480));
        card.add(lblTitle);
        card.add(Box.createVerticalStrut(4));
        card.add(lblSub);
        card.add(Box.createVerticalStrut(12));
        card.add(Estilos.separador());
        card.add(Box.createVerticalStrut(12));

        JTextField txtNombre = Estilos.campo("Juan Pérez");
        JTextField txtCedula = Estilos.campo("V-12345678");
        JTextField txtEmail = Estilos.campo("ejemplo@ucv.ve");
        JTextField txtCarnet = Estilos.campo("20-12345");
        JPasswordField txtPass = Estilos.campoClave("Mínimo 8 caracteres");
        JPasswordField txtPassConfirm = Estilos.campoClave("Repita su contraseña");

        card.add(Estilos.filaColumnas(
                Estilos.etiquetaCampo("Nombre y Apellido:", txtNombre),
                Estilos.etiquetaCampo("Cédula:", txtCedula)));
        card.add(Box.createVerticalStrut(10));
        card.add(Estilos.etiquetaCampo("Correo Electrónico:", txtEmail));
        card.add(Box.createVerticalStrut(10));

        JList<UserRole> listaRol = new JList<>(UserRole.values());
        listaRol.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaRol.setSelectedIndex(0);
        listaRol.setFixedCellHeight(28);
        listaRol.setBorder(BorderFactory.createLineBorder(Estilos.BORDE_CAMPO, 1));
        listaRol.setPreferredSize(new Dimension(260, 4 * 28 + 2));
        listaRol.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, false, false);
                l.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                l.setFont(Estilos.fuente(isSelected ? Font.BOLD : Font.PLAIN, 12));
                if (isSelected) {
                    l.setText("●  " + value);
                    l.setBackground(Estilos.VERDE_CLARO);
                    l.setForeground(Estilos.VERDE);
                } else {
                    l.setBackground(Color.WHITE);
                    l.setForeground(Estilos.NAVY);
                }
                return l;
            }
        });
        listaRol.addListSelectionListener(e -> {
            if (listaRol.isSelectionEmpty()) listaRol.setSelectedIndex(0);
        });

        JPanel contenedorLista = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        contenedorLista.setBackground(Color.WHITE);
        contenedorLista.add(listaRol);
        card.add(Estilos.etiquetaCampo("Tipo de Usuario:", contenedorLista));
        card.add(Box.createVerticalStrut(10));

        card.add(Estilos.etiquetaCampo("Carnet Estudiantil (si aplica):", txtCarnet));
        card.add(Box.createVerticalStrut(10));

        card.add(Estilos.filaColumnas(
                Estilos.etiquetaCampo("Contraseña:", txtPass),
                Estilos.etiquetaCampo("Confirmar Contraseña:", txtPassConfirm)));
        card.add(Box.createVerticalStrut(14));
        card.add(Estilos.separador());
        card.add(Box.createVerticalStrut(12));

        JButton btnRegistrar = Estilos.botonPrimario("Registrarse");
        JButton btnCancelar = Estilos.botonOutline("Cancelar");
        card.add(Estilos.filaColumnas(btnRegistrar, btnCancelar));

        card.setPreferredSize(new Dimension(520, card.getPreferredSize().height));

        btnCancelar.addActionListener(e -> controller.volverLogin());

        btnRegistrar.addActionListener(e -> controller.registrar(
                txtNombre.getText(),
                txtEmail.getText(),
                txtCedula.getText(),
                listaRol.getSelectedValue(),
                txtCarnet.getText(),
                new String(txtPass.getPassword()),
                new String(txtPassConfirm.getPassword())
        ));

        mainPanel.add(card);
    }
}
