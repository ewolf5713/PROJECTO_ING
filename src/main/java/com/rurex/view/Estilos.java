package com.rurex.view;

import com.rurex.controller.AuthController;
import com.rurex.controller.FleetController;
import com.rurex.controller.ItineraryController;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class Estilos {

    public static final Color VERDE = new Color(0x1E, 0x8E, 0x5A);
    public static final Color VERDE_CLARO = new Color(0xE6, 0xF4, 0xEC);
    public static final Color NAVY = new Color(0x1A, 0x22, 0x38);
    public static final Color GRIS = new Color(0x6B, 0x72, 0x80);
    public static final Color FONDO = new Color(0xF4, 0xF5, 0xF8);
    public static final Color BORDE = new Color(0xDA, 0xDD, 0xE3);
    public static final Color BORDE_CAMPO = new Color(0xD1, 0xD5, 0xDB);
    public static final Color SIDEBAR = new Color(0xE5, 0xE7, 0xEB);
    public static final Color SEPARADOR = new Color(0xE5, 0xE7, 0xEB);
    public static final Color ROJO = new Color(0xD3, 0x3A, 0x3A);
    public static final Color AMARILLO = new Color(0xF2, 0xC9, 0x4C);
    public static final Color GRIS_CLARO = new Color(0xF3, 0xF4, 0xF6);

    private static String nombreUsuario = "";
    private static JFrame ventanaInicio;
    private static AuthController authController;
    private static FleetController fleetController;
    private static ItineraryController itineraryController;

    public static void iniciarSesion(String nombre, JFrame inicio, AuthController auth, FleetController fleet, ItineraryController itinerary) {
        nombreUsuario = nombre;
        ventanaInicio = inicio;
        authController = auth;
        fleetController = fleet;
        itineraryController = itinerary;
    }

    public static void iniciarSesion(String nombre, AuthController auth) {
        nombreUsuario = nombre;
        ventanaInicio = null;
        authController = auth;
        fleetController = null;
        itineraryController = null;
    }

    public static Font fuente(int estilo, int tamano) {
        return new Font("SansSerif", estilo, tamano);
    }

    public static JLabel etiqueta(String texto, int estilo, int tamano, Color color) {
        JLabel l = new JLabel(texto);
        l.setFont(fuente(estilo, tamano));
        l.setForeground(color);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }

    private static JButton crearBoton(String texto, Color fondo, Color colorTexto, Color borde, int estilo) {
        JButton b = new JButton(texto);
        b.setUI(new BasicButtonUI());
        b.setContentAreaFilled(true);
        b.setOpaque(true);
        b.setFocusPainted(false);
        b.setBackground(fondo);
        b.setForeground(colorTexto);
        b.setFont(fuente(estilo, 13));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borde, 1),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(b.getPreferredSize().width, 34));
        return b;
    }

    public static JButton botonPrimario(String texto) {
        return crearBoton(texto, VERDE, Color.WHITE, VERDE, Font.PLAIN);
    }

    public static JButton botonOutline(String texto) {
        return crearBoton(texto, Color.WHITE, NAVY, BORDE_CAMPO, Font.PLAIN);
    }

    public static JButton botonPeligro(String texto) {
        return crearBoton(texto, Color.WHITE, ROJO, ROJO, Font.PLAIN);
    }

    public static void anchoCompleto(JComponent c) {
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, c.getPreferredSize().height));
    }

    private static void dibujarSugerencia(JTextComponent c, Graphics g, String sugerencia) {
        if (sugerencia.isEmpty() || c.getDocument().getLength() > 0) return;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(new Color(0x9C, 0xA3, 0xAF));
        g2.setFont(c.getFont());
        FontMetrics fm = g2.getFontMetrics();
        int y = (c.getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(sugerencia, c.getInsets().left, y);
        g2.dispose();
    }

    private static void estilizarCampo(JTextComponent c) {
        c.setFont(fuente(Font.PLAIN, 13));
        c.setForeground(NAVY);
        c.setBackground(Color.WHITE);
        c.setCaretColor(NAVY);
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE_CAMPO, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        c.setPreferredSize(new Dimension(180, 34));
    }

    public static JTextField campo(String sugerencia) {
        JTextField c = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarSugerencia(this, g, sugerencia);
            }
        };
        estilizarCampo(c);
        return c;
    }

    public static JPasswordField campoClave(String sugerencia) {
        JPasswordField c = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarSugerencia(this, g, sugerencia);
            }
        };
        estilizarCampo(c);
        return c;
    }

    public static void estilizarCombo(JComboBox<?> combo) {
        combo.setFont(fuente(Font.PLAIN, 13));
        combo.setBackground(Color.WHITE);
        combo.setForeground(NAVY);
        combo.setPreferredSize(new Dimension(180, 34));
    }

    public static JPanel etiquetaCampo(String texto, JComponent campo) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setBackground(Color.WHITE);
        JLabel l = etiqueta(texto, Font.PLAIN, 12, NAVY);
        p.add(l, BorderLayout.NORTH);
        p.add(campo, BorderLayout.CENTER);
        p.setAlignmentX(Component.CENTER_ALIGNMENT);
        anchoCompleto(p);
        return p;
    }

    public static JPanel filaColumnas(JComponent... columnas) {
        JPanel fila = new JPanel(new GridLayout(1, columnas.length, 12, 0));
        fila.setBackground(Color.WHITE);
        for (JComponent c : columnas) {
            fila.add(c);
        }
        fila.setAlignmentX(Component.CENTER_ALIGNMENT);
        anchoCompleto(fila);
        return fila;
    }

    public static JSeparator separador() {
        JSeparator s = new JSeparator();
        s.setForeground(BORDE);
        s.setBackground(Color.WHITE);
        s.setAlignmentX(Component.CENTER_ALIGNMENT);
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        return s;
    }

    public static JPanel crearCard(int margen) {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE, 1),
                BorderFactory.createEmptyBorder(margen, margen, margen, margen)));
        return card;
    }

    public static JPanel cardConTitulo(String titulo, JComponent contenido) {
        JPanel card = crearCard(16);
        card.setLayout(new BorderLayout(0, 12));
        JPanel encabezado = new JPanel(new BorderLayout(0, 8));
        encabezado.setBackground(Color.WHITE);
        JLabel lbl = etiqueta(titulo, Font.BOLD, 15, NAVY);
        encabezado.add(lbl, BorderLayout.NORTH);
        encabezado.add(separador(), BorderLayout.SOUTH);
        card.add(encabezado, BorderLayout.NORTH);
        card.add(contenido, BorderLayout.CENTER);
        return card;
    }

    private static class CeldaTexto extends DefaultTableCellRenderer {
        private final Color color;

        CeldaTexto(Color color) {
            this.color = color;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
            if (!isSelected) setForeground(color);
            return this;
        }
    }

    public static TableCellRenderer rendererTexto(Color color) {
        return new CeldaTexto(color);
    }

    public static JScrollPane estilizarTabla(JTable tabla) {
        tabla.setRowHeight(34);
        tabla.setFont(fuente(Font.PLAIN, 13));
        tabla.setForeground(NAVY);
        tabla.setBackground(Color.WHITE);
        tabla.setShowVerticalLines(false);
        tabla.setShowHorizontalLines(true);
        tabla.setGridColor(SEPARADOR);
        tabla.setIntercellSpacing(new Dimension(0, 1));
        tabla.setSelectionBackground(VERDE_CLARO);
        tabla.setSelectionForeground(NAVY);
        tabla.setFillsViewportHeight(true);
        tabla.setDefaultRenderer(Object.class, new CeldaTexto(NAVY));

        JTableHeader encabezado = tabla.getTableHeader();
        encabezado.setReorderingAllowed(false);
        encabezado.setPreferredSize(new Dimension(0, 34));
        encabezado.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, false, false, row, column);
                setBackground(SEPARADOR);
                setForeground(NAVY);
                setFont(fuente(Font.BOLD, 13));
                setHorizontalAlignment(SwingConstants.LEFT);
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setBackground(Color.WHITE);
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    public static JLabel crearBadge(String texto) {
        JLabel badge = new JLabel(texto);
        badge.setOpaque(true);
        badge.setFont(fuente(Font.BOLD, 11));
        badge.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        String t = texto.toLowerCase();
        if (t.contains("mantenimiento")) {
            badge.setBackground(AMARILLO);
            badge.setForeground(NAVY);
        } else if (t.contains("fuera")) {
            badge.setBackground(ROJO);
            badge.setForeground(Color.WHITE);
        } else {
            badge.setBackground(VERDE);
            badge.setForeground(Color.WHITE);
        }
        return badge;
    }

    public static TableCellRenderer rendererBadge() {
        return (table, value, isSelected, hasFocus, row, column) -> {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
            p.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            p.add(crearBadge(String.valueOf(value)));
            return p;
        };
    }

    public static class AccionesCelda extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

        public interface Accion {
            void ejecutar(int fila, int boton);
        }

        private final String[] textos;
        private final boolean peligro;
        private final boolean primario;
        private final Accion accion;
        private int filaActual;

        public AccionesCelda(String[] textos, boolean peligro, Accion accion) {
            this.textos = textos;
            this.peligro = peligro;
            this.primario = false;
            this.accion = accion;
        }

        public AccionesCelda(String[] textos, Accion accion) {
            this.textos = textos;
            this.peligro = false;
            this.primario = true;
            this.accion = accion;
        }

        private JPanel crearPanel(JTable tabla, boolean seleccionada, boolean activo) {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 3));
            p.setBackground(seleccionada ? tabla.getSelectionBackground() : tabla.getBackground());
            for (int i = 0; i < textos.length; i++) {
                JButton b = primario ? botonPrimario(textos[i]) : peligro ? botonPeligro(textos[i]) : botonOutline(textos[i]);
                b.setFont(fuente(Font.PLAIN, 12));
                b.setPreferredSize(new Dimension(b.getPreferredSize().width, 26));
                if (activo) {
                    final int indice = i;
                    b.addActionListener(e -> {
                        int fila = filaActual;
                        cancelCellEditing();
                        accion.ejecutar(fila, indice);
                    });
                }
                p.add(b);
            }
            return p;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return crearPanel(table, isSelected, false);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            filaActual = row;
            return crearPanel(table, isSelected, true);
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

    public static boolean validarEntero(Component padre, String texto, String mensaje) {
        try {
            Integer.parseInt(texto.trim());
            return true;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(padre, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static JPanel crearFormulario() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        return form;
    }

    public static void agregarCampo(JPanel form, String texto, JComponent campo) {
        form.add(etiquetaCampo(texto, campo));
        form.add(Box.createVerticalStrut(12));
    }

    public static JDialog crearDialogo(JFrame owner, String titulo, JPanel cuerpo, String textoGuardar, BooleanSupplier validar, Runnable alGuardar) {
        JDialog dialogo = new JDialog(owner, titulo, true);
        JPanel raiz = new JPanel(new BorderLayout(0, 8));
        raiz.setBackground(Color.WHITE);
        raiz.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        raiz.add(cuerpo, BorderLayout.CENTER);

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        pie.setBackground(Color.WHITE);
        JButton btnGuardar = botonPrimario(textoGuardar);
        JButton btnCancelar = botonOutline("Cancelar");
        btnGuardar.addActionListener(e -> {
            if (validar.getAsBoolean()) {
                dialogo.dispose();
                alGuardar.run();
            }
        });
        btnCancelar.addActionListener(e -> dialogo.dispose());
        pie.add(btnGuardar);
        pie.add(btnCancelar);
        raiz.add(pie, BorderLayout.SOUTH);

        dialogo.setContentPane(raiz);
        dialogo.setResizable(false);
        dialogo.pack();
        dialogo.setSize(400, dialogo.getHeight());
        dialogo.setLocationRelativeTo(owner);
        return dialogo;
    }

    public static void crearShell(JFrame ventana, String activo, String titulo, JComponent contenido) {
        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(FONDO);
        raiz.add(crearSidebar(ventana, activo), BorderLayout.WEST);

        JPanel derecha = new JPanel(new BorderLayout());
        derecha.setBackground(FONDO);
        derecha.add(crearEncabezado(titulo), BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout());
        cuerpo.setBackground(FONDO);
        cuerpo.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        cuerpo.add(contenido, BorderLayout.CENTER);
        derecha.add(cuerpo, BorderLayout.CENTER);

        raiz.add(derecha, BorderLayout.CENTER);
        ventana.setContentPane(raiz);
    }

    private static JPanel crearEncabezado(String titulo) {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(Color.WHITE);
        barra.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDE),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));

        JLabel lblTitulo = etiqueta(titulo, Font.BOLD, 18, NAVY);
        barra.add(lblTitulo, BorderLayout.WEST);

        JPanel usuario = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        usuario.setBackground(Color.WHITE);
        usuario.add(etiqueta("Usuario:", Font.PLAIN, 13, GRIS));
        usuario.add(etiqueta(nombreUsuario, Font.BOLD, 13, NAVY));
        barra.add(usuario, BorderLayout.EAST);
        return barra;
    }

    private static JButton crearBotonNav(String texto, boolean activo) {
        JButton b = activo
                ? crearBoton(texto, VERDE, Color.WHITE, VERDE, Font.PLAIN)
                : crearBoton(texto, Color.WHITE, NAVY, BORDE, Font.PLAIN);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        return b;
    }

    public static Consumer<String> crearShellNav(JFrame ventana, String titulo, String[] items, Consumer<String> alSeleccionar, JComponent contenido) {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(SIDEBAR);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.add(crearCabeceraSidebar(), BorderLayout.NORTH);

        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBackground(SIDEBAR);
        nav.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        Map<String, JButton> botones = new LinkedHashMap<>();
        Consumer<String> irA = nombre -> {
            for (Map.Entry<String, JButton> entrada : botones.entrySet()) {
                pintarBotonNav(entrada.getValue(), entrada.getKey().equals(nombre));
            }
            alSeleccionar.accept(nombre);
        };
        for (String item : items) {
            JButton boton = crearBotonNav(item, false);
            boton.addActionListener(e -> irA.accept(item));
            botones.put(item, boton);
            nav.add(boton);
            nav.add(Box.createVerticalStrut(4));
        }
        sidebar.add(nav, BorderLayout.CENTER);
        sidebar.add(crearPieSidebar(ventana), BorderLayout.SOUTH);

        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(FONDO);
        raiz.add(sidebar, BorderLayout.WEST);

        JPanel derecha = new JPanel(new BorderLayout());
        derecha.setBackground(FONDO);
        derecha.add(crearEncabezado(titulo), BorderLayout.NORTH);
        JPanel cuerpo = new JPanel(new BorderLayout());
        cuerpo.setBackground(FONDO);
        cuerpo.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        cuerpo.add(contenido, BorderLayout.CENTER);
        derecha.add(cuerpo, BorderLayout.CENTER);
        raiz.add(derecha, BorderLayout.CENTER);
        ventana.setContentPane(raiz);

        irA.accept(items[0]);
        return irA;
    }

    private static void pintarBotonNav(JButton b, boolean activo) {
        b.setBackground(activo ? VERDE : Color.WHITE);
        b.setForeground(activo ? Color.WHITE : NAVY);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(activo ? VERDE : BORDE, 1),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)));
    }

    private static JPanel crearCabeceraSidebar() {
        JPanel cabecera = new JPanel();
        cabecera.setLayout(new BoxLayout(cabecera, BoxLayout.Y_AXIS));
        cabecera.setBackground(Color.WHITE);
        cabecera.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDE),
                BorderFactory.createEmptyBorder(16, 14, 12, 14)));
        JLabel lblLogo = etiqueta("Transporte UCV", Font.BOLD, 15, NAVY);
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblSub = etiqueta("Sistema de Gestión", Font.PLAIN, 11, GRIS);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        cabecera.add(lblLogo);
        cabecera.add(Box.createVerticalStrut(2));
        cabecera.add(lblSub);
        return cabecera;
    }

    private static JPanel crearPieSidebar(JFrame ventana) {
        JPanel pie = new JPanel(new BorderLayout());
        pie.setBackground(SIDEBAR);
        pie.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDE),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        JButton btnLogout = crearBoton("Cerrar Sesión", Color.WHITE, ROJO, BORDE, Font.PLAIN);
        btnLogout.addActionListener(e -> {
            ventana.dispose();
            if (ventanaInicio != null && ventanaInicio != ventana) ventanaInicio.dispose();
            authController.volverLogin();
        });
        pie.add(btnLogout, BorderLayout.CENTER);
        return pie;
    }

    public static void cerrarVolviendoAlInicio(JFrame ventana) {
        ventana.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        ventana.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                ventana.setVisible(false);
                if (ventanaInicio != null) ventanaInicio.setVisible(true);
            }
        });
    }

    private static JPanel crearSidebar(JFrame ventana, String activo) {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(SIDEBAR);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.add(crearCabeceraSidebar(), BorderLayout.NORTH);

        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBackground(SIDEBAR);
        nav.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JButton btnInicio = crearBotonNav("Inicio", activo.equals("Inicio"));
        JButton btnUnidades = crearBotonNav("Unidades", activo.equals("Unidades"));
        JButton btnItinerarios = crearBotonNav("Itinerarios", activo.equals("Itinerarios"));

        btnInicio.addActionListener(e -> {
            if (activo.equals("Inicio") || ventanaInicio == null) return;
            ventanaInicio.setVisible(true);
            ventana.setVisible(false);
        });
        btnUnidades.addActionListener(e -> {
            if (activo.equals("Unidades")) return;
            fleetController.mostrarVista();
            ventana.setVisible(false);
        });
        btnItinerarios.addActionListener(e -> {
            if (activo.equals("Itinerarios")) return;
            itineraryController.mostrarVista();
            ventana.setVisible(false);
        });

        nav.add(btnInicio);
        nav.add(Box.createVerticalStrut(4));
        nav.add(btnUnidades);
        nav.add(Box.createVerticalStrut(4));
        nav.add(btnItinerarios);
        sidebar.add(nav, BorderLayout.CENTER);

        sidebar.add(crearPieSidebar(ventana), BorderLayout.SOUTH);

        return sidebar;
    }
}
