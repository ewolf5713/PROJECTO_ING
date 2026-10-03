package com.rurex.service;

import com.rurex.model.OperationalStatus;
import com.rurex.model.TransportUnit;

import java.util.*;

public class FleetService {

    private final Map<String, TransportUnit> unidadesMap = new LinkedHashMap<>();

    public FleetService() {
        cargarUnidadesDemo();
    }

    public synchronized TransportUnit registrarUnidad(String placa, String modelo, int capacidad, OperationalStatus estado) {
        if (placa == null || placa.isBlank()) {
            throw new IllegalArgumentException("La placa es obligatoria.");
        }
        if (modelo == null || modelo.isBlank()) {
            throw new IllegalArgumentException("El modelo es obligatorio.");
        }
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser un entero mayor a cero.");
        }

        String placaNorm = placa.trim().toUpperCase();
        if (unidadesMap.containsKey(placaNorm)) {
            throw new IllegalArgumentException("La placa " + placaNorm + " ya esta registrada.");
        }

        TransportUnit unidad = new TransportUnit(placaNorm, modelo, capacidad, estado);
        unidadesMap.put(placaNorm, unidad);
        return unidad;
    }

    public synchronized List<TransportUnit> getUnidades() {
        return new ArrayList<>(unidadesMap.values());
    }

    public synchronized List<TransportUnit> filtrarPorEstado(OperationalStatus estado) {
        if (estado == null) return getUnidades();
        List<TransportUnit> lista = new ArrayList<>();
        for (TransportUnit u : unidadesMap.values()) {
            if (u.getEstado() == estado) {
                lista.add(u);
            }
        }
        return lista;
    }

    public synchronized TransportUnit editarUnidad(String placa, String modelo, int capacidad, OperationalStatus nuevoEstado) {
        TransportUnit u = findByPlaca(placa)
                .orElseThrow(() -> new IllegalArgumentException("No se encontro la unidad con placa " + placa));

        if (modelo != null && !modelo.isBlank()) {
            u.setModelo(modelo.trim());
        }
        if (capacidad > 0) {
            u.setCapacidad(capacidad);
        }
        if (nuevoEstado != null) {
            u.setEstado(nuevoEstado);
        }
        return u;
    }

    public synchronized TransportUnit actualizarEstado(String placa, OperationalStatus nuevoEstado) {
        return editarUnidad(placa, null, 0, nuevoEstado);
    }

    public synchronized Optional<TransportUnit> findByPlaca(String placa) {
        if (placa == null) return Optional.empty();
        return Optional.ofNullable(unidadesMap.get(placa.trim().toUpperCase()));
    }

    public synchronized TransportUnit validarUnidadParaItinerario(String placa) {
        TransportUnit u = findByPlaca(placa)
                .orElseThrow(() -> new IllegalArgumentException("No existe unidad con placa " + placa));
        if (!u.isAssignable()) {
            throw new IllegalArgumentException("La unidad " + u.getPlaca() + " esta " + u.getEstado().getLabel() + " y no puede asignarse.");
        }
        return u;
    }

    private void cargarUnidadesDemo() {
        registrarUnidad("UCV-234", "Mercedes-Benz OF-1721", 32, OperationalStatus.ACTIVA);
        registrarUnidad("UCV-156", "Volkswagen 17.230", 30, OperationalStatus.ACTIVA);
        registrarUnidad("UCV-789", "Iveco Daily", 40, OperationalStatus.ACTIVA);
        registrarUnidad("UCV-445", "Mercedes-Benz Sprinter", 28, OperationalStatus.EN_MANTENIMIENTO);
        registrarUnidad("UCV-992", "Ford Transit", 25, OperationalStatus.FUERA_DE_SERVICIO);
    }
}
