package com.revworkforce.dao;

import com.revworkforce.model.Holiday;
import com.revworkforce.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class HolidayDAO {

    private static final Logger logger = Logger.getLogger(HolidayDAO.class.getName());

    public List<Holiday> getAllHolidays() {

        List<Holiday> list = new ArrayList<>();
        String query = "SELECT * FROM holiday ORDER BY holiday_date";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            logger.info("Fetching all holidays");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Holiday h = new Holiday();

                h.setHolidayId(rs.getInt("holiday_id"));
                h.setHolidayDate(rs.getDate("holiday_date"));
                h.setHolidayName(rs.getString("holiday_name"));

                list.add(h);
            }

        } catch (Exception e) {
            logger.warning("Error fetching holidays: " + e.getMessage());
        }

        return list;
    }

    public void addHoliday(Holiday h) {
        String sql = "INSERT INTO holiday (name, date, description) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            logger.info("Adding holiday: " + h.getHolidayName());

            ps.setString(1, h.getHolidayName());
            ps.setDate(2, h.getHolidayDate());
            ps.setString(3, h.getDescription());

            ps.executeUpdate();

        } catch (Exception e) {
            logger.warning("Error adding holiday: " + e.getMessage());
        }
    }

    public void deleteHoliday(int id) {
        String sql = "DELETE FROM holiday WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            logger.info("Deleting holiday with ID: " + id);

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            logger.warning("Error deleting holiday: " + e.getMessage());
        }
    }
}