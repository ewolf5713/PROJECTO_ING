package com.rurex.database;

import com.rurex.model.OperationalStatus;
import com.rurex.model.TransportUnit;

import java.util.*;

public class FleetDatabase {

    private final String filePath;

    public FleetDatabase(String filePath) {
        this.filePath = filePath;
    }

    public boolean existe() {
        return JsonFileStorage.exists(filePath);
    }

    public synchronized void guardar(List<TransportUnit> unidades) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TransportUnit u : unidades) {
            if (u == null) {
                continue;
            }
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("placa", u.getPlaca());
            map.put("modelo", u.getModelo());
            map.put("capacidad", u.getCapacidad());
            map.put("estado", u.getEstado().name());
            list.add(map);
        }
        JsonFileStorage.write(filePath, list);
    }

    public synchronized List<TransportUnit> cargar() {
        List<Map<String, Object>> list = JsonFileStorage.readList(filePath);
        List<TransportUnit> result = new ArrayList<>();
        for (Map<String, Object> map : list) {
            String placa = (String) map.get("placa");
            String modelo = (String) map.get("modelo");
            Number capNum = (Number) map.get("capacidad");
            int capacidad = capNum != null ? capNum.intValue() : 30;
            String estadoStr = (String) map.get("estado");
            OperationalStatus estado = OperationalStatus.ACTIVA;
            if (estadoStr != null) {
                try {
                    estado = OperationalStatus.valueOf(estadoStr.trim().toUpperCase());
                } catch (Exception e) {
                    estado = OperationalStatus.fromString(estadoStr);
                }
            }
            result.add(new TransportUnit(placa, modelo, capacidad, estado));
        }
        return result;
    }
}
