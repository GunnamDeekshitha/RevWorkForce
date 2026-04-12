package com.revworkforce.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import com.revworkforce.model.PerformanceReview;
import com.revworkforce.util.DBConnection;

import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.util.logging.Logger;

public class PerformanceDAO {

    private static final Logger logger = Logger.getLogger(PerformanceDAO.class.getName());

    public void addReview(PerformanceReview review) {

        String query = "INSERT INTO performance_review (employee_id, year, self_assessment, accomplishments, improvements, rating) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            logger.info("Adding performance review for employee ID: " + review.getEmployeeId());

            ps.setInt(1, review.getEmployeeId());
            ps.setInt(2, review.getYear());
            ps.setString(3, review.getSelfAssessment());
            ps.setString(4, review.getAccomplishments());
            ps.setString(5, review.getImprovements());
            ps.setInt(6, review.getRating());

            ps.executeUpdate();
        } catch (Exception e) {
            logger.warning("Error adding performance review: " + e.getMessage());
        }
    }

    public List<PerformanceReview> getReviewsByEmployee(int empId) {

        List<PerformanceReview> list = new ArrayList<>();
        String query = "SELECT * FROM performance_review WHERE employee_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            logger.info("Fetching reviews for employee ID: " + empId);

            ps.setInt(1, empId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PerformanceReview pr = new PerformanceReview();

                pr.setReviewId(rs.getInt("review_id"));
                pr.setEmployeeId(rs.getInt("employee_id"));
                pr.setYear(rs.getInt("year"));
                pr.setSelfAssessment(rs.getString("self_assessment"));
                pr.setAccomplishments(rs.getString("accomplishments"));
                pr.setImprovements(rs.getString("improvements"));
                pr.setRating(rs.getInt("rating"));
                pr.setManagerFeedback(rs.getString("manager_feedback"));

                list.add(pr);
            }
        } catch (Exception e) {
            logger.warning("Error fetching employee reviews: " + e.getMessage());
        }

        return list;
    }

    public List<PerformanceReview> getReviewsByManager(int managerId) {

        List<PerformanceReview> list = new ArrayList<>();

        String query = "SELECT pr.* FROM performance_review pr " +
                "JOIN employee e ON pr.employee_id = e.employee_id " +
                "WHERE e.manager_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            logger.info("Fetching reviews for manager ID: " + managerId);

            ps.setInt(1, managerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PerformanceReview pr = new PerformanceReview();

                pr.setReviewId(rs.getInt("review_id"));
                pr.setEmployeeId(rs.getInt("employee_id"));
                pr.setYear(rs.getInt("year"));
                pr.setSelfAssessment(rs.getString("self_assessment"));
                pr.setAccomplishments(rs.getString("accomplishments"));
                pr.setImprovements(rs.getString("improvements"));
                pr.setRating(rs.getInt("rating"));
                pr.setManagerFeedback(rs.getString("manager_feedback"));

                list.add(pr);
            }
        } catch (Exception e) {
            logger.warning("Error fetching manager reviews: " + e.getMessage());
        }

        return list;
    }

    public void addManagerFeedback(int reviewId, String feedback) {

        String query = "UPDATE performance_review SET manager_feedback = ? WHERE review_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            logger.info("Adding feedback for review ID: " + reviewId);

            ps.setString(1, feedback);
            ps.setInt(2, reviewId);

            ps.executeUpdate();

        } catch (Exception e) {
            logger.warning("Error adding manager feedback: " + e.getMessage());
        }
    }

    public PerformanceReview getReviewById(int reviewId) {

        PerformanceReview r = null;
        String sql = "SELECT * FROM performance_review WHERE review_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            logger.info("Fetching review by ID: " + reviewId);

            ps.setInt(1, reviewId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                r = new PerformanceReview();

                r.setReviewId(rs.getInt("review_id"));
                r.setEmployeeId(rs.getInt("employee_id"));
                r.setYear(rs.getInt("year"));
            }

        } catch (Exception e) {
            logger.warning("Error fetching review by ID: " + e.getMessage());
        }

        return r;
    }

    public void updateRating(int reviewId, int rating) {

        String sql = "UPDATE performance_review SET rating=? WHERE review_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            logger.info("Updating rating for review ID: " + reviewId);

            ps.setInt(1, rating);
            ps.setInt(2, reviewId);

            ps.executeUpdate();

        } catch (Exception e) {
            logger.warning("Error updating rating: " + e.getMessage());
        }
    }
}