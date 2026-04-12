package com.revworkforce.service;

import com.revworkforce.dao.*;
import com.revworkforce.model.*;
import java.util.List;
import java.util.logging.Logger;

public class ManagerService {
    private static final Logger logger = Logger.getLogger(ManagerService.class.getName());
    private LeaveDAO leaveDAO;
    private PerformanceDAO performanceDAO;
    private EmployeeDAO employeeDAO;
    private LeaveBalanceDAO leaveBalanceDAO;
    private GoalDAO goalDAO;
    private NotificationDAO notificationDAO;

    public ManagerService(LeaveDAO leaveDAO,
                          PerformanceDAO performanceDAO,
                          EmployeeDAO employeeDAO,
                          LeaveBalanceDAO leaveBalanceDAO,
                          GoalDAO goalDAO,
                          NotificationDAO notificationDAO) {

        this.leaveDAO = leaveDAO;
        this.performanceDAO = performanceDAO;
        this.employeeDAO = employeeDAO;
        this.leaveBalanceDAO = leaveBalanceDAO;
        this.goalDAO = goalDAO;
        this.notificationDAO = notificationDAO;
    }
    public ManagerService() {
        this.leaveDAO = new LeaveDAO();
        this.performanceDAO = new PerformanceDAO();
        this.employeeDAO = new EmployeeDAO();
        this.leaveBalanceDAO = new LeaveBalanceDAO();
        this.goalDAO = new GoalDAO();
        this.notificationDAO = new NotificationDAO();
    }

    public List<LeaveRequest> getLeaveRequests(int managerId) {
        logger.info("Fetching leave requests for manager ID: " + managerId);
        if (managerId <= 0) {
            logger.warning("Invalid manager ID");
            throw new RuntimeException("Invalid manager ID");
        }
        return leaveDAO.getLeavesByManager(managerId);
    }

    public void updateLeaveStatus(int leaveId, String status, String comment) {
        logger.info("Updating leave status for leave ID: " + leaveId);
        if (leaveId <= 0 || status == null) {
            logger.warning("Invalid input for leave status update");
            throw new RuntimeException("Invalid input");
        }
        LeaveRequest leave = leaveDAO.getLeaveById(leaveId);
        leaveDAO.updateLeaveStatus(leaveId, status, comment);
        if (leave != null) {
            Notification n = new Notification();
            n.setEmployeeId(leave.getEmployeeId());
            n.setMessage("Your leave has been " + status);
            notificationDAO.addNotification(n);
        }
        logger.info("Leave status updated successfully");
    }

    public List<PerformanceReview> getTeamReviews(int managerId) {
        logger.info("Fetching team reviews for manager ID: " + managerId);
        if (managerId <= 0) {
            logger.warning("Invalid manager ID");
            throw new RuntimeException("Invalid manager ID");
        }
        return performanceDAO.getReviewsByManager(managerId);
    }

    public void giveFeedback(int reviewId, String feedback) {
        logger.info("Adding feedback for review ID: " + reviewId);
        if (reviewId <= 0 || feedback == null) {
            logger.warning("Invalid input for feedback");
            throw new RuntimeException("Invalid input");
        }
        PerformanceReview review = performanceDAO.getReviewById(reviewId);
        performanceDAO.addManagerFeedback(reviewId, feedback);
        if (review != null) {
            Notification n = new Notification();
            n.setEmployeeId(review.getEmployeeId());
            n.setMessage("Manager added feedback to your performance review");
            notificationDAO.addNotification(n);
        }
        logger.info("Feedback added successfully");
    }

    public List<Employee> getTeamMembers(int managerId) {
        logger.info("Fetching team members for manager ID: " + managerId);
        if (managerId <= 0) {
            logger.warning("Invalid manager ID");
            throw new RuntimeException("Invalid manager ID");
        }
        return employeeDAO.getEmployeesByManager(managerId);
    }

    public List<LeaveBalance> getTeamLeaveBalances(int managerId) {
        logger.info("Fetching leave balances for manager ID: " + managerId);
        if (managerId <= 0) {
            logger.warning("Invalid manager ID");
            throw new RuntimeException("Invalid manager ID");
        }
        return leaveBalanceDAO.getTeamLeaveBalances(managerId);
    }

    public List<Goal> getTeamGoals(int managerId) {
        logger.info("Fetching team goals for manager ID: " + managerId);
        if (managerId <= 0) {
            logger.warning("Invalid manager ID");
            throw new RuntimeException("Invalid manager ID");
        }
        return goalDAO.getGoalsByManager(managerId);
    }

    public List<Notification> getNotifications(int empId) {
        logger.info("Fetching notifications for employee ID: " + empId);
        if (empId <= 0) {
            logger.warning("Invalid employee ID");
            throw new RuntimeException("Invalid employee ID");
        }
        return notificationDAO.getNotifications(empId);
    }

    public int getUnreadCount(int empId) {
        logger.info("Fetching unread count for employee ID: " + empId);
        if (empId <= 0) {
            logger.warning("Invalid employee ID");
            throw new RuntimeException("Invalid employee ID");
        }
        return notificationDAO.getUnreadCount(empId);
    }

    public void markNotificationsRead(int empId) {
        logger.info("Marking notifications as read for employee ID: " + empId);
        if (empId <= 0) {
            logger.warning("Invalid employee ID");
            throw new RuntimeException("Invalid employee ID");
        }
        notificationDAO.markAllAsRead(empId);
        logger.info("Notifications marked as read");
    }

    public void giveRating(int reviewId, int rating) {
        logger.info("Giving rating for review ID: " + reviewId);
        if (reviewId <= 0 || rating < 0) {
            logger.warning("Invalid input for rating");
            throw new RuntimeException("Invalid input");
        }
        performanceDAO.updateRating(reviewId, rating);
        logger.info("Rating updated successfully");
    }
}