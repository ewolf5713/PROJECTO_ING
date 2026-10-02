import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Itinerario_HU10 {
    private final String id;
    private String origen;
    private String destino;
    private LocalDateTime fechaSalida;
    private int cupos;
    private UnidadTransporte_HU10 unidad;
    private Conductor_HU10 conductor;
    private final List<Reserva_HU10> reservas = new ArrayList<>();

    public Itinerario_HU10(String id, String origen, String destino,
                           LocalDateTime fechaSalida, int cupos) {
        this.id = id;
        this.origen = origen;
        this.destino = destino;
        this.fechaSalida = fechaSalida;
        this.cupos = cupos;
    }

    public String getId() { return id; }
    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }
    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }
    public LocalDateTime getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(LocalDateTime fechaSalida) { this.fechaSalida = fechaSalida; }
    public int getCupos() { return cupos; }
    public void setCupos(int cupos) { this.cupos = cupos; }
    public UnidadTransporte_HU10 getUnidad() { return unidad; }
    public void setUnidad(UnidadTransporte_HU10 unidad) { this.unidad = unidad; }
    public Conductor_HU10 getConductor() { return conductor; }
    public void setConductor(Conductor_HU10 conductor) { this.conductor = conductor; }
    public List<Reserva_HU10> getReservas() { return reservas; }

    public void agregarReserva(Reserva_HU10 reserva) { reservas.add(reserva); }

    public int contarReservasActivas() {
        int total = 0;
        for (Reserva_HU10 r : reservas) {
            if (r.estaActiva()) total++;
        }
        return total;
    }
}
