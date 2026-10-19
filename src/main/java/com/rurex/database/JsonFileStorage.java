package com.rurex.database;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

public final class JsonFileStorage {

    private JsonFileStorage() {
    }

    public static boolean exists(String path) {
        if (path == null) {
            return false;
        }
        return Files.exists(Path.of(path));
    }

    public static String read(String path) {
        if (path == null) {
            return null;
        }
        try {
            Path p = Path.of(path);
            if (!Files.exists(p)) {
                return null;
            }
            return Files.readString(p, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }

    public static void write(String path, String content) {
        if (path == null || content == null) {
            return;
        }
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
                Path p = Path.of(path);
                Files.writeString(p, content, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException ignored) {
            }
        }
    }
}
