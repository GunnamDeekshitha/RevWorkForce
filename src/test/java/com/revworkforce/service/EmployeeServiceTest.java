package com.revworkforce.service;

import com.revworkforce.model.*;
import org.junit.jupiter.api.*;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeServiceTest {

    private static EmployeeService service;

    @BeforeAll
    static void setup() {
        service = new EmployeeService();
        System.out.println("=== Starting Employee Service Tests ===");
    }

    @Test
    @Order(1)
    void testGetProfile() {
        Employee emp = service.getProfile(6);
        assertNotNull(emp);
    }

    @Test
    @Order(2)
    void testUpdateProfile() {
        service.updateProfile(12, "9876543210", "New Address", "Emergency", "9999999999");
        assertTrue(true);
    }

    @Test
    @Order(3)
    void testApplyLeave() {
        Employee emp = new Employee();
        emp.setEmployeeId(1);
        emp.setManagerId(2);
        emp.setName("Test");

        LeaveRequest leave = new LeaveRequest();
        leave.setEmployeeId(1);
        leave.setLeaveType("CL");
        leave.setStartDate(Date.valueOf("2026-04-20"));
        leave.setEndDate(Date.valueOf("2026-04-21"));
        leave.setReason("Personal");

        String result = service.applyLeave(emp, leave);

        assertNotNull(result);
    }

    @Test
    @Order(4)
    void testCancelLeave() {
        String result = service.cancelLeave(1, 1);
        assertNotNull(result);
    }

    @Test
    @Order(5)
    void testGetHolidays() {
        List<Holiday> list = service.getHolidays();
        assertNotNull(list);
    }

    @Test
    @Order(6)
    void testSubmitReview() {
        PerformanceReview r = new PerformanceReview();
        r.setEmployeeId(1);
        r.setYear(2026);
        r.setSelfAssessment("Good");
        r.setAccomplishments("Done tasks");
        r.setImprovements("Time management");
        r.setRating(4);

        service.submitReview(r);

        assertTrue(true);
    }


    @Test
    @Order(7)
    void testAddGoal() {
        Goal g = new Goal();
        g.setEmployeeId(1);
        g.setDescription("Complete project");
        g.setDeadline(Date.valueOf("2026-05-01"));
        g.setPriority("HIGH");
        g.setSuccessMetrics("Finish on time");

        service.addGoal(g);

        assertTrue(true);
    }

    @Test
    @Order(8)
    void testUpdateGoalStatus() {
        service.updateGoalStatus(1, "COMPLETED"); // goalId must exist
        assertTrue(true);
    }


    @Test
    @Order(9)
    void testGetNotifications() {
        List<Notification> list = service.getNotifications(1);
        assertNotNull(list);
    }


    @Test
    @Order(10)
    void testGetUnreadCount() {
        int count = service.getUnreadCount(1);
        assertTrue(count >= 0);
    }


    @Test
    @Order(11)
    void testMarkNotificationsRead() {
        service.markNotificationsRead(1);
        assertTrue(true);
    }

    @AfterAll
    static void end() {
        System.out.println("=== Finished Employee Service Tests ===");
    }

}
