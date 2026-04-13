package com.revworkforce.service;

import com.revworkforce.model.Employee;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthServiceTest {

    private static AuthService authService;

    @BeforeAll
    static void setup() {
        authService = new AuthService();
        System.out.println("=== Starting Auth Service Tests ===");
    }

    @Test
    @Order(1)
    void testLoginSuccess() {
        Employee result = authService.login("navya@gmail.com", "navya1234");

        assertNotNull(result);
        assertEquals("navya@gmail.com", result.getEmail());
    }

    @Test
    @Order(2)
    void testLoginInvalidPassword() {
        Employee result = authService.login("test@mail.com", "wrong");

        assertNull(result);
    }

    @Test
    @Order(3)
    void testLoginUserNotFound() {
        Employee result = authService.login("x@mail.com", "123");

        assertNull(result);
    }

    @Test
    @Order(4)
    void testLoginInactiveUser() {
        // make sure this user exists with INACTIVE status in DB
        Employee result = authService.login("inactive@mail.com", "123");

        assertNull(result);
    }

    @Test
    @Order(5)
    void testLoginNullInput() {
        assertThrows(RuntimeException.class, () -> {
            authService.login(null, "123");
        });
    }

    @Test
    @Order(6)
    void testChangePasswordSuccess() {
        String result = authService.changePassword(1, "old", "new");

        assertNotNull(result);
    }

    @Test
    @Order(7)
    void testChangePasswordWrongOld() {
        String result = authService.changePassword(1, "wrong", "new");

        assertEquals("Incorrect current password!", result);
    }

    @Test
    @Order(8)
    void testResetPasswordSuccess() {
        String result = authService.resetPassword("kowshik@gmail.com", "new");
        assertEquals("Password reset successfully!", result);
    }

    @Test
    @Order(9)
    void testResetPasswordEmailNotFound() {
        String result = authService.resetPassword("kowshi@gmail.com", "new");

        assertEquals("Email not found!", result);
    }

    @AfterAll
    static void end() {
        System.out.println("=== Finished Auth Service Tests ===");
    }
}