package com.rurex.model;

public enum TripStage {
    AT_STOP("En parada"),
    IN_TRANSIT("En trayecto"),
    APPROACHING_DESTINATION("Llegando al destino");

    private final String label;

    TripStage(String label) { this.label = label; }
    public String label() { return label; }

    public boolean canTransitionTo(TripStage next) {
        if (next == this) return true;
        return switch (this) {
            case AT_STOP -> next == IN_TRANSIT;
            case IN_TRANSIT -> next == AT_STOP || next == APPROACHING_DESTINATION;
            case APPROACHING_DESTINATION -> false;
        };
    }
}
