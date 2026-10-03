/** Impide asignar a un itinerario una unidad que no esté activa. */
public class ValidadorAsignacion_HU24 {

    public void validarAsignable(Unidad_HU24 unidad) {
        if (unidad == null)
            throw new IllegalArgumentException("La unidad es obligatoria.");
        if (!unidad.estaActiva())
            throw new IllegalStateException("La unidad " + unidad.getPlaca() + " está "
                    + unidad.getEstado().getEtiqueta().toLowerCase()
                    + " y no puede asignarse a un itinerario.");
    }
}
