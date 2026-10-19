package com.rurex.database;

import com.rurex.model.Itinerary;
import com.rurex.model.TransportUnit;
import com.rurex.service.FleetService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class ItineraryDatabase {

    private final String filePath;

    public ItineraryDatabase(String filePath) {
        this.filePath = filePath;
    }

    public boolean existe() {
        return JsonFileStorage.exists(filePath);
    }

    public synchronized void guardar(List<Itinerary> itinerarios) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Itinerary it : itinerarios) {
            if (it == null) {
                continue;
            }
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", it.getId());
            map.put("rutaNombre", it.getRutaNombre());
            map.put("fecha", it.getFecha().toString());
            map.put("horaSalida", it.getHoraSalida().toString());
            map.put("horaLlegada", it.getHoraLlegada() != null ? it.getHoraLlegada().toString() : null);
            map.put("placa", it.getUnidad().getPlaca());
            map.put("conductor", it.getConductor());
            map.put("cuposDisponibles", it.getCuposDisponibles());
            list.add(map);
        }
        JsonFileStorage.write(filePath, Json.serialize(list));
    }

    public synchronized List<Itinerary> cargar(FleetService fleetService) {
        String content = JsonFileStorage.read(filePath);
        if (content == null || content.isBlank()) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> list = Json.parseList(content);
        List<Itinerary> result = new ArrayList<>();
        for (Map<String, Object> map : list) {
            String id = (String) map.get("id");
            String ruta = (String) map.get("rutaNombre");
            String fechaStr = (String) map.get("fecha");
            String salidaStr = (String) map.get("horaSalida");
            String llegadaStr = (String) map.get("horaLlegada");
            String placa = (String) map.get("placa");
            String conductor = (String) map.get("conductor");
            Number cuposNum = (Number) map.get("cuposDisponibles");
            int cupos = cuposNum != null ? cuposNum.intValue() : 30;

            LocalDate fecha = LocalDate.parse(fechaStr);
            LocalTime salida = LocalTime.parse(salidaStr);
            LocalTime llegada = llegadaStr != null ? LocalTime.parse(llegadaStr) : salida.plusMinutes(45);

            TransportUnit unidad = null;
            if (fleetService != null) {
                unidad = fleetService.findByPlaca(placa).orElse(null);
            }
            if (unidad == null) {
                unidad = new TransportUnit(placa, "Unidad", cupos, null);
            }

            result.add(new Itinerary(id, ruta, fecha, salida, llegada, unidad, conductor, cupos));
        }
        return result;
    }
}
