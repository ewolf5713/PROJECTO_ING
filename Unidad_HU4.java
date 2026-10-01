package HU003_004.transporte.modelo;
/*
Este record define un modelo de datos inmutable para representar una unidad de transporte
 con su identificador, placa, capacidad y estado de actividad. 
 Incluye un constructor compacto que valida que la capacidad asignada sea cero o positiva, 
 lanzando una excepción IllegalArgumentException si se intenta instanciar con un valor negativo.
*/
public record Unidad_HU4(String id, String placa, int capacidad, boolean activa) {

    public Unidad_HU4 {
        if (capacidad < 0) {
            throw new IllegalArgumentException("La capacidad no puede ser negativa");
        }
    }
}