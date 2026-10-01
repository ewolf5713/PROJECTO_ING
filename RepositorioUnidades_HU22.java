package hu_022;
// [HU-022]

import java.util.List;

/** Contrato de persistencia: hoy memoria, mañana archivo JSON o BD sin tocar el servicio. */
public interface RepositorioUnidades_HU22 {
    boolean existePlaca(String placa);
    void guardar(UnidadTransporte_HU22 unidad);
    List<UnidadTransporte_HU22> listar();
}
