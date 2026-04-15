package com.revworkforce.dao;

import com.revworkforce.exception.DatabaseException;
import com.revworkforce.model.LeaveBalance;
import com.revworkforce.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LeaveBalanceDAO {

    private static final Logger logger = Logger.getLogger(LeaveBalanceDAO.class.getName());

    public LeaveBalance getLeaveBalance(int empId) {
        String sql = "SELECT * FROM leave_balance WHERE employee_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                LeaveBalance lb = new LeaveBalance();
                lb.setEmployeeId(empId);
                lb.setCasualLeave(rs.getInt("casual_leave"));
                lb.setSickLeave(rs.getInt("sick_leave"));
                lb.setPaidLeave(rs.getInt("paid_leave"));
                lb.setPrivilegeLeave(rs.getInt("privilege_leave"));
                return lb;
            }
            return null;

        } catch (SQLException e) {
            logger.severe("DB error fetching leave balance for employee " + empId + ": " + e.getMessage());
            throw new DatabaseException("Failed to fetch leave balance for employee ID: " + empId, e);
        }
    }

    public boolean hasEnoughLeave(int empId, String type, int days) {
        LeaveBalance lb = getLeaveBalance(empId); // will throw DatabaseException if DB fails
        if (lb == null) return false;

        return switch (type.trim().toUpperCase()) {
            case "CL" -> lb.getCasualLeave()    >= days;
            case "SL" -> lb.getSickLeave()      >= days;
            case "PL" -> lb.getPaidLeave()      >= days;
            case "PR" -> lb.getPrivilegeLeave() >= days;
            default   -> false;
        };
    }

    public void updateLeaveBalance(int empId, String type, int days) {
        String column = resolveColumn(type);
        String sql = "UPDATE leave_balance SET " + column + " = " + column + " - ? WHERE employee_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, days);
            ps.setInt(2, empId);
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.severe("DB error updating leave balance for employee " + empId + ": " + e.getMessage());
            throw new DatabaseException("Failed to update leave balance for employee ID: " + empId, e);
        }
    }

    public void restoreLeaveBalance(int empId, String type, int days) {
        String column = resolveColumn(type);
        String sql = "UPDATE leave_balance SET " + column + " = " + column + " + ? WHERE employee_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, days);
            ps.setInt(2, empId);
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.severe("DB error restoring leave balance for employee " + empId + ": " + e.getMessage());
            throw new DatabaseException("Failed to restore leave balance for employee ID: " + empId, e);
        }
    }

    public void assignLeaveBalance(int empId, int cl, int sl, int pl, int pr) {
        String sql = "INSERT INTO leave_balance (employee_id, casual_leave, sick_leave, paid_leave, privilege_leave) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ps.setInt(2, cl);
            ps.setInt(3, sl);
            ps.setInt(4, pl);
            ps.setInt(5, pr);
            ps.executeUpdate();
            logger.info("Leave balance assigned for employee " + empId);

        } catch (SQLException e) {
            logger.severe("DB error assigning leave balance for employee " + empId + ": " + e.getMessage());
            throw new DatabaseException("Failed to assign leave balance for employee ID: " + empId, e);
        }
    }

    public void adjustLeaveBalance(int empId, String type, int days, boolean isAdd) {
        String column  = resolveColumn(type);
        String operator = isAdd ? "+" : "-";
        String sql = "UPDATE leave_balance SET " + column + " = " + column + " " + operator + " ? WHERE employee_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, days);
            ps.setInt(2, empId);
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.severe("DB error adjusting leave balance for employee " + empId + ": " + e.getMessage());
            throw new DatabaseException("Failed to adjust leave balance for employee ID: " + empId, e);
        }
    }

    public List<LeaveBalance> getTeamLeaveBalances(int managerId) {
        List<LeaveBalance> list = new ArrayList<>();
        String sql = "SELECT lb.* FROM leave_balance lb " +
                "JOIN employee e ON lb.employee_id = e.employee_id " +
                "WHERE e.manager_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, managerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LeaveBalance lb = new LeaveBalance();
                lb.setEmployeeId(rs.getInt("employee_id"));
                lb.setCasualLeave(rs.getInt("casual_leave"));
                lb.setSickLeave(rs.getInt("sick_leave"));
                lb.setPaidLeave(rs.getInt("paid_leave"));
                lb.setPrivilegeLeave(rs.getInt("privilege_leave"));
                list.add(lb);
            }
            return list;

        } catch (SQLException e) {
            logger.severe("DB error fetching team leave balances for manager " + managerId + ": " + e.getMessage());
            throw new DatabaseException("Failed to fetch team leave balances for manager ID: " + managerId, e);
        }
    }

    private String resolveColumn(String type) {
        return switch (type.trim().toUpperCase()) {
            case "CL" -> "casual_leave";
            case "SL" -> "sick_leave";
            case "PL" -> "paid_leave";
            case "PR" -> "privilege_leave";
            default   -> throw new DatabaseException("Unknown leave type: " + type, null);
        };
    }
}
