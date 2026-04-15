package com.revworkforce.dao;

import com.revworkforce.exception.DatabaseException;
import com.revworkforce.model.Notification;
import com.revworkforce.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class NotificationDAO {

    private static final Logger logger = Logger.getLogger(NotificationDAO.class.getName());

    public void addNotification(Notification n) {
        String sql = "INSERT INTO notifications (employee_id, message, status) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1,    n.getEmployeeId());
            ps.setString(2, n.getMessage());
            ps.setString(3, "UNREAD");
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.severe("DB error adding notification: " + e.getMessage());
            throw new DatabaseException("Failed to add notification for employee ID: " + n.getEmployeeId(), e);
        }
    }

    public List<Notification> getNotifications(int empId) {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE employee_id = ? ORDER BY created_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Notification n = new Notification();
                n.setNotificationId(rs.getInt("notification_id"));
                n.setEmployeeId(rs.getInt("employee_id"));
                n.setMessage(rs.getString("message"));
                n.setStatus(rs.getString("status"));
                n.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(n);
            }
            return list;

        } catch (SQLException e) {
            logger.severe("DB error fetching notifications for employee " + empId + ": " + e.getMessage());
            throw new DatabaseException("Failed to fetch notifications for employee ID: " + empId, e);
        }
    }

    public int getUnreadCount(int empId) {
        String sql = "SELECT COUNT(*) FROM notifications WHERE employee_id = ? AND status = 'UNREAD'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;

        } catch (SQLException e) {
            logger.severe("DB error fetching unread count for employee " + empId + ": " + e.getMessage());
            throw new DatabaseException("Failed to fetch unread count for employee ID: " + empId, e);
        }
    }

    public void markAllAsRead(int empId) {
        String sql = "UPDATE notifications SET status = 'READ' WHERE employee_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.severe("DB error marking notifications read for employee " + empId + ": " + e.getMessage());
            throw new DatabaseException("Failed to mark notifications as read for employee ID: " + empId, e);
        }
    }
}
