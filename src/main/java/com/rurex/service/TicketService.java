package com.rurex.service;

import com.rurex.database.TicketDatabase;
import com.rurex.model.Ticket;
import com.rurex.model.User;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

public class TicketService {

    private final List<Ticket> tickets = new ArrayList<>();
    private SecretKey clave;
    private TicketDatabase database;

    public TicketService() {
        byte[] k = new byte[32];
        new SecureRandom().nextBytes(k);
        clave = new SecretKeySpec(k, "AES");
    }

    public synchronized void setDatabase(TicketDatabase database) {
        this.database = database;
        if (database == null) return;
        clave = new SecretKeySpec(database.clave(), "AES");
        tickets.clear();
        tickets.addAll(database.cargar());
    }

    public synchronized Ticket emitir(User u, String itinerarioId, BigDecimal monto) {
        if (ticketDe(u, itinerarioId).isPresent()) {
            throw new IllegalStateException("Ya pagaste el ticket de este itinerario.");
        }
        Ticket t = new Ticket("TK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(), u.getCedula(),
                itinerarioId, monto, LocalDateTime.now().withNano(0).toString(), false);
        tickets.add(t);
        guardar();
        return t;
    }

    public synchronized Optional<Ticket> ticketDe(User u, String itinerarioId) {
        return tickets.stream().filter(t -> t.getCedula().equals(u.getCedula()) && t.getItinerarioId().equals(itinerarioId)).findFirst();
    }

    public synchronized List<Ticket> ticketsDe(User u) {
        return tickets.stream().filter(t -> t.getCedula().equals(u.getCedula())).toList();
    }

    public String codigo(Ticket t) {
        try {
            byte[] iv = new byte[12];
            new SecureRandom().nextBytes(iv);
            Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
            c.init(Cipher.ENCRYPT_MODE, clave, new GCMParameterSpec(128, iv));
            byte[] cifrado = c.doFinal((t.getId() + "|" + t.getCedula() + "|" + t.getItinerarioId()).getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(ByteBuffer.allocate(12 + cifrado.length).put(iv).put(cifrado).array());
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo generar el ticket.", e);
        }
    }

    public synchronized Ticket validar(String codigo, List<String> itinerariosDelConductor) {
        String[] p;
        try {
            byte[] datos = Base64.getUrlDecoder().decode(codigo.trim());
            Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
            c.init(Cipher.DECRYPT_MODE, clave, new GCMParameterSpec(128, datos, 0, 12));
            p = new String(c.doFinal(datos, 12, datos.length - 12), StandardCharsets.UTF_8).split("\\|");
        } catch (Exception e) {
            throw new IllegalArgumentException("Ticket inválido.");
        }
        Ticket t = tickets.stream()
                .filter(x -> p.length == 3 && x.getId().equals(p[0]) && x.getCedula().equals(p[1]) && x.getItinerarioId().equals(p[2]))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Ticket inválido."));
        if (t.isUsado()) throw new IllegalStateException("Ticket ya utilizado.");
        if (itinerariosDelConductor == null || !itinerariosDelConductor.contains(t.getItinerarioId())) {
            throw new IllegalArgumentException("El ticket no corresponde a sus itinerarios.");
        }
        t.setUsado(true);
        guardar();
        return t;
    }

    private void guardar() {
        if (database != null) database.guardar(tickets);
    }
}
