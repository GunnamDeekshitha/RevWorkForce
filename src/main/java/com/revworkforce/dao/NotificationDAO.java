package com.revworkforce.dao;

import com.revworkforce.model.Notification;
import com.revworkforce.util.DBConnection;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public class NotificationDAO {
    private static final Logger logger = Logger.getLogger(NotificationDAO.class.getName());
    public void addNotification(Notification n) {
        String sql = "INSERT INTO notifications (employee_id, message, status) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, n.getEmployeeId());
            ps.setString(2, n.getMessage());
            ps.setString(3, "UNREAD");
            ps.executeUpdate();
        } catch (Exception e) {
            logger.warning("Error adding notification: " + e.getMessage());
        }
    }

    public List<Notification> getNotifications(int empId) {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE employee_id=? ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
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
        } catch (Exception e) {
            logger.warning("Error fetching notifications: " + e.getMessage());
        }

        return list;
    }

    public int getUnreadCount(int empId) {
        String sql = "SELECT COUNT(*) FROM notifications WHERE employee_id=? AND status='UNREAD'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                logger.info("Unread count fetched: " + count);
                return count;
            }
        } catch (Exception e) {
            logger.warning("Error fetching unread count: " + e.getMessage());
        }
        return 0;
    }

    public void markAllAsRead(int empId) {
        String sql = "UPDATE notifications SET status='READ' WHERE employee_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empId);
            ps.executeUpdate();
            logger.info("Notifications marked as read");
        } catch (Exception e) {
            logger.warning("Error updating notifications: " + e.getMessage());
        }
    }
}