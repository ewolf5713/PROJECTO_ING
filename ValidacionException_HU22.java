package hu_022;
// [HU-022]

/** Error de validación de negocio (mostrable directamente en la UI Swing). */
public class ValidacionException_HU22 extends RuntimeException {
    public ValidacionException_HU22(String mensaje) { super(mensaje); }
}
