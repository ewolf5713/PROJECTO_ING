import java.util.ArrayList;
import java.util.List;

/** Tarea 2: registro de unidades con validación de placa única (en memoria). */
public class UnidadServicio_HU22 {
    private final List<Unidad_HU22> unidades = new ArrayList<>();

    public Unidad_HU22 registrar(String placa, String modelo, int capacidad,
                                 EstadoOperativo_HU22 estado) {
        Unidad_HU22 nueva = new Unidad_HU22(placa, modelo, capacidad, estado);
        if (existePlaca(nueva.getPlaca()))
            throw new IllegalArgumentException("Ya existe una unidad con la placa " + nueva.getPlaca() + ".");
        unidades.add(nueva);
        return nueva;
    }

    public boolean existePlaca(String placa) {
        if (placa == null) return false;
        String buscada = placa.trim();
        for (Unidad_HU22 u : unidades) {
            if (u.getPlaca().equalsIgnoreCase(buscada)) return true;
        }
        return false;
    }

    public List<Unidad_HU22> listar() { return new ArrayList<>(unidades); }
}
