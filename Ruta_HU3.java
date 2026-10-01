package HU003_004.transporte.modelo;
 
public record Ruta_HU3(String id, String nombre, TipoRuta_HU3 tipo,
                   String origen, String destino, boolean activa) {
}

/*
Creando un tipo de dato con una varaibles que nunca va a cambiar
usando getters de cada campo y un formato aceptable
*/