package com.revworkforce.dao;

import com.revworkforce.model.LeaveRequest;
import com.revworkforce.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LeaveDAO {

    // APPLY LEAVE
    public void applyLeave(LeaveRequest leave) {

        String query = "INSERT INTO leave_request (employee_id, leave_type, start_date, end_date, reason, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, leave.getEmployeeId());
            ps.setString(2, leave.getLeaveType());
            ps.setDate(3, leave.getStartDate());
            ps.setDate(4, leave.getEndDate());
            ps.setString(5, leave.getReason());
            ps.setString(6, "PENDING");

            ps.executeUpdate();
            System.out.println("Leave applied successfully!");

        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }

        return list;
    }
    public void updateLeaveStatus(int leaveId, String status, String comment) {

        String query = "UPDATE leave_request SET status = ?, manager_comment = ? WHERE leave_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, status);
            ps.setString(2, comment);
            ps.setInt(3, leaveId);

            ps.executeUpdate();
            System.out.println("Leave status updated!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<LeaveRequest> getLeavesByEmployeeId(int empId) {

        List<LeaveRequest> list = new ArrayList<>();
        String query = "SELECT * FROM leave_request WHERE employee_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

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
            e.printStackTrace();
        }

        return list;
    }
    public LeaveRequest getLeaveById(int leaveId) {

        String query = "SELECT * FROM leave_request WHERE leave_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

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
            e.printStackTrace();
        }

        return null;
    }
    public void cancelLeave(int leaveId) {

        String query = "UPDATE leave_request SET status = 'CANCELLED' WHERE leave_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, leaveId);
            ps.executeUpdate();

            System.out.println("Leave cancelled successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<LeaveRequest> getLeavesByManager(int managerId) {

        List<LeaveRequest> list = new ArrayList<>();

        String query = "SELECT lr.* FROM leave_request lr " +
                "JOIN employee e ON lr.employee_id = e.employee_id " +
                "WHERE e.manager_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

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
            e.printStackTrace();
        }

        return list;
    }
    public void adminCancelLeave(int leaveId) {

        String query = "UPDATE leave_request SET status = 'CANCELLED' WHERE leave_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, leaveId);
            ps.executeUpdate();

            System.out.println("Leave cancelled by admin!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getLeaveStatistics() {

        String query = "SELECT status, COUNT(*) as count FROM leave_request GROUP BY status";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();

            System.out.println("=== Leave Statistics ===");

            while (rs.next()) {
                String status = rs.getString("status");
                int count = rs.getInt("count");

                System.out.println(status + ": " + count);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}