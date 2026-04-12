package com.revworkforce.model;

public class PerformanceReview {

    private int reviewId;
    private int employeeId;
    private int year;
    private String selfAssessment;
    private String accomplishments;
    private String improvements;
    private int rating;
    private String managerFeedback;

    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getSelfAssessment() { return selfAssessment; }
    public void setSelfAssessment(String selfAssessment) { this.selfAssessment = selfAssessment; }

    public String getAccomplishments() { return accomplishments; }
    public void setAccomplishments(String accomplishments) { this.accomplishments = accomplishments; }

    public String getImprovements() { return improvements; }
    public void setImprovements(String improvements) { this.improvements = improvements; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getManagerFeedback() { return managerFeedback; }
    public void setManagerFeedback(String managerFeedback) { this.managerFeedback = managerFeedback; }
}