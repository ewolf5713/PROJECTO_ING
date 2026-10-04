package com.rurex.view;

import com.rurex.controller.AuthController;
import com.rurex.model.Itinerary;
import com.rurex.model.Trip;
import com.rurex.model.User;
import com.rurex.service.ItineraryService;
import com.rurex.service.TripService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ConductorDashboardView extends JFrame {

    private final User user;
    private final ItineraryService itineraryService;
    private final TripService tripService;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel panelContenido = new JPanel(cardLayout);
    private final JPanel panelTrip = new JPanel(new BorderLayout());
    private final JLabel lblAsignados = crearValor();
    private final JLabel lblAsignadosCaption = crearCaption("Recorridos asignados");
    private final JLabel lblUnidad = crearValor();
    private final JLabel lblModelo = crearCaption(" ");
    private final JLabel lblProximo = crearValor();
    private final JLabel lblProximoRuta = crearCaption(" ");
    private final DefaultTableModel modeloInicio = crearModeloTabla();
    private final DefaultTableModel modeloItinerarios = crearModeloTabla();
    private List<Itinerary> misItinerarios;
    private Consumer<String> irA;

    public ConductorDashboardView(User user, AuthController authController, ItineraryService itineraryService, TripService tripService) {
        this.user = user;
        this.itineraryService = itineraryService;
        this.tripService = tripService;

        setTitle("Transporte UCV - Panel del Conductor");
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Estilos.iniciarSesion(user.getNombreCompleto(), authController);

        panelContenido.setBackground(Estilos.FONDO);
        panelContenido.add(crearPanelInicio(), "Inicio");
        panelContenido.add(crearPanelItinerarios(), "Mis Itinerarios");
        panelContenido.add(crearPanelEstado(), "Estado del Recorrido");

        String[] items = {"Inicio", "Mis Itinerarios", "Estado del Recorrido"};
        irA = Estilos.crearShellNav(this, "Panel del Conductor", items, this::mostrarSeccion, panelContenido);

        cargarDatos();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                cargarDatos();
            }
        });
    }

    private JLabel crearValor() {
        JLabel v = Estilos.etiqueta("-", Font.BOLD, 18, Estilos.NAVY);
        v.setHorizontalAlignment(SwingConstants.CENTER);
        return v;
    }

    private JLabel crearCaption(String texto) {
        JLabel c = Estilos.etiqueta(texto, Font.PLAIN, 11, Estilos.GRIS);
        c.setHorizontalAlignment(SwingConstants.CENTER);
        return c;
    }

    private DefaultTableModel crearModeloTabla() {
        String[] cols = {"Ruta", "Fecha", "Salida", "Llegada Est.", "Unidad", "Cupos", "Acción"};
        return new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return col == 6; }
        };
    }

    private JPanel crearCardStat(String titulo, JLabel lblValor, JLabel lblCaption) {
        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBackground(Color.WHITE);
        centro.add(lblValor);
        centro.add(Box.createVerticalStrut(4));
        centro.add(lblCaption);
        return Estilos.cardConTitulo(titulo, centro);
    }

    private JScrollPane crearTablaItinerarios(DefaultTableModel modelo) {
        JTable tabla = new JTable(modelo);
        JScrollPane scroll = Estilos.estilizarTabla(tabla);
        tabla.getColumn("Fecha").setCellRenderer(Estilos.rendererTexto(Estilos.GRIS));
        tabla.getColumn("Llegada Est.").setCellRenderer(Estilos.rendererTexto(Estilos.GRIS));
        tabla.getColumn("Unidad").setCellRenderer(Estilos.rendererTexto(Estilos.GRIS));
        Estilos.AccionesCelda acciones = new Estilos.AccionesCelda(new String[]{"Iniciar"}, (fila, boton) -> iniciarRecorrido(fila));
        tabla.getColumn("Acción").setCellRenderer(acciones);
        tabla.getColumn("Acción").setCellEditor(acciones);
        tabla.getColumn("Acción").setPreferredWidth(110);
        return scroll;
    }

    private JPanel crearPanelInicio() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(Estilos.FONDO);

        JPanel filaStats = new JPanel(new GridLayout(1, 3, 12, 0));
        filaStats.setBackground(Estilos.FONDO);
        filaStats.add(crearCardStat("Itinerarios Asignados", lblAsignados, lblAsignadosCaption));
        filaStats.add(crearCardStat("Unidad Asignada", lblUnidad, lblModelo));
        filaStats.add(crearCardStat("Próximo Recorrido", lblProximo, lblProximoRuta));

        JPanel accesos = new JPanel(new GridLayout(1, 2, 12, 0));
        accesos.setBackground(Color.WHITE);
        JButton btnItinerarios = Estilos.botonPrimario("Mis Itinerarios");
        JButton btnEstado = Estilos.botonOutline("Estado del Recorrido");
        btnItinerarios.addActionListener(e -> irA.accept("Mis Itinerarios"));
        btnEstado.addActionListener(e -> irA.accept("Estado del Recorrido"));
        accesos.add(btnItinerarios);
        accesos.add(btnEstado);

        JPanel norte = new JPanel(new BorderLayout(0, 16));
        norte.setBackground(Estilos.FONDO);
        norte.add(filaStats, BorderLayout.NORTH);
        norte.add(Estilos.cardConTitulo("Accesos Rápidos", accesos), BorderLayout.CENTER);

        panel.add(norte, BorderLayout.NORTH);
        panel.add(Estilos.cardConTitulo("Mis Itinerarios", crearTablaItinerarios(modeloInicio)), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelItinerarios() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Estilos.FONDO);
        panel.add(Estilos.cardConTitulo("Mis Itinerarios", crearTablaItinerarios(modeloItinerarios)), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelEstado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Estilos.FONDO);
        panelTrip.setBackground(Color.WHITE);
        mostrarMensajeSinRecorrido();
        panel.add(Estilos.cardConTitulo("Estado del Recorrido", panelTrip), BorderLayout.CENTER);
        return panel;
    }

    private void mostrarMensajeSinRecorrido() {
        panelTrip.removeAll();
        panelTrip.add(Estilos.etiqueta("Seleccione un itinerario con Iniciar en Mis Itinerarios.", Font.PLAIN, 13, Estilos.GRIS), BorderLayout.NORTH);
        panelTrip.revalidate();
        panelTrip.repaint();
    }

    private void mostrarSeccion(String nombre) {
        if (nombre.equals("Inicio")) cargarDatos();
        cardLayout.show(panelContenido, nombre);
    }

    private void iniciarRecorrido(int fila) {
        if (fila < 0 || fila >= misItinerarios.size()) return;
        Itinerary it = misItinerarios.get(fila);
        Trip trip = itineraryService.crearTrip(it);

        JPanel cuerpo = new JPanel(new BorderLayout(0, 12));
        cuerpo.setBackground(Color.WHITE);
        JLabel lblRuta = Estilos.etiqueta(it.getRutaNombre() + " · " + it.getFecha() + " " + it.getHoraSalida() + " · " + it.getUnidad().getPlaca(), Font.PLAIN, 13, Estilos.NAVY);
        lblRuta.setHorizontalAlignment(SwingConstants.LEFT);
        cuerpo.add(lblRuta, BorderLayout.NORTH);
        cuerpo.add(new TripConductorPanel(trip, tripService), BorderLayout.CENTER);

        panelTrip.removeAll();
        panelTrip.add(cuerpo, BorderLayout.NORTH);
        panelTrip.revalidate();
        panelTrip.repaint();
        irA.accept("Estado del Recorrido");
    }

    private void cargarTabla(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        for (Itinerary it : misItinerarios) {
            modelo.addRow(new Object[]{
                    it.getRutaNombre(),
                    it.getFecha(),
                    it.getHoraSalida(),
                    it.getHoraLlegada(),
                    it.getUnidad().getPlaca(),
                    it.getCuposDisponibles(),
                    ""
            });
        }
    }

    private void cargarDatos() {
        misItinerarios = itineraryService.getItinerariosPorConductor(user.getNombreCompleto());
        lblAsignados.setText(String.valueOf(misItinerarios.size()));

        Optional<Itinerary> proximo = itineraryService.getProximoItinerario(misItinerarios, LocalDateTime.now());
        if (proximo.isPresent()) {
            Itinerary it = proximo.get();
            String dia = it.getFecha().equals(LocalDate.now()) ? "Hoy " : it.getFecha() + " ";
            lblProximo.setText(dia + it.getHoraSalida());
            lblProximoRuta.setText(it.getRutaNombre());
        } else {
            lblProximo.setText("Sin recorridos");
            lblProximoRuta.setText("No hay recorridos próximos");
        }

        Itinerary referencia = proximo.isPresent() ? proximo.get() : (misItinerarios.isEmpty() ? null : misItinerarios.get(0));
        if (referencia != null) {
            lblUnidad.setText(referencia.getUnidad().getPlaca());
            lblModelo.setText(referencia.getUnidad().getModelo());
        } else {
            lblUnidad.setText("Sin asignar");
            lblModelo.setText("No tiene unidad asignada");
        }

        cargarTabla(modeloInicio);
        cargarTabla(modeloItinerarios);
    }
}
