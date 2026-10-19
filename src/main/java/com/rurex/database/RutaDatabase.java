package com.rurex.database;

import com.rurex.model.Ruta;
import com.rurex.model.TipoRuta;

import java.util.*;

public class RutaDatabase {

    private final String filePath;

    public RutaDatabase(String filePath) {
        this.filePath = filePath;
    }

    public boolean existe() {
        return JsonFileStorage.exists(filePath);
    }

    public synchronized void guardar(List<Ruta> rutas) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Ruta r : rutas) {
            if (r == null) {
                continue;
            }
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("nombre", r.getNombre());
            map.put("origen", r.getOrigen());
            map.put("destino", r.getDestino());
            map.put("tipo", r.getTipo().name());
            map.put("paradas", r.getParadas());
            map.put("activada", r.isActivada());
            list.add(map);
        }
        JsonFileStorage.write(filePath, list);
    }

    public synchronized List<Ruta> cargar() {
        List<Map<String, Object>> list = JsonFileStorage.readList(filePath);
        List<Ruta> result = new ArrayList<>();
        for (Map<String, Object> map : list) {
            String nombre = (String) map.get("nombre");
            String origen = (String) map.get("origen");
            String destino = (String) map.get("destino");
            String tipoStr = (String) map.get("tipo");
            Boolean activada = (Boolean) map.get("activada");
            TipoRuta tipo = TipoRuta.URBANA;
            if (tipoStr != null) {
                try {
                    tipo = TipoRuta.valueOf(tipoStr.trim().toUpperCase());
                } catch (Exception ignored) {
                }
            }
            List<String> paradas = new ArrayList<>();
            Object pObj = map.get("paradas");
            if (pObj instanceof List<?>) {
                for (Object item : (List<?>) pObj) {
                    if (item != null) {
                        paradas.add(item.toString());
                    }
                }
            }
            Ruta r = new Ruta(nombre, origen, destino, tipo, paradas);
            if (activada != null) {
                r.setActivada(activada);
            }
            result.add(r);
        }
        return result;
    }
}
