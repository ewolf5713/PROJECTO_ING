import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
//Librerias requeridas para tarea 3

//todo en java se mueve con funciones y el tipos de datos primera letra en mayuscula
//TASK 2 ER
public class T1 {
    private final String Nombre_us;
    private final String Gmail_us;
    private final String Ciphercontr;

    public T1 (String Nombre_us, String Gmail_us) {
        this.Nombre_us = Nombre_us;
        this.Gmail_us = Gmail_us;
    }
}

public class ValidarUS {
    //temporal mientras la página este lista
    //esta parte es guarda el string aceptando Aa - Zz 0-9 simbolos y que tenga @gmail.com al final
    private static final Pattern GMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@gmail\\.com$");

    private static List<String> validar_info (String Nombre, String Gmail, String Contrasena) {
        List<String> error = new ArrayList<>();
        //teniendo una lista de string, chequeamos si no hay nada en la casilla de nombre, si es así ponemos error
        if (Nombre == null || Nombre.isBlank()) error.add("[NOMBRE AGREGAR]");

        if (Gmail == null || Gmail.isBlank()) error.add("[GMAIL AGREGAR]");
        else if (!GMAIL.matcher(Gmail.trim()).matches()) error.add("[GMAIL NO VALIDO]");

        if (Contrasena == null || Contrasena.isBlank()) error.add("[CONTRASEÑA AGREGAR]");

        return error;
    }
}