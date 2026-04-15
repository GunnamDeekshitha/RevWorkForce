package com.revworkforce.dao;

import com.revworkforce.exception.DatabaseException;
import com.revworkforce.model.PerformanceReview;
import com.revworkforce.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PerformanceDAO {

    private static final Logger logger = Logger.getLogger(PerformanceDAO.class.getName());

    private PerformanceReview mapRow(ResultSet rs) throws SQLException {
        PerformanceReview pr = new PerformanceReview();
        pr.setReviewId(rs.getInt("review_id"));
        pr.setEmployeeId(rs.getInt("employee_id"));
        pr.setYear(rs.getInt("year"));
        pr.setSelfAssessment(rs.getString("self_assessment"));
        pr.setAccomplishments(rs.getString("accomplishments"));
        pr.setImprovements(rs.getString("improvements"));
        pr.setRating(rs.getInt("rating"));
        pr.setManagerFeedback(rs.getString("manager_feedback"));
        return pr;
    }

    public void addReview(PerformanceReview review) {
        String sql = "INSERT INTO performance_review (employee_id, year, self_assessment, accomplishments, improvements, rating) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, review.getEmployeeId());
            ps.setInt(2, review.getYear());
            ps.setString(3, review.getSelfAssessment());
            ps.setString(4, review.getAccomplishments());
            ps.setString(5, review.getImprovements());
            ps.setInt(6, review.getRating());
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.severe("DB error adding performance review: " + e.getMessage());
            throw new DatabaseException("Failed to add performance review for employee ID: " + review.getEmployeeId(), e);
        }
    }

    public PerformanceReview getReviewById(int reviewId) {
        String sql = "SELECT * FROM performance_review WHERE review_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reviewId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? mapRow(rs) : null;

        } catch (SQLException e) {
            logger.severe("DB error fetching review " + reviewId + ": " + e.getMessage());
            throw new DatabaseException("Failed to fetch performance review with ID: " + reviewId, e);
        }
    }

    public List<PerformanceReview> getReviewsByEmployee(int empId) {
        List<PerformanceReview> list = new ArrayList<>();
        String sql = "SELECT * FROM performance_review WHERE employee_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
            return list;

        } catch (SQLException e) {
            logger.severe("DB error fetching reviews for employee " + empId + ": " + e.getMessage());
            throw new DatabaseException("Failed to fetch performance reviews for employee ID: " + empId, e);
        }
    }

    public List<PerformanceReview> getReviewsByManager(int managerId) {
        List<PerformanceReview> list = new ArrayList<>();
        String sql = "SELECT pr.* FROM performance_review pr " +
                "JOIN employee e ON pr.employee_id = e.employee_id " +
                "WHERE e.manager_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, managerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
            return list;

        } catch (SQLException e) {
            logger.severe("DB error fetching reviews for manager " + managerId + ": " + e.getMessage());
            throw new DatabaseException("Failed to fetch team reviews for manager ID: " + managerId, e);
        }
    }

    public void addManagerFeedback(int reviewId, String feedback) {
        String sql = "UPDATE performance_review SET manager_feedback = ? WHERE review_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, feedback);
            ps.setInt(2, reviewId);
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.severe("DB error adding feedback to review " + reviewId + ": " + e.getMessage());
            throw new DatabaseException("Failed to add manager feedback for review ID: " + reviewId, e);
        }
    }

    public void updateRating(int reviewId, int rating) {
        String sql = "UPDATE performance_review SET rating = ? WHERE review_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, rating);
            ps.setInt(2, reviewId);
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.severe("DB error updating rating for review " + reviewId + ": " + e.getMessage());
            throw new DatabaseException("Failed to update rating for review ID: " + reviewId, e);
        }
    }
}
