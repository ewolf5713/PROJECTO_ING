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
import java.math.BigDecimal;

public class AuthService {

    public static final String SUPERADMIN_EMAIL = "superadmin@ucv.ve";
    public static final String SUPERADMIN_CEDULA = "V-00000000";
    public static final String SUPERADMIN_USERNAME = "superadmin";

    private static final Pattern PATRON_CORREO = Pattern.compile("^[A-Za-z0-9._%+-]+@([A-Za-z0-9.-]+\\.)?(ucv\\.ve|gmail\\.com)$");
    private static final Pattern PATRON_TELEFONO = Pattern.compile("^04(12|14|16|24|26)\\d{7}$");
    private static final Pattern PATRON_CEDULA_PM = Pattern.compile("^[VEve]-?\\d{6,9}$");
    public static final List<BigDecimal> MONTOS_RECARGA = List.of(new BigDecimal("5.00"), new BigDecimal("10.00"), new BigDecimal("20.00"), new BigDecimal("50.00"));
    private final Map<String, User> usuariosPorEmail = new HashMap<>();
    private final Map<String, User> usuariosPorCedula = new HashMap<>();
    private UserDatabase database;
    private final Set<String> ticketsPagados = new HashSet<>();
    private final TarifaService tarifaService = new TarifaService();

    public TarifaService getTarifaService() {
        return tarifaService;
    }

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

    public synchronized void registrarUsuario(String nombreCompleto, String email, String cedula, String carnet, String password) {
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
        UserRole rol = carnet != null && !carnet.isBlank() ? UserRole.ESTUDIANTE
                : emailNorm.endsWith("@ucv.ve") || emailNorm.endsWith(".ucv.ve") ? UserRole.EMPLEADO
                : UserRole.PUBLICO_GENERAL;
        User nuevo = new User(nombreCompleto.trim(), emailNorm, cedulaNorm, rol, carnet, passHash);

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

        if (usuario != null && verificarPassword(password, usuario.getPasswordHash())) {
            return Optional.of(usuario);
        }
        return Optional.empty();
    }

    public boolean esSuperAdmin(User user) {
        return user != null && user.getRol() == UserRole.SUPER_ADMIN;
    }

    public synchronized void cambiarRol(User actor, String cedula, UserRole nuevoRol) {
        if (!esSuperAdmin(actor)) {
            throw new IllegalArgumentException("Solo el Super Administrador puede asignar roles.");
        }
        User usuario = usuariosPorCedula.get(cedula == null ? "" : cedula.trim().toUpperCase());
        if (usuario == null) {
            throw new IllegalArgumentException("[USUARIO NO ENCONTRADO]");
        }
        if (nuevoRol == null || nuevoRol == UserRole.SUPER_ADMIN || esSuperAdmin(usuario)) {
            throw new IllegalArgumentException("No se puede modificar el rol de Super Administrador.");
        }
        usuario.setRol(nuevoRol);
        notificarCambio();
    }

    public synchronized List<User> getUsuarios() {
        return new ArrayList<>(usuariosPorEmail.values());
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
        User existente = usuariosPorEmail.get(SUPERADMIN_EMAIL);
        if (existente != null) {
            existente.setRol(UserRole.SUPER_ADMIN);
        } else {
            User superAdmin = new User("Super Administrador", SUPERADMIN_EMAIL, SUPERADMIN_CEDULA, UserRole.SUPER_ADMIN, "", hashPassword("Superadmin123"));
            usuariosPorEmail.put(SUPERADMIN_EMAIL, superAdmin);
            usuariosPorCedula.put(SUPERADMIN_CEDULA, superAdmin);
        }
    }

    public synchronized BigDecimal consultarSaldo(String email) {
        User usuario = usuariosPorEmail.get(email == null ? "" : email.trim().toLowerCase());
        if (usuario == null) { throw new IllegalArgumentException("[USUARIO NO ENCONTRADO]");}
        if (!usuario.tieneMonedero()) {throw new IllegalStateException("[USUARIO NO TIENE MONEDERO]");}
        
        return usuario.getSaldo();
    }

    public synchronized String pagarTicketExtraurbano(String email, String itinerarioId) {
    User usuario = usuariosPorEmail.get(email == null ? "" : email.trim().toLowerCase());
    if (usuario == null) {
        throw new IllegalArgumentException("Usuario no encontrado.");
    }
    if (itinerarioId == null || itinerarioId.isBlank()) {
        throw new IllegalArgumentException("Itinerario invalido.");
    }
    if (!usuario.tieneMonedero()) {
        throw new IllegalStateException("Este tipo de usuario no tiene monedero.");
    }
    String clave = usuario.getEmail() + "|" + itinerarioId;
    if (ticketsPagados.contains(clave)) {
        throw new IllegalStateException("Ya pagaste el ticket de este itinerario.");
    }
    BigDecimal precio = tarifaService.precio(usuario.getRol());
    if (usuario.getSaldo().compareTo(precio) < 0) {
        throw new IllegalStateException("Saldo insuficiente para pagar el ticket.");
    }

    usuario.debitar(precio);
    ticketsPagados.add(clave);
    notificarCambio();
    return "TK-" + (100000 + new SecureRandom().nextInt(900000));
}

    public synchronized String recargarMonedero(String email, String banco, String telefono, String cedula, String referencia, BigDecimal monto) {
        User usuario = usuariosPorEmail.get(email == null ? "" : email.trim().toLowerCase());
        if (usuario == null) { throw new IllegalArgumentException("[USUARIO NO ENCONTRADO]"); }
        if (!usuario.tieneMonedero()) { throw new IllegalArgumentException("[USUARIO NO TIENE MONEDERO]"); }
        if (banco == null || banco.isBlank()) { throw new IllegalArgumentException("[SELECIONES BANCO]"); }
        if (telefono == null || !PATRON_TELEFONO.matcher(telefono.trim()).matches()) { throw new IllegalArgumentException("[TELEFONO INVALIDO]"); }
        if (cedula == null || !PATRON_CEDULA_PM.matcher(cedula.trim()).matches()) { throw new IllegalArgumentException("[CEDULA INVALIDA]"); }
        if (referencia == null || !referencia.trim().matches("\\d{4,12}")) { throw new IllegalArgumentException("[REFERENCIA INVALIDA]"); }
        if (monto == null || MONTOS_RECARGA.stream().noneMatch(m -> m.compareTo(monto) == 0)) { throw new IllegalArgumentException("[SELECCIONE MONTO A RECARGAR]"); }

        usuario.acreditar(monto);
        notificarCambio();
        return "PM-" + (10000 + new SecureRandom().nextInt(900000));
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

            User estudiante = new User("Estudiante Demo", "estudiante@ucv.ve", "V-25000000", UserRole.ESTUDIANTE, "20-12345", hashPassword("Estudiante123"), new BigDecimal("25.00"));
            usuariosPorEmail.put(estudiante.getEmail(), estudiante);
            usuariosPorCedula.put(estudiante.getCedula(), estudiante);

            User empleado = new User("Empleado Demo", "empleado@ucv.ve", "V-26000000", UserRole.EMPLEADO, "", hashPassword("Empleado123"));
            usuariosPorEmail.put(empleado.getEmail(), empleado);
            usuariosPorCedula.put(empleado.getCedula(), empleado);

            User conductor = new User("Carlos Gomez", "conductor@ucv.ve", "V-27000000", UserRole.CONDUCTOR, "", hashPassword("Conductor123"));
            usuariosPorEmail.put(conductor.getEmail(), conductor);
            usuariosPorCedula.put(conductor.getCedula(), conductor);

            for (String[] c : new String[][]{{"Jose Martinez", "jose.martinez@ucv.ve", "V-27000001"}, {"Pedro Perez", "pedro.perez@ucv.ve", "V-27000002"}}) {
                User u = new User(c[0], c[1], c[2], UserRole.CONDUCTOR, "", hashPassword("Conductor123"));
                usuariosPorEmail.put(u.getEmail(), u);
                usuariosPorCedula.put(u.getCedula(), u);
            }

            asegurarSuperAdmin();
        } catch (Exception ignored) {
        }
    }
}
