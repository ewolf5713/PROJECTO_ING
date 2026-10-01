package hu_024;
import hu_022.EstadoOperativo_HU22;
import hu_022.ValidacionException_HU22;
// [HU-024 NUEVO]

/**
 * HU-024: edita modelo, capacidad y estado de una unidad existente (guarda de inmediato)
 * y expone la regla "solo unidades activas se asignan a itinerarios" para HU-010.
 */
public class ActualizacionUnidadService_HU24 {
    private final RepositorioUnidades_HU24 repositorio;

    public ActualizacionUnidadService_HU24(RepositorioUnidades_HU24 repositorio) {
        this.repositorio = repositorio;
    }

    /** Edición completa de los campos editables. La placa identifica la unidad y no cambia. */
    public UnidadTransporte_HU24 actualizar(String placa, String modelo, Object capacidad, String estado) {
        UnidadTransporte_HU24 actual = obtener(placa);
        UnidadTransporte_HU24 nueva = actual.conCambios(
                ValidadorUnidad_HU24.modelo(modelo),
                ValidadorUnidad_HU24.capacidad(capacidad),
                EstadoOperativo_HU22.desde(estado));
        repositorio.actualizar(nueva);
        return nueva;
    }

    /** Solo cambia el estado (mantenimiento / fuera de servicio / activa). */
    public UnidadTransporte_HU24 cambiarEstado(String placa, String estado) {
        UnidadTransporte_HU24 actual = obtener(placa);
        UnidadTransporte_HU24 nueva = actual.conCambios(
                actual.getModelo(), actual.getCapacidadPasajeros(), EstadoOperativo_HU22.desde(estado));
        repositorio.actualizar(nueva);
        return nueva;
    }

    /** Para HU-010: lanza ValidacionException_HU22 si la unidad no existe o no está activa. */
    public UnidadTransporte_HU24 validarAsignable(String placa) {
        UnidadTransporte_HU24 u = obtener(placa);
        if (!u.esAsignable()) {
            throw new ValidacionException_HU22("La unidad " + u.getPlaca() + " está "
                    + u.getEstado().getEtiqueta() + " y no puede asignarse a un itinerario.");
        }
        return u;
    }

    private UnidadTransporte_HU24 obtener(String placa) {
        String p = ValidadorUnidad_HU24.placa(placa);
        return repositorio.buscarPorPlaca(p)
                .orElseThrow(() -> new ValidacionException_HU22("No existe una unidad con placa '" + p + "'."));
    }
}
