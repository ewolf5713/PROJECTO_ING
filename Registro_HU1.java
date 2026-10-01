import java.util.List;

public class Registro_HU1 {
    private final RepoUsuario_HU1 repo;

    public Registro_HU1(RepoUsuario_HU1 repo) { this.repo = repo; }

    public List<String> registro(String nombre, String gmail, String contrasena) throws Exception {
        List<String> error = ValidarUS_HU1.validar(nombre, gmail, contrasena);
        if (!error.isEmpty()) return error;

        String normalizar = gmail.trim().toLowerCase();
        if (repo.Correo_Encontrar(normalizar).isPresent()) return List.of("[GMAIL YA REGISTRADO]");

        User_HU1 usuario = new User_HU1(nombre.trim(), normalizar);
        usuario.setHashContra(HasherContrasena_HU1.hash(contrasena));
        repo.save(usuario);
        return List.of();
    }
}