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

    /**
     * Abre la ventana de rutas. Se crea una vista nueva cada vez para que el nombre
     * de usuario y los datos estén siempre al día; las rutas viven en el servicio,
     * así que no se pierde nada.
     */
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

    /** Activada = true pasa a false y viceversa. */
    public void alternarEstado(Ruta ruta) {
        ruta.setActivada(!ruta.isActivada());
    }
}
