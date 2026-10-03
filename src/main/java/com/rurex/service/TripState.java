package com.rurex.service;

import com.rurex.model.TripStage;

import java.time.LocalDateTime;

public record TripState(String tripId, TripStage stage, int stopIndex, int etaMinutes,
                        LocalDateTime updatedAt) {
}
