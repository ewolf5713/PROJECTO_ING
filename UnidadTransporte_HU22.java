package hu_022;
// [HU-022]

import java.util.Objects;

/** Modelo inmutable de una unidad de transporte. Sin dependencias externas. */
public final class UnidadTransporte_HU22 {
    private final String placa;
    private final String modelo;
    private final int capacidadPasajeros;
    private final EstadoOperativo_HU22 estado;

    public UnidadTransporte_HU22(String placa, String modelo, int capacidadPasajeros, EstadoOperativo_HU22 estado) {
        this.placa = placa;
        this.modelo = modelo;
        this.capacidadPasajeros = capacidadPasajeros;
        this.estado = estado;
    }

    public String getPlaca() { return placa; }
    public String getModelo() { return modelo; }
    public int getCapacidadPasajeros() { return capacidadPasajeros; }
    public EstadoOperativo_HU22 getEstado() { return estado; }

    @Override public boolean equals(Object o) {
        return o instanceof UnidadTransporte_HU22 u && placa.equals(u.placa);
    }
    @Override public int hashCode() { return Objects.hash(placa); }

    @Override public String toString() {
        return placa + " | " + modelo + " | " + capacidadPasajeros + " pax | " + estado.getEtiqueta();
    }
}
