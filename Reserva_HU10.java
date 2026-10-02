public class Reserva_HU10 {
    public enum Estado { ACTIVA, CANCELADA }

    private final String id;
    private final String pasajero;
    private Estado estado = Estado.ACTIVA;

    public Reserva_HU10(String id, String pasajero) {
        this.id = id;
        this.pasajero = pasajero;
    }

    public String getId() { return id; }
    public String getPasajero() { return pasajero; }
    public Estado getEstado() { return estado; }
    public boolean estaActiva() { return estado == Estado.ACTIVA; }
    public void cancelar() { estado = Estado.CANCELADA; }
}
