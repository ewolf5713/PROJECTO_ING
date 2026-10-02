public class User_HU1 {
    //los tipos de datos predefinidos para cada objeto
    private final String Nombre_us;
    private final String Gmail_us;
    private String Ciphercontr;

    /*
    Aca estamos guardando el nombre el correo y el nombre, para la contraseña estamos llamando para 
    cifrar la contraseña con el hasher
    */

    //Creamos el objeto con el nombre y correo
    public User_HU1(String Nombre_us, String Gmail_us) {
        this.Nombre_us = Nombre_us;
        this.Gmail_us = Gmail_us;
    }

    //devolvemos nombre, correo, contrasena cifrada y atarlo a un usuario
    public String getNombre() { return Nombre_us; }
    public String getEmail() { return Gmail_us; }
    public String getHashContra() { return Ciphercontr; }
    public void setHashContra(String hash) { this.Ciphercontr = hash; }
}
