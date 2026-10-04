package com.rurex.controller;

import com.rurex.model.Itinerary;
import com.rurex.service.ItineraryService;
import com.rurex.view.ItineraryView;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ItineraryController {

    private final ItineraryService itineraryService;
    private ItineraryView itineraryView;

    public ItineraryController(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    public void setItineraryView(ItineraryView itineraryView) {
        this.itineraryView = itineraryView;
    }

    public List<Itinerary> cargarItinerarios() {
        return itineraryService.getItinerarios();
    }

    public void guardarItinerario(String ruta, LocalDate fecha, LocalTime dep, LocalTime arr, String placa, String chofer, int cupos) {
        try {
            itineraryService.crearItinerario(ruta, fecha, dep, arr, placa, chofer, cupos);
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
        if (itineraryView == null) {
            itineraryView = new ItineraryView(this);
        }
        itineraryView.actualizarTabla();
        itineraryView.setVisible(true);
    }
}
