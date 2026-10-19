package com.rurex.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa una ruta. Los datos solo se pueden cambiar mediante
 * actualizar(...), que valida todo antes de modificar nada, así que
 * una ruta nunca queda en un estado inválido.
 */
public class Ruta {
    private String nombre;
    private String origen;
    private String destino;
    private TipoRuta tipo;
    private List<String> paradas;
    private boolean activada = true; // las rutas nuevas nacen activadas

    // Constructor para rutas sin paradas intermedias
    public Ruta(String nombre, String origen, String destino, TipoRuta tipo) {
        this(nombre, origen, destino, tipo, List.of());
    }

    public Ruta(String nombre, String origen, String destino, TipoRuta tipo, List<String> paradas) {
        actualizar(nombre, origen, destino, tipo, paradas);
    }

    /**
     * Valida y reemplaza todos los datos de la ruta.
     * Si algo es inválido lanza IllegalArgumentException y la ruta no cambia.
     */
    public final void actualizar(String nombre, String origen, String destino,
                                 TipoRuta tipo, List<String> paradas) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (origen == null || origen.isBlank()) {
            throw new IllegalArgumentException("El origen es obligatorio.");
        }
        if (destino == null || destino.isBlank()) {
            throw new IllegalArgumentException("El destino es obligatorio.");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("Debe seleccionar un tipo de ruta.");
        }
        if (origen.trim().equalsIgnoreCase(destino.trim())) {
            throw new IllegalArgumentException("El origen y el destino no pueden ser iguales.");
        }

        // Se ignoran las paradas vacías y se rechazan las repetidas
        List<String> limpias = new ArrayList<>();
        if (paradas != null) {
            for (String p : paradas) {
                if (p == null || p.isBlank()) {
                    continue;
                }
                String parada = p.trim();
                for (String existente : limpias) {
                    if (existente.equalsIgnoreCase(parada)) {
                        throw new IllegalArgumentException("La parada \"" + parada + "\" está repetida.");
                    }
                }
                limpias.add(parada);
            }
        }

        // Todo es válido: recién ahora se modifica la ruta
        this.nombre = nombre.trim();
        this.origen = origen.trim();
        this.destino = destino.trim();
        this.tipo = tipo;
        this.paradas = Collections.unmodifiableList(limpias);
    }

    public String getNombre()  { return nombre; }
    public String getOrigen()  { return origen; }
    public String getDestino() { return destino; }
    public TipoRuta getTipo()  { return tipo; }
    public List<String> getParadas() { return paradas; }

    /** true = la ruta está disponible; false = desactivada. */
    public boolean isActivada() { return activada; }
    public void setActivada(boolean activada) { this.activada = activada; }

    @Override
    public String toString() {
        String recorrido = origen;
        for (String p : paradas) {
            recorrido += " -> " + p;
        }
        recorrido += " -> " + destino;
        return nombre + " [" + tipo + "]: " + recorrido + (activada ? "" : " (desactivada)");
    }
}
