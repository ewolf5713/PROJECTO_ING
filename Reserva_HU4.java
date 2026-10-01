package HU003_004.transporte.modelo;

/*
Esta clase define el modelo de una reserva asociada a un itinerario de transporte, la cual se crea
automáticamente con un identificador único, un itinerario vinculado y en estado ACTIVA.
Permite consultar sus datos básicos, verificar si sigue vigente mediante estaActiva() y
modificar su ciclo de vida cambiando su estado a CANCELADA o EXPIRADA.
*/
public class Reserva_HU4 {

    private final String id;
    private final String itinerarioId;
    private EstadoReserva_HU4 estado;

    public Reserva_HU4(String id, String itinerarioId) {
        this.id = id;
        this.itinerarioId = itinerarioId;
        this.estado = EstadoReserva_HU4.ACTIVA;
    }

    public String getId() { return id; }
    public String getItinerarioId() { return itinerarioId; }
    public EstadoReserva_HU4 getEstado() { return estado; }

    public boolean estaActiva() {
        return estado == EstadoReserva_HU4.ACTIVA;
    }

    public void cancelar() {
        this.estado = EstadoReserva_HU4.CANCELADA;
    }

    public void expirar() {
        this.estado = EstadoReserva_HU4.EXPIRADA;
    }
}