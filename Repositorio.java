import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Repositorio implements RepoUsuario {
    private final Map<String, User> usuarios = new HashMap<>();

    public void save(User usuario) {
        usuarios.put(usuario.getEmail(), usuario);
    }

    public Optional<User> Correo_Encontrar(String gmail) {
        return Optional.ofNullable(usuarios.get(gmail));
    }
}