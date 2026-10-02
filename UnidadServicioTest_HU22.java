import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tarea 3: pruebas unitarias del registro de unidad (JUnit 5). */
class UnidadServicioTest_HU22 {
    private UnidadServicio_HU22 servicio;

    @BeforeEach
    void preparar() {
        servicio = new UnidadServicio_HU22();
    }

    @Test
    void registraUnidadValida() {
        Unidad_HU22 u = servicio.registrar("UCV-234", "Mercedes-Benz OF-1721", 32,
                EstadoOperativo_HU22.ACTIVA);

        assertEquals("UCV-234", u.getPlaca());
        assertEquals(32, u.getCapacidad());
        assertEquals(EstadoOperativo_HU22.ACTIVA, u.getEstado());
        assertEquals(1, servicio.listar().size());
        assertTrue(servicio.existePlaca("UCV-234"));
    }

    @Test
    void rechazaPlacaDuplicada() {
        servicio.registrar("UCV-234", "Mercedes-Benz OF-1721", 32, EstadoOperativo_HU22.ACTIVA);

        assertThrows(IllegalArgumentException.class, () ->
                servicio.registrar("ucv-234", "Iveco Daily", 40, EstadoOperativo_HU22.ACTIVA));
        assertEquals(1, servicio.listar().size());
    }

    @Test
    void rechazaCapacidadCero() {
        assertThrows(IllegalArgumentException.class, () ->
                servicio.registrar("UCV-156", "Volkswagen 17.230", 0, EstadoOperativo_HU22.ACTIVA));
        assertTrue(servicio.listar().isEmpty());
    }

    @Test
    void rechazaCapacidadNegativa() {
        assertThrows(IllegalArgumentException.class, () ->
                servicio.registrar("UCV-156", "Volkswagen 17.230", -5, EstadoOperativo_HU22.ACTIVA));
        assertTrue(servicio.listar().isEmpty());
    }
}
