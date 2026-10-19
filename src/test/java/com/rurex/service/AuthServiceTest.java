package com.rurex.service;

import com.rurex.model.User;
import com.rurex.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

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
                "",
                "MiClaveSegura2026"
        );

        Optional<User> byEmail = authService.autenticar("daniel.quiaro@ciens.ucv.ve", "MiClaveSegura2026");
        assertTrue(byEmail.isPresent());
        assertEquals("Daniel Quiaro", byEmail.get().getNombreCompleto());
        assertEquals(UserRole.EMPLEADO, byEmail.get().getRol());

        Optional<User> byIdCard = authService.autenticar("V-32080586", "MiClaveSegura2026");
        assertTrue(byIdCard.isPresent());
    }

    @Test
    void registroConCarnetAsignaEstudiante() {
        authService.registrarUsuario("Ana Perez", "ana@ucv.ve", "V-12345678", "20-11111", "clave123");
        assertEquals(UserRole.ESTUDIANTE, authService.autenticar("ana@ucv.ve", "clave123").get().getRol());
    }

    @Test
    void testRejectDuplicateEmail() {
        authService.registrarUsuario("Usuario Uno", "duplicado@ucv.ve", "V-11111111", "20-1111", "Clave123");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                authService.registrarUsuario("Usuario Dos", "duplicado@ucv.ve", "V-22222222", "20-2222", "Clave123")
        );
        assertTrue(ex.getMessage().contains("ya esta registrado"));
    }

    @Test
    void testRejectInvalidEmailFormat() {
        assertThrows(IllegalArgumentException.class, () ->
                authService.registrarUsuario("Invalido", "test@yahoo.com", "V-33333333", "", "Clave123")
        );
    }

    @Test
    void testRejectWrongPassword() {
        Optional<User> result = authService.autenticar("admin@ucv.ve", "PasswordEquivocado");
        assertTrue(result.isEmpty());
    }

    @Test
    void testSeededEmpleadoAuthenticates() {
        Optional<User> result = authService.autenticar("empleado@ucv.ve", "Empleado123");
        assertTrue(result.isPresent());
        assertEquals("Empleado Demo", result.get().getNombreCompleto());
        assertEquals(UserRole.EMPLEADO, result.get().getRol());
    }

    @Test
    void testSeededConductorAuthenticates() {
        Optional<User> result = authService.autenticar("conductor@ucv.ve", "Conductor123");
        assertTrue(result.isPresent());
        assertEquals("Carlos Gomez", result.get().getNombreCompleto());
        assertEquals(UserRole.CONDUCTOR, result.get().getRol());
    }

    @Test 
    void usuarioNuevoTieneSaldoCero() {
        AuthService auth = new AuthService();
        auth.registrarUsuario("Ana Perez", "ana@ucv.ve", "V-12345678", "20-11111", "clave123");
        assertEquals(0, auth.consultarSaldo("ana@ucv.ve").compareTo(BigDecimal.ZERO));
    }

    @Test 
    void estudianteDemoTieneSaldoInicial() {
        AuthService auth = new AuthService();
        assertEquals(0, auth.consultarSaldo("estudiante@ucv.ve").compareTo(new BigDecimal("25.00")));
    }

    @Test 
    void administradorNoTieneMonedero() {
        AuthService auth = new AuthService();
        assertThrows(IllegalStateException.class, () -> auth.consultarSaldo("admin@ucv.ve"));
    }

    @Test 
    void recargaValidaAumentaElSaldo() {
        AuthService auth = new AuthService();
        String ref = auth.recargarMonedero("estudiante@ucv.ve", "Banesco", "04141234567", "V12345678", "123456", new BigDecimal("10.00"));
        assumeTrue(ref.startsWith("PM-"));
        assertEquals(0, auth.consultarSaldo("estudiante@ucv.ve").compareTo(new BigDecimal("35.00")));
    }

    @Test 
    void telefonoInvalidoRechazaYnoCambiaElSaldo() {
        AuthService auth = new AuthService();
        assertThrows(IllegalArgumentException.class, () -> auth.recargarMonedero(
            "estudiante@ucv.ve", "Banesco", "123", "V12345678", "123456", new BigDecimal("10.00")));
    }

    @Test 
    void montoNoPermitidoSeRechaza() {
        AuthService auth = new AuthService();
        assertThrows(IllegalArgumentException.class, () -> auth.recargarMonedero(
            "estudiante@ucv.ve", "Banesco", "04141234567", "V12345678", "123456", new BigDecimal("7.00")));
    }

    @Test
    void referenciaInvalidaRechazaYNoCambiaElSaldo() {
        AuthService auth = new AuthService();
        for (String ref : new String[]{null, "", "12a456", "123", "1234567890123"}) {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> auth.recargarMonedero(
                "estudiante@ucv.ve", "Banesco", "04141234567", "V12345678", ref, new BigDecimal("10.00")));
            assertEquals("[REFERENCIA INVALIDA]", ex.getMessage());
        }
        assertEquals(0, auth.consultarSaldo("estudiante@ucv.ve").compareTo(new BigDecimal("25.00")));
    }

    @Test
    void administradorNoPuedeRecargar() {
        AuthService auth = new AuthService();
        assertThrows(IllegalArgumentException.class, () -> auth.recargarMonedero(
            "admin@ucv.ve", "Banesco", "04141234567", "V12345678", "123456", new BigDecimal("10.00")));
    }

    @Test
    void pagarTicketDescuentaElPrecio() {
        AuthService auth = new AuthService();
        auth.pagarTicketExtraurbano("estudiante@ucv.ve", "IT-1");
        assertEquals(0, auth.consultarSaldo("estudiante@ucv.ve").compareTo(new BigDecimal("15.00")));
    }

    @Test
    void noSePuedePagarDosVecesElMismoItinerario() {
        AuthService auth = new AuthService();
        auth.pagarTicketExtraurbano("estudiante@ucv.ve", "IT-1");
        assertThrows(IllegalStateException.class, () -> auth.pagarTicketExtraurbano("estudiante@ucv.ve", "IT-1"));
        assertEquals(0, auth.consultarSaldo("estudiante@ucv.ve").compareTo(new BigDecimal("15.00")));
    }

    @Test
    void saldoInsuficienteRechazaYNoCambiaElSaldo() {
        AuthService auth = new AuthService();
        auth.registrarUsuario("Ana Perez", "ana@ucv.ve", "V-12345678", "20-11111", "clave123");
        assertThrows(IllegalStateException.class, () -> auth.pagarTicketExtraurbano("ana@ucv.ve", "IT-1"));
        assertEquals(0, auth.consultarSaldo("ana@ucv.ve").compareTo(BigDecimal.ZERO));
    }

    @Test
    void administradorNoPuedePagarTicket() {
        AuthService auth = new AuthService();
        assertThrows(IllegalStateException.class, () -> auth.pagarTicketExtraurbano("admin@ucv.ve", "IT-1"));
    }
}
