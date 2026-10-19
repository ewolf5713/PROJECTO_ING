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
import java.math.BigDecimal;

public class PassengerDashboardView extends JFrame {

    private final User user;
    private final AuthController authController;
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
    private final JLabel lblSaldo = crearValor();
    private final DefaultTableModel modeloInicio = crearModeloTabla();
    private final DefaultTableModel modeloItinerarios = crearModeloTabla();
    private List<Itinerary> itinerarios;
    private Consumer<String> irA;

    public PassengerDashboardView(User user, AuthController authController, ItineraryService itineraryService, TripService tripService) {
        this.user = user;
        this.itineraryService = itineraryService;
        this.tripService = tripService;
        this.authController = authController;

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
        filaStats.add(crearCardStat("Saldo del Monedero", lblSaldo, crearCaptionFijo("Disponible para pagar")));
        filaStats.add(crearCardStat("Estado del Recorrido", lblEstado, lblEstadoRuta));

        JPanel accesos = new JPanel(new GridLayout(1, 2, 12, 0));
        accesos.setBackground(Color.WHITE);
        JButton btnItinerarios = Estilos.botonPrimario("Consultar Itinerarios");
        JButton btnEstado = Estilos.botonOutline("Estado del Recorrido");
        JButton btnRecargar = Estilos.botonPrimario("Recargar Monedero"); 
        btnItinerarios.addActionListener(e -> irA.accept("Itinerarios"));
        btnEstado.addActionListener(e -> irA.accept("Estado del Recorrido"));
        btnRecargar.addActionListener(e -> mostrarDialogoRecarga()); 
        accesos.add(btnItinerarios);
        accesos.add(btnEstado);
        accesos.add(btnRecargar);

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
            if (it.isExtraurbana()) {
                JButton btnPagar = Estilos.botonPrimario("Pagar ticket (Bs. "
                + authController.getPrecioTicketExtraurbano().toPlainString() + ")");
                btnPagar.addActionListener(e -> pagarTicketExtraurbano(it));
                JPanel sur = new JPanel(new FlowLayout(FlowLayout.LEFT));
                sur.setBackground(Color.WHITE);
                sur.add(btnPagar);
                panelTrip.add(sur, BorderLayout.SOUTH);
            }
            else {
            panelTrip.add(Estilos.etiqueta("No hay itinerarios disponibles.", Font.PLAIN, 13, Estilos.GRIS), BorderLayout.NORTH);
        }
        panelTrip.revalidate();
        panelTrip.repaint();
    }
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

    public void actualizarSaldo() {
        try { lblSaldo.setText("Bs. " + authController.consultarSaldo(user).toPlainString()); }
        catch (IllegalStateException | IllegalArgumentException ex) { lblSaldo.setText("-"); }
    }

    private void mostrarDialogoRecarga() {
        JComboBox<String> comboBanco = new JComboBox<>(new String[]
            {"Banesco", "Mercantil", "Provincial", "Banco de Venezuela", "BNC", "Bancamiga"});
        JTextField txtTelefono = new JTextField();
        JTextField txtCedula = new JTextField();
        JComboBox<BigDecimal> comboMonto = new JComboBox<>(authController.getMontosRecarga().toArray(new BigDecimal[0]));

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.add(new JLabel("BANCO: "));
        form.add(comboBanco);
        form.add(new JLabel("TELEFONO: "));
        form.add(txtTelefono);
        form.add(new JLabel("CEDULA: "));
        form.add(txtCedula);
        form.add(new JLabel("MONTO (.BS): "));
        form.add(comboMonto);

        while(true) {
            int opcion = JOptionPane.showConfirmDialog(this, form, "RECARGA CON PAGO MOVIL", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (opcion != JOptionPane.OK_OPTION) return;

            try {
                String referencia = authController.recargarMonedero(
                    user, (String) comboBanco.getSelectedItem(), txtTelefono.getText(), txtCedula.getText(), (BigDecimal) comboMonto.getSelectedItem());
                    actualizarSaldo();
                    JOptionPane.showMessageDialog(this, "[REFERENCIA EXITOSA]\n, REFERENCIA: " + referencia, "LISTO", JOptionPane.INFORMATION_MESSAGE);
                    return;
            }
            catch (IllegalArgumentException | IllegalStateException ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "[ERROR]", JOptionPane.ERROR_MESSAGE); }
        }
    }

    private void cargarDatos() {
        actualizarSaldo();
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

    private void pagarTicketExtraurbano(Itinerary it) {
        BigDecimal precio = authController.getPrecioTicketExtraurbano();
        BigDecimal saldo = authController.consultarSaldo(user);
        
        if (saldo.compareTo(precio) < 0) {
            int opcion = JOptionPane.showConfirmDialog(this, "Saldo insuficiente.\nTu saldo es Bs. " + saldo.toPlainString()
                        + " y el ticket cuesta Bs. " + precio.toPlainString()
                        + ".\n¿Deseas recargar ahora?","Saldo insuficiente", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (opcion == JOptionPane.YES_OPTION) mostrarDialogoRecarga();
        return;
    }

    int confirmar = JOptionPane.showConfirmDialog(this,
            "¿Pagar el ticket de " + it.getRutaNombre() + " por Bs. " + precio.toPlainString() + "?",
            "Confirmar pago", JOptionPane.YES_NO_OPTION);
    if (confirmar != JOptionPane.YES_OPTION) return;

    try {
        String referencia = authController.pagarTicketExtraurbano(user, it.getId());
        actualizarSaldo();
        JOptionPane.showMessageDialog(this, "Ticket pagado. Referencia: " + referencia,
                "Listo", JOptionPane.INFORMATION_MESSAGE);
    } catch (IllegalStateException | IllegalArgumentException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
}

