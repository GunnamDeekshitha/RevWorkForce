package com.revworkforce.service;

import com.revworkforce.dao.*;
import com.revworkforce.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {

    private EmployeeDAO employeeDAO;
    private LeaveDAO leaveDAO;
    private LeaveBalanceDAO leaveBalanceDAO;
    private HolidayDAO holidayDAO;
    private PerformanceDAO performanceDAO;
    private GoalDAO goalDAO;
    private AnnouncementDAO announcementDAO;
    private NotificationDAO notificationDAO;

    private EmployeeService employeeService;

    @BeforeEach
    void setup() {
        employeeDAO = mock(EmployeeDAO.class);
        leaveDAO = mock(LeaveDAO.class);
        leaveBalanceDAO = mock(LeaveBalanceDAO.class);
        holidayDAO = mock(HolidayDAO.class);
        performanceDAO = mock(PerformanceDAO.class);
        goalDAO = mock(GoalDAO.class);
        announcementDAO = mock(AnnouncementDAO.class);
        notificationDAO = mock(NotificationDAO.class);

        employeeService = new EmployeeService(
                employeeDAO,
                leaveDAO,
                leaveBalanceDAO,
                holidayDAO,
                performanceDAO,
                goalDAO,
                announcementDAO,
                notificationDAO
        );
    }

    // 🔹 PROFILE
    @Test
    void testGetProfileSuccess() {
        Employee emp = new Employee();
        when(employeeDAO.getEmployeeById(1)).thenReturn(emp);

        Employee result = employeeService.getProfile(1);

        assertNotNull(result);
    }

    @Test
    void testGetProfileInvalid() {
        assertThrows(RuntimeException.class,
                () -> employeeService.getProfile(0));
    }

    // 🔹 UPDATE PROFILE
    @Test
    void testUpdateProfileSuccess() {
        Employee emp = new Employee();
        when(employeeDAO.getEmployeeById(1)).thenReturn(emp);

        employeeService.updateProfile(1, "123", "addr", "name", "999");

        verify(employeeDAO).updateEmployeeProfile(eq(1), any(), any(), any(), any());
    }

    @Test
    void testUpdateProfileInvalid() {
        assertThrows(RuntimeException.class,
                () -> employeeService.updateProfile(0, "", "", "", ""));
    }

    // 🔹 APPLY LEAVE
    @Test
    void testApplyLeaveSuccess() {
        Employee emp = new Employee();
        emp.setEmployeeId(1);
        emp.setManagerId(2);
        emp.setName("Test");

        LeaveRequest leave = new LeaveRequest();
        leave.setStartDate(new Date(System.currentTimeMillis()));
        leave.setEndDate(new Date(System.currentTimeMillis()));
        leave.setLeaveType("CL");

        when(leaveBalanceDAO.hasEnoughLeave(anyInt(), anyString(), anyInt()))
                .thenReturn(true);

        String result = employeeService.applyLeave(emp, leave);

        assertEquals("Leave applied successfully!", result);
        verify(leaveDAO).applyLeave(leave);
        verify(notificationDAO).addNotification(any(Notification.class));
    }

    @Test
    void testApplyLeaveInsufficient() {
        Employee emp = new Employee();
        emp.setEmployeeId(1);

        LeaveRequest leave = new LeaveRequest();
        leave.setStartDate(new Date(System.currentTimeMillis()));
        leave.setEndDate(new Date(System.currentTimeMillis()));
        leave.setLeaveType("CL");

        when(leaveBalanceDAO.hasEnoughLeave(anyInt(), anyString(), anyInt()))
                .thenReturn(false);

        String result = employeeService.applyLeave(emp, leave);

        assertEquals("Not enough leave balance!", result);
    }

    @Test
    void testApplyLeaveInvalid() {
        assertThrows(RuntimeException.class,
                () -> employeeService.applyLeave(null, null));
    }

    // 🔹 CANCEL LEAVE
    @Test
    void testCancelLeaveSuccess() {
        LeaveRequest leave = new LeaveRequest();
        leave.setEmployeeId(1);
        leave.setStatus("PENDING");
        leave.setStartDate(new Date(System.currentTimeMillis()));
        leave.setEndDate(new Date(System.currentTimeMillis()));
        leave.setLeaveType("CL");

        when(leaveDAO.getLeaveById(1)).thenReturn(leave);

        String result = employeeService.cancelLeave(1, 1);

        assertEquals("Leave cancelled!", result);
        verify(leaveDAO).cancelLeave(1);
    }

    @Test
    void testCancelLeaveInvalid() {
        assertThrows(RuntimeException.class,
                () -> employeeService.cancelLeave(0, 0));
    }

    // 🔹 HOLIDAYS
    @Test
    void testGetHolidays() {
        when(holidayDAO.getAllHolidays()).thenReturn(new ArrayList<>());

        List<Holiday> list = employeeService.getHolidays();

        assertNotNull(list);
    }

    // 🔹 PERFORMANCE
    @Test
    void testSubmitReviewSuccess() {
        PerformanceReview review = new PerformanceReview();

        employeeService.submitReview(review);

        verify(performanceDAO).addReview(review);
    }

    @Test
    void testSubmitReviewInvalid() {
        assertThrows(RuntimeException.class,
                () -> employeeService.submitReview(null));
    }

    // 🔹 GOALS
    @Test
    void testAddGoalSuccess() {
        Goal goal = new Goal();

        employeeService.addGoal(goal);

        verify(goalDAO).addGoal(goal);
    }

    @Test
    void testAddGoalInvalid() {
        assertThrows(RuntimeException.class,
                () -> employeeService.addGoal(null));
    }

    @Test
    void testUpdateGoalStatus() {
        employeeService.updateGoalStatus(1, "DONE");

        verify(goalDAO).updateGoalStatus(1, "DONE");
    }

    @Test
    void testUpdateGoalStatusInvalid() {
        assertThrows(RuntimeException.class,
                () -> employeeService.updateGoalStatus(0, null));
    }

    // 🔹 NOTIFICATIONS
    @Test
    void testGetNotifications() {
        when(notificationDAO.getNotifications(1)).thenReturn(new ArrayList<>());

        List<Notification> list = employeeService.getNotifications(1);

        assertNotNull(list);
    }

    @Test
    void testGetUnreadCount() {
        when(notificationDAO.getUnreadCount(1)).thenReturn(3);

        int count = employeeService.getUnreadCount(1);

        assertEquals(3, count);
    }

    @Test
    void testMarkNotificationsRead() {
        employeeService.markNotificationsRead(1);

        verify(notificationDAO).markAllAsRead(1);
    }
}