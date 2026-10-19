package com.rurex.model;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;

/**
 * Colores, componentes y utilidades visuales compartidas por todas las ventanas.
 */
public final class Estilo {

    private Estilo() { }

    // ---------- Colores ----------
    public static final Color VERDE = new Color(30, 142, 90);
    public static final Color ROJO = new Color(198, 40, 40);
    public static final Color TEXTO = new Color(33, 37, 41);
    public static final Color TEXTO_SUAVE = new Color(108, 117, 125);
    public static final Color FONDO = new Color(243, 244, 246);
    public static final Color GRIS_LATERAL = new Color(233, 235, 238);
    public static final Color BORDE = new Color(222, 225, 230);
    public static final Color BORDE_BOTON = new Color(210, 214, 219);
    public static final Color BORDE_CAMPO = new Color(200, 205, 211);
    public static final Color ENCABEZADO_TABLA = new Color(230, 232, 236);

    // Insignias (tipo de ruta y estado)
    public static final Color INSIGNIA_OSCURA = new Color(27, 33, 51);
    public static final Color INSIGNIA_CLARA_FONDO = new Color(236, 238, 241);
    public static final Color INSIGNIA_CLARA_TEXTO = new Color(108, 117, 125);
    public static final Color INSIGNIA_CLARA_BORDE = new Color(214, 217, 222);
    public static final Color ACTIVA_FONDO = new Color(222, 243, 232);
    public static final Color ACTIVA_TEXTO = new Color(22, 115, 70);
    public static final Color ACTIVA_BORDE = new Color(186, 226, 205);
    public static final Color INACTIVA_FONDO = new Color(252, 234, 234);
    public static final Color INACTIVA_BORDE = new Color(240, 200, 200);

    /** Espacio horizontal entre columnas de los formularios. */
    public static final int SEPARACION = 15;

    // ---------- Utilidades ----------

    public static JLabel etiqueta(String texto, float tamano, boolean negrita, Color color) {
        JLabel label = new JLabel(texto);
        label.setFont(label.getFont().deriveFont(negrita ? Font.BOLD : Font.PLAIN, tamano));
        label.setForeground(color);
        return label;
    }

    /** Etiqueta encima de un campo. */
    public static JPanel campoConEtiqueta(String texto, JComponent campo) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setOpaque(false);
        panel.add(etiqueta(texto, 13f, false, TEXTO), BorderLayout.NORTH);
        panel.add(campo, BorderLayout.CENTER);
        return panel;
    }

    /** Título en negrita con una línea divisoria debajo. */
    public static JPanel cabecera(String titulo) {
        JPanel linea = new JPanel();
        linea.setBackground(BORDE);
        linea.setPreferredSize(new Dimension(1, 1));

        JPanel cabecera = new JPanel(new BorderLayout(0, 12));
        cabecera.setOpaque(false);
        cabecera.add(etiqueta(titulo, 16f, true, TEXTO), BorderLayout.NORTH);
        cabecera.add(linea, BorderLayout.CENTER);
        return cabecera;
    }

    public static void aplicarFuenteGlobal(Font fuente) {
        for (Object clave : Collections.list(UIManager.getDefaults().keys())) {
            if (UIManager.get(clave) instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(clave, new javax.swing.plaf.FontUIResource(fuente));
            }
        }
    }

    // ---------- Componentes ----------

    /** Campo de texto con borde fino, margen interno y texto de pista (placeholder). */
    public static class CampoTexto extends JTextField {
        private String pista;

        public CampoTexto(String pista) {
            this.pista = pista;
            setBackground(Color.WHITE);
            setForeground(TEXTO);
            setCaretColor(TEXTO);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDE_CAMPO),
                    BorderFactory.createEmptyBorder(7, 10, 7, 10)));
        }

        public void setPista(String pista) {
            this.pista = pista;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty() && pista != null && !pista.isEmpty()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setFont(getFont());
                g2.setColor(new Color(150, 155, 160));
                FontMetrics fm = g2.getFontMetrics();
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(pista, getInsets().left, y);
                g2.dispose();
            }
        }
    }

    /** Botón plano con color de fondo, borde opcional y efecto al pasar el mouse. */
    public static class Boton extends JButton {
        private Color fondo;
        private Color borde;
        private boolean encima = false;

        public Boton(String texto, Color fondo, Color colorTexto, Color borde) {
            super(texto);
            this.fondo = fondo;
            this.borde = borde;
            setForeground(colorTexto);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    encima = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    encima = false;
                    repaint();
                }
            });
        }

        /** Cambia los colores del botón (se usa en las celdas de la tabla). */
        public void setEstilo(Color fondo, Color colorTexto, Color borde) {
            this.fondo = fondo;
            this.borde = borde;
            setForeground(colorTexto);
            repaint();
        }

        private static Color oscurecer(Color c) {
            return new Color(Math.max(0, c.getRed() - 18),
                    Math.max(0, c.getGreen() - 18),
                    Math.max(0, c.getBlue() - 18));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(encima ? oscurecer(fondo) : fondo);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
            if (borde != null) {
                g2.setColor(borde);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
            }
            g2.dispose();
            super.paintComponent(g); // dibuja el texto
        }
    }

    /** Etiqueta pequeña con fondo redondeado (para "Urbana", "Activa", etc.). */
    public static class Insignia extends JLabel {
        private Color fondo = Color.WHITE;
        private Color borde = null;

        public Insignia() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(3, 9, 3, 9));
            setFont(getFont().deriveFont(Font.PLAIN, 11.5f));
        }

        public void configurar(String texto, Color fondo, Color colorTexto, Color borde) {
            setText(texto);
            setForeground(colorTexto);
            this.fondo = fondo;
            this.borde = borde;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fondo);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
            if (borde != null) {
                g2.setColor(borde);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
            }
            g2.dispose();
            super.paintComponent(g); // dibuja el texto
        }
    }
}
