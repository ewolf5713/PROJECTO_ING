/** Unidad editable (HU-024). La placa no cambia; modelo, capacidad y estado sí. */
public class Unidad_HU24 {
    private final String placa;
    private String modelo;
    private int capacidad;
    private EstadoOperativo_HU22 estado;

    public Unidad_HU24(String placa, String modelo, int capacidad, EstadoOperativo_HU22 estado) {
        if (placa == null || placa.isBlank())
            throw new IllegalArgumentException("La placa es obligatoria.");
        validar(modelo, capacidad, estado);
        this.placa = placa.trim().toUpperCase();
        this.modelo = modelo.trim();
        this.capacidad = capacidad;
        this.estado = estado;
    }

    /** Valida todo antes de modificar: si algo falla, la unidad queda intacta. */
    public void actualizar(String modelo, int capacidad, EstadoOperativo_HU22 estado) {
        validar(modelo, capacidad, estado);
        this.modelo = modelo.trim();
        this.capacidad = capacidad;
        this.estado = estado;
    }

    public void cambiarEstado(EstadoOperativo_HU22 nuevoEstado) {
        if (nuevoEstado == null)
            throw new IllegalArgumentException("El estado operativo es obligatorio.");
        this.estado = nuevoEstado;
    }

    public boolean estaActiva() { return estado == EstadoOperativo_HU22.ACTIVA; }

    private static void validar(String modelo, int capacidad, EstadoOperativo_HU22 estado) {
        if (modelo == null || modelo.isBlank())
            throw new IllegalArgumentException("El modelo es obligatorio.");
        if (capacidad <= 0)
            throw new IllegalArgumentException("La capacidad debe ser un entero mayor a cero.");
        if (estado == null)
            throw new IllegalArgumentException("El estado operativo es obligatorio.");
    }

    public String getPlaca() { return placa; }
    public String getModelo() { return modelo; }
    public int getCapacidad() { return capacidad; }
    public EstadoOperativo_HU22 getEstado() { return estado; }
}
