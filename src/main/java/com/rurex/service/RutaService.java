package com.rurex.service;

import com.rurex.database.RutaDatabase;
import com.rurex.model.Ruta;
import com.rurex.model.TipoRuta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RutaService {

    private final List<Ruta> rutas = new ArrayList<>();
    private RutaDatabase database;

    public RutaService() {
        cargarRutasPorDefecto();
    }

    public RutaService(RutaDatabase database) {
        this.database = database;
        if (database != null && database.existe()) {
            cargarRutas(database.cargar());
        } else {
            cargarRutasPorDefecto();
            if (database != null) {
                database.guardar(rutas);
            }
        }
    }

    public void setDatabase(RutaDatabase database) {
        this.database = database;
        if (database != null) {
            if (database.existe()) {
                cargarRutas(database.cargar());
            } else {
                database.guardar(rutas);
            }
        }
    }

    public synchronized void cargarRutas(List<Ruta> lista) {
        rutas.clear();
        if (lista != null) {
            rutas.addAll(lista);
        }
    }

    public synchronized List<Ruta> getRutas() {
        return Collections.unmodifiableList(rutas);
    }

    public synchronized List<Ruta> getRutasActivas() {
        List<Ruta> activas = new ArrayList<>();
        for (Ruta r : rutas) {
            if (r.isActivada()) {
                activas.add(r);
            }
        }
        return activas;
    }

    public synchronized void agregar(Ruta nueva) {
        validarNombreLibre(nueva.getNombre(), null);
        rutas.add(nueva);
        notificarCambio();
    }

    public synchronized void actualizar(Ruta ruta, String nombre, String origen, String destino,
                                         TipoRuta tipo, List<String> paradas) {
        validarNombreLibre(nombre, ruta);
        ruta.actualizar(nombre, origen, destino, tipo, paradas);
        notificarCambio();
    }

    public synchronized void alternarEstado(Ruta ruta) {
        if (ruta != null) {
            ruta.setActivada(!ruta.isActivada());
            notificarCambio();
        }
    }

    private void notificarCambio() {
        if (database != null) {
            database.guardar(rutas);
        }
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

    private void cargarRutasPorDefecto() {
        rutas.add(new Ruta("Plaza Venezuela - UCV", "Plaza Venezuela", "Ciudad Universitaria", TipoRuta.URBANA));
        rutas.add(new Ruta("La Rinconada - UCV", "La Rinconada", "Ciudad Universitaria", TipoRuta.URBANA));
        rutas.add(new Ruta("Guarenas - UCV", "Guarenas", "Ciudad Universitaria", TipoRuta.EXTRAURBANA));
        rutas.add(new Ruta("Los Teques - UCV", "Los Teques", "Ciudad Universitaria", TipoRuta.EXTRAURBANA));
        rutas.add(new Ruta("Petare - UCV", "Petare", "Ciudad Universitaria", TipoRuta.URBANA));
    }
}
