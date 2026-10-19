package com.rurex.model;

/**
 * Tipos de ruta permitidos. Usar un enum evita que se guarde
 * un valor inválido (por ejemplo "urbna" escrito a mano).
 */
public enum TipoRuta {
    URBANA("Urbana"),
    EXTRAURBANA("Extraurbana");

    private final String etiqueta;

    TipoRuta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    // El JComboBox y la tabla usan toString() para mostrar cada opción.
    @Override
    public String toString() {
        return etiqueta;
    }
}
