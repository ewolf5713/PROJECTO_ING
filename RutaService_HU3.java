package HU003_004.transporte.servicio;

import transporte.modelo.Ruta;
import transporte.modelo.TipoRuta;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import HU003_004.transporte.modelo.Ruta_HU3;
import HU003_004.transporte.modelo.TipoRuta_HU3;

/** HU-003 - Tarea 2: listado de rutas activas (urbanas y extraurbanas). */
public class RutaService_HU3 {

    //es una arreglo dínamico que guarda la data de Ruta Hu3
    private final List<Ruta_HU3> rutas = new ArrayList<>();

    //manteien la data que llega e ruta se lo pasa a la parte de registrar
    public void registrar(Ruta_HU3 ruta) {
        rutas.add(ruta);
    }

    /** Todas las rutas activas, sin importar el tipo. */
    //Es un getter de extraer la rutas activas usando la funcion listarActivas
    public List<Ruta_HU3> listarActivas() {
        return listarActivas(null);
    }

    /** Rutas activas filtradas por tipo. Si tipo es null devuelve todas. */
    //El metodo para filtrar las rutas activas
    public List<Ruta_HU3> listarActivas(TipoRuta_HU3 tipo) {
        List<Ruta_HU3> resultado = new ArrayList<>();
        for (Ruta_HU3 r : rutas) {
            if (r.activa() && (tipo == null || r.tipo() == tipo)) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    /** Rutas activas agrupadas por tipo (urbanas / extraurbanas). */
    //Usan el tipo de variable mapa se vincula las rutas urbana vs estraurbana
    public Map<TipoRuta_HU3, List<Ruta_HU3>> listarActivasPorTipo() {
        Map<TipoRuta_HU3, List<Ruta_HU3>> agrupadas = new EnumMap<>(TipoRuta_HU3.class);
        for (TipoRuta_HU3 t : TipoRuta_HU3.values()) {
            agrupadas.put(t, listarActivas(t));
        }
        return agrupadas;
    }
}