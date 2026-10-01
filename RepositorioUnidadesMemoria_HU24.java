package hu_024;
// [HU-024 MODIFICADO]

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RepositorioUnidadesMemoria_HU24 implements RepositorioUnidades_HU24 {
    private final Map<String, UnidadTransporte_HU24> unidades = new LinkedHashMap<>();

    @Override public boolean existePlaca(String placa) { return unidades.containsKey(placa); }
    @Override public Optional<UnidadTransporte_HU24> buscarPorPlaca(String placa) {
        return Optional.ofNullable(unidades.get(placa));
    }
    @Override public void guardar(UnidadTransporte_HU24 u) { unidades.put(u.getPlaca(), u); }
    @Override public void actualizar(UnidadTransporte_HU24 u) { unidades.put(u.getPlaca(), u); }
    @Override public List<UnidadTransporte_HU24> listar() { return new ArrayList<>(unidades.values()); }
}
