import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tarea 4: pruebas unitarias de actualización y cambio de estado operativo (JUnit 5). */
class UnidadServicioTest_HU24 {
    private UnidadServicio_HU24 servicio;
    private ValidadorAsignacion_HU24 validador;

    @BeforeEach
    void preparar() {
        servicio = new UnidadServicio_HU24();
        validador = new ValidadorAsignacion_HU24();
        servicio.registrar("UCV-234", "Mercedes-Benz OF-1721", 32, EstadoOperativo_HU22.ACTIVA);
    }

    @Test
    void actualizaDatosDeLaUnidad() {
        servicio.actualizar("UCV-234", "Iveco Daily", 40, EstadoOperativo_HU22.EN_MANTENIMIENTO);

        Unidad_HU24 u = servicio.buscarPorPlaca("UCV-234");
        assertEquals("Iveco Daily", u.getModelo());
        assertEquals(40, u.getCapacidad());
        assertEquals(EstadoOperativo_HU22.EN_MANTENIMIENTO, u.getEstado());
        // el cambio se refleja de inmediato en la consulta de flota
        assertEquals("Iveco Daily", servicio.listar().get(0).getModelo());
    }

    @Test
    void actualizacionInvalidaNoModificaLaUnidad() {
        assertThrows(IllegalArgumentException.class, () ->
                servicio.actualizar("UCV-234", "Iveco Daily", 0, EstadoOperativo_HU22.ACTIVA));

        Unidad_HU24 u = servicio.buscarPorPlaca("UCV-234");
        assertEquals("Mercedes-Benz OF-1721", u.getModelo());
        assertEquals(32, u.getCapacidad());
    }

    @Test
    void rechazaActualizarUnidadInexistente() {
        assertThrows(IllegalArgumentException.class, () ->
                servicio.actualizar("XXX-000", "Ford Transit", 25, EstadoOperativo_HU22.ACTIVA));
    }

    @Test
    void permiteTodasLasTransicionesEntreEstados() {
        for (EstadoOperativo_HU22 origen : EstadoOperativo_HU22.values()) {
            for (EstadoOperativo_HU22 destino : EstadoOperativo_HU22.values()) {
                servicio.cambiarEstado("UCV-234", origen);
                servicio.cambiarEstado("UCV-234", destino);
                assertEquals(destino, servicio.buscarPorPlaca("UCV-234").getEstado());
            }
        }
    }

    @Test
    void rechazaEstadoNulo() {
        assertThrows(IllegalArgumentException.class, () -> servicio.cambiarEstado("UCV-234", null));
        assertEquals(EstadoOperativo_HU22.ACTIVA, servicio.buscarPorPlaca("UCV-234").getEstado());
    }

    @Test
    void permiteAsignarUnidadActiva() {
        assertDoesNotThrow(() -> validador.validarAsignable(servicio.buscarPorPlaca("UCV-234")));
    }

    @Test
    void rechazaAsignarUnidadEnMantenimiento() {
        servicio.cambiarEstado("UCV-234", EstadoOperativo_HU22.EN_MANTENIMIENTO);
        assertThrows(IllegalStateException.class, () ->
                validador.validarAsignable(servicio.buscarPorPlaca("UCV-234")));
    }

    @Test
    void rechazaAsignarUnidadFueraDeServicio() {
        servicio.cambiarEstado("UCV-234", EstadoOperativo_HU22.FUERA_DE_SERVICIO);
        assertThrows(IllegalStateException.class, () ->
                validador.validarAsignable(servicio.buscarPorPlaca("UCV-234")));
    }
}
