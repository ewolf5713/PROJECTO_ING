package com.rurex.view;

import com.rurex.controller.AuthController;
import com.rurex.controller.FleetController;
import com.rurex.controller.ItineraryController;
import com.rurex.model.Itinerary;
import com.rurex.model.User;
import com.rurex.service.FleetService;
import com.rurex.service.ItineraryService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class AdminDashboardView extends JFrame {

    private final FleetService fleetService;
    private final ItineraryService itineraryService;
    private final JLabel lblActivas = crearValor();
    private final JLabel lblMantenimiento = crearValor();
    private final JLabel lblItinerarios = crearValor();
    private final DefaultTableModel tableModel;

    public AdminDashboardView(User user, AuthController authController, FleetController fleetController, ItineraryController itineraryController, FleetService fleetService, ItineraryService itineraryService) {
        this.fleetService = fleetService;
        this.itineraryService = itineraryService;

        setTitle("Transporte UCV - Panel Administrador");
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Estilos.iniciarSesion(user.getNombreCompleto(), this, authController, fleetController, itineraryController);

        JPanel contenido = new JPanel(new BorderLayout(16, 0));
        contenido.setBackground(Estilos.FONDO);

        JPanel columnaStats = new JPanel(new GridLayout(3, 1, 0, 12));
        columnaStats.setBackground(Estilos.FONDO);
        columnaStats.add(crearStat("Unidades Activas", lblActivas, "En ruta operativa"));
        columnaStats.add(crearStat("En Mantenimiento", lblMantenimiento, "Revisión programada"));
        columnaStats.add(crearStat("Itinerarios Activos", lblItinerarios, "Programados"));

        JPanel contenedorStats = new JPanel(new BorderLayout());
        contenedorStats.setBackground(Estilos.FONDO);
        contenedorStats.setPreferredSize(new Dimension(210, 0));
        contenedorStats.add(columnaStats, BorderLayout.NORTH);
        contenido.add(contenedorStats, BorderLayout.WEST);

        String[] cols = {"Ruta", "Fecha y Hora", "Unidad", "Cupos"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(tableModel);
        JScrollPane scroll = Estilos.estilizarTabla(table);
        table.getColumn("Fecha y Hora").setCellRenderer(Estilos.rendererTexto(Estilos.GRIS));
        table.getColumn("Unidad").setCellRenderer(Estilos.rendererTexto(Estilos.GRIS));
        contenido.add(Estilos.cardConTitulo("Últimos Itinerarios Creados", scroll), BorderLayout.CENTER);

        Estilos.crearShell(this, "Inicio", "Panel de Administración", contenido);

        cargarDatos();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                cargarDatos();
            }
        });
    }

    private JLabel crearValor() {
        JLabel v = Estilos.etiqueta("0", Font.BOLD, 24, Estilos.NAVY);
        v.setHorizontalAlignment(SwingConstants.CENTER);
        return v;
    }

    private JPanel crearStat(String titulo, JLabel lblValor, String caption) {
        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBackground(Color.WHITE);
        centro.add(lblValor);
        centro.add(Box.createVerticalStrut(4));
        centro.add(Estilos.etiqueta(caption, Font.PLAIN, 11, Estilos.VERDE));
        return Estilos.cardConTitulo(titulo, centro);
    }

    private void cargarDatos() {
        int cantActivas = fleetService != null ? fleetService.getCantidadActivas() : 3;
        int cantMant = fleetService != null ? fleetService.getCantidadMantenimiento() : 1;
        int cantItin = itineraryService != null ? itineraryService.getItinerarios().size() : 3;

        lblActivas.setText(String.valueOf(cantActivas));
        lblMantenimiento.setText(String.valueOf(cantMant));
        lblItinerarios.setText(String.valueOf(cantItin));

        tableModel.setRowCount(0);
        if (itineraryService == null) return;
        List<Itinerary> lista = itineraryService.getItinerarios();
        int mostrados = 0;
        for (int i = lista.size() - 1; i >= 0 && mostrados < 8; i--) {
            Itinerary it = lista.get(i);
            tableModel.addRow(new Object[]{
                    it.getRutaNombre(),
                    it.getFecha() + " " + it.getHoraSalida(),
                    it.getUnidad().getPlaca(),
                    it.getCuposDisponibles() + " cupos"
            });
            mostrados++;
        }
    }
}
