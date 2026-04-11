package com.revworkforce.dao;

import com.revworkforce.model.Designation;
import com.revworkforce.util.DBConnection;

import java.sql.*;
import java.util.*;

public class DesignationDAO {

    public void addDesignation(String name, int deptId) {
        String sql = "INSERT INTO designation (desig_name, dept_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setInt(2, deptId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Designation> getByDepartment(int deptId) {
        List<Designation> list = new ArrayList<>();

        String sql = "SELECT * FROM designation WHERE dept_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, deptId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Designation d = new Designation();
                d.setDesigId(rs.getInt("desig_id"));
                d.setDesigName(rs.getString("desig_name"));
                list.add(d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}