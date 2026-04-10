package com.revworkforce.service;

import com.revworkforce.dao.EmployeeDAO;
import com.revworkforce.model.Employee;

public class AuthService {

    private EmployeeDAO employeeDAO = new EmployeeDAO();

    public Employee login(String email, String password) {

        Employee emp = employeeDAO.getEmployeeByEmail(email);

        if (emp != null) {
            if (!emp.getStatus().equalsIgnoreCase("ACTIVE")) {
                System.out.println("Account is inactive!");
                return null;
            }
            else if (emp.getPassword().equals(password)) {
                System.out.println("Login Successful!");
                return emp;
            } else {
                System.out.println("Invalid Password!");
            }
        } else {
            System.out.println("User not found!");
        }

        return null;
    }
}