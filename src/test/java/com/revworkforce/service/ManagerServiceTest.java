package com.revworkforce.service;

import com.revworkforce.dao.*;
import com.revworkforce.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ManagerServiceTest {

    private LeaveDAO leaveDAO;
    private PerformanceDAO performanceDAO;
    private EmployeeDAO employeeDAO;
    private LeaveBalanceDAO leaveBalanceDAO;
    private GoalDAO goalDAO;
    private NotificationDAO notificationDAO;

    private ManagerService managerService;

    @BeforeEach
    void setup() {
        leaveDAO = mock(LeaveDAO.class);
        performanceDAO = mock(PerformanceDAO.class);
        employeeDAO = mock(EmployeeDAO.class);
        leaveBalanceDAO = mock(LeaveBalanceDAO.class);
        goalDAO = mock(GoalDAO.class);
        notificationDAO = mock(NotificationDAO.class);

        // ✅ constructor injection
        managerService = new ManagerService(
                leaveDAO,
                performanceDAO,
                employeeDAO,
                leaveBalanceDAO,
                goalDAO,
                notificationDAO
        );
    }

    // 🔹 GET LEAVE REQUESTS
    @Test
    void testGetLeaveRequestsSuccess() {
        when(leaveDAO.getLeavesByManager(1)).thenReturn(new ArrayList<>());

        List<LeaveRequest> result = managerService.getLeaveRequests(1);

        assertNotNull(result);
    }

    @Test
    void testGetLeaveRequestsInvalid() {
        assertThrows(RuntimeException.class,
                () -> managerService.getLeaveRequests(0));
    }

    // 🔹 UPDATE LEAVE STATUS
    @Test
    void testUpdateLeaveStatusWithNotification() {
        LeaveRequest leave = new LeaveRequest();
        leave.setEmployeeId(1);

        when(leaveDAO.getLeaveById(1)).thenReturn(leave);

        managerService.updateLeaveStatus(1, "APPROVED", "ok");

        verify(leaveDAO).updateLeaveStatus(1, "APPROVED", "ok");
        verify(notificationDAO).addNotification(any(Notification.class));
    }

    @Test
    void testUpdateLeaveStatusInvalid() {
        assertThrows(RuntimeException.class,
                () -> managerService.updateLeaveStatus(0, null, ""));
    }

    // 🔹 TEAM REVIEWS
    @Test
    void testGetTeamReviewsSuccess() {
        when(performanceDAO.getReviewsByManager(1)).thenReturn(new ArrayList<>());

        List<PerformanceReview> result = managerService.getTeamReviews(1);

        assertNotNull(result);
    }

    // 🔹 GIVE FEEDBACK
    @Test
    void testGiveFeedbackSuccess() {
        PerformanceReview review = new PerformanceReview();
        review.setEmployeeId(1);

        when(performanceDAO.getReviewById(1)).thenReturn(review);

        managerService.giveFeedback(1, "Good job");

        verify(performanceDAO).addManagerFeedback(1, "Good job");
        verify(notificationDAO).addNotification(any(Notification.class));
    }

    @Test
    void testGiveFeedbackInvalid() {
        assertThrows(RuntimeException.class,
                () -> managerService.giveFeedback(0, null));
    }

    // 🔹 TEAM MEMBERS
    @Test
    void testGetTeamMembersSuccess() {
        when(employeeDAO.getEmployeesByManager(1)).thenReturn(new ArrayList<>());

        List<Employee> result = managerService.getTeamMembers(1);

        assertNotNull(result);
    }

    // 🔹 LEAVE BALANCES
    @Test
    void testGetTeamLeaveBalancesSuccess() {
        when(leaveBalanceDAO.getTeamLeaveBalances(1)).thenReturn(new ArrayList<>());

        List<LeaveBalance> result = managerService.getTeamLeaveBalances(1);

        assertNotNull(result);
    }

    // 🔹 TEAM GOALS
    @Test
    void testGetTeamGoalsSuccess() {
        when(goalDAO.getGoalsByManager(1)).thenReturn(new ArrayList<>());

        List<Goal> result = managerService.getTeamGoals(1);

        assertNotNull(result);
    }

    // 🔹 NOTIFICATIONS
    @Test
    void testGetNotificationsSuccess() {
        when(notificationDAO.getNotifications(1)).thenReturn(new ArrayList<>());

        List<Notification> result = managerService.getNotifications(1);

        assertNotNull(result);
    }

    @Test
    void testGetUnreadCount() {
        when(notificationDAO.getUnreadCount(1)).thenReturn(5);

        int count = managerService.getUnreadCount(1);

        assertEquals(5, count);
    }

    @Test
    void testMarkNotificationsRead() {
        managerService.markNotificationsRead(1);

        verify(notificationDAO).markAllAsRead(1);
    }

    // 🔹 GIVE RATING
    @Test
    void testGiveRatingSuccess() {
        managerService.giveRating(1, 4);

        verify(performanceDAO).updateRating(1, 4);
    }

    @Test
    void testGiveRatingInvalid() {
        assertThrows(RuntimeException.class,
                () -> managerService.giveRating(0, -1));
    }
}