package com.rurex.service;

import com.rurex.model.OperationalStatus;
import com.rurex.model.TransportUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FleetServiceTest {

    private FleetService fleetService;

    @BeforeEach
    void setUp() {
        fleetService = new FleetService();
    }

    @Test
    void testRegisterUnitSuccess() {
        TransportUnit unit = fleetService.registrarUnidad("UCV-999", "Mercedes-Benz", 35, OperationalStatus.ACTIVA);
        assertNotNull(unit);
        assertEquals("UCV-999", unit.getPlaca());
        assertTrue(unit.isAssignable());
    }

    @Test
    void testRejectDuplicatePlate() {
        fleetService.registrarUnidad("UCV-500", "Autobus", 30, OperationalStatus.ACTIVA);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                fleetService.registrarUnidad("UCV-500", "Otro", 30, OperationalStatus.ACTIVA)
        );
        assertTrue(ex.getMessage().contains("ya esta registrada"));
    }

    @Test
    void testListAllRegisteredUnits() {
        List<TransportUnit> unidades = fleetService.getUnidades();
        assertEquals(5, unidades.size());
        assertEquals(unidades.size(), fleetService.filtrarPorEstado(null).size());
    }

    @Test
    void testNewUnitShowsInListAndFilterRightAway() {
        fleetService.registrarUnidad("UCV-321", "Yutong ZK6", 40, OperationalStatus.EN_MANTENIMIENTO);
        assertEquals(6, fleetService.getUnidades().size());
        assertTrue(fleetService.filtrarPorEstado(OperationalStatus.EN_MANTENIMIENTO).stream()
                .anyMatch(u -> u.getPlaca().equals("UCV-321")));
        assertTrue(fleetService.filtrarPorEstado(OperationalStatus.ACTIVA).stream()
                .noneMatch(u -> u.getPlaca().equals("UCV-321")));
    }

    @Test
    void testFailedEditKeepsUnitUnchanged() {
        assertThrows(IllegalArgumentException.class, () ->
                fleetService.editarUnidad("UCV-234", "Modelo Nuevo", 0, null)
        );
        TransportUnit unidad = fleetService.getUnidades().stream()
                .filter(u -> u.getPlaca().equals("UCV-234"))
                .findFirst().orElseThrow();
        assertEquals("Mercedes-Benz OF-1721", unidad.getModelo());
        assertEquals(32, unidad.getCapacidad());
    }

    @Test
    void testFilterByOperationalStatus() {
        List<TransportUnit> active = fleetService.filtrarPorEstado(OperationalStatus.ACTIVA);
        assertFalse(active.isEmpty());
        for (TransportUnit u : active) {
            assertEquals(OperationalStatus.ACTIVA, u.getEstado());
        }
    }

    @Test
    void testRejectInactiveUnitAssignment() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                fleetService.validarUnidadParaItinerario("UCV-445")
        );
        assertTrue(ex.getMessage().contains("no puede asignarse"));
    }
}
