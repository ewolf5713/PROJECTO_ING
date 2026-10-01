package hu_022;
// [HU-022]

public enum EstadoOperativo_HU22 {
    ACTIVA("activa"),
    EN_MANTENIMIENTO("en mantenimiento"),
    FUERA_DE_SERVICIO("fuera de servicio");

    private final String etiqueta;

    EstadoOperativo_HU22(String etiqueta) { this.etiqueta = etiqueta; }

    public String getEtiqueta() { return etiqueta; }

    /** Acepta "activa", "EN_MANTENIMIENTO", "en mantenimiento", etc. */
    public static EstadoOperativo_HU22 desde(String texto) {
        if (texto == null) {
            throw new ValidacionException_HU22("El estado operativo es obligatorio.");
        }
        String limpio = texto.trim();
        for (EstadoOperativo_HU22 e : values()) {
            if (e.name().equalsIgnoreCase(limpio.replace(' ', '_'))
                    || e.etiqueta.equalsIgnoreCase(limpio)) {
                return e;
            }
        }
        throw new ValidacionException_HU22("Estado operativo inválido: '" + texto
                + "'. Use: activa, en mantenimiento o fuera de servicio.");
    }
}
