package HU003_004.transporte.servicio;

import transporte.modelo.Itinerario;
import transporte.modelo.Reserva;

import java.util.ArrayList;
import java.util.List;

/** HU-004 - Tarea 2: cálculo de cupos disponibles de un itinerario. */
/*
Esta clase gestiona el almacenamiento de reservas y calcula los cupos de transporte
disponibles para un itinerario específico. Al contar únicamente las reservas en estado ACTIVA,
deduce la ocupación real respecto a la capacidad de la unidad sin permitir valores negativos.
Además, expone métodos para verificar disponibilidad inmediata (hayCupos)
y formatear el resultado para visualización tipo "disponibles/capacidad".
*/
public class CupoService_HU4 {

    private final List<Reserva_HU4> reservas = new ArrayList<>();

    public void agregarReserva(Reserva_HU4 reserva) {
        reservas.add(reserva);
    }

    /** Solo cuentan las reservas ACTIVAS; canceladas y expiradas liberan el cupo. */
    public int contarReservasActivas(String itinerarioId) {
        int total = 0;
        for (Reserva_HU4 r : reservas) {
            if (r.getItinerarioId().equals(itinerarioId) && r.estaActiva()) {
                total++;
            }
        }
        return total;
    }

    /** cupos disponibles = capacidad de la unidad - reservas activas (nunca menor a 0). */
    public int calcularCuposDisponibles(Itinerario_HU4 itinerario) {
        int capacidad = itinerario.unidad().capacidad();
        int ocupados = contarReservasActivas(itinerario.id());
        return Math.max(0, capacidad - ocupados);
    }

    public boolean hayCupos(Itinerario_HU4 itinerario) {
        return calcularCuposDisponibles(itinerario) > 0;
    }

    /** Formato de la tabla: "12/32" (disponibles/capacidad). */
    public String formatoCupos(Itinerario_HU4 itinerario) {
        return calcularCuposDisponibles(itinerario) + "/" + itinerario.unidad().capacidad();
    }
}