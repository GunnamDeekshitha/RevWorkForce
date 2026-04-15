package com.revworkforce.dao;

import com.revworkforce.exception.DatabaseException;
import com.revworkforce.model.Goal;
import com.revworkforce.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class GoalDAO {

    private static final Logger logger = Logger.getLogger(GoalDAO.class.getName());

    private Goal mapRow(ResultSet rs) throws SQLException {
        Goal g = new Goal();
        g.setGoalId(rs.getInt("goal_id"));
        g.setEmployeeId(rs.getInt("employee_id"));
        g.setDescription(rs.getString("description"));
        g.setDeadline(rs.getDate("deadline"));
        g.setPriority(rs.getString("priority"));
        g.setStatus(rs.getString("status"));
        g.setSuccessMetrics(rs.getString("success_metrics"));
        return g;
    }

    public void addGoal(Goal goal) {
        String sql = "INSERT INTO goals (employee_id, description, deadline, priority, success_metrics, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1,    goal.getEmployeeId());
            ps.setString(2, goal.getDescription());
            ps.setDate(3,   goal.getDeadline());
            ps.setString(4, goal.getPriority());
            ps.setString(5, goal.getSuccessMetrics());
            ps.setString(6, "NOT_STARTED");
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.severe("DB error adding goal: " + e.getMessage());
            throw new DatabaseException("Failed to add goal for employee ID: " + goal.getEmployeeId(), e);
        }
    }

    public List<Goal> getGoalsByEmployee(int empId) {
        List<Goal> list = new ArrayList<>();
        String sql = "SELECT * FROM goals WHERE employee_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
            return list;

        } catch (SQLException e) {
            logger.severe("DB error fetching goals for employee " + empId + ": " + e.getMessage());
            throw new DatabaseException("Failed to fetch goals for employee ID: " + empId, e);
        }
    }

    public void updateGoalStatus(int goalId, String status) {
        String sql = "UPDATE goals SET status = ? WHERE goal_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2,    goalId);
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.severe("DB error updating goal status for goal " + goalId + ": " + e.getMessage());
            throw new DatabaseException("Failed to update status for goal ID: " + goalId, e);
        }
    }

    public List<Goal> getGoalsByManager(int managerId) {
        List<Goal> list = new ArrayList<>();
        String sql = "SELECT g.* FROM goals g " +
                "JOIN employee e ON g.employee_id = e.employee_id " +
                "WHERE e.manager_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, managerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
            return list;

        } catch (SQLException e) {
            logger.severe("DB error fetching goals for manager " + managerId + ": " + e.getMessage());
            throw new DatabaseException("Failed to fetch team goals for manager ID: " + managerId, e);
        }
    }
}
