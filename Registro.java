import java.util.List;

public class Registro {
    private final RepoUsuario repo;

    public Registro(RepoUsuario repo) { this.repo = repo; }

    public List<String> registro(String nombre, String gmail, String contrasena) throws Exception {
        List<String> error = ValidarUS.validar(nombre, gmail, contrasena);
        if (!error.isEmpty()) return error;

        String normalizar = gmail.trim().toLowerCase();
        if (repo.Correo_Encontrar(normalizar).isPresent()) return List.of("[GMAIL YA REGISTRADO]");

        User usuario = new User(nombre.trim(), normalizar);
        usuario.setHashContra(HasherContrasena.hash(contrasena));
        repo.save(usuario);
        return List.of();
    }
}