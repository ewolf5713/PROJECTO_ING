package com.rurex.service;

import com.rurex.model.Ticket;
import com.rurex.model.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TicketServiceTest {

    private final AuthService auth = new AuthService();
    private final TicketService tickets = auth.getTicketService();
    private final User estudiante = auth.autenticar("estudiante@ucv.ve", "Estudiante123").orElseThrow();

    @Test
    void emitirYValidarElCodigo() {
        Ticket t = tickets.emitir(estudiante, "IT-1", new BigDecimal("1.50"));
        String codigo = tickets.codigo(t);
        assertFalse(codigo.contains(t.getId()));
        assertFalse(codigo.contains("="));
        Ticket validado = tickets.validar(codigo, List.of("IT-1"));
        assertSame(t, validado);
        assertTrue(t.isUsado());
        assertEquals(List.of(t), tickets.ticketsDe(estudiante));
    }

    @Test
    void codigoAlteradoEsInvalido() {
        String codigo = tickets.codigo(tickets.emitir(estudiante, "IT-1", BigDecimal.ONE));
        char c = codigo.charAt(20);
        String alterado = codigo.substring(0, 20) + (c == 'A' ? 'B' : 'A') + codigo.substring(21);
        assertEquals("Ticket inválido.", assertThrows(IllegalArgumentException.class, () -> tickets.validar(alterado, List.of("IT-1"))).getMessage());
        assertThrows(IllegalArgumentException.class, () -> tickets.validar("basura", List.of("IT-1")));
        assertFalse(tickets.ticketsDe(estudiante).get(0).isUsado());
    }

    @Test
    void segundoEscaneoEsRechazado() {
        String codigo = tickets.codigo(tickets.emitir(estudiante, "IT-1", BigDecimal.ONE));
        tickets.validar(codigo, List.of("IT-1"));
        assertEquals("Ticket ya utilizado.", assertThrows(IllegalStateException.class, () -> tickets.validar(codigo, List.of("IT-1"))).getMessage());
    }

    @Test
    void conductorDeOtroItinerarioEsRechazado() {
        Ticket t = tickets.emitir(estudiante, "IT-1", BigDecimal.ONE);
        assertEquals("El ticket no corresponde a sus itinerarios.",
                assertThrows(IllegalArgumentException.class, () -> tickets.validar(tickets.codigo(t), List.of("IT-2"))).getMessage());
        assertFalse(t.isUsado());
    }

    @Test
    void codigoDeOtraClaveEsInvalido() {
        Ticket t = tickets.emitir(estudiante, "IT-1", BigDecimal.ONE);
        String ajeno = new TicketService().codigo(t);
        assertThrows(IllegalArgumentException.class, () -> tickets.validar(ajeno, List.of("IT-1")));
    }

    @Test
    void pagarDevuelveElTicketYNoPermiteDoblePago() {
        Ticket t = auth.pagarTicketExtraurbano("estudiante@ucv.ve", "IT-1");
        assertEquals("V-25000000", t.getCedula());
        assertEquals("IT-1", t.getItinerarioId());
        assertEquals(0, t.getMonto().compareTo(new BigDecimal("1.50")));
        assertTrue(t.getId().startsWith("TK-"));
        assertTrue(tickets.ticketDe(estudiante, "IT-1").isPresent());
        assertThrows(IllegalStateException.class, () -> tickets.emitir(estudiante, "IT-1", BigDecimal.ONE));
    }
}
