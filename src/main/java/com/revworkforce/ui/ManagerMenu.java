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

                            for (LeaveRequest l : leaves) {
                                System.out.println(l.getLeaveId());
                            }

                            int id = sc.nextInt();
                            sc.nextLine();

                            String status = sc.nextLine().toUpperCase();
                            String comment = sc.nextLine();

                            service.updateLeaveStatus(id, status, comment);
                            System.out.println("Leave updated!");

                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                        }
                        break;

                    case 2:
                        try {
                            logger.info("View Reviews");
                            service.getTeamReviews(emp.getEmployeeId())
                                    .forEach(r -> System.out.println(r.getReviewId()));
                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                        }
                        break;

                    case 3:
                        try {
                            logger.info("Give Feedback");
                            int reviewId = sc.nextInt();
                            sc.nextLine();
                            String feedback = sc.nextLine();

                            service.giveFeedback(reviewId, feedback);
                            System.out.println("Feedback submitted!");
                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                        }
                        break;

                    case 4:
                        try {
                            logger.info("Team Members");
                            service.getTeamMembers(emp.getEmployeeId())
                                    .forEach(e -> System.out.println(e.getName()));
                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                        }
                        break;

                    case 5:
                        try {
                            logger.info("Leave Balances");
                            service.getTeamLeaveBalances(emp.getEmployeeId())
                                    .forEach(lb -> System.out.println(lb.getEmployeeId()));
                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                        }
                        break;

                    case 6:
                        try {
                            logger.info("Team Goals");
                            service.getTeamGoals(emp.getEmployeeId())
                                    .forEach(g -> System.out.println(g.getGoalId()));
                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                        }
                        break;

                    case 7:
                        try {
                            logger.info("Notifications");
                            service.getNotifications(emp.getEmployeeId())
                                    .forEach(n -> System.out.println(n.getMessage()));

                            service.markNotificationsRead(emp.getEmployeeId());
                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                        }
                        break;

                    case 8:
                        try {
                            logger.info("Give Rating");
                            int rid = sc.nextInt();
                            int rating = sc.nextInt();

                            service.giveRating(rid, rating);
                            System.out.println("Rating submitted!");
                        } catch (Exception e) {
                            logger.warning(e.getMessage());
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