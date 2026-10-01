package hu_024;
import hu_022.EstadoOperativo_HU22;
import hu_022.ValidacionException_HU22;
// [HU-024 MODIFICADO (lógica de HU-022)]

import java.util.List;

/** HU-022: registra unidades validando datos y placas duplicadas. */
public class RegistroUnidadService_HU24 {
    private final RepositorioUnidades_HU24 repositorio;

    public RegistroUnidadService_HU24(RepositorioUnidades_HU24 repositorio) {
        this.repositorio = repositorio;
    }

    public UnidadTransporte_HU24 registrar(String placa, String modelo, Object capacidad, String estado) {
        String placaNorm = ValidadorUnidad_HU24.placa(placa);
        String modeloNorm = ValidadorUnidad_HU24.modelo(modelo);
        int cap = ValidadorUnidad_HU24.capacidad(capacidad);
        EstadoOperativo_HU22 est = EstadoOperativo_HU22.desde(estado);

        if (repositorio.existePlaca(placaNorm)) {
            throw new ValidacionException_HU22("La placa '" + placaNorm + "' ya está registrada.");
        }

        UnidadTransporte_HU24 unidad = new UnidadTransporte_HU24(placaNorm, modeloNorm, cap, est);
        repositorio.guardar(unidad);
        return unidad;
    }

    public List<UnidadTransporte_HU24> listar() { return repositorio.listar(); }
}
