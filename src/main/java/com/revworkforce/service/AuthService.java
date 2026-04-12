
package com.revworkforce.service;

import com.revworkforce.dao.EmployeeDAO;
import com.revworkforce.model.Employee;
import java.util.logging.Logger;

public class AuthService {

    private static final Logger logger = Logger.getLogger(AuthService.class.getName());

    private EmployeeDAO employeeDAO;


    public AuthService() {
        this.employeeDAO = new EmployeeDAO();
    }

    public AuthService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }
    public Employee login(String email, String password) {

        logger.info("Login attempt for email: " + email);

        if (email == null || password == null) {
            logger.severe("Email or password is null");
            throw new RuntimeException("Invalid login input");
        }

        Employee emp = employeeDAO.getEmployeeByEmail(email);

        if (emp != null) {
            if (!emp.getStatus().equalsIgnoreCase("ACTIVE")) {
                logger.warning("Account is inactive for email: " + email);
                return null;
            }
            else if (emp.getPassword().equals(password)) {
                logger.info("Login successful for email: " + email);
                return emp;
            } else {
                logger.warning("Invalid password for email: " + email);
            }
        } else {
            logger.warning("User not found for email: " + email);
        }

        return null;
    }

    public String changePassword(int empId, String oldPass, String newPass) {

        logger.info("Password change request for employee ID: " + empId);

        if (empId <= 0 || oldPass == null || newPass == null) {
            logger.severe("Invalid input for password change");
            throw new RuntimeException("Invalid input");
        }

        if (!employeeDAO.verifyPassword(empId, oldPass)) {
            logger.warning("Incorrect current password for employee ID: " + empId);
            return "Incorrect current password!";
        }

        employeeDAO.updatePassword(empId, newPass);
        logger.info("Password updated successfully for employee ID: " + empId);

        return "Password updated successfully!";
    }

    public String resetPassword(String email, String newPass) {

        logger.info("Password reset request for email: " + email);

        if (email == null || newPass == null) {
            logger.severe("Invalid input for password reset");
            throw new RuntimeException("Invalid input");
        }

        Employee emp = employeeDAO.getByEmail(email);

        if (emp == null) {
            logger.warning("Email not found: " + email);
            return "Email not found!";
        }

        employeeDAO.updatePassword(emp.getEmployeeId(), newPass);
        logger.info("Password reset successful for email: " + email);

        return "Password reset successfully!";
    }
}