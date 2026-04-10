package com.revworkforce.dao;

import com.revworkforce.model.Holiday;
import com.revworkforce.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HolidayDAO {

    public List<Holiday> getAllHolidays() {

        List<Holiday> list = new ArrayList<>();

        String query = "SELECT * FROM holiday ORDER BY holiday_date";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Holiday h = new Holiday();

                h.setHolidayId(rs.getInt("holiday_id"));
                h.setHolidayDate(rs.getDate("holiday_date"));
                h.setHolidayName(rs.getString("holiday_name"));

                list.add(h);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}