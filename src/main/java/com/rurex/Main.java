package com.rurex;

import com.rurex.controller.AuthController;
import com.rurex.controller.FleetController;
import com.rurex.controller.ItineraryController;
import com.rurex.controller.RutaController;
import com.rurex.database.DatabaseManager;
import com.rurex.service.AuthService;
import com.rurex.service.FleetService;
import com.rurex.service.ItineraryService;
import com.rurex.service.RutaService;
import com.rurex.service.TripService;
import com.rurex.view.Estilos;
import com.rurex.view.LoginView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AuthService authService = new AuthService();
            FleetService fleetService = new FleetService();
            ItineraryService itineraryService = new ItineraryService(fleetService);
            RutaService rutaService = new RutaService();
            TripService tripService = new TripService((passengerId, tripId) -> true);

            DatabaseManager.getInstance().conectar(authService, fleetService, itineraryService, rutaService, tripService);

            FleetController fleetController = new FleetController(fleetService);
            ItineraryController itineraryController = new ItineraryController(itineraryService);
            RutaController rutaController = new RutaController(rutaService);
            Estilos.setRutaController(rutaController);

            AuthController authController = new AuthController(authService, fleetController, itineraryController, fleetService, itineraryService, tripService);

            LoginView loginView = new LoginView(authController);
            loginView.setVisible(true);
        });
    }
}
