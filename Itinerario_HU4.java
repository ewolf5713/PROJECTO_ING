package HU003_004.transporte.modelo;
/*
Este record define un modelo de datos inmutable para estructurar un itinerario de transporte,
agrupando un identificador único, su ruta asociada,
la unidad asignada y el horario de salida.
Java genera automáticamente sus atributos, métodos getter, equals(), hashCode() y toString(),
sirviendo como un contenedor liviano y de solo lectura.
*/
import java.time.LocalTime;
 
public record Itinerario_HU4(String id, Ruta_HU3 ruta, Unidad_HU4 unidad, LocalTime horario) {
}