package com.revworkforce.service;

import com.revworkforce.dao.*;
import com.revworkforce.model.*;

import java.util.List;
import java.util.logging.Logger;

public class EmployeeService {

    private static final Logger logger = Logger.getLogger(EmployeeService.class.getName());

    private EmployeeDAO employeeDAO;
    private LeaveDAO leaveDAO;
    private LeaveBalanceDAO leaveBalanceDAO;
    private HolidayDAO holidayDAO;
    private PerformanceDAO performanceDAO;
    private GoalDAO goalDAO;
    private AnnouncementDAO announcementDAO;
    private NotificationDAO notificationDAO;

    public EmployeeService(EmployeeDAO employeeDAO,
                           LeaveDAO leaveDAO,
                           LeaveBalanceDAO leaveBalanceDAO,
                           HolidayDAO holidayDAO,
                           PerformanceDAO performanceDAO,
                           GoalDAO goalDAO,
                           AnnouncementDAO announcementDAO,
                           NotificationDAO notificationDAO) {

        this.employeeDAO = employeeDAO;
        this.leaveDAO = leaveDAO;
        this.leaveBalanceDAO = leaveBalanceDAO;
        this.holidayDAO = holidayDAO;
        this.performanceDAO = performanceDAO;
        this.goalDAO = goalDAO;
        this.announcementDAO = announcementDAO;
        this.notificationDAO = notificationDAO;
    }

    public EmployeeService() {
        this.employeeDAO = new EmployeeDAO();
        this.leaveDAO = new LeaveDAO();
        this.leaveBalanceDAO = new LeaveBalanceDAO();
        this.holidayDAO = new HolidayDAO();
        this.performanceDAO = new PerformanceDAO();
        this.goalDAO = new GoalDAO();
        this.announcementDAO = new AnnouncementDAO();
        this.notificationDAO = new NotificationDAO();
    }

    // 🔹 PROFILE
    public Employee getProfile(int empId) {
        logger.info("Fetching profile for employee ID: " + empId);

        if (empId <= 0) {
            logger.warning("Invalid employee ID");
            throw new RuntimeException("Invalid employee ID");
        }

        return employeeDAO.getEmployeeById(empId);
    }

    public void updateProfile(int empId, String phone, String address,
                              String emergencyName, String emergencyPhone) {

        logger.info("Updating profile for employee ID: " + empId);

        if (empId <= 0) {
            logger.warning("Invalid employee ID");
            throw new RuntimeException("Invalid employee ID");
        }

        Employee emp = employeeDAO.getEmployeeById(empId);

        if (emp == null) {
            logger.warning("Employee not found");
            throw new RuntimeException("Employee not found");
        }

        if (phone != null && !phone.isEmpty()) emp.setPhone(phone);
        if (address != null && !address.isEmpty()) emp.setAddress(address);
        if (emergencyName != null && !emergencyName.isEmpty()) emp.setEmergencyContactName(emergencyName);
        if (emergencyPhone != null && !emergencyPhone.isEmpty()) emp.setEmergencyContactPhone(emergencyPhone);

        employeeDAO.updateEmployeeProfile(
                empId,
                emp.getPhone(),
                emp.getAddress(),
                emp.getEmergencyContactName(),
                emp.getEmergencyContactPhone()
        );

        logger.info("Profile updated successfully");
    }

    public Employee getManager(int managerId) {
        logger.info("Fetching manager with ID: " + managerId);

        if (managerId <= 0) {
            logger.warning("Invalid manager ID");
            throw new RuntimeException("Invalid manager ID");
        }

        return employeeDAO.getEmployeeById(managerId);
    }

    public List<Employee> getAllEmployees() {
        logger.info("Fetching all employees");
        return employeeDAO.getAllEmployeeDetails();
    }

    public String applyLeave(Employee emp, LeaveRequest leave) {

        logger.info("Applying leave for employee");

        if (emp == null || leave == null) {
            logger.warning("Employee or leave is null");
            throw new RuntimeException("Invalid input");
        }

        int days = (int) ((leave.getEndDate().getTime() - leave.getStartDate().getTime())
                / (1000 * 60 * 60 * 24)) + 1;

        if (leaveBalanceDAO.hasEnoughLeave(emp.getEmployeeId(), leave.getLeaveType(), days)) {
            leaveDAO.applyLeave(leave);
            leaveBalanceDAO.updateLeaveBalance(emp.getEmployeeId(), leave.getLeaveType(), days);

            Notification n = new Notification();
            n.setEmployeeId(emp.getManagerId());
            n.setMessage("New leave request from " + emp.getName());
            notificationDAO.addNotification(n);

            logger.info("Leave applied successfully");
            return "Leave applied successfully!";
        } else {
            logger.warning("Not enough leave balance");
            return "Not enough leave balance!";
        }
    }

    public List<LeaveRequest> getMyLeaves(int empId) {
        logger.info("Fetching leaves for employee ID: " + empId);

        if (empId <= 0) {
            logger.warning("Invalid employee ID");
            throw new RuntimeException("Invalid employee ID");
        }

        return leaveDAO.getLeavesByEmployeeId(empId);
    }

    public LeaveBalance getLeaveBalance(int empId) {
        logger.info("Fetching leave balance for employee ID: " + empId);

        if (empId <= 0) {
            logger.warning("Invalid employee ID");
            throw new RuntimeException("Invalid employee ID");
        }

        return leaveBalanceDAO.getLeaveBalance(empId);
    }

    public String cancelLeave(int empId, int leaveId) {

        logger.info("Cancelling leave ID: " + leaveId + " for employee ID: " + empId);

        if (empId <= 0 || leaveId <= 0) {
            logger.warning("Invalid input");
            throw new RuntimeException("Invalid input");
        }

        LeaveRequest leave = leaveDAO.getLeaveById(leaveId);

        if (leave != null
                && leave.getEmployeeId() == empId
                && leave.getStatus().equalsIgnoreCase("PENDING")) {

            int days = (int) ((leave.getEndDate().getTime() - leave.getStartDate().getTime())
                    / (1000 * 60 * 60 * 24)) + 1;

            leaveBalanceDAO.restoreLeaveBalance(empId, leave.getLeaveType(), days);
            leaveDAO.cancelLeave(leaveId);

            logger.info("Leave cancelled successfully");
            return "Leave cancelled!";
        }

        logger.warning("Cannot cancel leave");
        return "Cannot cancel leave!";
    }

    // 🔹 HOLIDAYS
    public List<Holiday> getHolidays() {
        logger.info("Fetching holidays");
        return holidayDAO.getAllHolidays();
    }

    // 🔹 PERFORMANCE
    public void submitReview(PerformanceReview review) {
        logger.info("Submitting performance review");

        if (review == null) {
            logger.warning("Review is null");
            throw new RuntimeException("Review cannot be null");
        }

        performanceDAO.addReview(review);
        logger.info("Review submitted");
    }

    public List<PerformanceReview> getReviews(int empId) {
        logger.info("Fetching reviews for employee ID: " + empId);

        if (empId <= 0) {
            logger.warning("Invalid employee ID");
            throw new RuntimeException("Invalid employee ID");
        }

        return performanceDAO.getReviewsByEmployee(empId);
    }

    public void addGoal(Goal goal) {
        logger.info("Adding goal");

        if (goal == null) {
            logger.warning("Goal is null");
            throw new RuntimeException("Goal cannot be null");
        }

        goalDAO.addGoal(goal);
        logger.info("Goal added");
    }

    public List<Goal> getGoals(int empId) {
        logger.info("Fetching goals for employee ID: " + empId);

        if (empId <= 0) {
            logger.warning("Invalid employee ID");
            throw new RuntimeException("Invalid employee ID");
        }

        return goalDAO.getGoalsByEmployee(empId);
    }

    public void updateGoalStatus(int goalId, String status) {
        logger.info("Updating goal status for goal ID: " + goalId);

        if (goalId <= 0 || status == null) {
            logger.warning("Invalid input");
            throw new RuntimeException("Invalid input");
        }

        goalDAO.updateGoalStatus(goalId, status);
        logger.info("Goal status updated");
    }

    public List<Announcement> getAnnouncements() {
        logger.info("Fetching announcements");
        return announcementDAO.getAllAnnouncements();
    }

    public List<Employee> getBirthdays() {
        logger.info("Fetching upcoming birthdays");
        return employeeDAO.getUpcomingBirthdays();
    }

    public List<Employee> getAnniversaries() {
        logger.info("Fetching work anniversaries");
        return employeeDAO.getWorkAnniversaries();
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
        logger.info("Fetching unread notification count for employee ID: " + empId);

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
}