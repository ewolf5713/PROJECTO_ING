package hu_022;
// [HU-022]

import java.util.List;

/**
 * HU-022: registra unidades validando datos y placas duplicadas.
 * Recibe datos "crudos" (Strings/Object), así que sirve igual desde JSON, Swing o pruebas.
 */
public class RegistroUnidadService_HU22 {
    private final RepositorioUnidades_HU22 repositorio;

    public RegistroUnidadService_HU22(RepositorioUnidades_HU22 repositorio) {
        this.repositorio = repositorio;
    }

    /** Capacidad como Object: acepta Integer, Double (Gson) o String (campo de texto Swing). */
    public UnidadTransporte_HU22 registrar(String placa, String modelo, Object capacidad, String estado) {
        String placaNorm = normalizarPlaca(placa);
        if (modelo == null || modelo.isBlank()) {
            throw new ValidacionException_HU22("El modelo es obligatorio.");
        }
        int cap = parsearCapacidad(capacidad);
        EstadoOperativo_HU22 est = EstadoOperativo_HU22.desde(estado);

        if (repositorio.existePlaca(placaNorm)) {
            throw new ValidacionException_HU22("La placa '" + placaNorm + "' ya está registrada.");
        }

        UnidadTransporte_HU22 unidad = new UnidadTransporte_HU22(placaNorm, modelo.trim(), cap, est);
        repositorio.guardar(unidad);
        return unidad;
    }

    public List<UnidadTransporte_HU22> listar() { return repositorio.listar(); }

    private String normalizarPlaca(String placa) {
        if (placa == null || placa.isBlank()) {
            throw new ValidacionException_HU22("La placa es obligatoria.");
        }
        return placa.trim().toUpperCase();
    }

    private int parsearCapacidad(Object valor) {
        if (valor == null) throw new ValidacionException_HU22("La capacidad de pasajeros es obligatoria.");
        int cap;
        if (valor instanceof Number n) {
            if (n.doubleValue() != Math.floor(n.doubleValue())) {
                throw new ValidacionException_HU22("La capacidad debe ser un número entero.");
            }
            cap = n.intValue();
        } else {
            try {
                cap = Integer.parseInt(valor.toString().trim());
            } catch (NumberFormatException e) {
                throw new ValidacionException_HU22("La capacidad debe ser un número entero.");
            }
        }
        if (cap <= 0) throw new ValidacionException_HU22("La capacidad debe ser mayor a cero.");
        return cap;
    }
}
