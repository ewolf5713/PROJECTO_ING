package com.rurex.view;

import com.rurex.model.Trip;
import com.rurex.model.TripStage;
import com.rurex.service.TripService;
import com.rurex.service.TripState;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

public final class TripConductorPanel extends JPanel {
    private final Trip trip;
    private final TripService service;
    private final JComboBox<TripStage> stage = new JComboBox<>(TripStage.values());
    private final JSpinner stop = new JSpinner(new SpinnerNumberModel(0, 0, 0, 1));
    private final JSpinner eta = new JSpinner(new SpinnerNumberModel(0, 0, 240, 1));
    private final JLabel status = Estilos.etiqueta("", Font.BOLD, 14, Estilos.NAVY);

    public TripConductorPanel(Trip trip, TripService service) {
        super(new BorderLayout(0, 16));
        this.trip = trip;
        this.service = service;
        setBackground(Color.WHITE);
        stop.setModel(new SpinnerNumberModel(0, 0, trip.stops().size() - 1, 1));
        Estilos.estilizarCombo(stage);
        stop.setPreferredSize(new Dimension(180, 34));
        eta.setPreferredSize(new Dimension(180, 34));

        JPanel controls = new JPanel(new GridLayout(2, 4, 12, 4));
        controls.setBackground(Color.WHITE);
        controls.add(Estilos.etiqueta("Estado", Font.PLAIN, 12, Estilos.NAVY));
        controls.add(Estilos.etiqueta("Parada", Font.PLAIN, 12, Estilos.NAVY));
        controls.add(Estilos.etiqueta("ETA (min)", Font.PLAIN, 12, Estilos.NAVY));
        controls.add(new JLabel());
        controls.add(stage);
        controls.add(stop);
        controls.add(eta);
        JButton update = Estilos.botonPrimario("Actualizar");
        controls.add(update);
        add(controls, BorderLayout.NORTH);
        status.setHorizontalAlignment(JLabel.LEFT);
        add(status, BorderLayout.CENTER);
        update.addActionListener(event -> update());
        show(service.stateFor(trip));
    }

    private void update() {
        try {
            show(service.updateState(trip, (TripStage) stage.getSelectedItem(), (Integer) stop.getValue(), (Integer) eta.getValue()));
            status.setForeground(Estilos.NAVY);
        } catch (IllegalArgumentException | IllegalStateException exception) {
            status.setText(exception.getMessage());
            status.setForeground(Estilos.ROJO);
        }
    }

    private void show(TripState state) {
        stage.setSelectedItem(state.stage());
        stop.setValue(state.stopIndex());
        eta.setValue(state.etaMinutes());
        status.setText(state.stage().label() + " · parada " + (state.stopIndex() + 1));
    }
}
