package com.revworkforce.service;

import com.revworkforce.dao.*;
import com.revworkforce.exception.*;
import com.revworkforce.model.*;

import java.util.List;
import java.util.logging.Logger;

public class ManagerService {

    private static final Logger logger = Logger.getLogger(ManagerService.class.getName());

    private static final List<String> VALID_LEAVE_STATUSES = List.of("APPROVED", "REJECTED");

    private final LeaveDAO        leaveDAO;
    private final PerformanceDAO  performanceDAO;
    private final EmployeeDAO     employeeDAO;
    private final LeaveBalanceDAO leaveBalanceDAO;
    private final GoalDAO         goalDAO;
    private final NotificationDAO notificationDAO;

    public ManagerService(LeaveDAO leaveDAO, PerformanceDAO performanceDAO,
                          EmployeeDAO employeeDAO, LeaveBalanceDAO leaveBalanceDAO,
                          GoalDAO goalDAO, NotificationDAO notificationDAO) {
        this.leaveDAO        = leaveDAO;
        this.performanceDAO  = performanceDAO;
        this.employeeDAO     = employeeDAO;
        this.leaveBalanceDAO = leaveBalanceDAO;
        this.goalDAO         = goalDAO;
        this.notificationDAO = notificationDAO;
    }

    public ManagerService() {
        this(new LeaveDAO(), new PerformanceDAO(), new EmployeeDAO(),
                new LeaveBalanceDAO(), new GoalDAO(), new NotificationDAO());
    }

    public List<LeaveRequest> getLeaveRequests(int managerId) {
        logger.info("Fetching leave requests for manager ID: " + managerId);
        if (managerId <= 0) throw new InvalidInputException("Manager ID must be a positive number");
        try {
            return leaveDAO.getLeavesByManager(managerId);
        } catch (DatabaseException e) {
            logger.severe("DB error fetching leave requests: " + e.getMessage());
            throw e;
        }
    }
    public void updateLeaveStatus(int leaveId, String status, String comment) {
        logger.info("Updating leave status for leave ID: " + leaveId);

        if (leaveId <= 0) throw new InvalidInputException("Leave ID must be a positive number");
        if (status == null || status.trim().isEmpty()) throw new InvalidInputException("Status cannot be empty");
        if (!VALID_LEAVE_STATUSES.contains(status.toUpperCase()))
            throw new InvalidInputException("Leave status must be one of: " + VALID_LEAVE_STATUSES);

        try {
            LeaveRequest leave = leaveDAO.getLeaveById(leaveId);
            if (leave == null) throw new LeaveNotFoundException(leaveId);

            leaveDAO.updateLeaveStatus(leaveId, status.toUpperCase(), comment);

            Notification n = new Notification();
            n.setEmployeeId(leave.getEmployeeId());
            n.setMessage("Your leave request has been " + status.toUpperCase());
            notificationDAO.addNotification(n);

            logger.info("Leave status updated to: " + status);
        } catch (LeaveNotFoundException e) {
            throw e;
        } catch (DatabaseException e) {
            logger.severe("DB error updating leave status: " + e.getMessage());
            throw e;
        }
    }
    public List<PerformanceReview> getTeamReviews(int managerId) {
        logger.info("Fetching team reviews for manager ID: " + managerId);
        if (managerId <= 0) throw new InvalidInputException("Manager ID must be a positive number");
        try {
            return performanceDAO.getReviewsByManager(managerId);
        } catch (DatabaseException e) {
            logger.severe("DB error fetching team reviews: " + e.getMessage());
            throw e;
        }
    }

    public void giveFeedback(int reviewId, String feedback) {
        logger.info("Adding feedback for review ID: " + reviewId);

        if (reviewId <= 0) throw new InvalidInputException("Review ID must be a positive number");
        if (feedback == null || feedback.trim().isEmpty())
            throw new InvalidInputException("Feedback cannot be null or empty");

        try {
            PerformanceReview review = performanceDAO.getReviewById(reviewId);
            if (review == null) throw new InvalidInputException("Performance review not found with ID: " + reviewId);

            performanceDAO.addManagerFeedback(reviewId, feedback.trim());

            Notification n = new Notification();
            n.setEmployeeId(review.getEmployeeId());
            n.setMessage("Manager added feedback to your performance review");
            notificationDAO.addNotification(n);

            logger.info("Feedback added successfully");
        } catch (InvalidInputException e) {
            throw e;
        } catch (DatabaseException e) {
            logger.severe("DB error giving feedback: " + e.getMessage());
            throw e;
        }
    }

    public void giveRating(int reviewId, int rating) {
        logger.info("Giving rating for review ID: " + reviewId);

        if (reviewId <= 0) throw new InvalidInputException("Review ID must be a positive number");
        if (rating < 1 || rating > 5) throw new InvalidInputException("Rating must be between 1 and 5");

        try {
            PerformanceReview review = performanceDAO.getReviewById(reviewId);
            if (review == null) throw new InvalidInputException("Performance review not found with ID: " + reviewId);

            performanceDAO.updateRating(reviewId, rating);
            logger.info("Rating updated to: " + rating);
        } catch (InvalidInputException e) {
            throw e;
        } catch (DatabaseException e) {
            logger.severe("DB error giving rating: " + e.getMessage());
            throw e;
        }
    }

    public List<Employee> getTeamMembers(int managerId) {
        logger.info("Fetching team members for manager ID: " + managerId);
        if (managerId <= 0) throw new InvalidInputException("Manager ID must be a positive number");
        try {
            return employeeDAO.getEmployeesByManager(managerId);
        } catch (DatabaseException e) {
            logger.severe("DB error fetching team members: " + e.getMessage());
            throw e;
        }
    }

    public List<LeaveBalance> getTeamLeaveBalances(int managerId) {
        logger.info("Fetching leave balances for manager ID: " + managerId);
        if (managerId <= 0) throw new InvalidInputException("Manager ID must be a positive number");
        try {
            return leaveBalanceDAO.getTeamLeaveBalances(managerId);
        } catch (DatabaseException e) {
            logger.severe("DB error fetching team leave balances: " + e.getMessage());
            throw e;
        }
    }

    public List<Goal> getTeamGoals(int managerId) {
        logger.info("Fetching team goals for manager ID: " + managerId);
        if (managerId <= 0) throw new InvalidInputException("Manager ID must be a positive number");
        try {
            return goalDAO.getGoalsByManager(managerId);
        } catch (DatabaseException e) {
            logger.severe("DB error fetching team goals: " + e.getMessage());
            throw e;
        }
    }


    public List<Notification> getNotifications(int empId) {
        logger.info("Fetching notifications for employee ID: " + empId);
        if (empId <= 0) throw new InvalidInputException("Employee ID must be a positive number");
        try {
            return notificationDAO.getNotifications(empId);
        } catch (DatabaseException e) {
            logger.severe("DB error fetching notifications: " + e.getMessage());
            throw e;
        }
    }

    public int getUnreadCount(int empId) {
        logger.info("Fetching unread count for employee ID: " + empId);
        if (empId <= 0) throw new InvalidInputException("Employee ID must be a positive number");
        try {
            return notificationDAO.getUnreadCount(empId);
        } catch (DatabaseException e) {
            logger.severe("DB error fetching unread count: " + e.getMessage());
            throw e;
        }
    }

    public void markNotificationsRead(int empId) {
        logger.info("Marking notifications as read for employee ID: " + empId);
        if (empId <= 0) throw new InvalidInputException("Employee ID must be a positive number");
        try {
            notificationDAO.markAllAsRead(empId);
            logger.info("Notifications marked as read");
        } catch (DatabaseException e) {
            logger.severe("DB error marking notifications read: " + e.getMessage());
            throw e;
        }
    }
}
