package com.rurex.model;

import java.math.BigDecimal;

public class Ticket {
    private final String id;
    private final String cedula;
    private final String itinerarioId;
    private final BigDecimal monto;
    private final String emision;
    private boolean usado;

    public Ticket(String id, String cedula, String itinerarioId, BigDecimal monto, String emision, boolean usado) {
        this.id = id;
        this.cedula = cedula;
        this.itinerarioId = itinerarioId;
        this.monto = monto;
        this.emision = emision;
        this.usado = usado;
    }

    public String getId() { return id; }
    public String getCedula() { return cedula; }
    public String getItinerarioId() { return itinerarioId; }
    public BigDecimal getMonto() { return monto; }
    public String getEmision() { return emision; }
    public boolean isUsado() { return usado; }
    public void setUsado(boolean usado) { this.usado = usado; }
}
