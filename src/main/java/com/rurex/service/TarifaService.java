package com.rurex.service;

import com.rurex.database.TarifaDatabase;
import com.rurex.model.UserRole;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TarifaService {

    private BigDecimal tarifaBase = new BigDecimal("10.00");
    private int porcentajeEstudiante = 15;
    private int porcentajeEmpleado = 45;
    private TarifaDatabase database;

    public void setDatabase(TarifaDatabase database) {
        this.database = database;
        if (database == null) return;
        if (database.existe()) {
            database.cargar(this);
        } else {
            database.guardar(this);
        }
    }

    public synchronized void configurar(BigDecimal base, int pctEstudiante, int pctEmpleado) {
        if (base == null || base.signum() <= 0) {
            throw new IllegalArgumentException("La tarifa base debe ser mayor que 0.");
        }
        if (pctEstudiante < 10 || pctEstudiante > 20) {
            throw new IllegalArgumentException("El porcentaje de Estudiantes debe estar entre 10% y 20%.");
        }
        if (pctEmpleado < 30 || pctEmpleado > 60) {
            throw new IllegalArgumentException("El porcentaje de Empleados y Profesores debe estar entre 30% y 60%.");
        }
        tarifaBase = base.setScale(2, RoundingMode.HALF_UP);
        porcentajeEstudiante = pctEstudiante;
        porcentajeEmpleado = pctEmpleado;
        if (database != null) database.guardar(this);
    }

    public synchronized BigDecimal precio(UserRole rol) {
        int pct;
        if (rol == UserRole.ESTUDIANTE) pct = porcentajeEstudiante;
        else if (rol == UserRole.EMPLEADO) pct = porcentajeEmpleado;
        else if (rol == UserRole.PUBLICO_GENERAL) pct = 100;
        else throw new IllegalArgumentException("Este tipo de usuario no paga tarifa.");
        return tarifaBase.multiply(BigDecimal.valueOf(pct)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    public synchronized BigDecimal getTarifaBase() { return tarifaBase; }
    public synchronized int getPorcentajeEstudiante() { return porcentajeEstudiante; }
    public synchronized int getPorcentajeEmpleado() { return porcentajeEmpleado; }
}
