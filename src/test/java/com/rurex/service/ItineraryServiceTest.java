package com.rurex.service;

import com.rurex.model.Itinerary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.rurex.model.Trip;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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

    @Test
    void testFiltrarPorConductorIgnoraMayusculas() {
        List<Itinerary> lista = itineraryService.getItinerariosPorConductor("  carlos GOMEZ ");
        assertEquals(1, lista.size());
        assertEquals("La Rinconada - UCV", lista.get(0).getRutaNombre());
        assertTrue(itineraryService.getItinerariosPorConductor("Nadie").isEmpty());
        assertTrue(itineraryService.getItinerariosPorConductor(null).isEmpty());
    }

    @Test
    void testProximoItinerarioEsElMasCercanoFuturo() {
        LocalDate hoy = LocalDate.now().plusDays(1);
        LocalDateTime ahora = LocalDateTime.of(hoy, LocalTime.of(6, 0));
        Optional<Itinerary> proximo = itineraryService.getProximoItinerario(itineraryService.getItinerarios(), ahora);
        assertTrue(proximo.isPresent());
        assertEquals("La Rinconada - UCV", proximo.get().getRutaNombre());

        LocalDateTime tarde = LocalDateTime.of(hoy, LocalTime.of(23, 0));
        assertTrue(itineraryService.getProximoItinerario(itineraryService.getItinerarios(), tarde).isEmpty());
    }

    @Test
    void testCuposYRutasDisponibles() {
        assertEquals(102, itineraryService.getCuposTotales());
        assertEquals(3, itineraryService.getCantidadRutas());
    }

    @Test
    void testCrearTripDesdeRutaConOrigenYDestino() {
        Itinerary it = itineraryService.getItinerariosPorConductor("Carlos Gomez").get(0);
        Trip trip = itineraryService.crearTrip(it);
        assertEquals(it.getId(), trip.id());
        assertEquals("La Rinconada - UCV", trip.route());
        assertEquals(2, trip.stops().size());
        assertEquals("La Rinconada", trip.stops().get(0).name());
        assertEquals("06:30", trip.stops().get(0).scheduledTime());
        assertEquals("UCV", trip.stops().get(1).name());
        assertEquals("07:30", trip.stops().get(1).scheduledTime());
    }

    @Test
    void testCrearTripSinSeparadorUsaUnaParada() {
        Itinerary it = itineraryService.crearItinerario("Circuito", LocalDate.now().plusDays(1), LocalTime.of(9, 0), LocalTime.of(10, 0), "UCV-234", "Ana", 10);
        Trip trip = itineraryService.crearTrip(it);
        assertEquals(1, trip.stops().size());
        assertEquals("Circuito", trip.stops().get(0).name());
        assertEquals("09:00", trip.stops().get(0).scheduledTime());
    }
}
