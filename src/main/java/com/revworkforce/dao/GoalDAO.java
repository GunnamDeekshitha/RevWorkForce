package com.revworkforce.dao;
import com.revworkforce.util.DBConnection;
import com.revworkforce.model.Goal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

public class GoalDAO {
    private static final Logger logger = Logger.getLogger(GoalDAO.class.getName());
    public void addGoal(Goal goal) {
        String query = "INSERT INTO goals (employee_id, description, deadline, priority, success_metrics, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, goal.getEmployeeId());
            ps.setString(2, goal.getDescription());
            ps.setDate(3, goal.getDeadline());
            ps.setString(4, goal.getPriority());
            ps.setString(5, goal.getSuccessMetrics());
            ps.setString(6, "PENDING");
            ps.executeUpdate();
        } catch (Exception e) {
            logger.warning("Error adding goal: " + e.getMessage());
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
                g.setSuccessMetrics(rs.getString("success_metrics"));
                list.add(g);
            }
        } catch (Exception e) {
            logger.warning("Error fetching goals: " + e.getMessage());
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
        } catch (Exception e) {
            logger.warning("Error updating goal status: " + e.getMessage());
        }
    }

    public List<Goal> getGoalsByManager(int managerId) {
        List<Goal> list = new ArrayList<>();
        String sql = "SELECT g.* FROM goals g " +
                "JOIN employee e ON g.employee_id = e.employee_id " +
                "WHERE e.manager_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, managerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Goal g = new Goal();
                g.setGoalId(rs.getInt("goal_id"));
                g.setEmployeeId(rs.getInt("employee_id"));
                g.setDescription(rs.getString("description"));
                g.setDeadline(rs.getDate("deadline"));
                g.setPriority(rs.getString("priority"));
                g.setStatus(rs.getString("status"));
                g.setSuccessMetrics(rs.getString("success_metrics"));
                list.add(g);
            }
        } catch (Exception e) {
            logger.warning("Error fetching manager goals: " + e.getMessage());
        }
        return list;
    }
}
