package com.revworkforce.dao;

import com.revworkforce.exception.DatabaseException;
import com.revworkforce.model.Department;
import com.revworkforce.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DepartmentDAO {

    private static final Logger logger = Logger.getLogger(DepartmentDAO.class.getName());

    public void addDepartment(String name) {
        String sql = "INSERT INTO department (dept_name) VALUES (?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.severe("DB error adding department: " + e.getMessage());
            throw new DatabaseException("Failed to add department: " + name, e);
        }
    }

    public List<Department> getAllDepartments() {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT * FROM department";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Department d = new Department();
                d.setDeptId(rs.getInt("dept_id"));
                d.setDeptName(rs.getString("dept_name"));
                list.add(d);
            }
            return list;

        } catch (SQLException e) {
            logger.severe("DB error fetching departments: " + e.getMessage());
            throw new DatabaseException("Failed to fetch department list", e);
        }
    }
}
