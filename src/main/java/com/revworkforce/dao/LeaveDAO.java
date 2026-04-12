package com.revworkforce.dao;

import com.revworkforce.model.LeaveRequest;
import com.revworkforce.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LeaveDAO {

    private static final Logger logger = Logger.getLogger(LeaveDAO.class.getName());

    // APPLY LEAVE
    public void applyLeave(LeaveRequest leave) {

        String query = "INSERT INTO leave_request (employee_id, leave_type, start_date, end_date, reason, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            logger.info("Applying leave for employee ID: " + leave.getEmployeeId());

            ps.setInt(1, leave.getEmployeeId());
            ps.setString(2, leave.getLeaveType());
            ps.setDate(3, leave.getStartDate());
            ps.setDate(4, leave.getEndDate());
            ps.setString(5, leave.getReason());
            ps.setString(6, "PENDING");

            ps.executeUpdate();

        } catch (Exception e) {
            logger.warning("Error applying leave: " + e.getMessage());
        }
    }

    public List<LeaveRequest> getAllLeaves() {

        List<LeaveRequest> list = new ArrayList<>();
        String query = "SELECT * FROM leave_request";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LeaveRequest leave = new LeaveRequest();

                leave.setLeaveId(rs.getInt("leave_id"));
                leave.setEmployeeId(rs.getInt("employee_id"));
                leave.setLeaveType(rs.getString("leave_type"));
                leave.setStartDate(rs.getDate("start_date"));
                leave.setEndDate(rs.getDate("end_date"));
                leave.setReason(rs.getString("reason"));
                leave.setStatus(rs.getString("status"));

                list.add(leave);
            }

        } catch (Exception e) {
            logger.warning("Error fetching leaves: " + e.getMessage());
        }

        return list;
    }

    public void updateLeaveStatus(int leaveId, String status, String comment) {

        String query = "UPDATE leave_request SET status = ?, manager_comment = ? WHERE leave_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            logger.info("Updating leave status for leave ID: " + leaveId);

            ps.setString(1, status);
            ps.setString(2, comment);
            ps.setInt(3, leaveId);

            ps.executeUpdate();

        } catch (Exception e) {
            logger.warning("Error updating leave status: " + e.getMessage());
        }
    }

    public List<LeaveRequest> getLeavesByEmployeeId(int empId) {

        List<LeaveRequest> list = new ArrayList<>();
        String query = "SELECT * FROM leave_request WHERE employee_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            logger.info("Fetching leaves for employee ID: " + empId);

            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LeaveRequest leave = new LeaveRequest();

                leave.setLeaveId(rs.getInt("leave_id"));
                leave.setLeaveType(rs.getString("leave_type"));
                leave.setStartDate(rs.getDate("start_date"));
                leave.setEndDate(rs.getDate("end_date"));
                leave.setReason(rs.getString("reason"));
                leave.setStatus(rs.getString("status"));
                leave.setManagerComment(rs.getString("manager_comment"));

                list.add(leave);
            }

        } catch (Exception e) {
            logger.warning("Error fetching employee leaves: " + e.getMessage());
        }

        return list;
    }

    public LeaveRequest getLeaveById(int leaveId) {

        String query = "SELECT * FROM leave_request WHERE leave_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            logger.info("Fetching leave by ID: " + leaveId);

            ps.setInt(1, leaveId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                LeaveRequest leave = new LeaveRequest();

                leave.setLeaveId(rs.getInt("leave_id"));
                leave.setEmployeeId(rs.getInt("employee_id"));
                leave.setLeaveType(rs.getString("leave_type"));
                leave.setStartDate(rs.getDate("start_date"));
                leave.setEndDate(rs.getDate("end_date"));
                leave.setStatus(rs.getString("status"));

                return leave;
            }

        } catch (Exception e) {
            logger.warning("Error fetching leave by ID: " + e.getMessage());
        }

        return null;
    }

    public void cancelLeave(int leaveId) {

        String query = "UPDATE leave_request SET status = 'CANCELLED' WHERE leave_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            logger.info("Cancelling leave ID: " + leaveId);

            ps.setInt(1, leaveId);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.warning("Error cancelling leave: " + e.getMessage());
        }
    }

    public List<LeaveRequest> getLeavesByManager(int managerId) {

        List<LeaveRequest> list = new ArrayList<>();

        String query = "SELECT lr.* FROM leave_request lr " +
                "JOIN employee e ON lr.employee_id = e.employee_id " +
                "WHERE e.manager_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            logger.info("Fetching leaves for manager ID: " + managerId);

            ps.setInt(1, managerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LeaveRequest leave = new LeaveRequest();

                leave.setLeaveId(rs.getInt("leave_id"));
                leave.setEmployeeId(rs.getInt("employee_id"));
                leave.setLeaveType(rs.getString("leave_type"));
                leave.setStartDate(rs.getDate("start_date"));
                leave.setEndDate(rs.getDate("end_date"));
                leave.setReason(rs.getString("reason"));
                leave.setStatus(rs.getString("status"));

                list.add(leave);
            }
        } catch (Exception e) {
            logger.warning("Error fetching manager leaves: " + e.getMessage());
        }

        return list;
    }

    public void adminCancelLeave(int leaveId) {

        String query = "UPDATE leave_request SET status = 'CANCELLED' WHERE leave_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            logger.info("Admin cancelling leave ID: " + leaveId);

            ps.setInt(1, leaveId);
            ps.executeUpdate();

            logger.info("Leave cancelled by admin");

        } catch (Exception e) {
            logger.warning("Error cancelling leave by admin: " + e.getMessage());
        }
    }

    public void getLeaveStatistics() {

        String query = "SELECT status, COUNT(*) as count FROM leave_request GROUP BY status";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            logger.info("Fetching leave statistics");

            ResultSet rs = ps.executeQuery();

            System.out.println("=== Leave Statistics ===");

            while (rs.next()) {
                String status = rs.getString("status");
                int count = rs.getInt("count");

                System.out.println(status + ": " + count);
            }

        } catch (Exception e) {
            logger.warning("Error fetching leave statistics: " + e.getMessage());
        }
    }
}