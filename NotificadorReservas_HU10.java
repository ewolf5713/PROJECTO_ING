import java.util.ArrayList;
import java.util.List;

/** Notifica a los pasajeros. Guarda los mensajes para poder mostrarlos en la UI Swing. */
public class NotificadorReservas_HU10 {
    private final List<String> mensajes = new ArrayList<>();

    public void notificarCancelacion(Reserva_HU10 reserva, Itinerario_HU10 itinerario) {
        String mensaje = "Estimado/a " + reserva.getPasajero() + ": su reserva " + reserva.getId()
                + " fue cancelada porque el itinerario " + itinerario.getOrigen()
                + " - " + itinerario.getDestino() + " ya no está disponible.";
        mensajes.add(mensaje);
        System.out.println(mensaje);
    }

    public List<String> getMensajes() { return mensajes; }
}
