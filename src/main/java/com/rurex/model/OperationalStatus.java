package com.rurex.model;

public enum OperationalStatus {
    ACTIVA("Activa"),
    EN_MANTENIMIENTO("En Mantenimiento"),
    FUERA_DE_SERVICIO("Fuera de Servicio");

    private final String label;

    OperationalStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static OperationalStatus fromString(String value) {
        if (value == null || value.isBlank()) return ACTIVA;
        String val = value.trim().toLowerCase();
        if (val.contains("mantenimiento")) return EN_MANTENIMIENTO;
        if (val.contains("fuera")) return FUERA_DE_SERVICIO;
        return ACTIVA;
    }

    @Override
    public String toString() {
        return label;
    }
}
