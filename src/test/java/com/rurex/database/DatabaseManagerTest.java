package com.rurex.database;

import com.rurex.model.*;
import com.rurex.service.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseManagerTest {

    private Path tempDir;
    private DatabaseManager dbManager;

    @BeforeEach
    void setUp() throws IOException {
        tempDir = Files.createTempDirectory("rurex-test-data");
        dbManager = new DatabaseManager(tempDir.toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        if (Files.exists(tempDir)) {
            Files.walk(tempDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    @Test
    void testRealtimeUserPersistence() {
        AuthService authService = new AuthService();
        dbManager.conectar(authService, null, null, null, null);

        assertTrue(Files.exists(tempDir.resolve("usuarios.json")));

        authService.registrarUsuario("Pedro Perez", "pedro@ucv.ve", "V-12345678", "21-0001", "ClaveSegura123");

        AuthService authService2 = new AuthService(dbManager.getUserDatabase());
        assertTrue(authService2.autenticar("pedro@ucv.ve", "ClaveSegura123").isPresent());
        assertTrue(authService2.autenticar("superadmin", "Superadmin123").isPresent());

        authService.recargarMonedero("estudiante@ucv.ve", "Banco", "04121234567", "V-25000000", "123456", new java.math.BigDecimal("10.00"));
        assertEquals(new java.math.BigDecimal("35.00"), new AuthService(dbManager.getUserDatabase()).consultarSaldo("estudiante@ucv.ve"));
    }

    @Test
    void testRealtimeFleetPersistence() {
        FleetService fleetService = new FleetService();
        dbManager.conectar(null, fleetService, null, null, null);

        assertTrue(Files.exists(tempDir.resolve("unidades.json")));

        fleetService.registrarUnidad("UCV-555", "Marcopolo", 45, OperationalStatus.ACTIVA);

        FleetService fleetService2 = new FleetService(dbManager.getFleetDatabase());
        assertTrue(fleetService2.findByPlaca("UCV-555").isPresent());
        assertEquals("Marcopolo", fleetService2.findByPlaca("UCV-555").get().getModelo());

        fleetService.actualizarEstado("UCV-555", OperationalStatus.EN_MANTENIMIENTO);
        FleetService fleetService3 = new FleetService(dbManager.getFleetDatabase());
        assertEquals(OperationalStatus.EN_MANTENIMIENTO, fleetService3.findByPlaca("UCV-555").get().getEstado());
    }

    @Test
    void testRealtimeItineraryPersistence() {
        FleetService fleetService = new FleetService();
        ItineraryService itineraryService = new ItineraryService(fleetService);
        dbManager.conectar(null, fleetService, itineraryService, null, null);

        assertTrue(Files.exists(tempDir.resolve("itinerarios.json")));

        LocalDate date = LocalDate.now().plusDays(2);
        Itinerary it = itineraryService.crearItinerario("Caracas - Maracay", date, LocalTime.of(8, 0), LocalTime.of(10, 0), "UCV-234", "Luis Chofer", 25, false);

        ItineraryService itineraryService2 = new ItineraryService(fleetService, dbManager.getItineraryDatabase());
        assertTrue(itineraryService2.getItinerarios().stream().anyMatch(i -> i.getId().equals(it.getId())));

        itineraryService.borrarItinerario(it.getId());
        ItineraryService itineraryService3 = new ItineraryService(fleetService, dbManager.getItineraryDatabase());
        assertFalse(itineraryService3.getItinerarios().stream().anyMatch(i -> i.getId().equals(it.getId())));
    }

    @Test
    void testRealtimeRutaPersistence() {
        RutaService rutaService = new RutaService();
        dbManager.conectar(null, null, null, rutaService, null);

        assertTrue(Files.exists(tempDir.resolve("rutas.json")));

        rutaService.agregar(new Ruta("Ruta Express", "Chacao", "UCV", TipoRuta.URBANA, List.of("Bello Monte")));

        RutaService rutaService2 = new RutaService(dbManager.getRutaDatabase());
        assertTrue(rutaService2.getRutas().stream().anyMatch(r -> r.getNombre().equals("Ruta Express")));

        Ruta r = rutaService.getRutas().stream().filter(x -> x.getNombre().equals("Ruta Express")).findFirst().get();
        rutaService.alternarEstado(r);

        RutaService rutaService3 = new RutaService(dbManager.getRutaDatabase());
        Ruta r2 = rutaService3.getRutas().stream().filter(x -> x.getNombre().equals("Ruta Express")).findFirst().get();
        assertFalse(r2.isActivada());
    }

    @Test
    void testRealtimeTripPersistence() {
        TripService tripService = new TripService((p, t) -> true);
        dbManager.conectar(null, null, null, null, tripService);

        Trip trip = new Trip("IT-99", "Ruta A", List.of(
                new Trip.Stop("Inicio", "08:00"),
                new Trip.Stop("Final", "09:00")
        ));

        tripService.updateState(trip, TripStage.IN_TRANSIT, 0, 15);
        assertTrue(Files.exists(tempDir.resolve("viajes.json")));

        TripService tripService2 = new TripService((p, t) -> true, dbManager.getTripDatabase());
        assertTrue(tripService2.currentState("IT-99").isPresent());
        assertEquals(TripStage.IN_TRANSIT, tripService2.currentState("IT-99").get().stage());
        assertEquals(15, tripService2.currentState("IT-99").get().etaMinutes());
    }
}
