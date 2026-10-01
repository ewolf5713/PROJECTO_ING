import java.util.Optional;

public interface RepoUsuario {
    void save(User usuario);
    Optional<User> Correo_Encontrar(String gmail);
}