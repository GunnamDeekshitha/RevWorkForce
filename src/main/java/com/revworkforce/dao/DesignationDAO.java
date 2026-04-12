package com.revworkforce.dao;

import com.revworkforce.model.Designation;
import com.revworkforce.util.DBConnection;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public class DesignationDAO {

    private static final Logger logger = Logger.getLogger(DesignationDAO.class.getName());

    public void addDesignation(String name, int deptId) {
        String sql = "INSERT INTO designation (desig_name, dept_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            logger.info("Adding designation: " + name + " for dept ID: " + deptId);

            ps.setString(1, name);
            ps.setInt(2, deptId);
            ps.executeUpdate();

        } catch (Exception e) {
            logger.warning("Error adding designation: " + e.getMessage());
        }
    }

    public List<Designation> getByDepartment(int deptId) {
        List<Designation> list = new ArrayList<>();

        String sql = "SELECT * FROM designation WHERE dept_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            logger.info("Fetching designations for dept ID: " + deptId);

            ps.setInt(1, deptId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Designation d = new Designation();
                d.setDesigId(rs.getInt("desig_id"));
                d.setDesigName(rs.getString("desig_name"));
                list.add(d);
            }
        } catch (Exception e) {
            logger.warning("Error fetching designations: " + e.getMessage());
        }

        return list;
    }
}