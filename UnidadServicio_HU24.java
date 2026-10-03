import java.util.ArrayList;
import java.util.List;

/** Tarea 2: registro, consulta y actualización inmediata de unidades (en memoria). */
public class UnidadServicio_HU24 {
    private final List<Unidad_HU24> unidades = new ArrayList<>();

    public Unidad_HU24 registrar(String placa, String modelo, int capacidad,
                                 EstadoOperativo_HU22 estado) {
        Unidad_HU24 nueva = new Unidad_HU24(placa, modelo, capacidad, estado);
        if (existePlaca(nueva.getPlaca()))
            throw new IllegalArgumentException("Ya existe una unidad con la placa " + nueva.getPlaca() + ".");
        unidades.add(nueva);
        return nueva;
    }

    public boolean existePlaca(String placa) {
        if (placa == null) return false;
        for (Unidad_HU24 u : unidades) {
            if (u.getPlaca().equalsIgnoreCase(placa.trim())) return true;
        }
        return false;
    }

    public Unidad_HU24 buscarPorPlaca(String placa) {
        if (placa != null) {
            for (Unidad_HU24 u : unidades) {
                if (u.getPlaca().equalsIgnoreCase(placa.trim())) return u;
            }
        }
        throw new IllegalArgumentException("No existe una unidad con la placa " + placa + ".");
    }

    /** Edita modelo, capacidad y estado. El cambio es inmediato: listar() ya lo refleja. */
    public Unidad_HU24 actualizar(String placa, String modelo, int capacidad,
                                  EstadoOperativo_HU22 estado) {
        Unidad_HU24 unidad = buscarPorPlaca(placa);
        unidad.actualizar(modelo, capacidad, estado);
        return unidad;
    }

    public Unidad_HU24 cambiarEstado(String placa, EstadoOperativo_HU22 nuevoEstado) {
        Unidad_HU24 unidad = buscarPorPlaca(placa);
        unidad.cambiarEstado(nuevoEstado);
        return unidad;
    }

    public List<Unidad_HU24> listar() { return new ArrayList<>(unidades); }
}
