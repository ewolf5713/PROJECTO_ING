package com.rurex.service;

import com.rurex.model.Ruta;
import com.rurex.model.TipoRuta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Guarda las rutas en memoria (igual que FleetService e ItineraryService).
 * Debe existir una sola instancia en toda la aplicación (se crea en Main).
 */
public class RutaService {
    private final List<Ruta> rutas = new ArrayList<>();

    public RutaService() {
        cargarRutasPorDefecto();
    }

    /** Rutas de prueba (sin paradas). Borra este método cuando ya no las necesites. */
    private void cargarRutasPorDefecto() {
        rutas.add(new Ruta("Plaza Venezuela - UCV", "Plaza Venezuela", "Ciudad Universitaria", TipoRuta.URBANA));
        rutas.add(new Ruta("La Rinconada - UCV", "La Rinconada", "Ciudad Universitaria", TipoRuta.URBANA));
        rutas.add(new Ruta("Guarenas - UCV", "Guarenas", "Ciudad Universitaria", TipoRuta.EXTRAURBANA));
        rutas.add(new Ruta("Los Teques - UCV", "Los Teques", "Ciudad Universitaria", TipoRuta.EXTRAURBANA));
        rutas.add(new Ruta("Petare - UCV", "Petare", "Ciudad Universitaria", TipoRuta.URBANA));
    }

    /** Todas las rutas, activas e inactivas. */
    public List<Ruta> getRutas() {
        return Collections.unmodifiableList(rutas);
    }

    /** Solo las rutas con Activada = true (para usar, por ejemplo, al crear itinerarios). */
    public List<Ruta> getRutasActivas() {
        List<Ruta> activas = new ArrayList<>();
        for (Ruta r : rutas) {
            if (r.isActivada()) {
                activas.add(r);
            }
        }
        return activas;
    }

    public void agregar(Ruta nueva) {
        validarNombreLibre(nueva.getNombre(), null);
        rutas.add(nueva);
    }

    /**
     * Modifica una ruta existente. El nuevo nombre no puede pertenecer a otra ruta
     * (sí puede conservar el suyo). Si algo falla, la ruta no cambia.
     */
    public void actualizar(Ruta ruta, String nombre, String origen, String destino,
                           TipoRuta tipo, List<String> paradas) {
        validarNombreLibre(nombre, ruta);
        ruta.actualizar(nombre, origen, destino, tipo, paradas);
    }

    private void validarNombreLibre(String nombre, Ruta excepto) {
        if (nombre == null) {
            return;
        }
        String buscado = nombre.trim();
        for (Ruta r : rutas) {
            if (r != excepto && r.getNombre().equalsIgnoreCase(buscado)) {
                throw new IllegalArgumentException("Ya existe una ruta con ese nombre.");
            }
        }
    }
}
