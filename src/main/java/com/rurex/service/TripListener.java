package com.rurex.service;

@FunctionalInterface
public interface TripListener {
    void stateChanged(TripState state);
}
