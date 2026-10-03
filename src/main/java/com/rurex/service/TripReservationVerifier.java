package com.rurex.service;

@FunctionalInterface
public interface TripReservationVerifier {
    boolean hasActiveReservation(String passengerId, String tripId);
}
