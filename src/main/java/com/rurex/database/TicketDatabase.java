package com.rurex.database;

import com.rurex.model.Ticket;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.*;

public class TicketDatabase {

    private final String filePath;
    private final String clavePath;

    public TicketDatabase(String filePath, String clavePath) {
        this.filePath = filePath;
        this.clavePath = clavePath;
    }

    public synchronized void guardar(List<Ticket> tickets) {
        List<Map<String, Object>> lista = new ArrayList<>();
        for (Ticket t : tickets) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", t.getId());
            map.put("cedula", t.getCedula());
            map.put("itinerarioId", t.getItinerarioId());
            map.put("monto", t.getMonto().toPlainString());
            map.put("emision", t.getEmision());
            map.put("usado", t.isUsado());
            lista.add(map);
        }
        JsonFileStorage.write(filePath, lista);
    }

    public synchronized List<Ticket> cargar() {
        List<Ticket> tickets = new ArrayList<>();
        for (Map<String, Object> m : JsonFileStorage.readList(filePath)) {
            tickets.add(new Ticket((String) m.get("id"), (String) m.get("cedula"), (String) m.get("itinerarioId"),
                    new BigDecimal(String.valueOf(m.get("monto"))), (String) m.get("emision"), Boolean.TRUE.equals(m.get("usado"))));
        }
        return tickets;
    }

    public synchronized byte[] clave() {
        Path p = Path.of(clavePath);
        try {
            if (Files.exists(p)) return Base64.getDecoder().decode(Files.readString(p).trim());
            byte[] clave = new byte[32];
            new SecureRandom().nextBytes(clave);
            if (p.getParent() != null) Files.createDirectories(p.getParent());
            Files.writeString(p, Base64.getEncoder().encodeToString(clave));
            return clave;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
