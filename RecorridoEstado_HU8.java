import java.time.LocalDateTime;

/*
Aca se logra una captura del estado catual del recorredi o
agrupando la etapa activa, para actual, estimación y ultimo registro
*/
public final class RecorridoEstado_HU8 {
    private final String itinerarioId;
    private final EtapasRecorrido_HU8 estado;
    private final int paradaActual;
    private final int etaMinutos;
    private final LocalDateTime ultimaAct;

    //se crea una instancia de todos los valres para el estado
    public RecorridoEstado_HU8(String itinerarioId, EtapasRecorrido_HU8 estado, int paradaActual, int etaMinutos, LocalDateTime ultimaAct) {
        this.itinerarioId = itinerarioId;
        this.estado = estado;
        this.paradaActual = paradaActual;
        this.etaMinutos = etaMinutos;
        this.ultimaAct = ultimaAct;
    }

    public String getItinerarioId() { return itinerarioId; } //devuelve el identificador único del itinerario asociado
    public EtapasRecorrido_HU8 getEstado() { return estado; } //Devuelve la etapa actual del recorrido
    public int getParadaActual() { return paradaActual; } //Duevuelve el indice de la parada actual
    public int getEtaMinutos() { return etaMinutos; } // devuelve el ETA
    public LocalDateTime getUltimaActualizacion() { return ultimaAct; } //devuelve la fecha y hora de la ultima actualización
}