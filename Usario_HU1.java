public class User_HU1 {
    private final String Nombre_us;
    private final String Gmail_us;
    private String Ciphercontr; // NOT final, it's assigned after hashing

    public User_HU1(String Nombre_us, String Gmail_us) {
        this.Nombre_us = Nombre_us;
        this.Gmail_us = Gmail_us;
    }

    public String getNombre() { return Nombre_us; }
    public String getEmail() { return Gmail_us; }
    public String getHashContra() { return Ciphercontr; }
    public void setHashContra(String hash) { this.Ciphercontr = hash; }
}
