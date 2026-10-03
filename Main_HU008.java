import javax.swing.*;

import PROJECTO_ING.ConductorRecorridoPanel_HU8;

import java.awt.*;
import java.util.Arrays;

/**
 * HU-008 - Demo para probar todo junto: conductor a la izquierda, pasajeros a la derecha.
 * Cambia el estado en el conductor y mira como se actualiza el pasajero con reserva.
 * (No es parte del sistema final: sirve de prueba hasta integrar con el menu principal.)
 */
public class Main_HU008 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Itinerario_HU8 it = new Itinerario_HU8("IT-001", "Plaza Venezuela - UCV",
                    "Bus #45 - UCV-234", "Carlos Rodríguez", "7:30 AM",
                    Arrays.asList(
                            new Itinerario_HU8.Parada("Plaza Venezuela", "7:30 AM"),
                            new Itinerario_HU8.Parada("Sabana Grande", "7:38 AM"),
                            new Itinerario_HU8.Parada("Plaza Las Tres Gracias", "7:45 AM"),
                            new Itinerario_HU8.Parada("Ciudad Universitaria UCV", "8:00 AM")));

            // Simulacion del modulo de reservas: solo "maria" tiene reserva
            RecorridoServicio_HU8.getInstancia().EstablecerVerificadorReservas((pasajeroId, itinerarioId) -> "maria".equals(pasajeroId));

            JTabbedPane pasajeros = new JTabbedPane();
            pasajeros.addTab("Pasajero con reserva",
                    new EstadoPasajeroPanel_HU8(it, "maria", "María González"));
            pasajeros.addTab("Pasajero sin reserva",
                    new EstadoPasajeroPanel_HU8(it, "pedro", "Pedro Pérez"));

            JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                    new ConductorRecorridoPanel_HU8(it, it.getConductor()), pasajeros);
            split.setResizeWeight(0.45);

            JFrame f = new JFrame("Transporte UCV - HU-008 (demo)");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setContentPane(split);
            f.setSize(1200, 720);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}