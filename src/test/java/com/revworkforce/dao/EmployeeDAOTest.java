package com.revworkforce.dao;

import com.revworkforce.model.Employee;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeDAOTest {

    @Test
    public void testGetEmployeeByEmail() {
        EmployeeDAO dao = new EmployeeDAO();

        Employee emp = dao.getEmployeeByEmail("admin@gmail.com");

        assertNotNull(emp);
        assertEquals("ADMIN", emp.getRole());
    }
    @Test
    public void testAddEmployee() {
        EmployeeDAO dao = new EmployeeDAO();

        Employee emp = new Employee();
        emp.setName("JUnit User");
        emp.setEmail("junit@gmail.com");
        emp.setPassword("1234");
        emp.setPhone("9999999999");
        emp.setAddress("Test");
        emp.setRole("EMPLOYEE");
        emp.setDepartment("IT");
        emp.setDesignation("Tester");
        emp.setSalary(30000);
        emp.setStatus("ACTIVE");

        dao.addEmployee(emp);

        Employee result = dao.getEmployeeByEmail("junit@gmail.com");

        assertNotNull(result);
    }
}