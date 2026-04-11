package com.revworkforce.dao;

import com.revworkforce.model.Notification;
import com.revworkforce.util.DBConnection;

import java.sql.*;
import java.util.*;

public class NotificationDAO {

    public void addNotification(Notification n) {
        String sql = "INSERT INTO notifications (employee_id, message, status) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, n.getEmployeeId());
            ps.setString(2, n.getMessage());
            ps.setString(3, "UNREAD");

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }

        return list;
    }

    public int getUnreadCount(int empId) {
        String sql = "SELECT COUNT(*) FROM notifications WHERE employee_id=? AND status='UNREAD'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void markAllAsRead(int empId) {
        String sql = "UPDATE notifications SET status='READ' WHERE employee_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}