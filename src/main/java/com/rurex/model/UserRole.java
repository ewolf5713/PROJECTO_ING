package com.rurex.model;

public enum UserRole {
    ESTUDIANTE("Estudiante"),
    EMPLEADO("Empleado"),
    PUBLICO_GENERAL("Público General"),
    ADMINISTRADOR("Administrador"),
    CONDUCTOR("Conductor"),
    SUPER_ADMIN("Super Administrador");

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
