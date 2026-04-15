package com.revworkforce.service;

import com.revworkforce.dao.*;
import com.revworkforce.exception.*;
import com.revworkforce.model.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
        employeeDAO = Mockito.mock(EmployeeDAO.class);
        leaveDAO = Mockito.mock(LeaveDAO.class);
        leaveBalanceDAO = Mockito.mock(LeaveBalanceDAO.class);
        holidayDAO = Mockito.mock(HolidayDAO.class);
        performanceDAO  = Mockito.mock(PerformanceDAO.class);
        goalDAO = Mockito.mock(GoalDAO.class);
        announcementDAO = Mockito.mock(AnnouncementDAO.class);
        notificationDAO = Mockito.mock(NotificationDAO.class);

        employeeService = new EmployeeService(
                employeeDAO, leaveDAO, leaveBalanceDAO, holidayDAO,
                performanceDAO, goalDAO, announcementDAO, notificationDAO
        );
    }


    // getProfile()

    @Test
    @Order(1)
    void getProfile_WhenValidId_ShouldReturnEmployee() {
        Employee emp = new Employee(); emp.setEmployeeId(6);
        when(employeeDAO.getEmployeeById(6)).thenReturn(emp);

        Employee result = employeeService.getProfile(6);

        assertNotNull(result);
        assertEquals(6, result.getEmployeeId());
    }

    @Test
    @Order(2)
    void getProfile_WhenInvalidId_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> employeeService.getProfile(0));
    }

    @Test
    @Order(3)
    void getProfile_WhenEmployeeNotFound_ShouldThrowEmployeeNotFoundException() {
        when(employeeDAO.getEmployeeById(999)).thenReturn(null);
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getProfile(999));
    }


    // updateProfile()

    @Test
    @Order(4)
    void updateProfile_WhenValidDataProvided_ShouldUpdateSuccessfully() {
        Employee emp = new Employee();
        emp.setEmployeeId(1);
        when(employeeDAO.getEmployeeById(1)).thenReturn(emp);

        assertDoesNotThrow(() ->
                employeeService.updateProfile(1, "9876543210", "New Address", "Emergency", "9999999999"));

        verify(employeeDAO).updateEmployeeProfile(eq(1), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    @Order(5)
    void updateProfile_WhenPhoneIsInvalid_ShouldThrowInvalidInputException() {
        Employee emp = new Employee(); emp.setEmployeeId(1);
        when(employeeDAO.getEmployeeById(1)).thenReturn(emp);

        assertThrows(InvalidInputException.class, () ->
                employeeService.updateProfile(1, "123", "Address", "Emergency", "9999999999"));
    }

    @Test
    @Order(6)
    void updateProfile_WhenEmployeeNotFound_ShouldThrowEmployeeNotFoundException() {
        when(employeeDAO.getEmployeeById(99)).thenReturn(null);
        assertThrows(EmployeeNotFoundException.class, () ->
                employeeService.updateProfile(99, "9876543210", "Addr", "Name", "9999999999"));
    }

    // applyLeave()

    @Test
    @Order(7)
    void applyLeave_WhenValidLeaveProvided_ShouldApplySuccessfully() {
        Employee emp = buildEmployee(1, 2);
        LeaveRequest leave = buildLeave(1, "CL", "2026-05-01", "2026-05-02");

        when(leaveBalanceDAO.hasEnoughLeave(1, "CL", 2)).thenReturn(true);

        String result = employeeService.applyLeave(emp, leave);

        assertEquals("Leave applied successfully!", result);
        verify(leaveDAO).applyLeave(leave);
    }

    @Test
    @Order(8)
    void applyLeave_WhenInsufficientBalance_ShouldThrowInsufficientLeaveBalanceException() {
        Employee emp = buildEmployee(1, 2);
        LeaveRequest leave = buildLeave(1, "CL", "2026-05-01", "2026-05-10");

        when(leaveBalanceDAO.hasEnoughLeave(1, "CL", 10)).thenReturn(false);

        assertThrows(InsufficientLeaveBalanceException.class, () -> employeeService.applyLeave(emp, leave));
    }

    @Test
    @Order(9)
    void applyLeave_WhenStartDateIsInPast_ShouldThrowInvalidLeaveDateException() {
        Employee emp = buildEmployee(1, 2);
        LeaveRequest leave = buildLeave(1, "CL", "2024-01-01", "2024-01-02");

        assertThrows(InvalidLeaveDateException.class, () -> employeeService.applyLeave(emp, leave));
    }

    @Test
    @Order(10)
    void applyLeave_WhenEndDateBeforeStartDate_ShouldThrowInvalidLeaveDateException() {
        Employee emp = buildEmployee(1, 2);
        LeaveRequest leave = buildLeave(1, "CL", "2026-05-10", "2026-05-05");

        assertThrows(InvalidLeaveDateException.class, () -> employeeService.applyLeave(emp, leave));
    }

    @Test
    @Order(11)
    void applyLeave_WhenLeaveTypeIsInvalid_ShouldThrowInvalidInputException() {
        Employee emp = buildEmployee(1, 2);
        LeaveRequest leave = buildLeave(1, "XX", "2026-05-01", "2026-05-02");

        assertThrows(InvalidInputException.class, () -> employeeService.applyLeave(emp, leave));
    }

    @Test
    @Order(12)
    void applyLeave_WhenEmployeeIsNull_ShouldThrowInvalidInputException() {
        LeaveRequest leave = buildLeave(1, "CL", "2026-05-01", "2026-05-02");
        assertThrows(InvalidInputException.class, () -> employeeService.applyLeave(null, leave));
    }

    @Test
    @Order(13)
    void applyLeave_WhenLeaveRequestIsNull_ShouldThrowInvalidInputException() {
        Employee emp = buildEmployee(1, 2);
        assertThrows(InvalidInputException.class, () -> employeeService.applyLeave(emp, null));
    }

    // cancelLeave()

    @Test
    @Order(14)
    void cancelLeave_WhenValidPendingLeaveExists_ShouldCancelSuccessfully() {
        LeaveRequest leave = new LeaveRequest();
        leave.setLeaveId(1);
        leave.setEmployeeId(1);
        leave.setLeaveType("CL");
        leave.setStatus("PENDING");
        leave.setStartDate(Date.valueOf("2026-05-01"));
        leave.setEndDate(Date.valueOf("2026-05-02"));

        when(leaveDAO.getLeaveById(1)).thenReturn(leave);

        String result = employeeService.cancelLeave(1, 1);
        assertEquals("Leave cancelled!", result);
        verify(leaveDAO).cancelLeave(1);
    }

    @Test
    @Order(15)
    void cancelLeave_WhenLeaveIsAlreadyApproved_ShouldReturnErrorMessage() {
        LeaveRequest leave = new LeaveRequest();
        leave.setLeaveId(1);
        leave.setEmployeeId(1);
        leave.setStatus("APPROVED");
        leave.setStartDate(Date.valueOf("2026-05-01"));
        leave.setEndDate(Date.valueOf("2026-05-02"));

        when(leaveDAO.getLeaveById(1)).thenReturn(leave);

        String result = employeeService.cancelLeave(1, 1);
        assertEquals("Only PENDING leaves can be cancelled!", result);
    }

    @Test
    @Order(16)
    void cancelLeave_WhenLeaveNotFound_ShouldThrowLeaveNotFoundException() {
        when(leaveDAO.getLeaveById(999)).thenReturn(null);
        assertThrows(LeaveNotFoundException.class, () -> employeeService.cancelLeave(1, 999));
    }

    @Test
    @Order(17)
    void cancelLeave_WhenLeaveDoesNotBelongToEmployee_ShouldThrowUnauthorizedAccessException() {
        LeaveRequest leave = new LeaveRequest();
        leave.setLeaveId(1);
        leave.setEmployeeId(5); // belongs to another employee
        leave.setStatus("PENDING");
        leave.setStartDate(Date.valueOf("2026-05-01"));
        leave.setEndDate(Date.valueOf("2026-05-02"));

        when(leaveDAO.getLeaveById(1)).thenReturn(leave);

        assertThrows(UnauthorizedAccessException.class, () -> employeeService.cancelLeave(1, 1));
    }


    // submitReview()

    @Test
    @Order(18)
    void submitReview_WhenValidReviewProvided_ShouldSaveSuccessfully() {
        PerformanceReview review = buildReview(1, 4);
        assertDoesNotThrow(() -> employeeService.submitReview(review));
        verify(performanceDAO).addReview(review);
    }

    @Test
    @Order(19)
    void submitReview_WhenRatingIsOutOfRange_ShouldThrowInvalidInputException() {
        PerformanceReview review = buildReview(1, 6);
        assertThrows(InvalidInputException.class, () -> employeeService.submitReview(review));
    }

    @Test
    @Order(20)
    void submitReview_WhenRatingIsBelowMinimum_ShouldThrowInvalidInputException() {
        PerformanceReview review = buildReview(1, 0);
        assertThrows(InvalidInputException.class, () -> employeeService.submitReview(review));
    }

    @Test
    @Order(21)
    void submitReview_WhenReviewIsNull_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> employeeService.submitReview(null));
    }


    // addGoal()

    @Test
    @Order(22)
    void addGoal_WhenValidGoalProvided_ShouldSaveSuccessfully() {
        Goal goal = buildGoal(1);
        assertDoesNotThrow(() -> employeeService.addGoal(goal));
        verify(goalDAO).addGoal(goal);
    }

    @Test
    @Order(23)
    void addGoal_WhenGoalIsNull_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> employeeService.addGoal(null));
    }

    @Test
    @Order(24)
    void addGoal_WhenDescriptionIsEmpty_ShouldThrowInvalidInputException() {
        Goal goal = buildGoal(1);
        goal.setDescription("");
        assertThrows(InvalidInputException.class, () -> employeeService.addGoal(goal));
    }

    // updateGoalStatus()
    @Test
    @Order(25)
    void updateGoalStatus_WhenValidStatusProvided_ShouldUpdateSuccessfully() {
        assertDoesNotThrow(() -> employeeService.updateGoalStatus(1, "COMPLETED"));
        verify(goalDAO).updateGoalStatus(1, "COMPLETED");
    }

    @Test
    @Order(26)
    void updateGoalStatus_WhenStatusIsInvalid_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> employeeService.updateGoalStatus(1, "DONE"));
    }

    @Test
    @Order(27)
    void updateGoalStatus_WhenGoalIdIsInvalid_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> employeeService.updateGoalStatus(0, "COMPLETED"));
    }

    // getNotifications() / getUnreadCount() / markNotificationsRead()

    @Test
    @Order(28)
    void getNotifications_WhenValidId_ShouldReturnNotificationList() {
        when(notificationDAO.getNotifications(1)).thenReturn(List.of());
        assertNotNull(employeeService.getNotifications(1));
    }

    @Test
    @Order(29)
    void getNotifications_WhenInvalidId_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> employeeService.getNotifications(0));
    }

    @Test
    @Order(30)
    void getUnreadCount_WhenValidId_ShouldReturnCount() {
        when(notificationDAO.getUnreadCount(1)).thenReturn(3);
        int count = employeeService.getUnreadCount(1);
        assertEquals(3, count);
    }

    @Test
    @Order(31)
    void markNotificationsRead_WhenValidId_ShouldMarkSuccessfully() {
        assertDoesNotThrow(() -> employeeService.markNotificationsRead(1));
        verify(notificationDAO).markAllAsRead(1);
    }

    // Helpers

    private Employee buildEmployee(int empId, int managerId) {
        Employee emp = new Employee();
        emp.setEmployeeId(empId);
        emp.setManagerId(managerId);
        emp.setName("Test Employee");
        return emp;
    }

    private LeaveRequest buildLeave(int empId, String type, String start, String end) {
        LeaveRequest leave = new LeaveRequest();
        leave.setEmployeeId(empId);
        leave.setLeaveType(type);
        leave.setStartDate(Date.valueOf(start));
        leave.setEndDate(Date.valueOf(end));
        leave.setReason("Personal work");
        return leave;
    }

    private PerformanceReview buildReview(int empId, int rating) {
        PerformanceReview r = new PerformanceReview();
        r.setEmployeeId(empId);
        r.setYear(2026);
        r.setSelfAssessment("Good performance");
        r.setAccomplishments("Completed all tasks");
        r.setImprovements("Time management");
        r.setRating(rating);
        return r;
    }

    private Goal buildGoal(int empId) {
        Goal g = new Goal();
        g.setEmployeeId(empId);
        g.setDescription("Complete project module");
        g.setDeadline(Date.valueOf("2026-06-01"));
        g.setPriority("HIGH");
        g.setSuccessMetrics("On-time delivery");
        return g;
    }
}
