//package com.revworkforce.service;
//
//import com.revworkforce.model.*;
//import org.junit.jupiter.api.*;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class ManagerServiceTest {
//
//    private static ManagerService service;
//
//    @BeforeAll
//    static void setup() {
//        service = new ManagerService(); // REAL service
//        System.out.println("=== Starting Manager Service Tests ===");
//    }
//
//    @Test
//    @Order(1)
//    void testGetLeaveRequests() {
//        List<LeaveRequest> list = service.getLeaveRequests(1); // managerId must exist
//        assertNotNull(list);
//    }
//
//
//    @Test
//    @Order(2)
//    void testGetLeaveRequestsInvalid() {
//        assertThrows(RuntimeException.class,
//                () -> service.getLeaveRequests(0));
//    }
//
//
//    @Test
//    @Order(3)
//    void testUpdateLeaveStatus() {
//        service.updateLeaveStatus(1, "APPROVED", "ok");
//        assertTrue(true);
//    }
//
//
//    @Test
//    @Order(4)
//    void testUpdateLeaveStatusInvalid() {
//        assertThrows(RuntimeException.class,
//                () -> service.updateLeaveStatus(0, null, ""));
//    }
//
//
//    @Test
//    @Order(5)
//    void testGetTeamReviews() {
//        List<PerformanceReview> list = service.getTeamReviews(1);
//        assertNotNull(list);
//    }
//
//
//    @Test
//    @Order(6)
//    void testGiveFeedback() {
//        service.giveFeedback(1, "Good job");
//        assertTrue(true);
//    }
//
//    @Test
//    @Order(7)
//    void testGiveFeedbackInvalid() {
//        assertThrows(RuntimeException.class,
//                () -> service.giveFeedback(0, null));
//    }
//
//    @Test
//    @Order(8)
//    void testGetTeamMembers() {
//        List<Employee> list = service.getTeamMembers(1);
//        assertNotNull(list);
//    }
//
//    @Test
//    @Order(9)
//    void testGetTeamLeaveBalances() {
//        List<LeaveBalance> list = service.getTeamLeaveBalances(1);
//        assertNotNull(list);
//    }
//
//    @Test
//    @Order(10)
//    void testGetTeamGoals() {
//        List<Goal> list = service.getTeamGoals(1);
//        assertNotNull(list);
//    }
//
//    @Test
//    @Order(11)
//    void testGetNotifications() {
//        List<Notification> list = service.getNotifications(1);
//        assertNotNull(list);
//    }
//
//    @Test
//    @Order(12)
//    void testGetUnreadCount() {
//        int count = service.getUnreadCount(1);
//        assertTrue(count >= 0);
//    }
//
//
//    @Test
//    @Order(13)
//    void testMarkNotificationsRead() {
//        service.markNotificationsRead(1);
//        assertTrue(true);
//    }
//
//
//    @Test
//    @Order(14)
//    void testGiveRating() {
//        service.giveRating(1, 4);
//        assertTrue(true);
//    }
//
//
//    @Test
//    @Order(15)
//    void testGiveRatingInvalid() {
//        assertThrows(RuntimeException.class,
//                () -> service.giveRating(0, -1));
//    }
//
//    @AfterAll
//    static void end() {
//        System.out.println("=== Finished Manager Service Tests ===");
//    }
//}


package com.revworkforce.service;

import com.revworkforce.dao.*;
import com.revworkforce.exception.*;
import com.revworkforce.model.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
        leaveDAO= Mockito.mock(LeaveDAO.class);
        performanceDAO= Mockito.mock(PerformanceDAO.class);
        employeeDAO = Mockito.mock(EmployeeDAO.class);
        leaveBalanceDAO = Mockito.mock(LeaveBalanceDAO.class);
        goalDAO = Mockito.mock(GoalDAO.class);
        notificationDAO = Mockito.mock(NotificationDAO.class);

        managerService = new ManagerService(
                leaveDAO, performanceDAO, employeeDAO,
                leaveBalanceDAO, goalDAO, notificationDAO
        );
    }

    // getLeaveRequests()

    @Test
    @Order(1)
    void getLeaveRequests_WhenValidManagerId_ShouldReturnLeaveList() {
        when(leaveDAO.getLeavesByManager(1)).thenReturn(List.of());
        List<LeaveRequest> result = managerService.getLeaveRequests(1);
        assertNotNull(result);
    }

    @Test
    @Order(2)
    void getLeaveRequests_WhenInvalidManagerId_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> managerService.getLeaveRequests(0));
    }

    // updateLeaveStatus()

    @Test
    @Order(3)
    void updateLeaveStatus_WhenApprovedStatusProvided_ShouldUpdateSuccessfully() {
        LeaveRequest leave = new LeaveRequest();
        leave.setLeaveId(1);
        leave.setEmployeeId(3);
        when(leaveDAO.getLeaveById(1)).thenReturn(leave);

        assertDoesNotThrow(() -> managerService.updateLeaveStatus(1, "APPROVED", "Looks good"));
        verify(leaveDAO).updateLeaveStatus(1, "APPROVED", "Looks good");
        verify(notificationDAO).addNotification(any(Notification.class));
    }

    @Test
    @Order(4)
    void updateLeaveStatus_WhenRejectedStatusProvided_ShouldUpdateSuccessfully() {
        LeaveRequest leave = new LeaveRequest();
        leave.setLeaveId(2);
        leave.setEmployeeId(4);
        when(leaveDAO.getLeaveById(2)).thenReturn(leave);

        assertDoesNotThrow(() -> managerService.updateLeaveStatus(2, "REJECTED", "No reason"));
        verify(leaveDAO).updateLeaveStatus(2, "REJECTED", "No reason");
    }

    @Test
    @Order(5)
    void updateLeaveStatus_WhenInvalidLeaveId_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> managerService.updateLeaveStatus(0, "APPROVED", "ok"));
    }

    @Test
    @Order(6)
    void updateLeaveStatus_WhenStatusIsNull_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> managerService.updateLeaveStatus(1, null, "ok"));
    }

    @Test
    @Order(7)
    void updateLeaveStatus_WhenStatusIsInvalid_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> managerService.updateLeaveStatus(1, "PENDING", "ok"));
    }

    @Test
    @Order(8)
    void updateLeaveStatus_WhenLeaveNotFound_ShouldThrowLeaveNotFoundException() {
        when(leaveDAO.getLeaveById(999)).thenReturn(null);
        assertThrows(LeaveNotFoundException.class, () -> managerService.updateLeaveStatus(999, "APPROVED", "ok"));
    }

    // giveFeedback()

    @Test
    @Order(9)
    void giveFeedback_WhenValidFeedbackProvided_ShouldSaveSuccessfully() {
        PerformanceReview review = new PerformanceReview();
        review.setReviewId(1);
        review.setEmployeeId(3);
        when(performanceDAO.getReviewById(1)).thenReturn(review);

        assertDoesNotThrow(() -> managerService.giveFeedback(1, "Great work!"));
        verify(performanceDAO).addManagerFeedback(1, "Great work!");
        verify(notificationDAO).addNotification(any(Notification.class));
    }

    @Test
    @Order(10)
    void giveFeedback_WhenReviewIdIsInvalid_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> managerService.giveFeedback(0, "Good job"));
    }

    @Test
    @Order(11)
    void giveFeedback_WhenFeedbackIsNull_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> managerService.giveFeedback(1, null));
    }

    @Test
    @Order(12)
    void giveFeedback_WhenFeedbackIsEmpty_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> managerService.giveFeedback(1, ""));
    }

    // giveRating()

    @Test
    @Order(13)
    void giveRating_WhenValidRatingProvided_ShouldUpdateSuccessfully() {
        PerformanceReview review = new PerformanceReview();
        review.setReviewId(1);
        when(performanceDAO.getReviewById(1)).thenReturn(review);

        assertDoesNotThrow(() -> managerService.giveRating(1, 4));
        verify(performanceDAO).updateRating(1, 4);
    }

    @Test
    @Order(14)
    void giveRating_WhenRatingIsZero_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> managerService.giveRating(1, 0));
    }

    @Test
    @Order(15)
    void giveRating_WhenRatingExceedsMaximum_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> managerService.giveRating(1, 6));
    }

    @Test
    @Order(16)
    void giveRating_WhenReviewIdIsInvalid_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> managerService.giveRating(0, 4));
    }

    // getTeamMembers()

    @Test
    @Order(17)
    void getTeamMembers_WhenValidManagerId_ShouldReturnEmployeeList() {
        when(employeeDAO.getEmployeesByManager(1)).thenReturn(List.of());
        assertNotNull(managerService.getTeamMembers(1));
    }

    @Test
    @Order(18)
    void getTeamMembers_WhenInvalidManagerId_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> managerService.getTeamMembers(0));
    }

    // getTeamLeaveBalances()

    @Test
    @Order(19)
    void getTeamLeaveBalances_WhenValidManagerId_ShouldReturnLeaveBalanceList() {
        when(leaveBalanceDAO.getTeamLeaveBalances(1)).thenReturn(List.of());
        assertNotNull(managerService.getTeamLeaveBalances(1));
    }

    @Test
    @Order(20)
    void getTeamLeaveBalances_WhenInvalidManagerId_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> managerService.getTeamLeaveBalances(0));
    }
    // getTeamGoals()

    @Test
    @Order(21)
    void getTeamGoals_WhenValidManagerId_ShouldReturnGoalList() {
        when(goalDAO.getGoalsByManager(1)).thenReturn(List.of());
        assertNotNull(managerService.getTeamGoals(1));
    }

    @Test
    @Order(22)
    void getTeamGoals_WhenInvalidManagerId_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> managerService.getTeamGoals(0));
    }

    // getNotifications() / getUnreadCount() / markNotificationsRead()

    @Test
    @Order(23)
    void getNotifications_WhenValidId_ShouldReturnNotificationList() {
        when(notificationDAO.getNotifications(1)).thenReturn(List.of());
        assertNotNull(managerService.getNotifications(1));
    }

    @Test
    @Order(24)
    void getNotifications_WhenInvalidId_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> managerService.getNotifications(0));
    }

    @Test
    @Order(25)
    void getUnreadCount_WhenValidId_ShouldReturnCount() {
        when(notificationDAO.getUnreadCount(1)).thenReturn(5);
        assertEquals(5, managerService.getUnreadCount(1));
    }

    @Test
    @Order(26)
    void markNotificationsRead_WhenValidId_ShouldMarkSuccessfully() {
        assertDoesNotThrow(() -> managerService.markNotificationsRead(1));
        verify(notificationDAO).markAllAsRead(1);
    }
}
