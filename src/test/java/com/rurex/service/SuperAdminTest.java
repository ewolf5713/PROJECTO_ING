package com.rurex.service;

import com.rurex.model.User;
import com.rurex.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class SuperAdminTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
    }

    @Test
    void testSuperAdminExistsByDefault() {
        Optional<User> byEmail = authService.autenticar(AuthService.SUPERADMIN_EMAIL, "Superadmin123");
        assertTrue(byEmail.isPresent());
        assertEquals("Super Administrador", byEmail.get().getNombreCompleto());
        assertEquals(UserRole.SUPER_ADMIN, byEmail.get().getRol());
        assertTrue(authService.esSuperAdmin(byEmail.get()));
    }

    private User superAdmin() {
        return authService.autenticar("superadmin", "Superadmin123").get();
    }

    @Test
    void superAdminCambiaRol() {
        authService.cambiarRol(superAdmin(), "V-26000000", UserRole.CONDUCTOR);
        assertEquals(UserRole.CONDUCTOR, authService.autenticar("empleado@ucv.ve", "Empleado123").get().getRol());
    }

    @Test
    void soloSuperAdminCambiaRoles() {
        User admin = authService.autenticar("admin@ucv.ve", "Admin123").get();
        assertThrows(IllegalArgumentException.class, () -> authService.cambiarRol(admin, "V-26000000", UserRole.CONDUCTOR));
        assertEquals(UserRole.EMPLEADO, authService.autenticar("empleado@ucv.ve", "Empleado123").get().getRol());
    }

    @Test
    void noSeAsignaNiSeQuitaSuperAdmin() {
        assertThrows(IllegalArgumentException.class, () -> authService.cambiarRol(superAdmin(), "V-26000000", UserRole.SUPER_ADMIN));
        assertThrows(IllegalArgumentException.class, () -> authService.cambiarRol(superAdmin(), AuthService.SUPERADMIN_CEDULA, UserRole.ESTUDIANTE));
        assertThrows(IllegalArgumentException.class, () -> authService.cambiarRol(superAdmin(), "V-99999999", UserRole.ESTUDIANTE));
    }

    @Test
    void superAdminAntiguoSeMigraARolSuperAdmin() {
        User viejo = new User("Super Administrador", AuthService.SUPERADMIN_EMAIL, AuthService.SUPERADMIN_CEDULA, UserRole.ADMINISTRADOR, "", AuthService.hashPassword("Superadmin123"));
        authService.cargarUsuarios(java.util.List.of(viejo));
        assertEquals(UserRole.SUPER_ADMIN, superAdmin().getRol());
    }

    @Test
    void testSuperAdminLoginByUsername() {
        Optional<User> byUsername = authService.autenticar("superadmin", "Superadmin123");
        assertTrue(byUsername.isPresent());
        assertEquals(AuthService.SUPERADMIN_EMAIL, byUsername.get().getEmail());
    }

    @Test
    void testSuperAdminLoginByCedula() {
        Optional<User> byCedula = authService.autenticar(AuthService.SUPERADMIN_CEDULA, "Superadmin123");
        assertTrue(byCedula.isPresent());
        assertEquals(AuthService.SUPERADMIN_EMAIL, byCedula.get().getEmail());
    }

    @Test
    void testSuperAdminRejectsHardcodedAlternativePasswords() {
        assertTrue(authService.autenticar("superadmin", "Superadmin123").isPresent());
        assertFalse(authService.autenticar("superadmin", "superadmin").isPresent());
        assertFalse(authService.autenticar("superadmin", "Admin123").isPresent());
        assertFalse(authService.autenticar("superadmin", "claveIncorrecta").isPresent());
    }

    @Test
    void testUsernameAliasIsNotAStoredUser() {
        assertEquals(1, authService.getUsuarios().stream().filter(authService::esSuperAdmin).count());
        assertTrue(authService.getUsuarios().stream().noneMatch(u -> u.getEmail().equals("superadmin")));
    }
}
