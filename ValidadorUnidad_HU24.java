package hu_024;
import hu_022.ValidacionException_HU22;
// [HU-024 NUEVO]

/** Reglas de validación reutilizables (registro y edición usan las mismas). */
public final class ValidadorUnidad_HU24 {
    private ValidadorUnidad_HU24() {}

    public static String placa(String placa) {
        if (placa == null || placa.isBlank()) {
            throw new ValidacionException_HU22("La placa es obligatoria.");
        }
        return placa.trim().toUpperCase();
    }

    public static String modelo(String modelo) {
        if (modelo == null || modelo.isBlank()) {
            throw new ValidacionException_HU22("El modelo es obligatorio.");
        }
        return modelo.trim();
    }

    /** Acepta Integer, Double (Gson) o String (campo de texto Swing). */
    public static int capacidad(Object valor) {
        if (valor == null) throw new ValidacionException_HU22("La capacidad de pasajeros es obligatoria.");
        int cap;
        if (valor instanceof Number n) {
            if (n.doubleValue() != Math.floor(n.doubleValue())) {
                throw new ValidacionException_HU22("La capacidad debe ser un número entero.");
            }
            cap = n.intValue();
        } else {
            try {
                cap = Integer.parseInt(valor.toString().trim());
            } catch (NumberFormatException e) {
                throw new ValidacionException_HU22("La capacidad debe ser un número entero.");
            }
        }
        if (cap <= 0) throw new ValidacionException_HU22("La capacidad debe ser mayor a cero.");
        return cap;
    }
}
