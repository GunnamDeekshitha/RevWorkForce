package com.revworkforce.service;

import com.revworkforce.dao.*;
import com.revworkforce.model.*;

import java.util.List;

public class EmployeeService {

    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private LeaveDAO leaveDAO = new LeaveDAO();
    private LeaveBalanceDAO leaveBalanceDAO = new LeaveBalanceDAO();
    private HolidayDAO holidayDAO = new HolidayDAO();
    private PerformanceDAO performanceDAO = new PerformanceDAO();
    private GoalDAO goalDAO = new GoalDAO();
    private AnnouncementDAO announcementDAO = new AnnouncementDAO();
    private NotificationDAO notificationDAO = new NotificationDAO();

    // 🔹 PROFILE
    public Employee getProfile(int empId) {
        return employeeDAO.getEmployeeById(empId);
    }

    public void updateProfile(int empId, String phone, String address,
                              String emergencyName, String emergencyPhone) {

        Employee emp = employeeDAO.getEmployeeById(empId);

        if (!phone.isEmpty()) emp.setPhone(phone);
        if (!address.isEmpty()) emp.setAddress(address);
        if (!emergencyName.isEmpty()) emp.setEmergencyContactName(emergencyName);
        if (!emergencyPhone.isEmpty()) emp.setEmergencyContactPhone(emergencyPhone);

        employeeDAO.updateEmployeeProfile(
                empId,
                emp.getPhone(),
                emp.getAddress(),
                emp.getEmergencyContactName(),
                emp.getEmergencyContactPhone()
        );
    }

    public Employee getManager(int managerId) {
        return employeeDAO.getEmployeeById(managerId);
    }

    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployeeDetails();
    }


    public String applyLeave(Employee emp, LeaveRequest leave) {

        int days = (int) ((leave.getEndDate().getTime() - leave.getStartDate().getTime())
                / (1000 * 60 * 60 * 24)) + 1;

        if (leaveBalanceDAO.hasEnoughLeave(emp.getEmployeeId(), leave.getLeaveType(), days)) {
            leaveDAO.applyLeave(leave);
            leaveBalanceDAO.updateLeaveBalance(emp.getEmployeeId(), leave.getLeaveType(), days);
            Notification n = new Notification();
            n.setEmployeeId(emp.getManagerId());
            n.setMessage("New leave request from " + emp.getName());
            notificationDAO.addNotification(n);
            return "Leave applied successfully!";
        } else {
            return "Not enough leave balance!";
        }
    }

    public List<LeaveRequest> getMyLeaves(int empId) {
        return leaveDAO.getLeavesByEmployeeId(empId);
    }

    public LeaveBalance getLeaveBalance(int empId) {
        return leaveBalanceDAO.getLeaveBalance(empId);
    }

    public String cancelLeave(int empId, int leaveId) {

        LeaveRequest leave = leaveDAO.getLeaveById(leaveId);

        if (leave != null
                && leave.getEmployeeId() == empId
                && leave.getStatus().equalsIgnoreCase("PENDING")) {

            int days = (int) ((leave.getEndDate().getTime() - leave.getStartDate().getTime())
                    / (1000 * 60 * 60 * 24)) + 1;

            leaveBalanceDAO.restoreLeaveBalance(empId, leave.getLeaveType(), days);
            leaveDAO.cancelLeave(leaveId);

            return "Leave cancelled!";
        }

        return "Cannot cancel leave!";
    }

    // 🔹 HOLIDAYS
    public List<Holiday> getHolidays() {
        return holidayDAO.getAllHolidays();
    }

    // 🔹 PERFORMANCE
    public void submitReview(PerformanceReview review) {
        performanceDAO.addReview(review);
    }

    public List<PerformanceReview> getReviews(int empId) {
        return performanceDAO.getReviewsByEmployee(empId);
    }
    public void addGoal(Goal goal) {
        goalDAO.addGoal(goal);
    }

    public List<Goal> getGoals(int empId) {
        return goalDAO.getGoalsByEmployee(empId);
    }

    public void updateGoalStatus(int goalId, String status) {
        goalDAO.updateGoalStatus(goalId, status);
    }
    public List<Announcement> getAnnouncements() {
        return announcementDAO.getAllAnnouncements();
    }
    public List<Employee> getBirthdays() {
        return employeeDAO.getUpcomingBirthdays();
    }

    public List<Employee> getAnniversaries() {
        return employeeDAO.getWorkAnniversaries();
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


}