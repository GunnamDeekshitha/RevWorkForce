package com.revworkforce.ui;

import com.revworkforce.model.*;
import com.revworkforce.service.*;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class EmployeeMenu {

    private static final Logger logger = Logger.getLogger(EmployeeMenu.class.getName());

    public static void show(Employee emp) {
        Scanner sc = new Scanner(System.in);
        EmployeeService service = new EmployeeService();
        AuthService authService = new AuthService();

        int unread = service.getUnreadCount(emp.getEmployeeId());
        System.out.println("Unread Notifications: " + unread);

        while (true) {
            try {
                System.out.println("\nWelcome Employee: " + emp.getName());
                System.out.println("1. View Profile");
                System.out.println("2. Apply Leave");
                System.out.println("3. View My Leaves");
                System.out.println("4. View Leave Balance");
                System.out.println("5. Cancel Leave");
                System.out.println("6. Edit Profile");
                System.out.println("7. View Manager Details");
                System.out.println("8. View Holiday Calendar");
                System.out.println("9. View Employee Directory");
                System.out.println("10. Submit Performance Review");
                System.out.println("11. View Performance Reviews");
                System.out.println("12. Add Goal");
                System.out.println("13. View Goals");
                System.out.println("14. Update Goal Status");
                System.out.println("15. View Announcements");
                System.out.println("16. View Birthdays");
                System.out.println("17. View Work Anniversaries");
                System.out.println("18. View Notifications");
                System.out.println("19. Change Password");
                System.out.println("20. Logout");

                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {

                    case 1:
                        try {
                            logger.info("View Profile");
                            Employee e = service.getProfile(emp.getEmployeeId());
                            if (e != null) {
                                System.out.println(e.getName());
                            }
                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                        }
                        break;

                    case 2:
                        try {
                            logger.info("Apply Leave");
                            LeaveRequest leave = new LeaveRequest();
                            leave.setEmployeeId(emp.getEmployeeId());

                            leave.setLeaveType(sc.nextLine());
                            leave.setStartDate(java.sql.Date.valueOf(sc.nextLine()));
                            leave.setEndDate(java.sql.Date.valueOf(sc.nextLine()));
                            leave.setReason(sc.nextLine());

                            System.out.println(service.applyLeave(emp, leave));
                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                        }
                        break;

                    case 3:
                        try {
                            logger.info("View Leaves");
                            service.getMyLeaves(emp.getEmployeeId())
                                    .forEach(l -> System.out.println(l.getLeaveId()));
                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                        }
                        break;

                    case 4:
                        try {
                            logger.info("View Leave Balance");
                            LeaveBalance lb = service.getLeaveBalance(emp.getEmployeeId());
                            if (lb != null) System.out.println(lb.getCasualLeave());
                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                        }
                        break;

                    case 5:
                        try {
                            logger.info("Cancel Leave");
                            int id = sc.nextInt();
                            System.out.println(service.cancelLeave(emp.getEmployeeId(), id));
                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                        }
                        break;

                    case 6:
                        try {
                            logger.info("Edit Profile");
                            service.updateProfile(emp.getEmployeeId(),
                                    sc.nextLine(), sc.nextLine(),
                                    sc.nextLine(), sc.nextLine());
                            System.out.println("Updated!");
                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                        }
                        break;

                    case 7:
                        try {
                            logger.info("View Manager");
                            Employee mgr = service.getManager(emp.getManagerId());
                            if (mgr != null) System.out.println(mgr.getName());
                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                        }
                        break;

                    case 8:
                        try {
                            logger.info("View Holidays");
                            service.getHolidays().forEach(h -> System.out.println(h.getHolidayName()));
                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                        }
                        break;

                    case 9:
                        try {
                            logger.info("Employee Directory");
                            service.getAllEmployees().forEach(e -> System.out.println(e.getName()));
                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                        }
                        break;

                    case 10:
                        try {
                            logger.info("Submit Review");
                            PerformanceReview r = new PerformanceReview();
                            r.setEmployeeId(emp.getEmployeeId());
                            r.setYear(sc.nextInt()); sc.nextLine();
                            r.setSelfAssessment(sc.nextLine());
                            r.setAccomplishments(sc.nextLine());
                            r.setImprovements(sc.nextLine());
                            r.setRating(sc.nextInt()); sc.nextLine();

                            service.submitReview(r);
                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                        }
                        break;

                    case 11:
                        try {
                            logger.info("View Reviews");
                            service.getReviews(emp.getEmployeeId())
                                    .forEach(r -> System.out.println(r.getYear()));
                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                        }
                        break;

                    case 12:
                        try {
                            logger.info("Add Goal");
                            Goal g = new Goal();
                            g.setEmployeeId(emp.getEmployeeId());
                            g.setDescription(sc.nextLine());
                            g.setDeadline(java.sql.Date.valueOf(sc.nextLine()));
                            g.setPriority(sc.nextLine());

                            service.addGoal(g);
                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                        }
                        break;

                    case 13:
                        try {
                            logger.info("View Goals");
                            service.getGoals(emp.getEmployeeId())
                                    .forEach(g -> System.out.println(g.getDescription()));
                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                        }
                        break;

                    case 14:
                        try {
                            logger.info("Update Goal");
                            service.updateGoalStatus(sc.nextInt(), sc.nextLine());
                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                        }
                        break;

                    case 15:
                        try {
                            logger.info("Announcements");
                            service.getAnnouncements().forEach(a -> System.out.println(a.getTitle()));
                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                        }
                        break;

                    case 16:
                        try {
                            logger.info("Birthdays");
                            service.getBirthdays().forEach(e -> System.out.println(e.getName()));
                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                        }
                        break;

                    case 17:
                        try {
                            logger.info("Anniversaries");
                            service.getAnniversaries().forEach(e -> System.out.println(e.getName()));
                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                        }
                        break;

                    case 18:
                        try {
                            logger.info("Notifications");
                            service.getNotifications(emp.getEmployeeId())
                                    .forEach(n -> System.out.println(n.getMessage()));
                            service.markNotificationsRead(emp.getEmployeeId());
                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                        }
                        break;

                    case 19:
                        try {
                            logger.info("Change Password");
                            String msg = authService.changePassword(emp.getEmployeeId(),
                                    sc.nextLine(), sc.nextLine());
                            System.out.println(msg);
                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                        }
                        break;

                    case 20:
                        logger.info("Logout");
                        System.out.println("Logged out!");
                        return;

                    default:
                        System.out.println("Invalid!");
                }

            } catch (Exception e) {
                logger.warning("Menu error: " + e.getMessage());
                sc.nextLine();
            }
        }
    }
}