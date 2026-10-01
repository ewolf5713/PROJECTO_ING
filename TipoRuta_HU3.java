package HU003_004.transporte.modelo;
 
/*
Define dos posibles datos para el tipo de ruta
usando un constructor vinculado a dos constantes strings
*/
public enum TipoRuta_HU3 {
    URBANA("Urbana"),
    EXTRAURBANA("Extraurbana");
 
    private final String etiqueta;
 
    TipoRuta_HU3(String etiqueta) {
        this.etiqueta = etiqueta;
    }
 
    @Override
    public String toString() {
        return etiqueta;
    }
}