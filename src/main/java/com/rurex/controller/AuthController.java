package com.rurex.controller;

import com.rurex.model.User;
import com.rurex.model.UserRole;
import com.rurex.service.AuthService;
import com.rurex.service.FleetService;
import com.rurex.service.ItineraryService;
import com.rurex.service.TripService;
import com.rurex.view.AdminDashboardView;
import com.rurex.view.ConductorDashboardView;
import com.rurex.view.LoginView;
import com.rurex.view.PassengerDashboardView;
import com.rurex.view.RegisterView;

import javax.swing.*;
import java.util.Optional;

public class AuthController {

    private final AuthService authService;
    private LoginView loginView;
    private RegisterView registerView;
    private final FleetController fleetController;
    private final ItineraryController itineraryController;
    private final FleetService fleetService;
    private final ItineraryService itineraryService;
    private final TripService tripService;

    public AuthController(AuthService authService, FleetController fleetController, ItineraryController itineraryController, FleetService fleetService, ItineraryService itineraryService, TripService tripService) {
        this.tripService = tripService;
        this.authService = authService;
        this.fleetController = fleetController;
        this.itineraryController = itineraryController;
        this.fleetService = fleetService;
        this.itineraryService = itineraryService;
    }

    public void setLoginView(LoginView loginView) {
        this.loginView = loginView;
    }

    public void setRegisterView(RegisterView registerView) {
        this.registerView = registerView;
    }

    public void login(String login, String password) {
        Optional<User> userOpt = authService.autenticar(login, password);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            JOptionPane.showMessageDialog(loginView, "Bienvenido " + user.getNombreCompleto(), "Login OK", JOptionPane.INFORMATION_MESSAGE);
            loginView.dispose();

            if (user.getRol() == UserRole.ADMINISTRADOR) {
                AdminDashboardView dashboard = new AdminDashboardView(user, this, fleetController, itineraryController, fleetService, itineraryService);
                dashboard.setVisible(true);
            } else if (user.getRol() == UserRole.CONDUCTOR) {
                ConductorDashboardView dashboard = new ConductorDashboardView(user, this, itineraryService, tripService);
                dashboard.setVisible(true);
            } else {
                PassengerDashboardView dashboard = new PassengerDashboardView(user, this, itineraryService, tripService);
                dashboard.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(loginView, "Usuario o clave incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mostrarRegistro() {
        if (loginView != null) loginView.setVisible(false);
        if (registerView != null) registerView.dispose();
        registerView = new RegisterView(this);
        registerView.setVisible(true);
    }

    public void registrar(String nombre, String email, String cedula, UserRole rol, String carnet, String pass, String confirmPass) {
        if (!pass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(registerView, "Las contraseñas no coinciden.", "Alerta", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            authService.registrarUsuario(nombre, email, cedula, rol, carnet, pass);
            JOptionPane.showMessageDialog(registerView, "Usuario registrado con exito.", "Listo", JOptionPane.INFORMATION_MESSAGE);
            volverLogin();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(registerView, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void volverLogin() {
        if (registerView != null) {
            registerView.dispose();
            registerView = null;
        }
        if (loginView != null) loginView.dispose();
        loginView = new LoginView(this);
        loginView.setVisible(true);
    }
}
