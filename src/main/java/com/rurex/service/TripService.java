package com.rurex.service;

import com.rurex.model.Trip;
import com.rurex.model.TripStage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class TripService {
    private final TripReservationVerifier reservationVerifier;
    private final Map<String, TripState> states = new ConcurrentHashMap<>();
    private final List<TripListener> listeners = new CopyOnWriteArrayList<>();

    public TripService(TripReservationVerifier reservationVerifier) {
        if (reservationVerifier == null) throw new IllegalArgumentException("El verificador es obligatorio.");
        this.reservationVerifier = reservationVerifier;
    }

    public TripState stateFor(Trip trip) {
        requireTrip(trip);
        return states.computeIfAbsent(trip.id(), id -> initialState(trip));
    }

    public Optional<TripState> stateForPassenger(String passengerId, Trip trip) {
        requireTrip(trip);
        if (passengerId == null || !reservationVerifier.hasActiveReservation(passengerId, trip.id())) {
            return Optional.empty();
        }
        return Optional.of(stateFor(trip));
    }

    public synchronized TripState updateState(Trip trip, TripStage nextStage, int stopIndex, int etaMinutes) {
        requireTrip(trip);
        if (nextStage == null) throw new IllegalArgumentException("Debe seleccionar un estado.");
        if (etaMinutes < 0) throw new IllegalArgumentException("El estimado no puede ser negativo.");
        if (stopIndex < 0 || stopIndex >= trip.stops().size()) throw new IllegalArgumentException("Parada no válida.");
        if (nextStage == TripStage.APPROACHING_DESTINATION && stopIndex != trip.stops().size() - 1) {
            throw new IllegalStateException("El destino solo puede alcanzarse en la última parada.");
        }

        TripState current = stateFor(trip);
        if (!current.stage().canTransitionTo(nextStage)) {
            throw new IllegalStateException("No se puede pasar de " + current.stage().label() + " a " + nextStage.label() + ".");
        }
        if (stopIndex < current.stopIndex()) throw new IllegalStateException("La parada actual no puede retroceder.");

        TripState updated = new TripState(trip.id(), nextStage, stopIndex, etaMinutes, LocalDateTime.now());
        states.put(trip.id(), updated);
        for (TripListener listener : listeners) listener.stateChanged(updated);
        return updated;
    }

    public void addListener(TripListener listener) {
        if (listener != null) listeners.add(listener);
    }

    public void removeListener(TripListener listener) { listeners.remove(listener); }

    private TripState initialState(Trip trip) {
        return new TripState(trip.id(), TripStage.AT_STOP, 0, 0, LocalDateTime.now());
    }

    private void requireTrip(Trip trip) {
        if (trip == null) throw new IllegalArgumentException("Recorrido no válido.");
    }
}
