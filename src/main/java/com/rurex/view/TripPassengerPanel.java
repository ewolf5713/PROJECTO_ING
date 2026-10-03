package com.rurex.view;

import com.rurex.model.Trip;
import com.rurex.service.TripListener;
import com.rurex.service.TripService;
import com.rurex.service.TripState;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Optional;

public final class TripPassengerPanel extends JPanel implements TripListener {
    private final Trip trip;
    private final String passengerId;
    private final TripService service;
    private final JLabel status = new JLabel();

    public TripPassengerPanel(Trip trip, String passengerId, String passengerName, TripService service) {
        super(new BorderLayout(8, 8));
        this.trip = trip;
        this.passengerId = passengerId;
        this.service = service;
        setBorder(BorderFactory.createTitledBorder("Pasajero: " + passengerName));
        add(new JLabel(trip.route()), BorderLayout.NORTH);
        add(status, BorderLayout.CENTER);
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
            status.setText("No tienes una reserva activa para este recorrido.");
            return;
        }
        TripState current = state.get();
        status.setText("Estado: " + current.stage().label() + " · parada " + (current.stopIndex() + 1)
                + " · ETA " + current.etaMinutes() + " min");
    }
}
