import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Repositorio_HU1 implements RepoUsuario_HU1 {
    private final Map<String, User_HU1> usuarios = new HashMap<>();

    public void save(User_HU1 usuario) {
        usuarios.put(usuario.getEmail(), usuario);
    }

    public Optional<User_HU1> Correo_Encontrar(String gmail) {
        return Optional.ofNullable(usuarios.get(gmail));
    }
}