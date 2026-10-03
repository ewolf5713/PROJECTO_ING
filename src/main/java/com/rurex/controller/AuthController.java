package com.rurex.controller;

import com.rurex.model.User;
import com.rurex.model.UserRole;
import com.rurex.service.AuthService;
import com.rurex.view.AdminDashboardView;
import com.rurex.view.LoginView;
import com.rurex.view.RegisterView;

import javax.swing.*;
import java.util.Optional;

public class AuthController {

    private final AuthService authService;
    private LoginView loginView;
    private RegisterView registerView;
    private final FleetController fleetController;
    private final ItineraryController itineraryController;

    public AuthController(AuthService authService, FleetController fleetController, ItineraryController itineraryController) {
        this.authService = authService;
        this.fleetController = fleetController;
        this.itineraryController = itineraryController;
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
                AdminDashboardView dashboard = new AdminDashboardView(user, this, fleetController, itineraryController);
                dashboard.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Panel de usuario en construccion para siguiente sprint.", "Info", JOptionPane.INFORMATION_MESSAGE);
                loginView.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(loginView, "Usuario o clave incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mostrarRegistro() {
        if (loginView != null) loginView.setVisible(false);
        if (registerView == null) {
            registerView = new RegisterView(this);
        }
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
            registerView.dispose();
            if (loginView != null) loginView.setVisible(true);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(registerView, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void volverLogin() {
        if (registerView != null) registerView.dispose();
        if (loginView != null) loginView.setVisible(true);
    }
}
