package com.rurex;

import com.rurex.model.Trip;
import com.rurex.service.TripService;
import com.rurex.view.TripConductorPanel;
import com.rurex.view.TripPassengerPanel;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import java.util.List;

public final class TripDemo {
    private TripDemo() { }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Trip trip = new Trip("IT-008", "Plaza Venezuela - UCV", List.of(
                    new Trip.Stop("Plaza Venezuela", "07:30"),
                    new Trip.Stop("Sabana Grande", "07:38"),
                    new Trip.Stop("Ciudad Universitaria UCV", "08:00")));
            TripService service = new TripService((passengerId, tripId) -> "maria".equals(passengerId));

            JFrame frame = new JFrame("RUREX - Estado de recorrido");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setContentPane(new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                    new TripConductorPanel(trip, service),
                    new TripPassengerPanel(trip, "maria", "María González", service)));
            frame.setSize(700, 360);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
