package com.revworkforce.ui;

import com.revworkforce.model.*;
import com.revworkforce.service.*;

import java.util.List;
import java.util.Scanner;

public class EmployeeMenu {

    public static void show(Employee emp) {
        Scanner sc = new Scanner(System.in);
        EmployeeService service = new EmployeeService();
        AuthService authService = new AuthService();
        int unread = service.getUnreadCount(emp.getEmployeeId());
        System.out.println("Unread Notifications: " + unread);


        while (true) {
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
                    Employee e = service.getProfile(emp.getEmployeeId());
                    if (e != null) {
                        System.out.println("Name: " + e.getName());
                        System.out.println("Email: " + e.getEmail());
                        System.out.println("Phone: " + e.getPhone());
                        System.out.println("Department: " + e.getDepartment());
                        System.out.println("Designation: " + e.getDesignation());
                        System.out.println("Emergency Contact Name: " + e.getEmergencyContactName());
                        System.out.println("Emergency Contact Phone: " + e.getEmergencyContactPhone());
                    }
                    break;
                case 2:
                    Employee manager = service.getManager(emp.getManagerId());
                    if (manager != null) {
                        System.out.println("Reporting Manager: " + manager.getName());
                        System.out.println("Manager Email: " + manager.getEmail());
                    }
                    LeaveRequest leave = new LeaveRequest();
                    leave.setEmployeeId(emp.getEmployeeId());
                    System.out.print("Enter Leave Type (CL/SL/PL/PR): ");
                    leave.setLeaveType(sc.nextLine());
                    System.out.print("Enter Start Date (YYYY-MM-DD): ");
                    leave.setStartDate(java.sql.Date.valueOf(sc.nextLine()));
                    System.out.print("Enter End Date (YYYY-MM-DD): ");
                    leave.setEndDate(java.sql.Date.valueOf(sc.nextLine()));
                    System.out.print("Enter Reason: ");
                    leave.setReason(sc.nextLine());
                    String result = service.applyLeave(emp, leave);
                    System.out.println(result);
                    break;
                case 3:
                    List<LeaveRequest> leaves = service.getMyLeaves(emp.getEmployeeId());
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
                    break;
                case 4:
                    LeaveBalance lb = service.getLeaveBalance(emp.getEmployeeId());
                    if (lb != null) {
                        System.out.println("Casual Leave: " + lb.getCasualLeave());
                        System.out.println("Sick Leave: " + lb.getSickLeave());
                        System.out.println("Paid Leave: " + lb.getPaidLeave());
                        System.out.println("Privilege Leave: " + lb.getPrivilegeLeave());
                    } else {
                        System.out.println("No leave balance found!");
                    }
                    break;
                case 5:
                    System.out.print("Enter Leave ID to cancel: ");
                    int leaveId = sc.nextInt();
                    sc.nextLine();
                    String msg = service.cancelLeave(emp.getEmployeeId(), leaveId);
                    System.out.println(msg);
                    break;

                case 6:
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
                        break;
                    }

                    if (!ePhone.isEmpty() && !phone.matches("\\d{10}")) {
                        System.out.println("Invalid emergency phone!");
                        break;
                    }
                    service.updateProfile(emp.getEmployeeId(), phone, address, eName, ePhone);
                    System.out.println("Profile updated!");
                    break;
                case 7:
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
                    break;
                case 8:
                    List<Holiday> holidays = service.getHolidays();

                    System.out.println("=== Company Holidays ===");

                    for (Holiday h : holidays) {
                        System.out.println(h.getHolidayDate() + " - " + h.getHolidayName());
                    }
                    break;
                case 9:
                    List<Employee> employees = service.getAllEmployees();

                    System.out.println("=== Employee Directory ===");

                    for (Employee ee : employees) {
                        System.out.println("Name: " + ee.getName());
                        System.out.println("Email: " + ee.getEmail());
                        System.out.println("Department: " + ee.getDepartment());
                        System.out.println("Designation: " + ee.getDesignation());
                        System.out.println("----------------------");
                    }
                    break;
                case 10:
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
                    break;

                case 11:
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
                    break;

                // 🔹 ADD GOAL
                case 12:
                    Goal goal = new Goal();

                    goal.setEmployeeId(emp.getEmployeeId());

                    System.out.print("Enter goal description: ");
                    goal.setDescription(sc.nextLine());

                    System.out.print("Enter deadline (YYYY-MM-DD): ");
                    goal.setDeadline(java.sql.Date.valueOf(sc.nextLine()));

                    System.out.print("Enter priority (HIGH/MEDIUM/LOW): ");
                    goal.setPriority(sc.nextLine());

                    service.addGoal(goal);
                    System.out.println("Goal added!");

                    System.out.print("Enter Success Metrics: ");
                    goal.setSuccessMetrics(sc.nextLine());
                    break;

                // 🔹 VIEW GOALS
                case 13:
                    List<Goal> goals = service.getGoals(emp.getEmployeeId());

                    for (Goal g : goals) {
                        System.out.println("Goal ID: " + g.getGoalId());
                        System.out.println("Description: " + g.getDescription());
                        System.out.println("Deadline: " + g.getDeadline());
                        System.out.println("Priority: " + g.getPriority());
                        System.out.println("Status: " + g.getStatus());
                        System.out.println("Success Metrics: " + g.getSuccessMetrics());
                        System.out.println("----------------------");
                    }
                    break;
                case 14:
                    System.out.print("Enter Goal ID: ");
                    int goalId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter new status (PENDING/IN_PROGRESS/COMPLETED): ");
                    String status = sc.nextLine();

                    service.updateGoalStatus(goalId, status);
                    System.out.println("Goal updated!");
                    break;
                case 15:
                    List<Announcement> list = service.getAnnouncements();

                    System.out.println("=== Announcements ===");

                    for (Announcement a : list) {
                        System.out.println("Title: " + a.getTitle());
                        System.out.println("Message: " + a.getMessage());
                        System.out.println("Date: " + a.getCreatedAt());
                        System.out.println("----------------------");
                    }
                    break;
                case 16:
                    List<Employee> birthdays = service.getBirthdays();

                    System.out.println("=== Upcoming Birthdays ===");

                    for (Employee eb : birthdays) {
                        System.out.println(eb.getName() + " - " + eb.getDateOfBirth());
                    }
                    break;

                case 17:
                    List<Employee> anniversaries = service.getAnniversaries();

                    System.out.println("=== Work Anniversaries ===");

                    for (Employee ea : anniversaries) {
                        System.out.println(ea.getName() + " - " + ea.getJoiningDate());
                    }
                    break;
                case 18:
                    List<Notification> notes = service.getNotifications(emp.getEmployeeId());

                    for (Notification n : notes) {
                        System.out.println("Message: " + n.getMessage());
                        System.out.println("Status: " + n.getStatus());
                        System.out.println("Date: " + n.getCreatedAt());
                        System.out.println("----------------------");
                    }

                    service.markNotificationsRead(emp.getEmployeeId());
                    break;
                case 19:
                    System.out.print("Enter current password: ");
                    String oldPass = sc.nextLine();

                    System.out.print("Enter new password: ");
                    String newPass = sc.nextLine();

                    String msgg = authService.changePassword(emp.getEmployeeId(), oldPass, newPass);
                    System.out.println(msgg);
                    break;
                case 20:
                    System.out.println("Logged out!");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }

        }
    }
}