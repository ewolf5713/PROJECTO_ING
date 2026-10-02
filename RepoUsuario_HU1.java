import java.util.Optional;

//acá es la lógica donde guardamos usuarios nuevos y almacena existentes
public interface RepoUsuario_HU1 {
    //guardamos usuarios nuevos y almacena existentes
    void save(User_HU1 usuario);

    //buscamos usuarios por correo 
    Optional<User_HU1> Correo_Encontrar(String gmail);
}
