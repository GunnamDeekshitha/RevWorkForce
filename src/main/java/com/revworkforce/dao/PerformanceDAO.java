package com.revworkforce.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import com.revworkforce.model.PerformanceReview;
import com.revworkforce.util.DBConnection;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class PerformanceDAO {
    public void addReview(PerformanceReview review) {

        String query = "INSERT INTO performance_review (employee_id, year, self_assessment, accomplishments, improvements, rating) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, review.getEmployeeId());
            ps.setInt(2, review.getYear());
            ps.setString(3, review.getSelfAssessment());
            ps.setString(4, review.getAccomplishments());
            ps.setString(5, review.getImprovements());
            ps.setInt(6, review.getRating());

            ps.executeUpdate();

            System.out.println("Performance review submitted!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<PerformanceReview> getReviewsByEmployee(int empId) {

        List<PerformanceReview> list = new ArrayList<>();

        String query = "SELECT * FROM performance_review WHERE employee_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

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
            e.printStackTrace();
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
            e.printStackTrace();
        }

        return list;
    }
    public void addManagerFeedback(int reviewId, String feedback) {

        String query = "UPDATE performance_review SET manager_feedback = ? WHERE review_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, feedback);
            ps.setInt(2, reviewId);

            ps.executeUpdate();

            System.out.println("Feedback added successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
