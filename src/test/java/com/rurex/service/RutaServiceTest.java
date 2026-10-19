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

    private Ruta buscar(String nombre) {
        return rutaService.getRutas().stream()
                .filter(r -> r.getNombre().equals(nombre))
                .findFirst().orElseThrow();
    }

    @Test
    void testDefaultRoutesLoaded() {
        List<Ruta> rutas = rutaService.getRutas();
        assertEquals(5, rutas.size());
        assertTrue(rutas.stream().allMatch(Ruta::isActivada));
        assertEquals(TipoRuta.URBANA, buscar("Plaza Venezuela - UCV").getTipo());
    }

    @Test
    void testAddNewRoute() {
        rutaService.agregar(new Ruta("Caricuao - UCV", "Caricuao", "Ciudad Universitaria",
                TipoRuta.URBANA, List.of("La Paz", "")));
        assertEquals(6, rutaService.getRutas().size());
        assertEquals(List.of("La Paz"), buscar("Caricuao - UCV").getParadas());
    }

    @Test
    void testRejectDuplicateNameIgnoringCaseAndSpaces() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                rutaService.agregar(new Ruta("  petare - ucv ", "Petare", "Chacaito", TipoRuta.URBANA))
        );
        assertTrue(ex.getMessage().contains("Ya existe una ruta"));
        assertEquals(5, rutaService.getRutas().size());
    }

    @Test
    void testUpdateCanKeepOwnName() {
        Ruta ruta = buscar("Petare - UCV");
        rutaService.actualizar(ruta, "Petare - UCV", "Petare", "Ciudad Universitaria",
                TipoRuta.EXTRAURBANA, List.of("Los Dos Caminos"));
        assertEquals(TipoRuta.EXTRAURBANA, ruta.getTipo());
        assertEquals(List.of("Los Dos Caminos"), ruta.getParadas());
    }

    @Test
    void testUpdateRejectsOtherRouteNameAndKeepsRouteUnchanged() {
        Ruta ruta = buscar("Petare - UCV");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                rutaService.actualizar(ruta, "Guarenas - UCV", "Otro Origen", "Otro Destino",
                        TipoRuta.EXTRAURBANA, List.of("Parada"))
        );
        assertTrue(ex.getMessage().contains("Ya existe una ruta"));
        assertEquals("Petare - UCV", ruta.getNombre());
        assertEquals("Petare", ruta.getOrigen());
        assertEquals("Ciudad Universitaria", ruta.getDestino());
        assertEquals(TipoRuta.URBANA, ruta.getTipo());
        assertTrue(ruta.getParadas().isEmpty());
    }

    @Test
    void testActiveRoutesExcludeDeactivated() {
        Ruta ruta = buscar("Los Teques - UCV");
        ruta.setActivada(false);
        List<Ruta> activas = rutaService.getRutasActivas();
        assertEquals(4, activas.size());
        assertFalse(activas.contains(ruta));
        assertEquals(5, rutaService.getRutas().size());
    }

    @Test
    void testRouteRejectsBlankName() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Ruta("   ", "Origen", "Destino", TipoRuta.URBANA)
        );
        assertEquals("El nombre es obligatorio.", ex.getMessage());
    }

    @Test
    void testAlternarEstado() {
        Ruta r = rutaService.getRutas().get(0);
        rutaService.alternarEstado(r);
        assertFalse(r.isActivada());
        assertEquals(4, rutaService.getRutasActivas().size());
        rutaService.alternarEstado(r);
        assertTrue(r.isActivada());
    }
}
