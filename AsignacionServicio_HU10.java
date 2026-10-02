/** Tarea 4: asignar unidad activa y conductor a un itinerario. */
public class AsignacionServicio_HU10 {

    public void asignar(Itinerario_HU10 itinerario, UnidadTransporte_HU10 unidad,
                        Conductor_HU10 conductor) {
        if (itinerario == null) throw new IllegalArgumentException("Itinerario requerido.");
        if (unidad == null) throw new IllegalArgumentException("Unidad requerida.");
        if (conductor == null) throw new IllegalArgumentException("Conductor requerido.");
        if (!unidad.isActiva())
            throw new IllegalStateException("La unidad " + unidad.getPlaca() + " no está activa.");
        if (itinerario.getCupos() > unidad.getCapacidad())
            throw new IllegalStateException("Los cupos del itinerario (" + itinerario.getCupos()
                    + ") superan la capacidad de la unidad (" + unidad.getCapacidad() + ").");

        itinerario.setUnidad(unidad);
        itinerario.setConductor(conductor);
    }
}
