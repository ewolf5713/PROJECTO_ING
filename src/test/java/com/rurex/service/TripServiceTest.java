package com.rurex.service;

import com.rurex.model.Trip;
import com.rurex.model.TripStage;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TripServiceTest {
    private static Trip trip(String id) {
        return new Trip(id, "Plaza Venezuela - UCV", List.of(
                new Trip.Stop("Plaza Venezuela", "07:30"),
                new Trip.Stop("Sabana Grande", "07:38"),
                new Trip.Stop("UCV", "08:00")));
    }

    @Test
    void advancesOnlyThroughValidStagesAndStops() {
        TripService service = new TripService((passengerId, tripId) -> true);
        Trip trip = trip("IT-1");

        assertEquals(TripStage.AT_STOP, service.stateFor(trip).stage());
        TripState state = service.updateState(trip, TripStage.IN_TRANSIT, 1, 12);

        assertEquals(TripStage.IN_TRANSIT, state.stage());
        assertEquals(1, state.stopIndex());
        assertThrows(IllegalStateException.class,
                () -> service.updateState(trip, TripStage.AT_STOP, 0, 5));
        assertThrows(IllegalArgumentException.class,
                () -> service.updateState(trip, TripStage.IN_TRANSIT, 3, 5));
        assertThrows(IllegalArgumentException.class,
                () -> service.updateState(trip, TripStage.IN_TRANSIT, 1, -1));
    }

    @Test
    void rejectsEarlyTerminalStateAndAllowsItOnlyAtFinalStop() {
        TripService service = new TripService((passengerId, tripId) -> true);
        Trip trip = trip("IT-TERMINAL");

        service.updateState(trip, TripStage.IN_TRANSIT, 1, 4);

        assertThrows(IllegalStateException.class,
                () -> service.updateState(trip, TripStage.APPROACHING_DESTINATION, 1, 1));

        TripState terminal = service.updateState(trip, TripStage.APPROACHING_DESTINATION, 2, 0);
        assertEquals(TripStage.APPROACHING_DESTINATION, terminal.stage());
        assertEquals(2, terminal.stopIndex());
    }

    @Test
    void isolatesItinerariesAndExposesStateOnlyToReservedPassengers() {
        TripService service = new TripService((passengerId, tripId) -> passengerId.equals("maria") && tripId.equals("IT-1"));
        Trip first = trip("IT-1");
        Trip second = trip("IT-2");

        service.updateState(first, TripStage.IN_TRANSIT, 1, 4);

        assertEquals(TripStage.AT_STOP, service.stateFor(second).stage());
        assertTrue(service.stateForPassenger("maria", first).isPresent());
        assertEquals(Optional.empty(), service.stateForPassenger("pedro", first));
        assertEquals(Optional.empty(), service.stateForPassenger("maria", second));
    }
}
