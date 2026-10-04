package com.rurex.model;

public class TransportUnit {
    private String placa;
    private String modelo;
    private int capacidad;
    private OperationalStatus estado;

    public TransportUnit(String placa, String modelo, int capacidad, OperationalStatus estado) {
        this.placa = placa != null ? placa.trim().toUpperCase() : "";
        this.modelo = modelo != null ? modelo.trim() : "";
        this.capacidad = capacidad;
        this.estado = estado != null ? estado : OperationalStatus.ACTIVA;
    }

    public String getPlaca() { return placa; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
    public OperationalStatus getEstado() { return estado; }
    public void setEstado(OperationalStatus estado) { this.estado = estado; }
    public boolean isAssignable() { return estado == OperationalStatus.ACTIVA; }
}
