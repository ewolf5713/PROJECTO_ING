package com.rurex.controller;

import com.rurex.model.OperationalStatus;
import com.rurex.model.TransportUnit;
import com.rurex.service.FleetService;
import com.rurex.view.FleetView;

import javax.swing.*;
import java.util.List;

public class FleetController {

    private final FleetService fleetService;
    private FleetView fleetView;

    public FleetController(FleetService fleetService) {
        this.fleetService = fleetService;
    }

    public void setFleetView(FleetView fleetView) {
        this.fleetView = fleetView;
    }

    public List<TransportUnit> cargarUnidades(OperationalStatus filtro) {
        return fleetService.filtrarPorEstado(filtro);
    }

    public void registrarNuevaUnidad(String placa, String modelo, int capacidad, OperationalStatus estado) {
        try {
            fleetService.registrarUnidad(placa, modelo, capacidad, estado);
            JOptionPane.showMessageDialog(fleetView, "Unidad registrada correctamente.", "Listo", JOptionPane.INFORMATION_MESSAGE);
            if (fleetView != null) fleetView.actualizarTabla();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(fleetView, ex.getMessage(), "Error al Registrar", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void editarUnidad(String placa, String modelo, int capacidad, OperationalStatus estado) {
        try {
            fleetService.editarUnidad(placa, modelo, capacidad, estado);
            JOptionPane.showMessageDialog(fleetView, "Unidad editada con exito.", "Listo", JOptionPane.INFORMATION_MESSAGE);
            if (fleetView != null) fleetView.actualizarTabla();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(fleetView, ex.getMessage(), "Error al Editar", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cambiarEstadoUnidad(String placa, OperationalStatus nuevoEstado) {
        try {
            fleetService.actualizarEstado(placa, nuevoEstado);
            JOptionPane.showMessageDialog(fleetView, "Estado actualizado con exito.", "Listo", JOptionPane.INFORMATION_MESSAGE);
            if (fleetView != null) fleetView.actualizarTabla();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(fleetView, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mostrarVista() {
        if (fleetView == null) {
            fleetView = new FleetView(this);
        }
        fleetView.actualizarTabla();
        fleetView.setVisible(true);
    }
}
