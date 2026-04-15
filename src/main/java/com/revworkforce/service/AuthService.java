package com.revworkforce.service;

import com.revworkforce.dao.EmployeeDAO;
import com.revworkforce.exception.DatabaseException;
import com.revworkforce.exception.InvalidInputException;
import com.revworkforce.model.Employee;

import java.util.logging.Logger;

public class AuthService {

    private static final Logger logger = Logger.getLogger(AuthService.class.getName());

    private final EmployeeDAO employeeDAO;

    public AuthService() {
        this.employeeDAO = new EmployeeDAO();
    }

    public AuthService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }


    public Employee login(String email, String password) {
        logger.info("Login attempt for email: " + email);

        if (email == null || email.trim().isEmpty()) {
            throw new InvalidInputException("Email cannot be null or empty");
        }
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new InvalidInputException("Invalid email format: " + email);
        }
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidInputException("Password cannot be null or empty");
        }

        try {
            Employee emp = employeeDAO.getEmployeeByEmail(email);
            if (emp == null) {
                logger.warning("User not found for email: " + email);
                return null;
            }
            if (!emp.getStatus().equalsIgnoreCase("ACTIVE")) {
                logger.warning("Inactive account login attempt: " + email);
                return null;
            }
            if (!emp.getPassword().equals(password)) {
                logger.warning("Wrong password for email: " + email);
                return null;
            }
            logger.info("Login successful for: " + email);
            return emp;
        } catch (DatabaseException e) {
            logger.severe("DB error during login for " + email + ": " + e.getMessage());
            throw e;
        }
    }

    public String changePassword(int empId, String oldPass, String newPass) {
        logger.info("Password change request for employee ID: " + empId);

        if (empId <= 0) {
            throw new InvalidInputException("Employee ID must be a positive number");
        }
        if (oldPass == null || oldPass.trim().isEmpty()) {
            throw new InvalidInputException("Current password cannot be empty");
        }
        if (newPass == null || newPass.trim().isEmpty()) {
            throw new InvalidInputException("New password cannot be empty");
        }
        if (newPass.length() < 6) {
            throw new InvalidInputException("New password must be at least 6 characters");
        }
        if (oldPass.equals(newPass)) {
            throw new InvalidInputException("New password must be different from current password");
        }

        try {
            if (!employeeDAO.verifyPassword(empId, oldPass)) {
                logger.warning("Incorrect current password for employee ID: " + empId);
                return "Incorrect current password!";
            }
            employeeDAO.updatePassword(empId, newPass);
            logger.info("Password updated for employee ID: " + empId);
            return "Password updated successfully!";
        } catch (DatabaseException e) {
            logger.severe("DB error during password change for employee " + empId + ": " + e.getMessage());
            throw e;
        }
    }

    public String resetPassword(String email, String newPass) {
        logger.info("Password reset request for email: " + email);

        if (email == null || email.trim().isEmpty()) {
            throw new InvalidInputException("Email cannot be null or empty");
        }
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new InvalidInputException("Invalid email format: " + email);
        }
        if (newPass == null || newPass.trim().isEmpty()) {
            throw new InvalidInputException("New password cannot be empty");
        }
        if (newPass.length() < 6) {
            throw new InvalidInputException("New password must be at least 6 characters");
        }

        try {
            Employee emp = employeeDAO.getByEmail(email);
            if (emp == null) {
                logger.warning("Email not found: " + email);
                return "Email not found!";
            }
            employeeDAO.updatePassword(emp.getEmployeeId(), newPass);
            logger.info("Password reset successful for: " + email);
            return "Password reset successfully!";
        } catch (DatabaseException e) {
            logger.severe("DB error during password reset for " + email + ": " + e.getMessage());
            throw e;
        }
    }
}
