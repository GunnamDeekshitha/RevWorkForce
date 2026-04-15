package com.revworkforce.dao;

import com.revworkforce.exception.DatabaseException;
import com.revworkforce.model.Announcement;
import com.revworkforce.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AnnouncementDAO {

    private static final Logger logger = Logger.getLogger(AnnouncementDAO.class.getName());

    public void addAnnouncement(Announcement a) {
        String sql = "INSERT INTO announcements (title, message) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, a.getTitle());
            ps.setString(2, a.getMessage());
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.severe("DB error adding announcement: " + e.getMessage());
            throw new DatabaseException("Failed to add announcement: " + a.getTitle(), e);
        }
    }

    public List<Announcement> getAllAnnouncements() {
        List<Announcement> list = new ArrayList<>();
        String sql = "SELECT * FROM announcements ORDER BY created_at DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Announcement a = new Announcement();
                a.setId(rs.getInt("id"));
                a.setTitle(rs.getString("title"));
                a.setMessage(rs.getString("message"));
                a.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(a);
            }
            return list;

        } catch (SQLException e) {
            logger.severe("DB error fetching announcements: " + e.getMessage());
            throw new DatabaseException("Failed to fetch announcements", e);
        }
    }
}
