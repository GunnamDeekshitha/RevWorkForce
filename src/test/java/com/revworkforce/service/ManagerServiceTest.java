package com.revworkforce.service;

import com.revworkforce.model.*;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ManagerServiceTest {

    private static ManagerService service;

    @BeforeAll
    static void setup() {
        service = new ManagerService(); // REAL service
        System.out.println("=== Starting Manager Service Tests ===");
    }

    @Test
    @Order(1)
    void testGetLeaveRequests() {
        List<LeaveRequest> list = service.getLeaveRequests(1); // managerId must exist
        assertNotNull(list);
    }


    @Test
    @Order(2)
    void testGetLeaveRequestsInvalid() {
        assertThrows(RuntimeException.class,
                () -> service.getLeaveRequests(0));
    }


    @Test
    @Order(3)
    void testUpdateLeaveStatus() {
        service.updateLeaveStatus(1, "APPROVED", "ok");
        assertTrue(true);
    }


    @Test
    @Order(4)
    void testUpdateLeaveStatusInvalid() {
        assertThrows(RuntimeException.class,
                () -> service.updateLeaveStatus(0, null, ""));
    }


    @Test
    @Order(5)
    void testGetTeamReviews() {
        List<PerformanceReview> list = service.getTeamReviews(1);
        assertNotNull(list);
    }


    @Test
    @Order(6)
    void testGiveFeedback() {
        service.giveFeedback(1, "Good job");
        assertTrue(true);
    }

    @Test
    @Order(7)
    void testGiveFeedbackInvalid() {
        assertThrows(RuntimeException.class,
                () -> service.giveFeedback(0, null));
    }

    @Test
    @Order(8)
    void testGetTeamMembers() {
        List<Employee> list = service.getTeamMembers(1);
        assertNotNull(list);
    }

    @Test
    @Order(9)
    void testGetTeamLeaveBalances() {
        List<LeaveBalance> list = service.getTeamLeaveBalances(1);
        assertNotNull(list);
    }

    @Test
    @Order(10)
    void testGetTeamGoals() {
        List<Goal> list = service.getTeamGoals(1);
        assertNotNull(list);
    }

    @Test
    @Order(11)
    void testGetNotifications() {
        List<Notification> list = service.getNotifications(1);
        assertNotNull(list);
    }

    @Test
    @Order(12)
    void testGetUnreadCount() {
        int count = service.getUnreadCount(1);
        assertTrue(count >= 0);
    }


    @Test
    @Order(13)
    void testMarkNotificationsRead() {
        service.markNotificationsRead(1);
        assertTrue(true);
    }


    @Test
    @Order(14)
    void testGiveRating() {
        service.giveRating(1, 4);
        assertTrue(true);
    }


    @Test
    @Order(15)
    void testGiveRatingInvalid() {
        assertThrows(RuntimeException.class,
                () -> service.giveRating(0, -1));
    }

    @AfterAll
    static void end() {
        System.out.println("=== Finished Manager Service Tests ===");
    }
}