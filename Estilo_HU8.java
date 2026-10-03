import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

//Aca ocurre el diseño, colores y fuentes
final class Estilo_HU8 {
static final Color VERDE = new Color(0x1F8F5A);
    static final Color VERDE_CLARO = new Color(0xEEFAF2);
    static final Color FONDO = new Color(0xF4F6FA);
    static final Color BORDE = new Color(0xD9DDE3);
    static final Color TEXTO = new Color(0x1E2430);
    static final Color TEXTO_SUAVE = new Color(0x6B7280);
    static final Color AMARILLO = new Color(0xF5C84B);
    static final Color GRIS_BADGE = new Color(0x6B7280);
 
    static final Font BASE = new Font("SansSerif", Font.PLAIN, 12);
    static final Font NEGRITA = BASE.deriveFont(Font.BOLD);
    static final Font PEQUENA = BASE.deriveFont(11f);
    static final Font TITULO_TARJETA = BASE.deriveFont(16f);
    static final Font TITULO_PANTALLA = BASE.deriveFont(Font.BOLD, 18f);
 
    private Estilo_HU8() { }
 
    static JLabel etiqueta(String texto, Font fuente, Color color) {
        JLabel l = new JLabel(texto);
        l.setFont(fuente);
        l.setForeground(color);
        return l;
    }
 
    /** Par "etiqueta pequena gris / valor en negrita" usado en Informacion del Viaje. */
    static JPanel campo(String etiqueta, String valor) {
        JPanel p = new JPanel(new GridLayout(2, 1, 0, 2));
        p.setOpaque(false);
        p.add(etiqueta(etiqueta, PEQUENA, TEXTO_SUAVE));
        p.add(etiqueta(valor, BASE, TEXTO));
        return p;
    }
 
    /** Cuadro numerado (badge) para pasos y paradas. */
    static JLabel badge(String texto, Color fondo, Color letra, Color borde, int lado) {
        JLabel l = new JLabel(texto, SwingConstants.CENTER);
        l.setOpaque(true);
        l.setBackground(fondo);
        l.setForeground(letra);
        l.setFont(PEQUENA.deriveFont(Font.BOLD));
        l.setBorder(BorderFactory.createLineBorder(borde));
        Dimension d = new Dimension(lado, lado);
        l.setPreferredSize(d);
        l.setMinimumSize(d);
        l.setMaximumSize(d);
        return l;
    }
 
    /** Tarjeta blanca con titulo y separador; el contenido va en {@link #cuerpo}. */
    static class Tarjeta extends JPanel {
        final JPanel cuerpo = new JPanel(new BorderLayout());
 
        Tarjeta(String titulo) {
            super(new BorderLayout(0, 12));
            setBackground(Color.WHITE);
            setAlignmentX(LEFT_ALIGNMENT);
            setBorder(new CompoundBorder(new LineBorder(BORDE), new EmptyBorder(14, 14, 14, 14)));
 
            JPanel cabecera = new JPanel(new BorderLayout(0, 8));
            cabecera.setOpaque(false);
            cabecera.add(etiqueta(titulo, TITULO_TARJETA, TEXTO), BorderLayout.NORTH);
            cabecera.add(new JSeparator(), BorderLayout.SOUTH);
 
            cuerpo.setOpaque(false);
            add(cabecera, BorderLayout.NORTH);
            add(cuerpo, BorderLayout.CENTER);
        }
 
        @Override
        public Dimension getMaximumSize() {
            return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
        }
    }
 
    /** Barra de progreso fina del stepper. */
    static class Barra extends JComponent {
        private final double fraccion;
 
        Barra(double fraccion) {
            this.fraccion = Math.max(0, Math.min(1, fraccion));
            setPreferredSize(new Dimension(100, 4));
        }
 
        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(BORDE);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(VERDE);
            g.fillRect(0, 0, (int) (getWidth() * fraccion), getHeight());
        }
    }
 
    static JButton botonPrimario(String texto) {
        JButton b = new JButton(texto);
        b.setBackground(VERDE);
        b.setForeground(Color.WHITE);
        b.setFont(NEGRITA);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorder(new EmptyBorder(8, 18, 8, 18));
        return b;
    }  
}