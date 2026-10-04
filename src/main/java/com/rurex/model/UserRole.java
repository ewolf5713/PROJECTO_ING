package com.rurex.model;

public enum UserRole {
    ESTUDIANTE("Estudiante"),
    EMPLEADO("Empleado"),
    ADMINISTRADOR("Administrador"),
    CONDUCTOR("Conductor");

    private final String label;

    UserRole(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
