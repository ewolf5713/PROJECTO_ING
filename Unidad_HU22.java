/** Tarea 2: entidad Unidad con sus validaciones propias (placa, modelo, capacidad, estado). */
public class Unidad_HU22 {
    private final String placa;
    private final String modelo;
    private final int capacidad;
    private final EstadoOperativo_HU22 estado;

    public Unidad_HU22(String placa, String modelo, int capacidad, EstadoOperativo_HU22 estado) {
        if (placa == null || placa.isBlank())
            throw new IllegalArgumentException("La placa es obligatoria.");
        if (modelo == null || modelo.isBlank())
            throw new IllegalArgumentException("El modelo es obligatorio.");
        if (capacidad <= 0)
            throw new IllegalArgumentException("La capacidad debe ser un entero mayor a cero.");
        if (estado == null)
            throw new IllegalArgumentException("El estado operativo es obligatorio.");

        this.placa = placa.trim().toUpperCase();
        this.modelo = modelo.trim();
        this.capacidad = capacidad;
        this.estado = estado;
    }

    public String getPlaca() { return placa; }
    public String getModelo() { return modelo; }
    public int getCapacidad() { return capacidad; }
    public EstadoOperativo_HU22 getEstado() { return estado; }
}
