package com.revworkforce.service;

import com.revworkforce.dao.EmployeeDAO;
import com.revworkforce.model.Employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    private EmployeeDAO employeeDAO;
    private AuthService authService;

    @BeforeEach
    void setup() {
        employeeDAO = Mockito.mock(EmployeeDAO.class);
        authService = new AuthService(employeeDAO);
    }

    // 🔹 LOGIN SUCCESS
    @Test
    void testLoginSuccess() {
        Employee emp = new Employee();
        emp.setEmail("test@mail.com");
        emp.setPassword("123");
        emp.setStatus("ACTIVE");

        when(employeeDAO.getEmployeeByEmail("test@mail.com")).thenReturn(emp);

        Employee result = authService.login("test@mail.com", "123");

        assertNotNull(result);
        assertEquals("test@mail.com", result.getEmail());
    }

    // 🔹 INVALID PASSWORD
    @Test
    void testLoginInvalidPassword() {
        Employee emp = new Employee();
        emp.setPassword("123");
        emp.setStatus("ACTIVE");

        when(employeeDAO.getEmployeeByEmail("test@mail.com")).thenReturn(emp);

        Employee result = authService.login("test@mail.com", "wrong");

        assertNull(result);
    }

    // 🔹 USER NOT FOUND
    @Test
    void testLoginUserNotFound() {
        when(employeeDAO.getEmployeeByEmail("x@mail.com")).thenReturn(null);

        Employee result = authService.login("x@mail.com", "123");

        assertNull(result);
    }

    // 🔹 INACTIVE USER
    @Test
    void testLoginInactiveUser() {
        Employee emp = new Employee();
        emp.setPassword("123");
        emp.setStatus("INACTIVE");

        when(employeeDAO.getEmployeeByEmail("test@mail.com")).thenReturn(emp);

        Employee result = authService.login("test@mail.com", "123");

        assertNull(result);
    }

    // 🔹 NULL INPUT
    @Test
    void testLoginNullInput() {
        assertThrows(RuntimeException.class, () -> {
            authService.login(null, "123");
        });
    }

    // 🔹 CHANGE PASSWORD SUCCESS
    @Test
    void testChangePasswordSuccess() {
        when(employeeDAO.verifyPassword(1, "old")).thenReturn(true);

        String result = authService.changePassword(1, "old", "new");

        assertEquals("Password updated successfully!", result);
        verify(employeeDAO).updatePassword(1, "new");
    }

    // 🔹 CHANGE PASSWORD WRONG OLD
    @Test
    void testChangePasswordWrongOld() {
        when(employeeDAO.verifyPassword(1, "old")).thenReturn(false);

        String result = authService.changePassword(1, "old", "new");

        assertEquals("Incorrect current password!", result);
    }

    // 🔹 RESET PASSWORD SUCCESS
    @Test
    void testResetPasswordSuccess() {
        Employee emp = new Employee();
        emp.setEmployeeId(1);

        when(employeeDAO.getByEmail("test@mail.com")).thenReturn(emp);

        String result = authService.resetPassword("test@mail.com", "new");

        assertEquals("Password reset successfully!", result);
        verify(employeeDAO).updatePassword(1, "new");
    }

    // 🔹 RESET PASSWORD EMAIL NOT FOUND
    @Test
    void testResetPasswordEmailNotFound() {
        when(employeeDAO.getByEmail("x@mail.com")).thenReturn(null);

        String result = authService.resetPassword("x@mail.com", "new");

        assertEquals("Email not found!", result);
    }
}