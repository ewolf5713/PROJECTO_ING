import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Tarea 3: edición y eliminación de itinerarios. */
public class ItinerarioServicio_HU10 {
    private final List<Itinerario_HU10> itinerarios = new ArrayList<>();
    private final NotificadorReservas_HU10 notificador;

    public ItinerarioServicio_HU10(NotificadorReservas_HU10 notificador) {
        this.notificador = notificador;
    }

    public void agregar(Itinerario_HU10 itinerario) { itinerarios.add(itinerario); }

    public List<Itinerario_HU10> listar() { return new ArrayList<>(itinerarios); }

    public Itinerario_HU10 buscarPorId(String id) {
        for (Itinerario_HU10 i : itinerarios) {
            if (i.getId().equals(id)) return i;
        }
        throw new IllegalArgumentException("No existe el itinerario " + id);
    }

    public void editar(String id, String origen, String destino,
                       LocalDateTime fechaSalida, int cupos) {
        Itinerario_HU10 it = buscarPorId(id);
        if (origen == null || origen.isBlank() || destino == null || destino.isBlank())
            throw new IllegalArgumentException("Origen y destino son obligatorios.");
        if (fechaSalida == null)
            throw new IllegalArgumentException("La fecha de salida es obligatoria.");
        if (cupos < it.contarReservasActivas())
            throw new IllegalArgumentException("Los cupos no pueden ser menores a las reservas activas ("
                    + it.contarReservasActivas() + ").");
        if (it.getUnidad() != null && cupos > it.getUnidad().getCapacidad())
            throw new IllegalArgumentException("Los cupos superan la capacidad de la unidad asignada ("
                    + it.getUnidad().getCapacidad() + ").");

        it.setOrigen(origen);
        it.setDestino(destino);
        it.setFechaSalida(fechaSalida);
        it.setCupos(cupos);
    }

    public void eliminar(String id) {
        Itinerario_HU10 it = buscarPorId(id);
        for (Reserva_HU10 r : it.getReservas()) {
            if (r.estaActiva()) {
                r.cancelar();
                notificador.notificarCancelacion(r, it);
            }
        }
        itinerarios.remove(it);
    }
}
