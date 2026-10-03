import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/*
Acá se actualiza el estado del recorrido y se muestra solo a pasajeros con reserva
 */
public class RecorridoServicio_HU8 {
    private static final RecorridoServicio_HU8 INSTANCIA = new RecorridoServicio_HU8();

    //tenemos una instacnia del servicio del recorrido
    public static RecorridoServicio_HU8 getInstancia() { return INSTANCIA; }

    //Guarda el estado de cada recorrido (clave: id del itinerario)
    private final Map<String, RecorridoEstado_HU8> estados = new ConcurrentHashMap<>();
    private final List<RecorridoLista_HU8> lista = new CopyOnWriteArrayList<>();
    private volatile VerificadorReservas_HU8 verificador = (pasajeroId, itinerarioId) -> false;

    //Un constructor privado para mayor seguridad ante interrupción
    private RecorridoServicio_HU8() { }

    // Los nombres de estos metodos deben coincidir con los que llaman Main y los paneles
    public void EstablecerVerificadorReservas(VerificadorReservas_HU8 verificador) { this.verificador = verificador; }

    public void agregarLista(RecorridoLista_HU8 l) { lista.add(l); } //Suscribe un nuevo oyente para recibir notificaciones
    public void quitarLista(RecorridoLista_HU8 l) { lista.remove(l); } //Remueve un nuevo oyente para recibir notificaciones

    //Si el viaje no ha comenzado, arranca "En Parada" en la primera parada
    public RecorridoEstado_HU8 obtenerEstado(Itinerario_HU8 it) {
        return estados.computeIfAbsent(it.getId(),
                id -> new RecorridoEstado_HU8(id, EtapasRecorrido_HU8.EN_PARADA, 0, 0, LocalDateTime.now()));
    }

    // Devuelve el estado solo si el pasajero tiene reserva para el itinerario
    public Optional<RecorridoEstado_HU8> obtenerEstadoParaPasajero(String pasajeroId, Itinerario_HU8 it) {
        if (pasajeroId == null || !verificador.tieneReservaActiva(pasajeroId, it.getId())) {
            return Optional.empty();
        }
        return Optional.of(obtenerEstado(it));
    }

    // Valida y guarda el nuevo estado, y avisa a los listeners
    // it = itinerario, etaMinutos = minutos estimados de llegada
    public synchronized RecorridoEstado_HU8 actualizarEstado(Itinerario_HU8 it, EtapasRecorrido_HU8 nuevo,int paradaActual, int etaMinutos) {

        if (it == null) throw new IllegalArgumentException("Itinerario no válido");
        if (nuevo == null) throw new IllegalArgumentException("Debe seleccionar un estado");
        if (etaMinutos < 0) throw new IllegalArgumentException("El estimado no puede ser negativo");
        if (paradaActual < 0 || paradaActual >= it.getParadas().size()) { throw new IllegalArgumentException("Parada no válida"); }

        RecorridoEstado_HU8 actual = obtenerEstado(it);
        if (!actual.getEstado().puedeCambiarA(nuevo)) { throw new IllegalStateException("No se puede pasar de \"" + actual.getEstado().getEtiqueta()
                    + "\" a \"" + nuevo.getEtiqueta() + "\"");
        }

        if (paradaActual < actual.getParadaActual()) { throw new IllegalStateException("La parada actual no puede retroceder"); }

        RecorridoEstado_HU8 actualizado = new RecorridoEstado_HU8(it.getId(), nuevo, paradaActual, etaMinutos,LocalDateTime.now());
        estados.put(it.getId(), actualizado);

        for (RecorridoLista_HU8 l : lista) { l.alCambiarEstado(actualizado); }
        return actualizado;
    }
}
