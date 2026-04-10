package com.revworkforce.dao;
import com.revworkforce.util.DBConnection;
import com.revworkforce.model.Goal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;


public class GoalDAO {
    public void addGoal(Goal goal) {

        String query = "INSERT INTO goals (employee_id, description, deadline, priority, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, goal.getEmployeeId());
            ps.setString(2, goal.getDescription());
            ps.setDate(3, goal.getDeadline());
            ps.setString(4, goal.getPriority());
            ps.setString(5, "PENDING");

            ps.executeUpdate();

            System.out.println("Goal added successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Goal> getGoalsByEmployee(int empId) {

        List<Goal> list = new ArrayList<>();

        String query = "SELECT * FROM goals WHERE employee_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Goal g = new Goal();

                g.setGoalId(rs.getInt("goal_id"));
                g.setEmployeeId(rs.getInt("employee_id"));
                g.setDescription(rs.getString("description"));
                g.setDeadline(rs.getDate("deadline"));
                g.setPriority(rs.getString("priority"));
                g.setStatus(rs.getString("status"));

                list.add(g);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public void updateGoalStatus(int goalId, String status) {

        String query = "UPDATE goals SET status = ? WHERE goal_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, status);
            ps.setInt(2, goalId);

            ps.executeUpdate();

            System.out.println("Goal status updated!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
