package com.rurex.view;

import com.rurex.model.Trip;
import com.rurex.service.TripListener;
import com.rurex.service.TripService;
import com.rurex.service.TripState;

import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Component;
import java.util.Optional;

public final class TripPassengerPanel extends JPanel implements TripListener {
    private final Trip trip;
    private final String passengerId;
    private final TripService service;
    private final JLabel status = Estilos.etiqueta("", Font.BOLD, 18, Estilos.NAVY);
    private final JLabel detalle = Estilos.etiqueta("", Font.PLAIN, 13, Estilos.GRIS);

    public TripPassengerPanel(Trip trip, String passengerId, String passengerName, TripService service) {
        super(new BorderLayout());
        this.trip = trip;
        this.passengerId = passengerId;
        this.service = service;
        setBackground(Color.WHITE);

        JPanel cuerpo = new JPanel();
        cuerpo.setLayout(new BoxLayout(cuerpo, BoxLayout.Y_AXIS));
        cuerpo.setBackground(Color.WHITE);
        JLabel lblRuta = Estilos.etiqueta(trip.route(), Font.PLAIN, 13, Estilos.NAVY);
        JLabel lblPasajero = Estilos.etiqueta("Pasajero: " + passengerName, Font.PLAIN, 12, Estilos.GRIS);
        cuerpo.add(lblRuta);
        cuerpo.add(Box.createVerticalStrut(4));
        cuerpo.add(lblPasajero);
        cuerpo.add(Box.createVerticalStrut(16));
        cuerpo.add(status);
        cuerpo.add(Box.createVerticalStrut(6));
        cuerpo.add(detalle);
        for (Component c : cuerpo.getComponents()) {
            if (c instanceof JLabel etiqueta) etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        add(cuerpo, BorderLayout.NORTH);
        refresh();
    }

    @Override public void addNotify() {
        super.addNotify();
        service.addListener(this);
        refresh();
    }

    @Override public void removeNotify() {
        service.removeListener(this);
        super.removeNotify();
    }

    @Override public void stateChanged(TripState state) {
        if (trip.id().equals(state.tripId())) SwingUtilities.invokeLater(this::refresh);
    }

    private void refresh() {
        Optional<TripState> state = service.stateForPassenger(passengerId, trip);
        if (state.isEmpty()) {
            status.setText("Sin reserva activa");
            detalle.setText("No tienes una reserva activa para este recorrido.");
            return;
        }
        TripState current = state.get();
        status.setText(current.stage().label());
        detalle.setText("Parada " + (current.stopIndex() + 1) + " de " + trip.stops().size()
                + " · ETA " + current.etaMinutes() + " min");
    }
}
