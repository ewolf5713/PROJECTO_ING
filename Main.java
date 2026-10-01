import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        RepoUsuario repo = new Repositorio();
        Registro reg = new Registro(repo);

        System.out.println(reg.registro("H", "prueba123@gmail.com", "123"));
        //registro exitoso

        System.out.println(reg.registro("H", "prueba123@gmail.com", "123"));
        //esperado gmail ya registrado

        System.out.println(reg.registro("", "disnuts@yahoo.com", ""));
        //esperado agregar nombre, gmail no vlaido o agregar contra

        User u = repo.Correo_Encontrar("prueba123@gmail.com").get();
        System.out.println(u.getHashContra());
        //esperado salth:hash, no Clave123

        System.out.println(HasherContrasena.iguales("69696969", u.getHashContra()));
        System.out.println(HasherContrasena.iguales("incorrecta", u.getHashContra()));
    }
}
