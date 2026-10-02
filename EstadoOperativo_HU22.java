public enum EstadoOperativo_HU22 {
    ACTIVA("Activo"),
    EN_MANTENIMIENTO("En Mantenimiento"),
    FUERA_DE_SERVICIO("Fuera de Servicio");

    private final String etiqueta;

    EstadoOperativo_HU22(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() { return etiqueta; }

    @Override
    public String toString() { return etiqueta; }
}
