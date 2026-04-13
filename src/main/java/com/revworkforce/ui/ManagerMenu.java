package com.revworkforce.ui;

import com.revworkforce.model.*;
import com.revworkforce.service.ManagerService;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class ManagerMenu {

    private static final Logger logger = Logger.getLogger(ManagerMenu.class.getName());

    public static void show(Employee emp) {
        Scanner sc = new Scanner(System.in);
        ManagerService service = new ManagerService();

        while (true) {
            try {
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
                        try {
                            logger.info("View Leave Requests");

                            List<LeaveRequest> leaves = service.getLeaveRequests(emp.getEmployeeId());

                            if (leaves.isEmpty()) {
                                System.out.println("No leave requests found!");
                                break;
                            }

                            // 🔹 Display full details
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

                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                            System.out.println("Error updating leave!");
                        }
                        break;

                    case 2:
                        try {
                            logger.info("View Reviews");

                            List<PerformanceReview> reviews = service.getTeamReviews(emp.getEmployeeId());

                            if (reviews.isEmpty()) {
                                System.out.println("No reviews found!");
                            } else {
                                for (PerformanceReview r : reviews) {
                                    System.out.println("Review ID: " + r.getReviewId());
                                    System.out.println("Employee ID: " + r.getEmployeeId());
                                    System.out.println("Year: " + r.getYear());
                                    System.out.println("Rating: " + r.getRating());
                                    System.out.println("Self Assessment: " + r.getSelfAssessment());
                                    System.out.println("Manager Feedback: " + r.getManagerFeedback());
                                    System.out.println("----------------------");
                                }
                            }

                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                            System.out.println("Error fetching reviews!");
                        }
                        break;

                    case 3:
                        try {
                            logger.info("Give Feedback");

                            System.out.print("Enter Review ID: ");
                            int reviewId = sc.nextInt();
                            sc.nextLine();

                            System.out.print("Enter feedback: ");
                            String feedback = sc.nextLine();

                            service.giveFeedback(reviewId, feedback);

                            System.out.println("Feedback submitted!");

                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                            System.out.println("Error submitting feedback!");
                        }
                        break;

                    case 4:
                        try {
                            logger.info("View Team Members");

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

                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                            System.out.println("Error fetching team members!");
                        }
                        break;

                    case 5:
                        try {
                            logger.info("View Team Leave Balances");

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

                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                            System.out.println("Error fetching leave balances!");
                        }
                        break;

                    case 6:
                        try {
                            logger.info("View Team Goals");

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

                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                            System.out.println("Error fetching team goals!");
                        }
                        break;

                    case 7:
                        try {
                            logger.info("View Notifications");

                            List<Notification> notes = service.getNotifications(emp.getEmployeeId());

                            if (notes.isEmpty()) {
                                System.out.println("No notifications!");
                            } else {
                                System.out.println("=== Notifications ===");

                                for (Notification n : notes) {
                                    System.out.println("Message: " + n.getMessage());
                                    System.out.println("Date: " + n.getCreatedAt());
                                    System.out.println("----------------------");
                                }
                            }

                            service.markNotificationsRead(emp.getEmployeeId());

                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                            System.out.println("Error fetching notifications!");
                        }
                        break;

                    case 8:
                        try {
                            logger.info("Give Rating");

                            System.out.print("Enter Review ID: ");
                            int rid = sc.nextInt();

                            System.out.print("Enter Rating (1-5): ");
                            int rating = sc.nextInt();
                            sc.nextLine();

                            service.giveRating(rid, rating);

                            System.out.println("Rating submitted!");

                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                            System.out.println("Error submitting rating!");
                        }
                        break;

                    case 9:
                        logger.info("Logout");
                        System.out.println("Logged out!");
                        return;

                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (Exception e) {
                logger.warning("Menu error: " + e.getMessage());
                sc.nextLine();
            }
        }
    }
}