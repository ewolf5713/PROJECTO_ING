import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Vista principal "Gestión de Unidades" (diseño de Figma) con edición y cambio de estado (HU-024).
 * No incluye el pop up de registro: se conecta con setAccionNuevaUnidad(...).
 */
public class VistaUnidades_HU24 extends JFrame {
    private static final Color VERDE = new Color(0x1E8E5A);
    private static final Color AMARILLO = new Color(0xF2C94C);
    private static final Color ROJO = new Color(0xD64545);
    private static final Color FONDO = new Color(0xF5F6FA);
    private static final Color BORDE = new Color(0xDADCE2);
    private static final Color TEXTO = new Color(0x1F2937);
    private static final Color TEXTO_SUAVE = new Color(0x6B7280);
    private static final int COL_ACCIONES = 4;
    private static final String[] SECCIONES =
            {"Inicio", "Unidades", "Rutas", "Conductores", "Reportes", "Mantenimiento"};

    private final UnidadServicio_HU24 servicio;
    private final DefaultTableModel modeloTabla;
    private final JTable tabla;
    private final List<Unidad_HU24> filas = new ArrayList<>();

    private Runnable accionNuevaUnidad = () -> {};
    private Runnable accionCerrarSesion = () -> {};
    private Consumer<String> accionNavegar = seccion -> {};
    private Consumer<Unidad_HU24> accionEditar = unidad -> {};
    private Consumer<Unidad_HU24> accionCambiarEstado = unidad -> {};

    public VistaUnidades_HU24(UnidadServicio_HU24 servicio, String usuario) {
        super("Transporte UCV - Gestión de Unidades");
        this.servicio = servicio;

        modeloTabla = new DefaultTableModel(
                new String[]{"Placa", "Modelo", "Capacidad", "Estado", "Acciones"}, 0) {
            @Override public boolean isCellEditable(int fila, int columna) { return false; }
        };
        tabla = crearTabla();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 680);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(crearBarraLateral(), BorderLayout.WEST);
        add(crearContenido(usuario), BorderLayout.CENTER);

        refrescar();

        // HU-024: Editar y Cambiar Estado abren sus pop ups y refrescan la tabla al guardar
        accionEditar = unidad ->
                new DialogoEditarUnidad_HU24(this, servicio, unidad, this::refrescar).setVisible(true);
        accionCambiarEstado = unidad ->
                new DialogoCambiarEstado_HU24(this, servicio, unidad, this::refrescar).setVisible(true);
    }

    // ---------- API pública ----------

    /** Vuelve a cargar la tabla desde el servicio (llamar tras registrar o cambiar una unidad). */
    public void refrescar() {
        modeloTabla.setRowCount(0);
        filas.clear();
        for (Unidad_HU24 u : servicio.listar()) {
            filas.add(u);
            modeloTabla.addRow(new Object[]{
                    u.getPlaca(), u.getModelo(), u.getCapacidad() + " pasajeros", u.getEstado(), ""});
        }
    }

    public void setAccionNuevaUnidad(Runnable accion) { this.accionNuevaUnidad = accion; }
    public void setAccionCerrarSesion(Runnable accion) { this.accionCerrarSesion = accion; }
    public void setAccionNavegar(Consumer<String> accion) { this.accionNavegar = accion; }
    public void setAccionEditar(Consumer<Unidad_HU24> accion) { this.accionEditar = accion; }
    public void setAccionCambiarEstado(Consumer<Unidad_HU24> accion) { this.accionCambiarEstado = accion; }

    // ---------- Construcción de la interfaz ----------

    private JPanel crearBarraLateral() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setPreferredSize(new Dimension(250, 0));
        barra.setBackground(new Color(0xE5E7EB));
        barra.setBorder(new LineBorder(BORDE));

        JPanel cabecera = new JPanel(new GridLayout(2, 1));
        cabecera.setBackground(Color.WHITE);
        cabecera.setBorder(new EmptyBorder(18, 20, 14, 20));
        JLabel titulo = new JLabel("Transporte UCV");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(TEXTO);
        JLabel subtitulo = new JLabel("Sistema de Gestión");
        subtitulo.setForeground(TEXTO_SUAVE);
        cabecera.add(titulo);
        cabecera.add(subtitulo);
        barra.add(cabecera, BorderLayout.NORTH);

        JPanel menu = new JPanel(new GridLayout(0, 1, 0, 8));
        menu.setOpaque(false);
        JPanel menuWrap = new JPanel(new BorderLayout());
        menuWrap.setOpaque(false);
        menuWrap.setBorder(new EmptyBorder(12, 10, 0, 10));
        for (String seccion : SECCIONES) {
            JButton boton = botonMenu(seccion, seccion.equals("Unidades"));
            boton.addActionListener(e -> accionNavegar.accept(seccion));
            menu.add(boton);
        }
        menuWrap.add(menu, BorderLayout.NORTH);
        barra.add(menuWrap, BorderLayout.CENTER);

        JButton cerrar = botonMenu("Cerrar Sesión", false);
        cerrar.setHorizontalAlignment(SwingConstants.CENTER);
        cerrar.setForeground(ROJO);
        cerrar.addActionListener(e -> accionCerrarSesion.run());
        JPanel pie = new JPanel(new BorderLayout());
        pie.setOpaque(false);
        pie.setBorder(new EmptyBorder(10, 10, 10, 10));
        pie.add(cerrar, BorderLayout.CENTER);
        barra.add(pie, BorderLayout.SOUTH);
        return barra;
    }

    private JButton botonMenu(String texto, boolean activo) {
        JButton b = new JButton(texto);
        b.setFocusPainted(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(activo ? VERDE : BORDE), new EmptyBorder(12, 14, 12, 14)));
        b.setBackground(activo ? VERDE : Color.WHITE);
        b.setForeground(activo ? Color.WHITE : TEXTO);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JPanel crearContenido(String usuario) {
        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(FONDO);

        // Encabezado
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(Color.WHITE);
        encabezado.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDE), new EmptyBorder(14, 24, 14, 24)));
        JLabel titulo = new JLabel("Gestión de Unidades");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        titulo.setForeground(TEXTO);
        JLabel lblUsuario = new JLabel("Usuario:  " + usuario);
        lblUsuario.setForeground(TEXTO_SUAVE);
        encabezado.add(titulo, BorderLayout.WEST);
        encabezado.add(lblUsuario, BorderLayout.EAST);
        contenido.add(encabezado, BorderLayout.NORTH);

        // Cuerpo
        JPanel cuerpo = new JPanel(new BorderLayout(0, 18));
        cuerpo.setOpaque(false);
        cuerpo.setBorder(new EmptyBorder(24, 24, 24, 24));

        JButton nueva = new JButton("+ Nueva Unidad");
        nueva.setFocusPainted(false);
        nueva.setBackground(VERDE);
        nueva.setForeground(Color.WHITE);
        nueva.setOpaque(true);
        nueva.setBorder(new EmptyBorder(12, 22, 12, 22));
        nueva.setFont(nueva.getFont().deriveFont(Font.BOLD, 14f));
        nueva.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        nueva.addActionListener(e -> accionNuevaUnidad.run());
        JPanel filaBoton = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        filaBoton.setOpaque(false);
        filaBoton.add(nueva);
        cuerpo.add(filaBoton, BorderLayout.NORTH);

        JPanel tarjeta = new JPanel(new BorderLayout(0, 12));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDE), new EmptyBorder(18, 20, 18, 20)));
        JLabel tituloLista = new JLabel("Lista de Unidades");
        tituloLista.setFont(new Font("SansSerif", Font.PLAIN, 18));
        tituloLista.setForeground(TEXTO);
        tarjeta.add(tituloLista, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        tarjeta.add(scroll, BorderLayout.CENTER);

        JPanel zonaTarjeta = new JPanel(new BorderLayout());
        zonaTarjeta.setOpaque(false);
        zonaTarjeta.add(tarjeta, BorderLayout.NORTH);
        cuerpo.add(zonaTarjeta, BorderLayout.CENTER);

        contenido.add(cuerpo, BorderLayout.CENTER);
        return contenido;
    }

    private JTable crearTabla() {
        JTable t = new JTable(modeloTabla);
        t.setRowHeight(52);
        t.setShowVerticalLines(false);
        t.setGridColor(BORDE);
        t.setIntercellSpacing(new Dimension(0, 1));
        t.setSelectionBackground(new Color(0xEEF7F2));
        t.setSelectionForeground(TEXTO);
        t.setFillsViewportHeight(false);
        t.setRowSelectionAllowed(false);
        t.getTableHeader().setReorderingAllowed(false);

        JTableHeader encabezado = t.getTableHeader();
        encabezado.setPreferredSize(new Dimension(0, 40));
        encabezado.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable tb, Object v,
                    boolean sel, boolean foc, int fila, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(tb, v, sel, foc, fila, col);
                l.setBackground(new Color(0xE5E7EB));
                l.setForeground(TEXTO);
                l.setFont(l.getFont().deriveFont(Font.PLAIN, 14f));
                l.setBorder(new EmptyBorder(0, 12, 0, 12));
                l.setOpaque(true);
                return l;
            }
        });

        // Placa (negrita) y texto suave para el modelo
        t.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable tb, Object v,
                    boolean sel, boolean foc, int fila, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(tb, v, sel, foc, fila, col);
                l.setFont(l.getFont().deriveFont(Font.BOLD, 15f));
                l.setForeground(TEXTO);
                l.setBorder(new EmptyBorder(0, 12, 0, 12));
                return l;
            }
        });
        DefaultTableCellRenderer suave = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable tb, Object v,
                    boolean sel, boolean foc, int fila, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(tb, v, sel, foc, fila, col);
                l.setForeground(col == 1 ? TEXTO_SUAVE : TEXTO);
                l.setFont(l.getFont().deriveFont(Font.PLAIN, 15f));
                l.setBorder(new EmptyBorder(0, 12, 0, 12));
                return l;
            }
        };
        t.getColumnModel().getColumn(1).setCellRenderer(suave);
        t.getColumnModel().getColumn(2).setCellRenderer(suave);

        // Estado: etiqueta de color
        t.getColumnModel().getColumn(3).setCellRenderer((tb, v, sel, foc, fila, col) -> {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
            p.setBackground(Color.WHITE);
            JLabel etiqueta = new JLabel(String.valueOf(v));
            etiqueta.setOpaque(true);
            etiqueta.setBorder(new EmptyBorder(4, 10, 4, 10));
            etiqueta.setFont(etiqueta.getFont().deriveFont(Font.BOLD, 13f));
            EstadoOperativo_HU22 estado = (EstadoOperativo_HU22) v;
            switch (estado) {
                case ACTIVA -> { etiqueta.setBackground(VERDE); etiqueta.setForeground(Color.WHITE); }
                case EN_MANTENIMIENTO -> { etiqueta.setBackground(AMARILLO); etiqueta.setForeground(TEXTO); }
                default -> { etiqueta.setBackground(ROJO); etiqueta.setForeground(Color.WHITE); }
            }
            p.add(etiqueta);
            return p;
        });

        // Acciones: botones Editar / Cambiar Estado
        t.getColumnModel().getColumn(COL_ACCIONES).setCellRenderer(
                (tb, v, sel, foc, fila, col) -> crearPanelAcciones());

        t.getColumnModel().getColumn(0).setPreferredWidth(110);
        t.getColumnModel().getColumn(1).setPreferredWidth(240);
        t.getColumnModel().getColumn(2).setPreferredWidth(130);
        t.getColumnModel().getColumn(3).setPreferredWidth(170);
        t.getColumnModel().getColumn(4).setPreferredWidth(260);

        t.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { manejarClicAcciones(e); }
        });
        return t;
    }

    private JPanel crearPanelAcciones() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 8));
        p.setBackground(Color.WHITE);
        p.add(botonAccion("Editar"));
        p.add(botonAccion("Cambiar Estado"));
        return p;
    }

    private JButton botonAccion(String texto) {
        JButton b = new JButton(texto);
        b.setFocusPainted(false);
        b.setBackground(Color.WHITE);
        b.setForeground(TEXTO);
        b.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDE), new EmptyBorder(8, 18, 8, 18)));
        return b;
    }

    /** Detecta cuál botón de la columna Acciones se pulsó. */
    private void manejarClicAcciones(MouseEvent e) {
        int fila = tabla.rowAtPoint(e.getPoint());
        int col = tabla.columnAtPoint(e.getPoint());
        if (fila < 0 || col != COL_ACCIONES || fila >= filas.size()) return;

        Rectangle celda = tabla.getCellRect(fila, col, false);
        Component render = tabla.prepareRenderer(tabla.getCellRenderer(fila, col), fila, col);
        render.setBounds(0, 0, celda.width, celda.height);
        ((Container) render).doLayout();
        Component golpeado = ((Container) render).getComponentAt(e.getX() - celda.x, e.getY() - celda.y);

        if (golpeado instanceof JButton boton) {
            Unidad_HU24 unidad = filas.get(fila);
            if (boton.getText().equals("Editar")) accionEditar.accept(unidad);
            else accionCambiarEstado.accept(unidad);
        }
    }
}
