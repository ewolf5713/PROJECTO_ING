package com.rurex.database;

import com.rurex.model.TripStage;
import com.rurex.service.TripState;

import java.time.LocalDateTime;
import java.util.*;

public class TripDatabase {

    private final String filePath;

    public TripDatabase(String filePath) {
        this.filePath = filePath;
    }

    public boolean existe() {
        return JsonFileStorage.exists(filePath);
    }

    public synchronized void guardar(Map<String, TripState> states) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TripState state : states.values()) {
            if (state == null) {
                continue;
            }
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("tripId", state.tripId());
            map.put("stage", state.stage().name());
            map.put("stopIndex", state.stopIndex());
            map.put("etaMinutes", state.etaMinutes());
            map.put("updatedAt", state.updatedAt().toString());
            list.add(map);
        }
        JsonFileStorage.write(filePath, list);
    }

    public synchronized Map<String, TripState> cargar() {
        List<Map<String, Object>> list = JsonFileStorage.readList(filePath);
        Map<String, TripState> map = new HashMap<>();
        for (Map<String, Object> item : list) {
            String tripId = (String) item.get("tripId");
            String stageStr = (String) item.get("stage");
            Number stopNum = (Number) item.get("stopIndex");
            Number etaNum = (Number) item.get("etaMinutes");
            String updatedStr = (String) item.get("updatedAt");
            TripStage stage = TripStage.AT_STOP;
            if (stageStr != null) {
                try {
                    stage = TripStage.valueOf(stageStr.trim().toUpperCase());
                } catch (Exception ignored) {
                }
            }
            int stopIndex = stopNum != null ? stopNum.intValue() : 0;
            int etaMinutes = etaNum != null ? etaNum.intValue() : 0;
            LocalDateTime updatedAt = updatedStr != null ? LocalDateTime.parse(updatedStr) : LocalDateTime.now();
            map.put(tripId, new TripState(tripId, stage, stopIndex, etaMinutes, updatedAt));
        }
        return map;
    }
}
