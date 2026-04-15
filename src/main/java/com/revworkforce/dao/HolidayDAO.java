package com.revworkforce.dao;

import com.revworkforce.exception.DatabaseException;
import com.revworkforce.model.Holiday;
import com.revworkforce.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class HolidayDAO {

    private static final Logger logger = Logger.getLogger(HolidayDAO.class.getName());

    public List<Holiday> getAllHolidays() {
        List<Holiday> list = new ArrayList<>();
        String sql = "SELECT * FROM holiday ORDER BY holiday_date";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Holiday h = new Holiday();
                h.setHolidayId(rs.getInt("holiday_id"));
                h.setHolidayDate(rs.getDate("holiday_date"));
                h.setHolidayName(rs.getString("holiday_name"));
                list.add(h);
            }
            return list;

        } catch (SQLException e) {
            logger.severe("DB error fetching holidays: " + e.getMessage());
            throw new DatabaseException("Failed to fetch holiday list", e);
        }
    }

    public void addHoliday(Holiday h) {
        String sql = "INSERT INTO holiday (name, date, description) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, h.getHolidayName());
            ps.setDate(2,   h.getHolidayDate());
            ps.setString(3, h.getDescription());
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.severe("DB error adding holiday: " + e.getMessage());
            throw new DatabaseException("Failed to add holiday: " + h.getHolidayName(), e);
        }
    }

    public void deleteHoliday(int id) {
        String sql = "DELETE FROM holiday WHERE holiday_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.severe("DB error deleting holiday " + id + ": " + e.getMessage());
            throw new DatabaseException("Failed to delete holiday with ID: " + id, e);
        }
    }
}
