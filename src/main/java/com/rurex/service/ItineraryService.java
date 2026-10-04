package com.rurex.service;

import com.rurex.model.Itinerary;
import com.rurex.model.TransportUnit;
import com.rurex.model.Trip;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class ItineraryService {

    private final List<Itinerary> itinerariosList = new ArrayList<>();
    private final FleetService fleetService;

    public ItineraryService(FleetService fleetService) {
        this.fleetService = fleetService;
        cargarItinerariosDemo();
    }

    public synchronized Itinerary crearItinerario(String ruta, LocalDate fecha, LocalTime salida, LocalTime llegada, String placa, String chofer, Integer cupos) {
        if (ruta == null || ruta.isBlank()) {
            throw new IllegalArgumentException("La ruta es obligatoria.");
        }
        if (fecha == null || salida == null) {
            throw new IllegalArgumentException("Fecha y hora de salida son obligatorias.");
        }
        if (llegada != null && !llegada.isAfter(salida)) {
            throw new IllegalArgumentException("La hora de llegada debe ser posterior a la salida.");
        }

        TransportUnit unidad = fleetService.validarUnidadParaItinerario(placa);

        int cuposFinal = (cupos == null) ? unidad.getCapacidad() : cupos;
        if (cuposFinal <= 0) {
            throw new IllegalArgumentException("Los cupos deben ser mayores a cero.");
        }
        if (cuposFinal > unidad.getCapacidad()) {
            throw new IllegalArgumentException("Los cupos superan la capacidad maxima del bus (" + unidad.getCapacidad() + ").");
        }

        boolean cruce = itinerariosList.stream().anyMatch(it ->
                it.getFecha().equals(fecha) &&
                it.getUnidad().getPlaca().equalsIgnoreCase(unidad.getPlaca()) &&
                it.getHoraSalida().equals(salida)
        );
        if (cruce) {
            throw new IllegalArgumentException("El vehiculo " + unidad.getPlaca() + " ya tiene viaje a esa misma hora.");
        }

        String id = "IT-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        Itinerary nuevo = new Itinerary(
                id,
                ruta.trim(),
                fecha,
                salida,
                llegada != null ? llegada : salida.plusMinutes(45),
                unidad,
                chofer != null && !chofer.isBlank() ? chofer.trim() : "Chofer por asignar",
                cuposFinal
        );

        itinerariosList.add(nuevo);
        return nuevo;
    }

    public synchronized List<Itinerary> getItinerarios() {
        return Collections.unmodifiableList(new ArrayList<>(itinerariosList));
    }

    public synchronized List<Itinerary> getItinerariosPorConductor(String conductor) {
        List<Itinerary> resultado = new ArrayList<>();
        if (conductor == null || conductor.isBlank()) return resultado;
        for (Itinerary it : itinerariosList) {
            if (it.getConductor().equalsIgnoreCase(conductor.trim())) {
                resultado.add(it);
            }
        }
        resultado.sort(Comparator.comparing(Itinerary::getFecha).thenComparing(Itinerary::getHoraSalida));
        return resultado;
    }

    public Optional<Itinerary> getProximoItinerario(List<Itinerary> lista, LocalDateTime ahora) {
        Itinerary proximo = null;
        for (Itinerary it : lista) {
            LocalDateTime salida = LocalDateTime.of(it.getFecha(), it.getHoraSalida());
            if (salida.isBefore(ahora)) continue;
            if (proximo == null || salida.isBefore(LocalDateTime.of(proximo.getFecha(), proximo.getHoraSalida()))) {
                proximo = it;
            }
        }
        return Optional.ofNullable(proximo);
    }

    public synchronized int getCuposTotales() {
        int total = 0;
        for (Itinerary it : itinerariosList) {
            total += it.getCuposDisponibles();
        }
        return total;
    }

    public synchronized int getCantidadRutas() {
        Set<String> rutas = new HashSet<>();
        for (Itinerary it : itinerariosList) {
            rutas.add(it.getRutaNombre().toLowerCase());
        }
        return rutas.size();
    }

    public Trip crearTrip(Itinerary it) {
        String[] partes = it.getRutaNombre().split(" - ", 2);
        List<Trip.Stop> paradas = new ArrayList<>();
        if (partes.length == 2 && !partes[0].isBlank() && !partes[1].isBlank()) {
            paradas.add(new Trip.Stop(partes[0].trim(), it.getHoraSalida().toString()));
            paradas.add(new Trip.Stop(partes[1].trim(), it.getHoraLlegada().toString()));
        } else {
            paradas.add(new Trip.Stop(it.getRutaNombre(), it.getHoraSalida().toString()));
        }
        return new Trip(it.getId(), it.getRutaNombre(), paradas);
    }

    public synchronized boolean borrarItinerario(String id) {
        return itinerariosList.removeIf(it -> it.getId().equalsIgnoreCase(id));
    }

    private void cargarItinerariosDemo() {
        try {
            LocalDate hoy = LocalDate.now().plusDays(1);
            crearItinerario("Plaza Venezuela - UCV", hoy, LocalTime.of(7, 30), LocalTime.of(8, 15), "UCV-234", "Jose Martinez", 32);
            crearItinerario("La Rinconada - UCV", hoy, LocalTime.of(6, 30), LocalTime.of(7, 30), "UCV-156", "Carlos Gomez", 30);
            crearItinerario("Guarenas - UCV", hoy, LocalTime.of(5, 45), LocalTime.of(7, 0), "UCV-789", "Pedro Perez", 40);
        } catch (Exception ignored) {}
    }
}
