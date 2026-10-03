package com.rurex.model;

public class User {
    private String nombreCompleto;
    private String email;
    private String cedula;
    private UserRole rol;
    private String carnet;
    private String passwordHash;

    public User(String nombreCompleto, String email, String cedula, UserRole rol, String carnet, String passwordHash) {
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.cedula = cedula;
        this.rol = rol;
        this.carnet = carnet;
        this.passwordHash = passwordHash;
    }

    public String getNombreCompleto() { return nombreCompleto; }
    public String getEmail() { return email; }
    public String getCedula() { return cedula; }
    public UserRole getRol() { return rol; }
    public String getCarnet() { return carnet; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}
