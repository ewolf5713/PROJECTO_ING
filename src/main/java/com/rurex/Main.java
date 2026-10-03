package com.rurex;

import com.rurex.controller.AuthController;
import com.rurex.controller.FleetController;
import com.rurex.controller.ItineraryController;
import com.rurex.service.AuthService;
import com.rurex.service.FleetService;
import com.rurex.service.ItineraryService;
import com.rurex.view.LoginView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AuthService authService = new AuthService();
            FleetService fleetService = new FleetService();
            ItineraryService itineraryService = new ItineraryService(fleetService);

            FleetController fleetController = new FleetController(fleetService);
            ItineraryController itineraryController = new ItineraryController(itineraryService);
            AuthController authController = new AuthController(authService, fleetController, itineraryController, fleetService, itineraryService);

            LoginView loginView = new LoginView(authController);
            loginView.setVisible(true);
        });
    }
}
