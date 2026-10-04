import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;

public class GestionUnidades_HU23 extends JFrame {

    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> sorter;

    public String rutaArchivo = "unidades.txt"; 

    public GestionUnidades_HU23() {
        setTitle("Transporte UCV - Gestión de Unidades");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Sidebar ---
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(200, 600));
        sidebar.setBackground(new Color(245, 245, 245));
        sidebar.setLayout(new BorderLayout());
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));

        JPanel sidebarHeader = new JPanel(new GridLayout(2, 1));
        sidebarHeader.setBackground(new Color(245, 245, 245));
        sidebarHeader.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel lblMarca = new JLabel("Transporte UCV");
        lblMarca.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel lblSubMarca = new JLabel("Sistema de Gestión");
        lblSubMarca.setFont(new Font("Arial", Font.PLAIN, 11));
        lblSubMarca.setForeground(Color.GRAY);
        
        sidebarHeader.add(lblMarca);
        sidebarHeader.add(lblSubMarca);
        sidebar.add(sidebarHeader, BorderLayout.NORTH);

        JPanel sidebarMenu = new JPanel();
        sidebarMenu.setLayout(new BoxLayout(sidebarMenu, BoxLayout.Y_AXIS));
        sidebarMenu.setBackground(new Color(245, 245, 245));
        sidebarMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] opciones = {"Inicio", "Unidades", "Rutas", "Conductores", "Reportes", "Mantenimiento"};
        for (String opcion : opciones) {
            JButton btnMenu = new JButton(opcion);
            btnMenu.setAlignmentX(Component.LEFT_ALIGNMENT);
            btnMenu.setMaximumSize(new Dimension(180, 30)); 
            btnMenu.setFocusPainted(false);
            btnMenu.setFont(new Font("Arial", Font.PLAIN, 13));
            
            if (opcion.equals("Unidades")) {
                btnMenu.setBackground(new Color(25, 135, 84));
                btnMenu.setForeground(Color.WHITE);
            } else {
                btnMenu.setBackground(Color.WHITE);
                btnMenu.setForeground(Color.BLACK);
                btnMenu.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            }
            sidebarMenu.add(btnMenu);
            sidebarMenu.add(Box.createVerticalStrut(6));
        }
        sidebar.add(sidebarMenu, BorderLayout.CENTER);

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setForeground(Color.RED);
        btnCerrarSesion.setBackground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        btnCerrarSesion.setPreferredSize(new Dimension(180, 35));
        
        JPanel sidebarFooter = new JPanel();
        sidebarFooter.setBackground(new Color(245, 245, 245));
        sidebarFooter.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sidebarFooter.add(btnCerrarSesion);
        sidebar.add(sidebarFooter, BorderLayout.SOUTH);

        add(sidebar, BorderLayout.WEST);

        // --- Panel de Contenido Principal ---
        JPanel contenido = new JPanel();
        contenido.setBackground(Color.WHITE);
        contenido.setLayout(null);
        add(contenido, BorderLayout.CENTER);

        JLabel lblTitulo = new JLabel("Gestión de Unidades");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setBounds(25, 20, 250, 30);
        contenido.add(lblTitulo);

        JLabel lblUsuario = new JLabel("Usuario: Admin - [Usuario]");
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 12));
        lblUsuario.setBounds(480, 25, 200, 20);
        contenido.add(lblUsuario);

        JButton btnNuevaUnidad = new JButton("+ Nueva Unidad");
        btnNuevaUnidad.setBackground(new Color(25, 135, 84));
        btnNuevaUnidad.setForeground(Color.WHITE);
        btnNuevaUnidad.setFont(new Font("Arial", Font.BOLD, 12));
        btnNuevaUnidad.setFocusPainted(false);
        btnNuevaUnidad.setBounds(25, 70, 140, 30);
        contenido.add(btnNuevaUnidad);

        JLabel lblFiltrar = new JLabel("Filtrar por Estado:");
        lblFiltrar.setFont(new Font("Arial", Font.PLAIN, 12));
        lblFiltrar.setBounds(390, 72, 110, 25);
        contenido.add(lblFiltrar);

        String[] opcionesFiltro = {"No filtrar", "Activo", "En Mantenimiento", "Fuera de Servicio"};
        JComboBox<String> cmbFiltro = new JComboBox<>(opcionesFiltro);
        cmbFiltro.setBounds(505, 70, 160, 28);
        cmbFiltro.setBackground(Color.WHITE);
        contenido.add(cmbFiltro);

        JLabel lblListaTitulo = new JLabel("Lista de Unidades");
        lblListaTitulo.setFont(new Font("Arial", Font.BOLD, 15));
        lblListaTitulo.setBounds(25, 120, 200, 25);
        contenido.add(lblListaTitulo);

        // --- Tabla de Datos (Estructura inicial vacía) ---
        String[] columnas = {"Placa", "Modelo", "Capacidad", "Estado", "Acciones"};
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(40);
        tabla.setFont(new Font("Arial", Font.PLAIN, 12));
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(new Color(230, 235, 240));
        tabla.setShowVerticalLines(false);

        sorter = new TableRowSorter<>(modeloTabla);
        tabla.setRowSorter(sorter);

        tabla.getColumnModel().getColumn(4).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(160);

        tabla.getColumnModel().getColumn(3).setCellRenderer(new EstadoRenderer());
        tabla.getColumnModel().getColumn(4).setCellRenderer(new AccionesRenderer());
        tabla.getColumnModel().getColumn(4).setCellEditor(new AccionesEditor());

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBounds(25, 155, 640, 250);
        scrollTabla.getViewport().setBackground(Color.WHITE);
        scrollTabla.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        contenido.add(scrollTabla);

        // === Cargar los datos desde el archivo de texto ===
        verificarO_CrearArchivoEjemplo();
        cargarDatosDesdeArchivo();

        // Lógica de filtrado por JComboBox
        cmbFiltro.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String seleccion = (String) cmbFiltro.getSelectedItem();
                    if (seleccion.equals("No filtrar")) {
                        sorter.setRowFilter(null);
                    } else {
                        sorter.setRowFilter(RowFilter.regexFilter("^" + seleccion + "$", 3));
                    }
                }
            }
        });

        // Enlace para abrir la ventana de registro pasándole "this" (esta misma ventana)
        btnNuevaUnidad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistroUnidad_HU22 ventanaRegistro = new RegistroUnidad_HU22(GestionUnidades_HU23.this);
                ventanaRegistro.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                ventanaRegistro.setVisible(true);
            }
        });
    }

    // === REGISTRO: Lee el archivo txt y rellena la tabla ===
    public void cargarDatosDesdeArchivo() {
        modeloTabla.setRowCount(0); // Limpiar la tabla antes de recargar
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    String[] datosUnidad = linea.split("\\|"); // Separar por el caracter '|'
                    if (datosUnidad.length >= 4) {
                        // Estructura: {Placa, Modelo, Capacidad, Estado, Botones}
                        modeloTabla.addRow(new Object[]{datosUnidad[0], datosUnidad[1], datosUnidad[2], datosUnidad[3], ""});
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los datos del archivo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
/** 
    // Verifica si el archivo existe. Si no existe, lo crea con ejemplos
    private void verificarO_CrearArchivoEjemplo() {
        File archivo = new File("unidades.txt");
        if (!archivo.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            } catch (IOException e) {
System.out.println("No se pudo inicializar el archivo de pruebas.");
}
}
}
*/

private void verificarO_CrearArchivoEjemplo() {
    File archivo = new File(rutaArchivo);
    if (!archivo.exists()) {
        try {
            archivo.createNewFile(); // Crea el archivo totalmente limpio, sin registros por defecto
            System.out.println("Archivo de unidades creado vacío con éxito.");
        } catch (IOException e) {
            System.out.println("No se pudo crear el archivo vacío.");
        }
    }
}

// --- Renderizadores de Estilo Visual  ---
class EstadoRenderer extends DefaultTableCellRenderer {
@Override
public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
lbl.setHorizontalAlignment(SwingConstants.CENTER);
lbl.setOpaque(true);
lbl.setFont(new Font("Arial", Font.BOLD, 11));
String estado = (String) value;
if ("Activo".equals(estado)) {
lbl.setBackground(new Color(25, 135, 84));
lbl.setForeground(Color.WHITE);
} else if ("En Mantenimiento".equals(estado)) {
lbl.setBackground(new Color(255, 193, 7));
lbl.setForeground(Color.BLACK);
} else if ("Fuera de Servicio".equals(estado)) {
lbl.setBackground(new Color(220, 53, 69));
lbl.setForeground(Color.WHITE);
}
return lbl;
}
}
class AccionesRenderer extends JPanel implements TableCellRenderer {
public AccionesRenderer() {
setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
setBackground(Color.WHITE);
JButton btnEditar = new JButton("Editar");
JButton btnEstado = new JButton("Cambiar Estado");
btnEditar.setFont(new Font("Arial", Font.BOLD, 11));
btnEditar.setBackground(new Color(0, 123, 255));
btnEditar.setForeground(Color.WHITE);
btnEditar.setFocusPainted(false);
btnEstado.setFont(new Font("Arial", Font.BOLD, 11));
btnEstado.setBackground(new Color(108, 117, 125));
btnEstado.setForeground(Color.WHITE);
btnEstado.setFocusPainted(false);
add(btnEditar);
add(btnEstado);
}
@Override
public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
return this;
}
}
class AccionesEditor extends DefaultCellEditor {
protected JPanel panel;
public AccionesEditor() {
super(new JCheckBox());
panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
panel.setBackground(Color.WHITE);
JButton btnEditar = new JButton("Editar");
JButton btnEstado = new JButton("Cambiar Estado");
btnEditar.setFont(new Font("Arial", Font.BOLD, 11));
btnEditar.setBackground(new Color(0, 123, 255));
btnEditar.setForeground(Color.WHITE);
btnEstado.setFont(new Font("Arial", Font.BOLD, 11));
btnEstado.setBackground(new Color(108, 117, 125));
btnEstado.setForeground(Color.WHITE);
btnEditar.addActionListener(e -> fireEditingStopped());
btnEstado.addActionListener(e -> fireEditingStopped());
panel.add(btnEditar);
panel.add(btnEstado);
}
@Override
public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
return panel;
}
@Override
public Object getCellEditorValue() { return ""; }
}

// habilitar los asserts del Test
public DefaultTableModel getExtendedTableModel() {
    return this.modeloTabla;
}

public RowSorter<DefaultTableModel> getTableSorter() {
    return this.sorter;
}

public static void main(String[] args) {
SwingUtilities.invokeLater(() -> new GestionUnidades_HU23().setVisible(true));
}
}