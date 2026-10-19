package com.rurex.controller;

import com.rurex.model.Ruta;
import com.rurex.model.TipoRuta;
import com.rurex.service.RutaService;
import com.rurex.view.RutasView;

import java.util.List;

public class RutaController {

    private final RutaService rutaService;
    private RutasView vista;

    public RutaController(RutaService rutaService) {
        this.rutaService = rutaService;
    }

    public void mostrarVista() {
        if (vista != null) {
            vista.dispose();
        }
        vista = new RutasView(this);
        vista.setVisible(true);
    }

    public List<Ruta> getRutas() {
        return rutaService.getRutas();
    }

    public void crearRuta(String nombre, String origen, String destino,
                          TipoRuta tipo, List<String> paradas) {
        rutaService.agregar(new Ruta(nombre, origen, destino, tipo, paradas));
    }

    public void actualizarRuta(Ruta ruta, String nombre, String origen, String destino,
                               TipoRuta tipo, List<String> paradas) {
        rutaService.actualizar(ruta, nombre, origen, destino, tipo, paradas);
    }

    public void alternarEstado(Ruta ruta) {
        rutaService.alternarEstado(ruta);
    }
}
