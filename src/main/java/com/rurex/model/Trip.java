package com.rurex.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Trip {
    public record Stop(String name, String scheduledTime) {
        public Stop {
            if (name == null || name.isBlank() || scheduledTime == null || scheduledTime.isBlank()) {
                throw new IllegalArgumentException("La parada requiere nombre y hora.");
            }
        }
    }

    private final String id;
    private final String route;
    private final List<Stop> stops;

    public Trip(String id, String route, List<Stop> stops) {
        if (id == null || id.isBlank() || route == null || route.isBlank() || stops == null || stops.isEmpty()) {
            throw new IllegalArgumentException("El recorrido requiere identificador, ruta y paradas.");
        }
        this.id = id.trim();
        this.route = route.trim();
        this.stops = Collections.unmodifiableList(new ArrayList<>(stops));
    }

    public String id() { return id; }
    public String route() { return route; }
    public List<Stop> stops() { return stops; }

    @Override public boolean equals(Object other) {
        return other instanceof Trip trip && id.equals(trip.id);
    }

    @Override public int hashCode() { return Objects.hash(id); }
}
