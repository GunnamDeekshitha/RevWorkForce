package com.revworkforce.dao;

import com.revworkforce.model.Department;
import com.revworkforce.util.DBConnection;

import java.sql.*;
import java.util.*;

public class DepartmentDAO {

    public void addDepartment(String name) {
        String sql = "INSERT INTO department (dept_name) VALUES (?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Department> getAllDepartments() {
        List<Department> list = new ArrayList<>();

        String sql = "SELECT * FROM department";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Department d = new Department();
                d.setDeptId(rs.getInt("dept_id"));
                d.setDeptName(rs.getString("dept_name"));
                list.add(d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}