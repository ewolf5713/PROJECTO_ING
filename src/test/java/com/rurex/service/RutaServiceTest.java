package com.rurex.service;

import com.rurex.model.Ruta;
import com.rurex.model.TipoRuta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RutaServiceTest {

    private RutaService rutaService;

    @BeforeEach
    void setUp() {
        rutaService = new RutaService();
    }

    @Test
    void testInitialRutas() {
        List<Ruta> rutas = rutaService.getRutas();
        assertFalse(rutas.isEmpty());
        assertEquals(5, rutas.size());
        assertEquals(5, rutaService.getRutasActivas().size());
    }

    @Test
    void testAgregarRutaSuccess() {
        Ruta nueva = new Ruta("Caracas - Valencia", "Caracas", "Valencia", TipoRuta.EXTRAURBANA, List.of("Maracay", "Guacara"));
        rutaService.agregar(nueva);
        assertEquals(6, rutaService.getRutas().size());
    }

    @Test
    void testRejectDuplicateNombre() {
        assertThrows(IllegalArgumentException.class, () ->
                rutaService.agregar(new Ruta("Plaza Venezuela - UCV", "Origen", "Destino", TipoRuta.URBANA))
        );
    }

    @Test
    void testAlternarEstado() {
        Ruta r = rutaService.getRutas().get(0);
        assertTrue(r.isActivada());
        rutaService.alternarEstado(r);
        assertFalse(r.isActivada());
        assertEquals(4, rutaService.getRutasActivas().size());
        rutaService.alternarEstado(r);
        assertTrue(r.isActivada());
        assertEquals(5, rutaService.getRutasActivas().size());
    }

    @Test
    void testActualizarRuta() {
        Ruta r = rutaService.getRutas().get(0);
        rutaService.actualizar(r, "Plaza Venezuela - UCV Modificada", "Plaza Venezuela", "Facultad de Ciencias", TipoRuta.URBANA, List.of("Puerta Tamanaco"));
        assertEquals("Plaza Venezuela - UCV Modificada", r.getNombre());
        assertEquals("Facultad de Ciencias", r.getDestino());
        assertEquals(1, r.getParadas().size());
    }
}
