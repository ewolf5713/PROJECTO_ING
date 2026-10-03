package com.rurex.service;

import com.rurex.model.User;
import com.rurex.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
    }

    @Test
    void testRegisterAndAuthenticateSuccess() {
        authService.registrarUsuario(
                "Daniel Quiaro",
                "daniel.quiaro@ciens.ucv.ve",
                "V-32080586",
                UserRole.ESTUDIANTE,
                "",
                "MiClaveSegura2026"
        );

        Optional<User> byEmail = authService.autenticar("daniel.quiaro@ciens.ucv.ve", "MiClaveSegura2026");
        assertTrue(byEmail.isPresent());
        assertEquals("Daniel Quiaro", byEmail.get().getNombreCompleto());
        assertEquals(UserRole.ESTUDIANTE, byEmail.get().getRol());

        Optional<User> byIdCard = authService.autenticar("V-32080586", "MiClaveSegura2026");
        assertTrue(byIdCard.isPresent());
    }

    @Test
    void testRejectAdminSelfRegistration() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                authService.registrarUsuario(
                        "Hacker Admin",
                        "hacker@ucv.ve",
                        "V-99999999",
                        UserRole.ADMINISTRADOR,
                        "",
                        "Clave123"
                )
        );
        assertTrue(ex.getMessage().contains("No se permite el autorregistro"));
    }

    @Test
    void testRejectDuplicateEmail() {
        authService.registrarUsuario("Usuario Uno", "duplicado@ucv.ve", "V-11111111", UserRole.ESTUDIANTE, "20-1111", "Clave123");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                authService.registrarUsuario("Usuario Dos", "duplicado@ucv.ve", "V-22222222", UserRole.ESTUDIANTE, "20-2222", "Clave123")
        );
        assertTrue(ex.getMessage().contains("ya esta registrado"));
    }

    @Test
    void testRejectInvalidEmailFormat() {
        assertThrows(IllegalArgumentException.class, () ->
                authService.registrarUsuario("Invalido", "test@yahoo.com", "V-33333333", UserRole.ESTUDIANTE, "", "Clave123")
        );
    }

    @Test
    void testRejectWrongPassword() {
        Optional<User> result = authService.autenticar("admin@ucv.ve", "PasswordEquivocado");
        assertTrue(result.isEmpty());
    }
}
