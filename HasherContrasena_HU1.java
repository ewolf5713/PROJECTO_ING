import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class HasherContrasena_HU1 {
    public static String hash(String contrasena) throws Exception {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        byte[] hash = pbkdf2(contrasena, salt);
        return Base64.getEncoder().encodeToString(salt) + ":" +
               Base64.getEncoder().encodeToString(hash);
    }

    public static boolean iguales(String contrasena, String almacenado) throws Exception {
        String[] partes = almacenado.split(":");
        byte[] salt = Base64.getDecoder().decode(partes[0]);
        byte[] esperado = Base64.getDecoder().decode(partes[1]);
        return java.security.MessageDigest.isEqual(esperado, pbkdf2(contrasena, salt));
    }

    private static byte[] pbkdf2(String contrasena, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(contrasena.toCharArray(), salt, 120_000, 256);
        return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                               .generateSecret(spec).getEncoded();
    }
}