package com.rurex.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Itinerary {
    private String id;
    private String rutaNombre;
    private LocalDate fecha;
    private LocalTime horaSalida;
    private LocalTime horaLlegada;
    private TransportUnit unidad;
    private String conductor;
    private int cuposDisponibles;

    public Itinerary(String id, String rutaNombre, LocalDate fecha, LocalTime horaSalida, LocalTime horaLlegada, TransportUnit unidad, String conductor, int cuposDisponibles) {
        this.id = id;
        this.rutaNombre = rutaNombre;
        this.fecha = fecha;
        this.horaSalida = horaSalida;
        this.horaLlegada = horaLlegada;
        this.unidad = unidad;
        this.conductor = conductor;
        this.cuposDisponibles = cuposDisponibles;
    }

    public String getId() { return id; }
    public String getRutaNombre() { return rutaNombre; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHoraSalida() { return horaSalida; }
    public LocalTime getHoraLlegada() { return horaLlegada; }
    public TransportUnit getUnidad() { return unidad; }
    public String getConductor() { return conductor; }
    public int getCuposDisponibles() { return cuposDisponibles; }
}
