package com.revworkforce.dao;
import com.revworkforce.model.Announcement;
import com.revworkforce.util.DBConnection;
import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AnnouncementDAO {

    private static final Logger logger = Logger.getLogger(AnnouncementDAO.class.getName());

    public void addAnnouncement(Announcement a) {
        String sql = "INSERT INTO announcements (title, message) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            logger.info("Adding announcement to database");

            ps.setString(1, a.getTitle());
            ps.setString(2, a.getMessage());

            ps.executeUpdate();

        } catch (Exception e) {
            logger.warning("Error adding announcement: " + e.getMessage());
        }
    }

    public List<Announcement> getAllAnnouncements() {
        List<Announcement> list = new ArrayList<>();
        String sql = "SELECT * FROM announcements ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            logger.info("Fetching all announcements");

            while (rs.next()) {
                Announcement a = new Announcement();
                a.setId(rs.getInt("id"));
                a.setTitle(rs.getString("title"));
                a.setMessage(rs.getString("message"));
                a.setCreatedAt(rs.getTimestamp("created_at"));

                list.add(a);
            }

        } catch (Exception e) {
            logger.warning("Error fetching announcements: " + e.getMessage());
        }

        return list;
    }
}