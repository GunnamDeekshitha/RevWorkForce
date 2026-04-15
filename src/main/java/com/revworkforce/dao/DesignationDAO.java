package com.revworkforce.dao;

import com.revworkforce.exception.DatabaseException;
import com.revworkforce.model.Designation;
import com.revworkforce.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DesignationDAO {

    private static final Logger logger = Logger.getLogger(DesignationDAO.class.getName());

    public void addDesignation(String name, int deptId) {
        String sql = "INSERT INTO designation (desig_name, dept_id) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setInt(2,    deptId);
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.severe("DB error adding designation: " + e.getMessage());
            throw new DatabaseException("Failed to add designation: " + name, e);
        }
    }

    public List<Designation> getByDepartment(int deptId) {
        List<Designation> list = new ArrayList<>();
        String sql = "SELECT * FROM designation WHERE dept_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, deptId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Designation d = new Designation();
                d.setDesigId(rs.getInt("desig_id"));
                d.setDesigName(rs.getString("desig_name"));
                list.add(d);
            }
            return list;

        } catch (SQLException e) {
            logger.severe("DB error fetching designations for dept " + deptId + ": " + e.getMessage());
            throw new DatabaseException("Failed to fetch designations for department ID: " + deptId, e);
        }
    }
}
