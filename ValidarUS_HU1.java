import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ValidarUS_HU1 {
    private static final Pattern GMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@gmail\\.com$");

    //atada al validar usuario para ver si en el registro no falte/ duplicado el
    //nombre, gmail o contraseña
    public static List<String> validar(String Nombre, String Gmail, String Contrasena) {
        List<String> error = new ArrayList<>();
        if (Nombre == null || Nombre.isBlank()) error.add("[NOMBRE AGREGAR]");

        if (Gmail == null || Gmail.isBlank()) error.add("[GMAIL AGREGAR]");
        else if (!GMAIL.matcher(Gmail.trim()).matches()) error.add("[GMAIL NO VALIDO]");

        if (Contrasena == null || Contrasena.isBlank()) error.add("[CONTRASEÑA AGREGAR]");

        return error;
    }
}
