package com.rurex.service;

import com.rurex.model.UserRole;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class TarifaServiceTest {

    private final TarifaService tarifas = new TarifaService();

    @Test
    void preciosPorDefecto() {
        assertEquals(new BigDecimal("1.50"), tarifas.precio(UserRole.ESTUDIANTE));
        assertEquals(new BigDecimal("4.50"), tarifas.precio(UserRole.EMPLEADO));
        assertEquals(new BigDecimal("10.00"), tarifas.precio(UserRole.PUBLICO_GENERAL));
    }

    @Test
    void configurarCambiaLosPrecios() {
        tarifas.configurar(new BigDecimal("20.00"), 10, 60);
        assertEquals(new BigDecimal("2.00"), tarifas.precio(UserRole.ESTUDIANTE));
        assertEquals(new BigDecimal("12.00"), tarifas.precio(UserRole.EMPLEADO));
        assertEquals(new BigDecimal("20.00"), tarifas.precio(UserRole.PUBLICO_GENERAL));
    }

    @Test
    void redondeaHalfUp() {
        tarifas.configurar(new BigDecimal("10.05"), 15, 30);
        assertEquals(new BigDecimal("1.51"), tarifas.precio(UserRole.ESTUDIANTE));
    }

    @Test
    void valoresInvalidosNoCambianNada() {
        assertThrows(IllegalArgumentException.class, () -> tarifas.configurar(BigDecimal.ZERO, 15, 45));
        assertThrows(IllegalArgumentException.class, () -> tarifas.configurar(null, 15, 45));
        assertThrows(IllegalArgumentException.class, () -> tarifas.configurar(new BigDecimal("20"), 9, 45));
        assertThrows(IllegalArgumentException.class, () -> tarifas.configurar(new BigDecimal("20"), 21, 45));
        assertThrows(IllegalArgumentException.class, () -> tarifas.configurar(new BigDecimal("20"), 15, 29));
        assertThrows(IllegalArgumentException.class, () -> tarifas.configurar(new BigDecimal("20"), 15, 61));
        assertEquals(new BigDecimal("10.00"), tarifas.getTarifaBase());
        assertEquals(15, tarifas.getPorcentajeEstudiante());
        assertEquals(45, tarifas.getPorcentajeEmpleado());
    }

    @Test
    void rolSinMonederoNoTienePrecio() {
        assertThrows(IllegalArgumentException.class, () -> tarifas.precio(UserRole.ADMINISTRADOR));
        assertThrows(IllegalArgumentException.class, () -> tarifas.precio(UserRole.CONDUCTOR));
    }
}
