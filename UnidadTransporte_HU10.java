public class UnidadTransporte_HU10 {
    private final String placa;
    private final int capacidad;
    private boolean activa;

    public UnidadTransporte_HU10(String placa, int capacidad, boolean activa) {
        this.placa = placa;
        this.capacidad = capacidad;
        this.activa = activa;
    }

    public String getPlaca() { return placa; }
    public int getCapacidad() { return capacidad; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
}
