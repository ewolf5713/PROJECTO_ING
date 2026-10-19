package com.rurex.database;

import com.rurex.service.TarifaService;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TarifaDatabase {

    private final String filePath;

    public TarifaDatabase(String filePath) {
        this.filePath = filePath;
    }

    public boolean existe() {
        return JsonFileStorage.exists(filePath);
    }

    public synchronized void guardar(TarifaService t) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("tarifaBase", t.getTarifaBase().toPlainString());
        map.put("porcentajeEstudiante", t.getPorcentajeEstudiante());
        map.put("porcentajeEmpleado", t.getPorcentajeEmpleado());
        JsonFileStorage.write(filePath, List.of(map));
    }

    public synchronized void cargar(TarifaService t) {
        try {
            Map<String, Object> map = JsonFileStorage.readList(filePath).get(0);
            t.configurar(new BigDecimal(String.valueOf(map.get("tarifaBase"))),
                    ((Number) map.get("porcentajeEstudiante")).intValue(),
                    ((Number) map.get("porcentajeEmpleado")).intValue());
        } catch (RuntimeException ignored) {
        }
    }
}
