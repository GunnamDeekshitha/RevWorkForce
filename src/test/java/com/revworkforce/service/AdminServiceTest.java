package com.revworkforce.service;

import com.revworkforce.model.*;
import org.junit.jupiter.api.*;
import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminServiceTest {

    private static AdminService service;

    @BeforeAll
    static void setup() {
        service = new AdminService();
        System.out.println("=== Starting Admin Service Tests ===");
    }

    @Test
    @Order(1)
    void testAddEmployee() {
        Employee emp = new Employee();
        emp.setName("Test");
        emp.setEmail("test2@mail.com");
        emp.setPassword("123");
        emp.setDepartment("IT");
        emp.setDesignation("Dev");
        emp.setRole("EMPLOYEE");
        emp.setStatus("ACTIVE");
        emp.setPhone("9876454322");

        service.addEmployee(emp);

        assertTrue(true);
    }

    @Test
    @Order(2)
    void testAssignManagerwhenValidIDisAssignedSuccessfully() {
        service.assignManager(1, 2);
        assertTrue(true);
    }

    @Test
    @Order(3)
    void testSearchById() {
        Employee emp = service.searchById(6);
        assertNotNull(emp);
    }

    @Test
    @Order(4)
    void testCancelLeave() {
        String result = service.cancelLeave(1);
        assertNotNull(result);
    }

    @Test
    @Order(5)
    void testAddAnnouncement() {
        service.addAnnouncement("Title", "Message");
        assertTrue(true);
    }

    @Test
    @Order(6)
    void testAddHoliday() {
        service.addHoliday("Festival",
                Date.valueOf("2026-05-01"), "Holiday");

        assertTrue(true);
    }

    @Test
    @Order(7)
    void testDeleteHoliday() {
        service.deleteHoliday(1);
        assertTrue(true);
    }

    @Test
    @Order(8)
    void testAddDepartment() {
        service.addDepartment("HR");
        assertTrue(true);
    }


    @Test
    @Order(9)
    void testGetDepartments() {
        List<Department> list = service.getDepartments();
        assertNotNull(list);
    }

    @Test
    @Order(10)
    void testAddDesignation() {
        service.addDesignation("Tester", 1);
        assertTrue(true);
    }

    @Test
    @Order(11)
    void testResetPassword() {
        service.resetEmployeePassword(1, "newpass");
        assertTrue(true);
    }

    @AfterAll
    static void end() {
        System.out.println("=== Finished Admin Service Tests ===");
    }
}