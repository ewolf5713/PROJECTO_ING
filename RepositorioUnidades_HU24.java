package hu_024;
// [HU-024 MODIFICADO]

import java.util.List;
import java.util.Optional;

/** Contrato de persistencia: hoy memoria, mañana archivo JSON o BD sin tocar los servicios. */
public interface RepositorioUnidades_HU24 {
    boolean existePlaca(String placa);
    Optional<UnidadTransporte_HU24> buscarPorPlaca(String placa);
    void guardar(UnidadTransporte_HU24 unidad);      // crea
    void actualizar(UnidadTransporte_HU24 unidad);   // reemplaza la existente con esa placa
    List<UnidadTransporte_HU24> listar();
}
