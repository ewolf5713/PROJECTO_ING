import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//acá "guardamos y alamcenamos" la data en memporía mientras la base de datos se realiza
public class Repositorio_HU1 implements RepoUsuario_HU1 {
    //estrucutra del mapa atar un nuevo usuario con un map
    private final Map<String, User_HU1> usuarios = new HashMap<>();

    //la clve es el correo, guardando y la ubicación es el correo
    public void save(User_HU1 usuario) {
        usuarios.put(usuario.getEmail(), usuario);
    }

    //usando el correo como clave busca y lo devuelve en un optinal
    //optional es en java buscar una variable con/sin valor
    public Optional<User_HU1> Correo_Encontrar(String gmail) {
        return Optional.ofNullable(usuarios.get(gmail));
    }
}
