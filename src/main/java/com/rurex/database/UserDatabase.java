package com.rurex.database;

import com.rurex.model.User;
import com.rurex.model.UserRole;

import java.util.*;

public class UserDatabase {

    private final String filePath;

    public UserDatabase(String filePath) {
        this.filePath = filePath;
    }

    public boolean existe() {
        return JsonFileStorage.exists(filePath);
    }

    public synchronized void guardar(Collection<User> usuarios) {
        List<Map<String, Object>> list = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (User u : usuarios) {
            if (u == null || seen.contains(u.getEmail().toLowerCase())) {
                continue;
            }
            seen.add(u.getEmail().toLowerCase());
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("nombreCompleto", u.getNombreCompleto());
            map.put("email", u.getEmail());
            map.put("cedula", u.getCedula());
            map.put("rol", u.getRol().name());
            map.put("carnet", u.getCarnet());
            map.put("passwordHash", u.getPasswordHash());
            list.add(map);
        }
        JsonFileStorage.write(filePath, Json.serialize(list));
    }

    public synchronized List<User> cargar() {
        String content = JsonFileStorage.read(filePath);
        if (content == null || content.isBlank()) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> list = Json.parseList(content);
        List<User> users = new ArrayList<>();
        for (Map<String, Object> map : list) {
            String nombre = (String) map.get("nombreCompleto");
            String email = (String) map.get("email");
            String cedula = (String) map.get("cedula");
            String rolStr = (String) map.get("rol");
            String carnet = (String) map.get("carnet");
            String hash = (String) map.get("passwordHash");
            UserRole rol = UserRole.ESTUDIANTE;
            if (rolStr != null) {
                try {
                    rol = UserRole.valueOf(rolStr.trim().toUpperCase());
                } catch (Exception ignored) {
                }
            }
            users.add(new User(nombre, email, cedula, rol, carnet != null ? carnet : "", hash));
        }
        return users;
    }
}
