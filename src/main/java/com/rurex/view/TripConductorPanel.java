package com.rurex.view;

import com.rurex.model.Trip;
import com.rurex.model.TripStage;
import com.rurex.service.TripService;
import com.rurex.service.TripState;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public final class TripConductorPanel extends JPanel {
    private final Trip trip;
    private final TripService service;
    private final JComboBox<TripStage> stage = new JComboBox<>(TripStage.values());
    private final JSpinner stop = new JSpinner(new SpinnerNumberModel(0, 0, 0, 1));
    private final JSpinner eta = new JSpinner(new SpinnerNumberModel(0, 0, 240, 1));
    private final JLabel status = new JLabel();

    public TripConductorPanel(Trip trip, TripService service) {
        super(new BorderLayout(8, 8));
        this.trip = trip;
        this.service = service;
        stop.setModel(new SpinnerNumberModel(0, 0, trip.stops().size() - 1, 1));
        setBorder(BorderFactory.createTitledBorder("Conductor: " + trip.route()));

        JPanel controls = new JPanel(new GridLayout(2, 4, 8, 4));
        controls.add(new JLabel("Estado"));
        controls.add(new JLabel("Parada"));
        controls.add(new JLabel("ETA (min)"));
        controls.add(new JLabel());
        controls.add(stage);
        controls.add(stop);
        controls.add(eta);
        JButton update = new JButton("Actualizar");
        controls.add(update);
        add(controls, BorderLayout.NORTH);
        add(status, BorderLayout.SOUTH);
        update.addActionListener(event -> update());
        show(service.stateFor(trip));
    }

    private void update() {
        try {
            show(service.updateState(trip, (TripStage) stage.getSelectedItem(), (Integer) stop.getValue(), (Integer) eta.getValue()));
        } catch (IllegalArgumentException | IllegalStateException exception) {
            status.setText(exception.getMessage());
        }
    }

    private void show(TripState state) {
        stage.setSelectedItem(state.stage());
        stop.setValue(state.stopIndex());
        eta.setValue(state.etaMinutes());
        status.setText(state.stage().label() + " · parada " + (state.stopIndex() + 1));
    }
}
