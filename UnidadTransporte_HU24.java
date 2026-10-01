package hu_024;
import hu_022.EstadoOperativo_HU22;
// [HU-024 MODIFICADO]

import java.util.Objects;

/** Modelo inmutable de una unidad de transporte. Sin dependencias externas. */
public final class UnidadTransporte_HU24 {
    private final String placa;
    private final String modelo;
    private final int capacidadPasajeros;
    private final EstadoOperativo_HU22 estado;

    public UnidadTransporte_HU24(String placa, String modelo, int capacidadPasajeros, EstadoOperativo_HU22 estado) {
        this.placa = placa;
        this.modelo = modelo;
        this.capacidadPasajeros = capacidadPasajeros;
        this.estado = estado;
    }

    public String getPlaca() { return placa; }
    public String getModelo() { return modelo; }
    public int getCapacidadPasajeros() { return capacidadPasajeros; }
    public EstadoOperativo_HU22 getEstado() { return estado; }

    /** HU-024: solo las unidades activas pueden asignarse a un itinerario. */
    public boolean esAsignable() { return estado == EstadoOperativo_HU22.ACTIVA; }

    /** Copia con los campos editables cambiados; la placa (identificador) no cambia. */
    public UnidadTransporte_HU24 conCambios(String nuevoModelo, int nuevaCapacidad, EstadoOperativo_HU22 nuevoEstado) {
        return new UnidadTransporte_HU24(placa, nuevoModelo, nuevaCapacidad, nuevoEstado);
    }

    @Override public boolean equals(Object o) {
        return o instanceof UnidadTransporte_HU24 u && placa.equals(u.placa);
    }
    @Override public int hashCode() { return Objects.hash(placa); }

    @Override public String toString() {
        return placa + " | " + modelo + " | " + capacidadPasajeros + " pax | " + estado.getEtiqueta();
    }
}
