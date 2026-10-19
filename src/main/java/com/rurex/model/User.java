package com.rurex.model;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class User {
    private String nombreCompleto;
    private String email;
    private String cedula;
    private UserRole rol;
    private String carnet;
    private String passwordHash;
    private BigDecimal saldo = BigDecimal.ZERO.setScale(2);

    public User(String nombreCompleto, String email, String cedula, UserRole rol, String carnet, String passwordHash) {
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.cedula = cedula;
        this.rol = rol;
        this.carnet = carnet;
        this.passwordHash = passwordHash;
    }

    public User(String nombreCompleto, String email, String cedula, UserRole rol, String carnet, String passwordHash, BigDecimal saldoInicial) {
        this(nombreCompleto, email, cedula, rol, carnet, passwordHash);
        if (saldoInicial == null || saldoInicial.signum() < 0) {
            throw new IllegalArgumentException("Saldo no puede ser negativo");
        }
        this.saldo = saldoInicial.setScale(2, RoundingMode.HALF_UP);
    }

    public String getNombreCompleto() { return nombreCompleto; }
    public String getEmail() { return email; }
    public String getCedula() { return cedula; }
    public UserRole getRol() { return rol; }
    public String getCarnet() { return carnet; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public BigDecimal getSaldo() { return saldo; }

    public void acreditar(BigDecimal monto) {
        saldo = saldo.add(normalizar(monto));
    }

    public void debitar(BigDecimal monto) {
        BigDecimal m = normalizar(monto);
        if (m.compareTo(saldo) > 0) {
            throw new IllegalStateException("[SALDO INSUFICIENTE]");
        }
        saldo = saldo.subtract(m);
    }

    public boolean tieneMonedero() { return rol == UserRole.ESTUDIANTE || rol == UserRole.EMPLEADO; }

    private BigDecimal normalizar(BigDecimal monto) {
        if (monto == null || monto.signum() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que 0");
        }
        return monto.setScale(2, RoundingMode.HALF_UP);
    }
}