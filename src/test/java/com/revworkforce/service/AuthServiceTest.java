package com.revworkforce.service;

import com.revworkforce.dao.EmployeeDAO;
import com.revworkforce.exception.InvalidInputException;
import com.revworkforce.model.Employee;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthServiceTest {

    private EmployeeDAO employeeDAO;
    private AuthService authService;

    @BeforeEach
    void setup() {
        employeeDAO = Mockito.mock(EmployeeDAO.class);
        authService = new AuthService(employeeDAO);
    }

    // login()

    @Test
    @Order(1)
    void login_WhenValidCredentials_ShouldReturnEmployee() {
        Employee emp = new Employee();
        emp.setEmail("navya@gmail.com");
        emp.setPassword("navya1234");
        emp.setStatus("ACTIVE");

        when(employeeDAO.getEmployeeByEmail("navya@gmail.com")).thenReturn(emp);

        Employee result = authService.login("navya@gmail.com", "navya1234");

        assertNotNull(result);
        assertEquals("navya@gmail.com", result.getEmail());
    }

    @Test
    @Order(2)
    void login_WhenInvalidPassword_ShouldReturnNull() {
        Employee emp = new Employee();
        emp.setEmail("navya@gmail.com");
        emp.setPassword("navya1234");
        emp.setStatus("ACTIVE");

        when(employeeDAO.getEmployeeByEmail("navya@gmail.com")).thenReturn(emp);

        Employee result = authService.login("navya@gmail.com", "wrongpassword");

        assertNull(result);
    }

    @Test
    @Order(3)
    void login_WhenUserNotFound_ShouldReturnNull() {
        when(employeeDAO.getEmployeeByEmail("unknown@mail.com")).thenReturn(null);

        Employee result = authService.login("unknown@mail.com", "anypass");

        assertNull(result);
    }

    @Test
    @Order(4)
    void login_WhenAccountIsInactive_ShouldReturnNull() {
        Employee emp = new Employee();
        emp.setEmail("inactive@mail.com");
        emp.setPassword("pass123");
        emp.setStatus("INACTIVE");

        when(employeeDAO.getEmployeeByEmail("inactive@mail.com")).thenReturn(emp);

        Employee result = authService.login("inactive@mail.com", "pass123");

        assertNull(result);
    }

    @Test
    @Order(5)
    void login_WhenEmailIsNull_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> authService.login(null, "pass123"));
    }

    @Test
    @Order(6)
    void login_WhenEmailIsEmpty_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> authService.login("", "pass123"));
    }

    @Test
    @Order(7)
    void login_WhenEmailFormatIsInvalid_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> authService.login("notanemail", "pass123"));
    }

    @Test
    @Order(8)
    void login_WhenPasswordIsNull_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> authService.login("navya@gmail.com", null));
    }

    @Test
    @Order(9)
    void login_WhenPasswordIsEmpty_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> authService.login("navya@gmail.com", ""));
    }

    // changePassword()

    @Test
    @Order(10)
    void changePassword_WhenValidInput_ShouldReturnSuccessMessage() {
        when(employeeDAO.verifyPassword(1, "oldPass1")).thenReturn(true);

        String result = authService.changePassword(1, "oldPass1", "newPass1");

        assertEquals("Password updated successfully!", result);
        verify(employeeDAO).updatePassword(1, "newPass1");
    }

    @Test
    @Order(11)
    void changePassword_WhenOldPasswordIsWrong_ShouldReturnErrorMessage() {
        when(employeeDAO.verifyPassword(1, "wrongOld")).thenReturn(false);

        String result = authService.changePassword(1, "wrongOld", "newPass1");

        assertEquals("Incorrect current password!", result);
        verify(employeeDAO, never()).updatePassword(anyInt(), anyString());
    }

    @Test
    @Order(12)
    void changePassword_WhenEmployeeIdIsInvalid_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> authService.changePassword(0, "old", "newPass1"));
    }

    @Test
    @Order(13)
    void changePassword_WhenNewPasswordIsTooShort_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> authService.changePassword(1, "oldPass1", "abc"));
    }

    @Test
    @Order(14)
    void changePassword_WhenNewPasswordIsNull_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> authService.changePassword(1, "oldPass1", null));
    }

    // resetPassword()

    @Test
    @Order(15)
    void resetPassword_WhenValidEmail_ShouldReturnSuccessMessage() {
        Employee emp = new Employee();
        emp.setEmployeeId(5);
        emp.setEmail("kowshik@gmail.com");

        when(employeeDAO.getByEmail("kowshik@gmail.com")).thenReturn(emp);

        String result = authService.resetPassword("kowshik@gmail.com", "newPass1");

        assertEquals("Password reset successfully!", result);
        verify(employeeDAO).updatePassword(5, "newPass1");
    }

    @Test
    @Order(16)
    void resetPassword_WhenEmailNotFound_ShouldReturnNotFoundMessage() {
        when(employeeDAO.getByEmail("nobody@mail.com")).thenReturn(null);

        String result = authService.resetPassword("nobody@mail.com", "newPass1");

        assertEquals("Email not found!", result);
    }

    @Test
    @Order(17)
    void resetPassword_WhenEmailIsNull_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> authService.resetPassword(null, "newPass1"));
    }

    @Test
    @Order(18)
    void resetPassword_WhenNewPasswordIsTooShort_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> authService.resetPassword("test@mail.com", "abc"));
    }
}
