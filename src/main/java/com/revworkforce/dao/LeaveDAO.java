package com.revworkforce.dao;

import com.revworkforce.exception.DatabaseException;
import com.revworkforce.model.LeaveRequest;
import com.revworkforce.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LeaveDAO {

    private static final Logger logger = Logger.getLogger(LeaveDAO.class.getName());

    private LeaveRequest mapRow(ResultSet rs) throws SQLException {
        LeaveRequest leave = new LeaveRequest();
        leave.setLeaveId(rs.getInt("leave_id"));
        leave.setEmployeeId(rs.getInt("employee_id"));
        leave.setLeaveType(rs.getString("leave_type"));
        leave.setStartDate(rs.getDate("start_date"));
        leave.setEndDate(rs.getDate("end_date"));
        leave.setReason(rs.getString("reason"));
        leave.setStatus(rs.getString("status"));
        return leave;
    }

    public void applyLeave(LeaveRequest leave) {
        String sql = "INSERT INTO leave_request (employee_id, leave_type, start_date, end_date, reason, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, leave.getEmployeeId());
            ps.setString(2, leave.getLeaveType());
            ps.setDate(3, leave.getStartDate());
            ps.setDate(4, leave.getEndDate());
            ps.setString(5, leave.getReason());
            ps.setString(6, "PENDING");
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.severe("DB error applying leave: " + e.getMessage());
            throw new DatabaseException("Failed to apply leave for employee ID: " + leave.getEmployeeId(), e);
        }
    }

    public LeaveRequest getLeaveById(int leaveId) {
        String sql = "SELECT * FROM leave_request WHERE leave_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, leaveId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? mapRow(rs) : null;

        } catch (SQLException e) {
            logger.severe("DB error fetching leave by ID " + leaveId + ": " + e.getMessage());
            throw new DatabaseException("Failed to fetch leave request with ID: " + leaveId, e);
        }
    }

    public List<LeaveRequest> getAllLeaves() {
        List<LeaveRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM leave_request";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));
            return list;

        } catch (SQLException e) {
            logger.severe("DB error fetching all leaves: " + e.getMessage());
            throw new DatabaseException("Failed to fetch all leave requests", e);
        }
    }

    public List<LeaveRequest> getLeavesByEmployeeId(int empId) {
        List<LeaveRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM leave_request WHERE employee_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LeaveRequest leave = mapRow(rs);
                leave.setManagerComment(rs.getString("manager_comment"));
                list.add(leave);
            }
            return list;

        } catch (SQLException e) {
            logger.severe("DB error fetching leaves for employee " + empId + ": " + e.getMessage());
            throw new DatabaseException("Failed to fetch leave requests for employee ID: " + empId, e);
        }
    }

    public List<LeaveRequest> getLeavesByManager(int managerId) {
        List<LeaveRequest> list = new ArrayList<>();
        String sql = "SELECT lr.* FROM leave_request lr " +
                "JOIN employee e ON lr.employee_id = e.employee_id " +
                "WHERE e.manager_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, managerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
            return list;

        } catch (SQLException e) {
            logger.severe("DB error fetching leaves for manager " + managerId + ": " + e.getMessage());
            throw new DatabaseException("Failed to fetch leave requests for manager ID: " + managerId, e);
        }
    }

    public void updateLeaveStatus(int leaveId, String status, String comment) {
        String sql = "UPDATE leave_request SET status = ?, manager_comment = ? WHERE leave_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, comment);
            ps.setInt(3, leaveId);
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.severe("DB error updating leave status for leave " + leaveId + ": " + e.getMessage());
            throw new DatabaseException("Failed to update leave status for leave ID: " + leaveId, e);
        }
    }

    public void cancelLeave(int leaveId) {
        String sql = "UPDATE leave_request SET status = 'CANCELLED' WHERE leave_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, leaveId);
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.severe("DB error cancelling leave " + leaveId + ": " + e.getMessage());
            throw new DatabaseException("Failed to cancel leave with ID: " + leaveId, e);
        }
    }

    public void adminCancelLeave(int leaveId) {
        String sql = "UPDATE leave_request SET status = 'CANCELLED' WHERE leave_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, leaveId);
            ps.executeUpdate();
            logger.info("Leave " + leaveId + " cancelled by admin");

        } catch (SQLException e) {
            logger.severe("DB error during admin cancel leave " + leaveId + ": " + e.getMessage());
            throw new DatabaseException("Failed to cancel leave (admin) with ID: " + leaveId, e);
        }
    }

    public void getLeaveStatistics() {
        String sql = "SELECT status, COUNT(*) as count FROM leave_request GROUP BY status";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("=== Leave Statistics ===");
            while (rs.next()) {
                System.out.println(rs.getString("status") + ": " + rs.getInt("count"));
            }

        } catch (SQLException e) {
            logger.severe("DB error fetching leave statistics: " + e.getMessage());
            throw new DatabaseException("Failed to fetch leave statistics", e);
        }
    }
}
