package com.revworkforce.service;

import com.revworkforce.dao.*;
import com.revworkforce.model.*;
import java.util.List;

public class ManagerService {

    private LeaveDAO leaveDAO = new LeaveDAO();
    private PerformanceDAO performanceDAO = new PerformanceDAO();
    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private LeaveBalanceDAO leaveBalanceDAO = new LeaveBalanceDAO();
    private GoalDAO goalDAO = new GoalDAO();
    private NotificationDAO notificationDAO = new NotificationDAO();


    public List<LeaveRequest> getLeaveRequests(int managerId) {
        return leaveDAO.getLeavesByManager(managerId);
    }

    public void updateLeaveStatus(int leaveId, String status, String comment) {

        LeaveRequest leave = leaveDAO.getLeaveById(leaveId);

        leaveDAO.updateLeaveStatus(leaveId, status, comment);

        if (leave != null) {
            Notification n = new Notification();
            n.setEmployeeId(leave.getEmployeeId());
            n.setMessage("Your leave has been " + status);

            notificationDAO.addNotification(n);
        }
    }
    public List<PerformanceReview> getTeamReviews(int managerId) {
        return performanceDAO.getReviewsByManager(managerId);
    }

    public void giveFeedback(int reviewId, String feedback) {

        PerformanceReview review = performanceDAO.getReviewById(reviewId);

        performanceDAO.addManagerFeedback(reviewId, feedback);

        if (review != null) {
            Notification n = new Notification();
            n.setEmployeeId(review.getEmployeeId());
            n.setMessage("Manager added feedback to your performance review");

            notificationDAO.addNotification(n);
        }
    }
    public List<Employee> getTeamMembers(int managerId) {
        return employeeDAO.getEmployeesByManager(managerId);
    }
    public List<LeaveBalance> getTeamLeaveBalances(int managerId) {
        return leaveBalanceDAO.getTeamLeaveBalances(managerId);
    }
    public List<Goal> getTeamGoals(int managerId) {
        return goalDAO.getGoalsByManager(managerId);
    }
    public List<Notification> getNotifications(int empId) {
        return notificationDAO.getNotifications(empId);
    }

    public int getUnreadCount(int empId) {
        return notificationDAO.getUnreadCount(empId);
    }

    public void markNotificationsRead(int empId) {
        notificationDAO.markAllAsRead(empId);
    }
    public void giveRating(int reviewId, int rating) {
        performanceDAO.updateRating(reviewId, rating);
    }
}