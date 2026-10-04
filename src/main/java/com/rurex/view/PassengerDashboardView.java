package com.rurex.view;

import com.rurex.controller.AuthController;
import com.rurex.model.Itinerary;
import com.rurex.model.User;
import com.rurex.service.ItineraryService;
import com.rurex.service.TripService;
import com.rurex.service.TripState;

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

public class PassengerDashboardView extends JFrame {

    private final User user;
    private final ItineraryService itineraryService;
    private final TripService tripService;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel panelContenido = new JPanel(cardLayout);
    private final JPanel panelTrip = new JPanel(new BorderLayout());
    private final JComboBox<String> comboItinerarios = new JComboBox<>();
    private final JLabel lblProximaSalida = crearValor();
    private final JLabel lblProximaRuta = crearCaption();
    private final JLabel lblRutas = crearValor();
    private final JLabel lblCupos = crearValor();
    private final JLabel lblEstado = crearValor();
    private final JLabel lblEstadoRuta = crearCaption();
    private final DefaultTableModel modeloInicio = crearModeloTabla();
    private final DefaultTableModel modeloItinerarios = crearModeloTabla();
    private List<Itinerary> itinerarios;
    private Consumer<String> irA;

    public PassengerDashboardView(User user, AuthController authController, ItineraryService itineraryService, TripService tripService) {
        this.user = user;
        this.itineraryService = itineraryService;
        this.tripService = tripService;

        setTitle("Transporte UCV - Panel Pasajero");
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Estilos.iniciarSesion(user.getNombreCompleto(), authController);

        panelContenido.setBackground(Estilos.FONDO);
        panelContenido.add(crearPanelInicio(), "Inicio");
        panelContenido.add(crearPanelItinerarios(), "Itinerarios");
        panelContenido.add(crearPanelEstado(), "Estado del Recorrido");

        String[] items = {"Inicio", "Itinerarios", "Estado del Recorrido"};
        irA = Estilos.crearShellNav(this, "Panel Principal", items, this::mostrarSeccion, panelContenido);

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

    private JLabel crearCaption() {
        JLabel c = Estilos.etiqueta(" ", Font.PLAIN, 11, Estilos.GRIS);
        c.setHorizontalAlignment(SwingConstants.CENTER);
        return c;
    }

    private DefaultTableModel crearModeloTabla() {
        String[] cols = {"Ruta", "Fecha", "Salida", "Llegada", "Unidad", "Cupos"};
        return new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
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
        tabla.getColumn("Llegada").setCellRenderer(Estilos.rendererTexto(Estilos.GRIS));
        tabla.getColumn("Unidad").setCellRenderer(Estilos.rendererTexto(Estilos.GRIS));
        return scroll;
    }

    private JPanel crearPanelInicio() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(Estilos.FONDO);

        JPanel filaStats = new JPanel(new GridLayout(1, 4, 12, 0));
        filaStats.setBackground(Estilos.FONDO);
        filaStats.add(crearCardStat("Próxima Salida", lblProximaSalida, lblProximaRuta));
        filaStats.add(crearCardStat("Rutas Disponibles", lblRutas, crearCaptionFijo("Rutas con itinerarios")));
        filaStats.add(crearCardStat("Cupos Disponibles", lblCupos, crearCaptionFijo("En todos los itinerarios")));
        filaStats.add(crearCardStat("Estado del Recorrido", lblEstado, lblEstadoRuta));

        JPanel accesos = new JPanel(new GridLayout(1, 2, 12, 0));
        accesos.setBackground(Color.WHITE);
        JButton btnItinerarios = Estilos.botonPrimario("Consultar Itinerarios");
        JButton btnEstado = Estilos.botonOutline("Estado del Recorrido");
        btnItinerarios.addActionListener(e -> irA.accept("Itinerarios"));
        btnEstado.addActionListener(e -> irA.accept("Estado del Recorrido"));
        accesos.add(btnItinerarios);
        accesos.add(btnEstado);

        JPanel norte = new JPanel(new BorderLayout(0, 16));
        norte.setBackground(Estilos.FONDO);
        norte.add(filaStats, BorderLayout.NORTH);
        norte.add(Estilos.cardConTitulo("Accesos Rápidos", accesos), BorderLayout.CENTER);

        panel.add(norte, BorderLayout.NORTH);
        panel.add(Estilos.cardConTitulo("Itinerarios Disponibles", crearTablaItinerarios(modeloInicio)), BorderLayout.CENTER);
        return panel;
    }

    private JLabel crearCaptionFijo(String texto) {
        JLabel c = crearCaption();
        c.setText(texto);
        return c;
    }

    private JPanel crearPanelItinerarios() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Estilos.FONDO);
        panel.add(Estilos.cardConTitulo("Itinerarios Disponibles", crearTablaItinerarios(modeloItinerarios)), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelEstado() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(Estilos.FONDO);

        Estilos.estilizarCombo(comboItinerarios);
        comboItinerarios.setPreferredSize(new Dimension(420, 34));
        comboItinerarios.addActionListener(e -> mostrarRecorrido());
        JPanel selector = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        selector.setBackground(Estilos.FONDO);
        selector.add(Estilos.etiqueta("Itinerario:", Font.PLAIN, 13, Estilos.NAVY));
        selector.add(comboItinerarios);

        panelTrip.setBackground(Color.WHITE);
        panel.add(selector, BorderLayout.NORTH);
        panel.add(Estilos.cardConTitulo("Estado del Recorrido", panelTrip), BorderLayout.CENTER);
        return panel;
    }

    private void mostrarSeccion(String nombre) {
        if (nombre.equals("Inicio")) cargarDatos();
        cardLayout.show(panelContenido, nombre);
    }

    private void mostrarRecorrido() {
        panelTrip.removeAll();
        int indice = comboItinerarios.getSelectedIndex();
        if (indice >= 0 && indice < itinerarios.size()) {
            Itinerary it = itinerarios.get(indice);
            panelTrip.add(new TripPassengerPanel(itineraryService.crearTrip(it), user.getEmail(), user.getNombreCompleto(), tripService), BorderLayout.CENTER);
        } else {
            panelTrip.add(Estilos.etiqueta("No hay itinerarios disponibles.", Font.PLAIN, 13, Estilos.GRIS), BorderLayout.NORTH);
        }
        panelTrip.revalidate();
        panelTrip.repaint();
    }

    private void cargarTabla(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        for (Itinerary it : itinerarios) {
            modelo.addRow(new Object[]{
                    it.getRutaNombre(),
                    it.getFecha(),
                    it.getHoraSalida(),
                    it.getHoraLlegada(),
                    it.getUnidad().getPlaca(),
                    it.getCuposDisponibles()
            });
        }
    }

    private void cargarDatos() {
        itinerarios = itineraryService.getItinerarios();

        Optional<Itinerary> proximo = itineraryService.getProximoItinerario(itinerarios, LocalDateTime.now());
        if (proximo.isPresent()) {
            Itinerary it = proximo.get();
            String dia = it.getFecha().equals(LocalDate.now()) ? "Hoy " : it.getFecha() + " ";
            lblProximaSalida.setText(dia + it.getHoraSalida());
            lblProximaRuta.setText(it.getRutaNombre());
        } else {
            lblProximaSalida.setText("Sin salidas");
            lblProximaRuta.setText("No hay salidas próximas");
        }

        lblRutas.setText(String.valueOf(itineraryService.getCantidadRutas()));
        lblCupos.setText(String.valueOf(itineraryService.getCuposTotales()));

        lblEstado.setText("Sin iniciar");
        lblEstadoRuta.setText("Ningún recorrido en curso");
        for (Itinerary it : itinerarios) {
            Optional<TripState> estado = tripService.currentState(it.getId());
            if (estado.isPresent()) {
                lblEstado.setText(estado.get().stage().label());
                lblEstadoRuta.setText(it.getRutaNombre());
                break;
            }
        }

        cargarTabla(modeloInicio);
        cargarTabla(modeloItinerarios);

        int seleccionado = comboItinerarios.getSelectedIndex();
        comboItinerarios.setModel(new DefaultComboBoxModel<>(crearOpcionesCombo()));
        if (seleccionado >= 0 && seleccionado < itinerarios.size()) {
            comboItinerarios.setSelectedIndex(seleccionado);
        }
        mostrarRecorrido();
    }

    private String[] crearOpcionesCombo() {
        String[] opciones = new String[itinerarios.size()];
        for (int i = 0; i < itinerarios.size(); i++) {
            Itinerary it = itinerarios.get(i);
            opciones[i] = it.getRutaNombre() + " - " + it.getFecha() + " " + it.getHoraSalida();
        }
        return opciones;
    }
}
