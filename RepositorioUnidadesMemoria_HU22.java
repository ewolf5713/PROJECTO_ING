package hu_022;
// [HU-022]

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RepositorioUnidadesMemoria_HU22 implements RepositorioUnidades_HU22 {
    private final Map<String, UnidadTransporte_HU22> unidades = new LinkedHashMap<>();

    @Override public boolean existePlaca(String placa) { return unidades.containsKey(placa); }
    @Override public void guardar(UnidadTransporte_HU22 u) { unidades.put(u.getPlaca(), u); }
    @Override public List<UnidadTransporte_HU22> listar() { return new ArrayList<>(unidades.values()); }
}
