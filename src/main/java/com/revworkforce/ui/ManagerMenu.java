package com.revworkforce.ui;

import com.revworkforce.dao.LeaveDAO;
import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveRequest;
import com.revworkforce.model.PerformanceReview;
import com.revworkforce.dao.PerformanceDAO;

import java.util.List;
import java.util.Scanner;

public class ManagerMenu {

    public static void show(Employee emp) {
        Scanner sc = new Scanner(System.in);
        LeaveDAO leaveDAO = new LeaveDAO();

        while (true) {
            System.out.println("\nWelcome Manager: " + emp.getName());
            System.out.println("1. View Leave Requests");
            System.out.println("2. View Team Performance Reviews");
            System.out.println("3. Give Feedback");
            System.out.println("4. Logout");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    List<LeaveRequest> leaves = leaveDAO.getLeavesByManager(emp.getEmployeeId());

                    for (LeaveRequest l : leaves) {
                        System.out.println("Leave ID: " + l.getLeaveId());
                        System.out.println("Employee ID: " + l.getEmployeeId());
                        System.out.println("Type: " + l.getLeaveType());
                        System.out.println("From: " + l.getStartDate());
                        System.out.println("To: " + l.getEndDate());
                        System.out.println("Reason: " + l.getReason());
                        System.out.println("Status: " + l.getStatus());
                        System.out.println("----------------------");
                    }

                    System.out.print("Enter Leave ID to update: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Approve or Reject: ");
                    String status = sc.nextLine().toUpperCase();

                    System.out.print("Enter comment: ");
                    String comment = sc.nextLine();

                    leaveDAO.updateLeaveStatus(id, status, comment);
                    break;
                case 2:
                    PerformanceDAO pDAO = new PerformanceDAO();

                    List<PerformanceReview> reviews = pDAO.getReviewsByManager(emp.getEmployeeId());

                    for (PerformanceReview r : reviews) {
                        System.out.println("Review ID: " + r.getReviewId());
                        System.out.println("Employee ID: " + r.getEmployeeId());
                        System.out.println("Year: " + r.getYear());
                        System.out.println("Rating: " + r.getRating());
                        System.out.println("Self Assessment: " + r.getSelfAssessment());
                        System.out.println("Manager Feedback: " + r.getManagerFeedback());
                        System.out.println("----------------------");
                    }

                    break;
                case 3:
                    PerformanceDAO pDAO2 = new PerformanceDAO();

                    System.out.print("Enter Review ID: ");
                    int reviewId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter feedback: ");
                    String feedback = sc.nextLine();

                    pDAO2.addManagerFeedback(reviewId, feedback);

                    break;
                    case 4:
                    System.out.println("Logged out!");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}