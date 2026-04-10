package com.revworkforce.dao;

import com.revworkforce.model.LeaveBalance;
import com.revworkforce.util.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LeaveBalanceDAO {

    private static final Logger logger = LogManager.getLogger(LeaveBalanceDAO.class);

    // 🔹 GET LEAVE BALANCE
    public LeaveBalance getLeaveBalance(int empId) {

        String query = "SELECT * FROM leave_balance WHERE employee_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                LeaveBalance lb = new LeaveBalance();

                lb.setEmployeeId(empId);
                lb.setCasualLeave(rs.getInt("casual_leave"));
                lb.setSickLeave(rs.getInt("sick_leave"));
                lb.setPaidLeave(rs.getInt("paid_leave"));

                return lb;
            }

        } catch (Exception e) {
            logger.error("Error fetching leave balance: " + e.getMessage());
        }

        return null;
    }

    // 🔹 UPDATE (DEDUCT) LEAVE BALANCE
    public void updateLeaveBalance(int empId, String type, int days) {

        String column = null;

        if (type.equalsIgnoreCase("CL")) column = "casual_leave";
        else if (type.equalsIgnoreCase("SL")) column = "sick_leave";
        else if (type.equalsIgnoreCase("PL")) column = "paid_leave";

        if (column == null) {
            System.out.println("Invalid leave type!");
            logger.warn("Invalid leave type entered: " + type);
            return;
        }

        String query = "UPDATE leave_balance SET " + column + " = " + column + " - ? WHERE employee_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, days);
            ps.setInt(2, empId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                logger.info("Leave balance updated for employee ID: " + empId);
            }

        } catch (Exception e) {
            logger.error("Error updating leave balance: " + e.getMessage());
        }
    }

    // 🔹 CHECK IF ENOUGH LEAVE AVAILABLE
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

        if (column == null) return;

        String query = "UPDATE leave_balance SET " + column + " = " + column + " + ? WHERE employee_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, days);
            ps.setInt(2, empId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void assignLeaveBalance(int empId, int cl, int sl, int pl) {

        String query = "INSERT INTO leave_balance (employee_id, casual_leave, sick_leave, paid_leave) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, empId);
            ps.setInt(2, cl);
            ps.setInt(3, sl);
            ps.setInt(4, pl);

            ps.executeUpdate();

            System.out.println("Leave balance assigned successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    public void adjustLeaveBalance(int empId, String type, int days, boolean isAdd) {
//
//        String column = null;
//
//        if (type.equalsIgnoreCase("CL")) column = "casual_leave";
//        else if (type.equalsIgnoreCase("SL")) column = "sick_leave";
//        else if (type.equalsIgnoreCase("PL")) column = "paid_leave";
//
//        if (column == null) {
//            System.out.println("Invalid leave type!");
//            return;
//        }
//
//        String operator = isAdd ? "+" : "-";
//
//        String query = "UPDATE leave_balance SET " + column + " = " + column + " " + operator + " ? WHERE employee_id = ?";
//
//        try (Connection con = DBConnection.getConnection();
//             PreparedStatement ps = con.prepareStatement(query)) {
//
//            ps.setInt(1, days);
//            ps.setInt(2, empId);
//
//            ps.executeUpdate();
//
//            System.out.println("Leave balance adjusted successfully!");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public void adjustLeaveBalance(int empId, String type, int days, boolean isAdd) {

        String column = null;

        if (type.equalsIgnoreCase("CL")) column = "casual_leave";
        else if (type.equalsIgnoreCase("SL")) column = "sick_leave";
        else if (type.equalsIgnoreCase("PL")) column = "paid_leave";

        if (column == null) {
            System.out.println("Invalid leave type!");
            return;
        }

        String operator = isAdd ? "+" : "-";

        String query = "UPDATE leave_balance SET " + column + " = " + column + " " + operator + " ? WHERE employee_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, days);
            ps.setInt(2, empId);

            ps.executeUpdate();

            System.out.println("Leave balance adjusted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}