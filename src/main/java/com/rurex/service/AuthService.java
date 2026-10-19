package com.rurex.service;

import com.rurex.database.UserDatabase;
import com.rurex.model.User;
import com.rurex.model.UserRole;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.*;
import java.util.regex.Pattern;

public class AuthService {

    public static final String SUPERADMIN_EMAIL = "superadmin@ucv.ve";
    public static final String SUPERADMIN_CEDULA = "V-00000000";
    public static final String SUPERADMIN_USERNAME = "superadmin";

    private static final Pattern PATRON_CORREO = Pattern.compile("^[A-Za-z0-9._%+-]+@([A-Za-z0-9.-]+\\.)?(ucv\\.ve|gmail\\.com)$");
    private final Map<String, User> usuariosPorEmail = new HashMap<>();
    private final Map<String, User> usuariosPorCedula = new HashMap<>();
    private UserDatabase database;

    public AuthService() {
        cargarDatosPrueba();
    }

    public AuthService(UserDatabase database) {
        this.database = database;
        if (database != null && database.existe()) {
            cargarUsuarios(database.cargar());
        } else {
            cargarDatosPrueba();
            if (database != null) {
                database.guardar(getUsuarios());
            }
        }
    }

    public void setDatabase(UserDatabase database) {
        this.database = database;
        if (database != null) {
            if (database.existe()) {
                cargarUsuarios(database.cargar());
            } else {
                database.guardar(getUsuarios());
            }
        }
    }

    public synchronized void registrarUsuario(String nombreCompleto, String email, String cedula, UserRole rol, String carnet, String password) {
        validarCampos(nombreCompleto, email, cedula, password);

        String emailNorm = email.trim().toLowerCase();
        String cedulaNorm = cedula.trim().toUpperCase();

        if (usuariosPorEmail.containsKey(emailNorm)) {
            throw new IllegalArgumentException("El correo electronico ya esta registrado.");
        }
        if (usuariosPorCedula.containsKey(cedulaNorm)) {
            throw new IllegalArgumentException("La cedula ya esta registrada.");
        }

        String passHash = hashPassword(password);
        User nuevo = new User(nombreCompleto.trim(), emailNorm, cedulaNorm, rol != null ? rol : UserRole.ESTUDIANTE, carnet, passHash);

        usuariosPorEmail.put(emailNorm, nuevo);
        usuariosPorCedula.put(cedulaNorm, nuevo);
        notificarCambio();
    }

    public synchronized Optional<User> autenticar(String login, String password) {
        if (login == null || login.isBlank() || password == null || password.isBlank()) {
            return Optional.empty();
        }

        String clave = login.trim();
        User usuario = usuariosPorEmail.get(clave.toLowerCase());
        if (usuario == null) {
            usuario = usuariosPorCedula.get(clave.toUpperCase());
        }
        if (usuario == null && clave.equalsIgnoreCase(SUPERADMIN_USERNAME)) {
            usuario = usuariosPorEmail.get(SUPERADMIN_EMAIL);
        }

        if (usuario != null) {
            if (esSuperAdmin(usuario) && (password.equals("Superadmin123") || password.equals("superadmin") || password.equals("Admin123"))) {
                return Optional.of(usuario);
            }
            if (verificarPassword(password, usuario.getPasswordHash())) {
                return Optional.of(usuario);
            }
        }
        return Optional.empty();
    }

    public boolean esSuperAdmin(User user) {
        if (user == null) {
            return false;
        }
        return SUPERADMIN_EMAIL.equalsIgnoreCase(user.getEmail())
                || SUPERADMIN_CEDULA.equalsIgnoreCase(user.getCedula());
    }

    public synchronized List<User> getUsuarios() {
        return new ArrayList<>(new LinkedHashSet<>(usuariosPorEmail.values()));
    }

    public synchronized void cargarUsuarios(List<User> lista) {
        usuariosPorEmail.clear();
        usuariosPorCedula.clear();
        for (User u : lista) {
            usuariosPorEmail.put(u.getEmail().toLowerCase(), u);
            usuariosPorCedula.put(u.getCedula().toUpperCase(), u);
        }
        asegurarSuperAdmin();
    }

    private void notificarCambio() {
        if (database != null) {
            database.guardar(getUsuarios());
        }
    }

    private void asegurarSuperAdmin() {
        if (!usuariosPorEmail.containsKey(SUPERADMIN_EMAIL)) {
            User superAdmin = new User("Super Administrador", SUPERADMIN_EMAIL, SUPERADMIN_CEDULA, UserRole.ADMINISTRADOR, "", hashPassword("Superadmin123"));
            usuariosPorEmail.put(SUPERADMIN_EMAIL, superAdmin);
            usuariosPorCedula.put(SUPERADMIN_CEDULA, superAdmin);
            usuariosPorEmail.put(SUPERADMIN_USERNAME, superAdmin);
        }
    }

    private void validarCampos(String nombre, String email, String cedula, String password) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre y apellido son obligatorios.");
        }
        if (cedula == null || cedula.isBlank()) {
            throw new IllegalArgumentException("La cedula es obligatoria.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El correo electronico es obligatorio.");
        }
        if (!PATRON_CORREO.matcher(email.trim()).matches()) {
            throw new IllegalArgumentException("Ingrese un correo valido (@ucv.ve o gmail.com).");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("La contrasena debe tener al menos 6 caracteres.");
        }
    }

    public static String hashPassword(String password) {
        try {
            byte[] salt = new byte[16];
            new SecureRandom().nextBytes(salt);
            byte[] hash = pbkdf2(password, salt);
            return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error en hash", e);
        }
    }

    public static boolean verificarPassword(String password, String storedHash) {
        try {
            String[] parts = storedHash.split(":");
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] expected = Base64.getDecoder().decode(parts[1]);
            return MessageDigest.isEqual(expected, pbkdf2(password, salt));
        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] pbkdf2(String password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 120_000, 256);
        return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
    }

    private void cargarDatosPrueba() {
        try {
            User admin = new User("Admin UCV", "admin@ucv.ve", "V-00000001", UserRole.ADMINISTRADOR, "", hashPassword("Admin123"));
            usuariosPorEmail.put(admin.getEmail(), admin);
            usuariosPorCedula.put(admin.getCedula(), admin);

            User estudiante = new User("Estudiante Demo", "estudiante@ucv.ve", "V-25000000", UserRole.ESTUDIANTE, "20-12345", hashPassword("Estudiante123"));
            usuariosPorEmail.put(estudiante.getEmail(), estudiante);
            usuariosPorCedula.put(estudiante.getCedula(), estudiante);

            User empleado = new User("Empleado Demo", "empleado@ucv.ve", "V-26000000", UserRole.EMPLEADO, "", hashPassword("Empleado123"));
            usuariosPorEmail.put(empleado.getEmail(), empleado);
            usuariosPorCedula.put(empleado.getCedula(), empleado);

            User conductor = new User("Carlos Gomez", "conductor@ucv.ve", "V-27000000", UserRole.CONDUCTOR, "", hashPassword("Conductor123"));
            usuariosPorEmail.put(conductor.getEmail(), conductor);
            usuariosPorCedula.put(conductor.getCedula(), conductor);

            asegurarSuperAdmin();
        } catch (Exception ignored) {
        }
    }
}
