import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//Encapsulamos el itinerario completo de transporte
public class Itinerario_HU8 {
    public static class Parada {
        private final String nombre;
        private final String hora;

        //muestra la parada y el ETA
        public Parada(String nombre, String hora) {
            this.nombre = nombre;
            this.hora = hora;
        }
        //inicializa la parada y ETA
        public String getNombre() { return nombre; } //Devuelve el nombre de la parada
        public String getHora() { return hora; } //Duvuelve la hora programada para la parada
    }

    private final String id;
    private final String ruta;
    private final String unidad;
    private final String conductor;
    private final String horario;
    private final List<Parada> paradas;

    //Inicializa el itinerario con una parada 
    public Itinerario_HU8(String id, String ruta, String unidad, String conductor,String horario, List<Parada> paradas) {
        if (paradas == null || paradas.isEmpty()) { throw new IllegalArgumentException("El itinerario debe tener al menos una parada."); }
        
        this.id = id;
        this.ruta = ruta;
        this.unidad = unidad;
        this.conductor = conductor;
        this.horario = horario;
        this.paradas = Collections.unmodifiableList(new ArrayList<>(paradas));
    }

    public String getId() { return id; } //devuelve ID del itinerario
    public String getRuta() { return ruta; } //devuelve nombre de la ruta
    public String getUnidad() { return unidad; } //devuelve identificación del vehiculo
    public String getConductor() { return conductor; } //devuelve identificación del conductor
    public String getHorario() { return horario; } // ETA del itinerario
    public List<Parada> getParadas() { return paradas; } // Lista de las paradas 
}