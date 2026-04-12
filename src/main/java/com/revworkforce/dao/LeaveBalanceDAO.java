package com.revworkforce.dao;

import com.revworkforce.model.LeaveBalance;
import com.revworkforce.util.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

public class LeaveBalanceDAO {

    private static final Logger logger = LogManager.getLogger(LeaveBalanceDAO.class);

    public LeaveBalance getLeaveBalance(int empId) {

        String query = "SELECT * FROM leave_balance WHERE employee_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            logger.info("Fetching leave balance for employee ID: " + empId);

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

        } catch (Exception e) {
            logger.warn("Error fetching leave balance: " + e.getMessage());
        }

        return null;
    }

    public void updateLeaveBalance(int empId, String type, int days) {

        String column = null;

        if (type.equalsIgnoreCase("CL")) column = "casual_leave";
        else if (type.equalsIgnoreCase("SL")) column = "sick_leave";
        else if (type.equalsIgnoreCase("PL")) column = "paid_leave";

        if (column == null) {
            logger.warn("Invalid leave type entered: " + type);
            return;
        }

        String query = "UPDATE leave_balance SET " + column + " = " + column + " - ? WHERE employee_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            logger.info("Updating leave balance for employee ID: " + empId);

            ps.setInt(1, days);
            ps.setInt(2, empId);

            ps.executeUpdate();
        } catch (Exception e) {
            logger.warn("Error updating leave balance: " + e.getMessage());
        }
    }

    public boolean hasEnoughLeave(int empId, String type, int days) {

        LeaveBalance lb = getLeaveBalance(empId);

        if (lb == null) {
            logger.warn("No leave balance found for employee ID: " + empId);
            return false;
        }

        switch (type.trim().toUpperCase()) {
            case "CL":
                return lb.getCasualLeave() >= days;
            case "SL":
                return lb.getSickLeave() >= days;
            case "PL":
                return lb.getPaidLeave() >= days;
            case "PR":
                return lb.getPrivilegeLeave() >= days;
            default:
                logger.warn("Invalid leave type checked: " + type);
                return false;
        }
    }

    public void restoreLeaveBalance(int empId, String type, int days) {

        String column = null;

        if (type.equalsIgnoreCase("CL")) column = "casual_leave";
        else if (type.equalsIgnoreCase("SL")) column = "sick_leave";
        else if (type.equalsIgnoreCase("PL")) column = "paid_leave";
        else if (type.equalsIgnoreCase("PR")) column = "privilege_leave";

        if (column == null) {
            logger.warn("Invalid leave type for restore: " + type);
            return;
        }

        String query = "UPDATE leave_balance SET " + column + " = " + column + " + ? WHERE employee_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            logger.info("Restoring leave balance for employee ID: " + empId);

            ps.setInt(1, days);
            ps.setInt(2, empId);

            ps.executeUpdate();
        } catch (Exception e) {
            logger.warn("Error restoring leave balance: " + e.getMessage());
        }
    }

    public void assignLeaveBalance(int empId, int cl, int sl, int pl, int pr) {

        String query = "INSERT INTO leave_balance (employee_id, casual_leave, sick_leave, paid_leave,privilege_leave) VALUES (?, ?, ?, ?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            logger.info("Assigning leave balance for employee ID: " + empId);

            ps.setInt(1, empId);
            ps.setInt(2, cl);
            ps.setInt(3, sl);
            ps.setInt(4, pl);
            ps.setInt(5, pr);

            ps.executeUpdate();

        } catch (Exception e) {
            logger.warn("Error assigning leave balance: " + e.getMessage());
        }
    }

    public void adjustLeaveBalance(int empId, String type, int days, boolean isAdd) {

        String column = null;

        if (type.equalsIgnoreCase("CL")) column = "casual_leave";
        else if (type.equalsIgnoreCase("SL")) column = "sick_leave";
        else if (type.equalsIgnoreCase("PL")) column = "paid_leave";
        else if (type.equalsIgnoreCase("PR")) column = "privilege_leave";

        if (column == null) {
            logger.warn("Invalid leave type for adjustment: " + type);
            return;
        }

        String operator = isAdd ? "+" : "-";

        String query = "UPDATE leave_balance SET " + column + " = " + column + " " + operator + " ? WHERE employee_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            logger.info("Adjusting leave balance for employee ID: " + empId);

            ps.setInt(1, days);
            ps.setInt(2, empId);

            ps.executeUpdate();

        } catch (Exception e) {
            logger.warn("Error adjusting leave balance: " + e.getMessage());
        }
    }

    public List<LeaveBalance> getTeamLeaveBalances(int managerId) {
        List<LeaveBalance> list = new ArrayList<>();

        String sql = "SELECT lb.* FROM leave_balance lb " +
                "JOIN employee e ON lb.employee_id = e.employee_id " +
                "WHERE e.manager_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            logger.info("Fetching team leave balances for manager ID: " + managerId);

            ps.setInt(1, managerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LeaveBalance lb = new LeaveBalance();

                lb.setEmployeeId(rs.getInt("employee_id"));
                lb.setCasualLeave(rs.getInt("casual_leave"));
                lb.setSickLeave(rs.getInt("sick_leave"));
                lb.setPaidLeave(rs.getInt("paid_leave"));

                list.add(lb);
            }
        } catch (Exception e) {
            logger.warn("Error fetching team leave balances: " + e.getMessage());
        }

        return list;
    }
}