package com.rurex.controller;

import com.rurex.model.Itinerary;
import com.rurex.model.OperationalStatus;
import com.rurex.model.Ruta;
import com.rurex.model.TipoRuta;
import com.rurex.model.TransportUnit;
import com.rurex.model.User;
import com.rurex.model.UserRole;
import com.rurex.service.AuthService;
import com.rurex.service.FleetService;
import com.rurex.service.ItineraryService;
import com.rurex.service.RutaService;
import com.rurex.view.ItineraryView;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ItineraryController {

    private final ItineraryService itineraryService;
    private final RutaService rutaService;
    private final FleetService fleetService;
    private final AuthService authService;
    private ItineraryView itineraryView;

    public ItineraryController(ItineraryService itineraryService, RutaService rutaService, FleetService fleetService, AuthService authService) {
        this.itineraryService = itineraryService;
        this.rutaService = rutaService;
        this.fleetService = fleetService;
        this.authService = authService;
    }

    public void setItineraryView(ItineraryView itineraryView) {
        this.itineraryView = itineraryView;
    }

    public List<Itinerary> cargarItinerarios() {
        return itineraryService.getItinerarios();
    }

    public List<Ruta> getRutasActivas() {
        return rutaService.getRutasActivas();
    }

    public List<String> getPlacasActivas() {
        return fleetService.filtrarPorEstado(OperationalStatus.ACTIVA).stream().map(TransportUnit::getPlaca).sorted().toList();
    }

    public int getCapacidad(String placa) {
        return fleetService.getUnidades().stream().filter(u -> u.getPlaca().equals(placa)).mapToInt(TransportUnit::getCapacidad).findFirst().orElse(0);
    }

    public List<String> getConductores() {
        return authService.getUsuarios().stream().filter(u -> u.getRol() == UserRole.CONDUCTOR).map(User::getNombreCompleto).sorted().toList();
    }

    public void guardarItinerario(Ruta ruta, LocalDate fecha, LocalTime dep, LocalTime arr, String placa, String chofer, int cupos) {
        try {
            if (ruta == null) throw new IllegalArgumentException("Seleccione una ruta activa.");
            itineraryService.crearItinerario(ruta.getNombre(), fecha, dep, arr, placa, chofer, cupos, ruta.getTipo() == TipoRuta.EXTRAURBANA);
            JOptionPane.showMessageDialog(itineraryView, "Itinerario creado con exito.", "Listo", JOptionPane.INFORMATION_MESSAGE);
            if (itineraryView != null) itineraryView.actualizarTabla();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(itineraryView, ex.getMessage(), "Atencion", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void eliminarItinerario(String id) {
        int resp = JOptionPane.showConfirmDialog(itineraryView, "Eliminar itinerario " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            itineraryService.borrarItinerario(id);
            if (itineraryView != null) itineraryView.actualizarTabla();
        }
    }

    public void mostrarVista() {
        if (itineraryView != null) itineraryView.dispose();
        itineraryView = new ItineraryView(this);
        itineraryView.actualizarTabla();
        itineraryView.setVisible(true);
    }
}
