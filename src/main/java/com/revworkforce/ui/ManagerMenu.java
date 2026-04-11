package com.revworkforce.ui;

import com.revworkforce.model.*;
import com.revworkforce.service.ManagerService;

import java.util.List;
import java.util.Scanner;

public class ManagerMenu {

    public static void show(Employee emp) {
        Scanner sc = new Scanner(System.in);
        ManagerService service = new ManagerService();

        while (true) {

            System.out.println("\nWelcome Manager: " + emp.getName());
            int unread = service.getUnreadCount(emp.getEmployeeId());
            System.out.println(" Unread Notifications: " + unread);
            System.out.println("1. View Leave Requests");
            System.out.println("2. View Team Performance Reviews");
            System.out.println("3. Give Feedback");
            System.out.println("4. View Team Members");
            System.out.println("5. View Team Leave Balance");
            System.out.println("6. View Team Goals");
            System.out.println("7. View Notifications");
            System.out.println("8. Give Rating");
            System.out.println("9. Logout");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    List<LeaveRequest> leaves = service.getLeaveRequests(emp.getEmployeeId());

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

                    service.updateLeaveStatus(id, status, comment);
                    System.out.println("Leave updated!");
                    break;


                case 2:
                    List<PerformanceReview> reviews = service.getTeamReviews(emp.getEmployeeId());

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
                    System.out.print("Enter Review ID: ");
                    int reviewId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter feedback: ");
                    String feedback = sc.nextLine();

                    service.giveFeedback(reviewId, feedback);
                    System.out.println("Feedback submitted!");
                    break;

                case 4:
                    List<Employee> team = service.getTeamMembers(emp.getEmployeeId());

                    if (team.isEmpty()) {
                        System.out.println("No team members found!");
                    } else {
                        System.out.println("=== Your Team ===");

                        for (Employee e : team) {
                            System.out.println("ID: " + e.getEmployeeId());
                            System.out.println("Name: " + e.getName());
                            System.out.println("Email: " + e.getEmail());
                            System.out.println("Department: " + e.getDepartment());
                            System.out.println("Designation: " + e.getDesignation());
                            System.out.println("----------------------");
                        }
                    }
                    break;

                case 5:
                    List<LeaveBalance> balances = service.getTeamLeaveBalances(emp.getEmployeeId());

                    if (balances.isEmpty()) {
                        System.out.println("No team data found!");
                    } else {
                        System.out.println("=== Team Leave Balances ===");

                        for (LeaveBalance lb : balances) {
                            System.out.println("Employee ID: " + lb.getEmployeeId());
                            System.out.println("CL: " + lb.getCasualLeave());
                            System.out.println("SL: " + lb.getSickLeave());
                            System.out.println("PL: " + lb.getPaidLeave());
                            System.out.println("----------------------");
                        }
                    }
                    break;
                case 6:
                    List<Goal> goals = service.getTeamGoals(emp.getEmployeeId());

                    if (goals.isEmpty()) {
                        System.out.println("No team goals found!");
                    } else {
                        System.out.println("=== Team Goals ===");

                        for (Goal g : goals) {
                            System.out.println("Goal ID: " + g.getGoalId());
                            System.out.println("Employee ID: " + g.getEmployeeId());
                            System.out.println("Description: " + g.getDescription());
                            System.out.println("Deadline: " + g.getDeadline());
                            System.out.println("Priority: " + g.getPriority());
                            System.out.println("Status: " + g.getStatus());
                            System.out.println("Success Metrics: " + g.getSuccessMetrics());
                            System.out.println("----------------------");
                        }
                    }
                    break;
                case 7:
                    List<Notification> notes = service.getNotifications(emp.getEmployeeId());

                    if (notes.isEmpty()) {
                        System.out.println("No notifications!");
                    } else {
                        System.out.println("=== Notifications ===");

                        for (Notification n : notes) {
                            System.out.println(n.getMessage());
                            System.out.println("Date: " + n.getCreatedAt());
                            System.out.println("----------------------");
                        }
                    }

                    service.markNotificationsRead(emp.getEmployeeId());
                    break;
                case 8:
                    System.out.print("Enter Review ID: ");
                    int rid = sc.nextInt();

                    System.out.print("Enter Rating (1-5): ");
                    int rating = sc.nextInt();

                    service.giveRating(rid, rating);

                    System.out.println("Rating submitted!");
                    break;
                case 9:
                    System.out.println("Logged out!");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}