package com.rurex.database;

import com.rurex.service.*;

public class DatabaseManager {

    private static DatabaseManager instance;
    private final String dataDirectory;
    private final UserDatabase userDatabase;
    private final FleetDatabase fleetDatabase;
    private final ItineraryDatabase itineraryDatabase;
    private final RutaDatabase rutaDatabase;
    private final TripDatabase tripDatabase;
    private final TarifaDatabase tarifaDatabase;
    private final TicketDatabase ticketDatabase;

    public DatabaseManager() {
        this("data");
    }

    public DatabaseManager(String dataDirectory) {
        this.dataDirectory = dataDirectory;
        this.userDatabase = new UserDatabase(dataDirectory + "/usuarios.json");
        this.fleetDatabase = new FleetDatabase(dataDirectory + "/unidades.json");
        this.itineraryDatabase = new ItineraryDatabase(dataDirectory + "/itinerarios.json");
        this.rutaDatabase = new RutaDatabase(dataDirectory + "/rutas.json");
        this.tripDatabase = new TripDatabase(dataDirectory + "/viajes.json");
        this.tarifaDatabase = new TarifaDatabase(dataDirectory + "/tarifas.json");
        this.ticketDatabase = new TicketDatabase(dataDirectory + "/tickets.json", dataDirectory + "/clave-tickets.key");
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public static synchronized void setInstance(DatabaseManager customInstance) {
        instance = customInstance;
    }

    public void conectar(AuthService authService,
                         FleetService fleetService,
                         ItineraryService itineraryService,
                         RutaService rutaService,
                         TripService tripService) {
        if (authService != null) {
            authService.setDatabase(userDatabase);
            authService.getTarifaService().setDatabase(tarifaDatabase);
            authService.getTicketService().setDatabase(ticketDatabase);
        }
        if (fleetService != null) {
            fleetService.setDatabase(fleetDatabase);
        }
        if (itineraryService != null) {
            itineraryService.setDatabase(itineraryDatabase);
        }
        if (rutaService != null) {
            rutaService.setDatabase(rutaDatabase);
        }
        if (tripService != null) {
            tripService.setDatabase(tripDatabase);
        }
    }

    public String getDataDirectory() {
        return dataDirectory;
    }

    public UserDatabase getUserDatabase() {
        return userDatabase;
    }

    public FleetDatabase getFleetDatabase() {
        return fleetDatabase;
    }

    public ItineraryDatabase getItineraryDatabase() {
        return itineraryDatabase;
    }

    public RutaDatabase getRutaDatabase() {
        return rutaDatabase;
    }

    public TripDatabase getTripDatabase() {
        return tripDatabase;
    }

    public TarifaDatabase getTarifaDatabase() {
        return tarifaDatabase;
    }

    public TicketDatabase getTicketDatabase() {
        return ticketDatabase;
    }

    public static void main(String[] args) {
        DatabaseManager dm = getInstance();
        AuthService authService = new AuthService();
        FleetService fleetService = new FleetService();
        ItineraryService itineraryService = new ItineraryService(fleetService);
        RutaService rutaService = new RutaService();
        TripService tripService = new TripService((p, t) -> true);
        dm.conectar(authService, fleetService, itineraryService, rutaService, tripService);
        System.out.println("Base de datos JSON inicializada en: " + dm.getDataDirectory());
    }
}
