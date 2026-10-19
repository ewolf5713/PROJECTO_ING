package com.rurex.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class JsonFileStorage {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private JsonFileStorage() {
    }

    public static boolean exists(String path) {
        if (path == null) {
            return false;
        }
        return Files.exists(Path.of(path));
    }

    public static List<Map<String, Object>> readList(String path) {
        if (path == null || !Files.exists(Path.of(path))) {
            return new ArrayList<>();
        }
        try {
            List<Map<String, Object>> list = GSON.fromJson(Files.readString(Path.of(path), StandardCharsets.UTF_8),
                    new TypeToken<List<Map<String, Object>>>() {}.getType());
            return list != null ? list : new ArrayList<>();
        } catch (IOException | JsonParseException e) {
            return new ArrayList<>();
        }
    }

    public static void write(String path, Object data) {
        if (path == null || data == null) {
            return;
        }
        String content = GSON.toJson(data);
        try {
            Path p = Path.of(path);
            if (p.getParent() != null) {
                Files.createDirectories(p.getParent());
            }
            Path temp = Path.of(path + ".tmp");
            Files.writeString(temp, content, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            Files.move(temp, p, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            try {
                Files.writeString(Path.of(path), content, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException ignored) {
            }
        }
    }
}
