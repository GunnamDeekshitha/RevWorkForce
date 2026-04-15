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
                                System.out.println("----- PROFILE -----");
                                System.out.println("Name: " + e.getName());
                                System.out.println("Email: " + e.getEmail());
                                System.out.println("Phone: " + e.getPhone());
                                System.out.println("Department: " + e.getDepartment());
                                System.out.println("Designation: " + e.getDesignation());
                                System.out.println("Status: " + e.getStatus());
                            } else {
                                System.out.println("Profile not found!");
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

                            System.out.print("Enter Leave Type: ");
                            leave.setLeaveType(sc.nextLine());

                            System.out.print("Enter Start Date (yyyy-mm-dd): ");
                            leave.setStartDate(java.sql.Date.valueOf(sc.nextLine()));

                            System.out.print("Enter End Date (yyyy-mm-dd): ");
                            leave.setEndDate(java.sql.Date.valueOf(sc.nextLine()));

                            System.out.print("Enter Reason: ");
                            leave.setReason(sc.nextLine());

                            System.out.println(service.applyLeave(emp, leave));

                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                            System.out.println("Error applying leave!");
                        }
                        break;

                    case 3:
                        try {
                            logger.info("View Leaves");

                            List<LeaveRequest> leaves = service.getMyLeaves(emp.getEmployeeId());

                            if (leaves.isEmpty()) {
                                System.out.println("No leave records found!");
                            } else {
                                for (LeaveRequest l : leaves) {
                                    System.out.println("Leave ID: " + l.getLeaveId());
                                    System.out.println("Type: " + l.getLeaveType());
                                    System.out.println("From: " + l.getStartDate());
                                    System.out.println("To: " + l.getEndDate());
                                    System.out.println("Reason: " + l.getReason());
                                    System.out.println("Status: " + l.getStatus());
                                    System.out.println("Manager Comment: " + l.getManagerComment());
                                    System.out.println("----------------------");
                                }
                            }

                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                            System.out.println("Error fetching leaves!");
                        }
                        break;

                    case 4:
                        try {
                            logger.info("View Leave Balance");

                            LeaveBalance lb = service.getLeaveBalance(emp.getEmployeeId());

                            if (lb != null) {
                                System.out.println("=== Leave Balance ===");
                                System.out.println("Casual Leave: " + lb.getCasualLeave());
                                System.out.println("Sick Leave: " + lb.getSickLeave());
                                System.out.println("Paid Leave: " + lb.getPaidLeave());
                                System.out.println("Privilege Leave: " + lb.getPrivilegeLeave());
                            } else {
                                System.out.println("No leave balance found!");
                            }

                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                            System.out.println("Error fetching leave balance!");
                        }
                        break;

                    case 5:
                        try {
                            logger.info("Cancel Leave");

                            System.out.print("Enter Leave ID to cancel: ");
                            int leaveId = sc.nextInt();
                            sc.nextLine();

                            String msg = service.cancelLeave(emp.getEmployeeId(), leaveId);

                            System.out.println(msg);

                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                            System.out.println("Error cancelling leave!");
                        }
                        break;

                    case 6:
                        try {
                            logger.info("Edit Profile");

                            System.out.print("Enter new phone (press Enter to skip): ");
                            String phone = sc.nextLine();

                            System.out.print("Enter new address (press Enter to skip): ");
                            String address = sc.nextLine();

                            System.out.print("Enter emergency contact name (press Enter to skip): ");
                            String eName = sc.nextLine();

                            System.out.print("Enter emergency contact phone (press Enter to skip): ");
                            String ePhone = sc.nextLine();

                            if (!phone.isEmpty() && !phone.matches("\\d{10}")) {
                                System.out.println("Invalid phone number! Must be 10 digits.");
                                return; // or break depending on your loop structure
                            }

                            if (!ePhone.isEmpty() && !ePhone.matches("\\d{10}")) {
                                System.out.println("Invalid emergency phone!");
                                return;
                            }

                            service.updateProfile(emp.getEmployeeId(), phone, address, eName, ePhone);

                            System.out.println("Profile updated!");

                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                            System.out.println("Error updating profile!");
                        }
                        break;

                    case 7:
                        try {
                            logger.info("View Manager");

                            if (emp.getManagerId() == 0) {
                                System.out.println("No manager assigned!");
                                break;
                            }

                            Employee mgr = service.getManager(emp.getManagerId());

                            if (mgr != null) {
                                System.out.println("Manager Name: " + mgr.getName());
                                System.out.println("Manager Email: " + mgr.getEmail());
                                System.out.println("Department: " + mgr.getDepartment());
                            } else {
                                System.out.println("Manager not found!");
                            }

                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                            System.out.println("Error fetching manager details!");
                        }
                        break;

                    case 8:
                        try {
                            logger.info("View Holidays");

                            List<Holiday> holidays = service.getHolidays();

                            System.out.println("=== Company Holidays ===");

                            if (holidays.isEmpty()) {
                                System.out.println("No holidays found!");
                            } else {
                                for (Holiday h : holidays) {
                                    System.out.println(h.getHolidayDate() + " - " + h.getHolidayName());
                                }
                            }

                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                            System.out.println("Error fetching holidays!");
                        }
                        break;

                    case 9:
                        try {
                            logger.info("Employee Directory");

                            List<Employee> employees = service.getAllEmployees();

                            System.out.println("=== Employee Directory ===");

                            if (employees.isEmpty()) {
                                System.out.println("No employees found!");
                            } else {
                                for (Employee ee : employees) {
                                    System.out.println("Name: " + ee.getName());
                                    System.out.println("Email: " + ee.getEmail());
                                    System.out.println("Department: " + ee.getDepartment());
                                    System.out.println("Designation: " + ee.getDesignation());
                                    System.out.println("----------------------");
                                }
                            }

                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                            System.out.println("Error fetching employees!");
                        }
                        break;

                    case 10:
                        try {
                            logger.info("Submit Review");

                            PerformanceReview review = new PerformanceReview();
                            review.setEmployeeId(emp.getEmployeeId());

                            System.out.print("Enter year: ");
                            review.setYear(sc.nextInt());
                            sc.nextLine();

                            System.out.print("Enter self assessment: ");
                            review.setSelfAssessment(sc.nextLine());

                            System.out.print("Enter accomplishments: ");
                            review.setAccomplishments(sc.nextLine());

                            System.out.print("Enter improvements: ");
                            review.setImprovements(sc.nextLine());

                            System.out.print("Enter rating (1-5): ");
                            review.setRating(sc.nextInt());
                            sc.nextLine();

                            service.submitReview(review);

                            System.out.println("Review submitted!");

                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                            System.out.println("Error submitting review!");
                        }
                        break;

                    case 11:
                        try {
                            logger.info("View Reviews");

                            List<PerformanceReview> reviews = service.getReviews(emp.getEmployeeId());

                            if (reviews.isEmpty()) {
                                System.out.println("No reviews found!");
                            } else {
                                for (PerformanceReview r : reviews) {
                                    System.out.println("Year: " + r.getYear());
                                    System.out.println("Self Assessment: " + r.getSelfAssessment());
                                    System.out.println("Accomplishments: " + r.getAccomplishments());
                                    System.out.println("Improvements: " + r.getImprovements());
                                    System.out.println("Rating: " + r.getRating());
                                    System.out.println("Manager Feedback: " + r.getManagerFeedback());
                                    System.out.println("----------------------");
                                }
                            }

                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                            System.out.println("Error fetching reviews!");
                        }
                        break;

                    case 12:
                        try {
                            logger.info("Add Goal");

                            Goal goal = new Goal();
                            goal.setEmployeeId(emp.getEmployeeId());

                            System.out.print("Enter goal description: ");
                            goal.setDescription(sc.nextLine());

                            System.out.print("Enter deadline (YYYY-MM-DD): ");
                            goal.setDeadline(java.sql.Date.valueOf(sc.nextLine()));

                            System.out.print("Enter priority (HIGH/MEDIUM/LOW): ");
                            goal.setPriority(sc.nextLine());

                            System.out.print("Enter Success Metrics: ");
                            goal.setSuccessMetrics(sc.nextLine());

                            service.addGoal(goal);

                            System.out.println("Goal added!");

                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                            System.out.println("Error adding goal!");
                        }
                        break;

                    case 13:
                        try {
                            logger.info("View Goals");

                            List<Goal> goals = service.getGoals(emp.getEmployeeId());

                            if (goals.isEmpty()) {
                                System.out.println("No goals found!");
                            } else {
                                for (Goal g : goals) {
                                    System.out.println("Goal ID: " + g.getGoalId());
                                    System.out.println("Description: " + g.getDescription());
                                    System.out.println("Deadline: " + g.getDeadline());
                                    System.out.println("Priority: " + g.getPriority());
                                    System.out.println("Status: " + g.getStatus());
                                    System.out.println("Success Metrics: " + g.getSuccessMetrics());
                                    System.out.println("----------------------");
                                }
                            }

                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                            System.out.println("Error fetching goals!");
                        }
                        break;

                    case 14:
                        try {
                            logger.info("Update Goal");

                            System.out.print("Enter Goal ID: ");
                            int goalId = sc.nextInt();
                            sc.nextLine(); // IMPORTANT

                            System.out.print("Enter new status (PENDING/IN_PROGRESS/COMPLETED): ");
                            String status = sc.nextLine();

                            service.updateGoalStatus(goalId, status);

                            System.out.println("Goal updated!");

                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                            System.out.println("Error updating goal!");
                        }
                        break;

                    case 15:
                        try {
                            logger.info("View Announcements");

                            List<Announcement> list = service.getAnnouncements();

                            System.out.println("=== Announcements ===");

                            if (list.isEmpty()) {
                                System.out.println("No announcements found!");
                            } else {
                                for (Announcement a : list) {
                                    System.out.println("Title: " + a.getTitle());
                                    System.out.println("Message: " + a.getMessage());
                                    System.out.println("Date: " + a.getCreatedAt());
                                    System.out.println("----------------------");
                                }
                            }

                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                            System.out.println("Error fetching announcements!");
                        }
                        break;

                    case 16:
                        try {
                            logger.info("View Birthdays");

                            List<Employee> birthdays = service.getBirthdays();

                            System.out.println("=== Upcoming Birthdays ===");

                            if (birthdays.isEmpty()) {
                                System.out.println("No upcoming birthdays found!");
                            } else {
                                for (Employee eb : birthdays) {
                                    System.out.println(eb.getName() + " - " + eb.getDateOfBirth());
                                }
                            }

                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                            System.out.println("Error fetching birthdays!");
                        }
                        break;

                    case 17:
                        try {
                            logger.info("View Anniversaries");

                            List<Employee> anniversaries = service.getAnniversaries();

                            System.out.println("=== Work Anniversaries ===");

                            if (anniversaries.isEmpty()) {
                                System.out.println("No anniversaries found!");
                            } else {
                                for (Employee ea : anniversaries) {
                                    System.out.println(ea.getName() + " - " + ea.getJoiningDate());
                                }
                            }

                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                            System.out.println("Error fetching anniversaries!");
                        }
                        break;

                    case 18:
                        try {
                            logger.info("View Notifications");

                            List<Notification> notes = service.getNotifications(emp.getEmployeeId());

                            if (notes.isEmpty()) {
                                System.out.println("No notifications found!");
                            } else {
                                for (Notification n : notes) {
                                    System.out.println("Message: " + n.getMessage());
                                    System.out.println("Status: " + n.getStatus());
                                    System.out.println("Date: " + n.getCreatedAt());
                                    System.out.println("----------------------");
                                }
                            }

                            service.markNotificationsRead(emp.getEmployeeId());

                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                            System.out.println("Error fetching notifications!");
                        }
                        break;
                    case 19:
                        try {
                            logger.info("Change Password");

                            System.out.print("Enter current password: ");
                            String oldPass = sc.nextLine();

                            System.out.print("Enter new password: ");
                            String newPass = sc.nextLine();

                            String msg = authService.changePassword(emp.getEmployeeId(), oldPass, newPass);

                            System.out.println(msg);

                        } catch (Exception ex) {
                            logger.warning(ex.getMessage());
                            System.out.println("Error changing password!");
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