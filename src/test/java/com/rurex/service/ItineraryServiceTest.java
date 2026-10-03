package com.rurex.service;

import com.rurex.model.Itinerary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class ItineraryServiceTest {

    private FleetService fleetService;
    private ItineraryService itineraryService;

    @BeforeEach
    void setUp() {
        fleetService = new FleetService();
        itineraryService = new ItineraryService(fleetService);
    }

    @Test
    void testCreateItinerarySuccess() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Itinerary it = itineraryService.crearItinerario(
                "Caracas - Los Teques",
                tomorrow,
                LocalTime.of(8, 0),
                LocalTime.of(9, 0),
                "UCV-234",
                "Pedro Infante",
                32
        );

        assertNotNull(it);
        assertEquals("Caracas - Los Teques", it.getRutaNombre());
        assertEquals("UCV-234", it.getUnidad().getPlaca());
    }

    @Test
    void testRejectInactiveUnitForItinerary() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                itineraryService.crearItinerario(
                        "Ruta 1",
                        tomorrow,
                        LocalTime.of(8, 0),
                        LocalTime.of(9, 0),
                        "UCV-445",
                        "Chofer",
                        20
                )
        );
        assertTrue(ex.getMessage().contains("no puede asignarse"));
    }

    @Test
    void testRejectScheduleConflictOnSameUnit() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalTime dep = LocalTime.of(10, 0);

        itineraryService.crearItinerario("Ruta A", tomorrow, dep, dep.plusHours(1), "UCV-234", "Chofer A", 30);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                itineraryService.crearItinerario("Ruta B", tomorrow, dep, dep.plusHours(1), "UCV-234", "Chofer B", 30)
        );
        assertTrue(ex.getMessage().contains("ya tiene viaje"));
    }

    @Test
    void testRejectSeatsExceedingCapacity() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                itineraryService.crearItinerario("Ruta Exceso", tomorrow, LocalTime.of(12, 0), LocalTime.of(13, 0), "UCV-234", "Chofer", 999)
        );
        assertTrue(ex.getMessage().contains("superan la capacidad"));
    }
}
