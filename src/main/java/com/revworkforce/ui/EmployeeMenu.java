package com.revworkforce.ui;

import com.revworkforce.dao.EmployeeDAO;
import com.revworkforce.dao.LeaveDAO;
import com.revworkforce.dao.LeaveBalanceDAO;
import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveRequest;
import com.revworkforce.model.LeaveBalance;
import com.revworkforce.model.Holiday;
import com.revworkforce.dao.HolidayDAO;
import com.revworkforce.dao.PerformanceDAO;
import com.revworkforce.model.PerformanceReview;
import com.revworkforce.model.Goal;
import com.revworkforce.dao.GoalDAO;
import java.util.List;
import java.util.Scanner;

public class EmployeeMenu {

    public static void show(Employee emp) {
        Scanner sc = new Scanner(System.in);
        EmployeeDAO dao = new EmployeeDAO();

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
            System.out.println("15. Logout");

            int choice = sc.nextInt();
            sc.nextLine(); // 🔥 clear buffer

            switch (choice) {

                // 🔹 VIEW PROFILE
                case 1:
                    Employee e = dao.getEmployeeById(emp.getEmployeeId());

                    if (e != null) {
                        System.out.println("Name: " + e.getName());
                        System.out.println("Email: " + e.getEmail());
                        System.out.println("Phone: " + e.getPhone());
                        System.out.println("Department: " + e.getDepartment());
                        System.out.println("Designation: " + e.getDesignation());
                    }
                    break;

                // 🔹 APPLY LEAVE
                case 2:
                    EmployeeDAO daoo = new EmployeeDAO();

                    int managerIdd = emp.getManagerId();

                    if (managerIdd != 0) {
                        Employee manager = daoo.getEmployeeById(managerIdd);

                        if (manager != null) {
                            System.out.println("Reporting Manager: " + manager.getName());
                            System.out.println("Manager Email: " + manager.getEmail());
                        }
                    }
                    LeaveDAO leaveDAO = new LeaveDAO();
                    LeaveBalanceDAO lbDAO = new LeaveBalanceDAO();
                    LeaveRequest leave = new LeaveRequest();

                    leave.setEmployeeId(emp.getEmployeeId());

                    System.out.print("Enter Leave Type (CL/SL/PL): ");
                    String type = sc.nextLine();
                    leave.setLeaveType(type);

                    System.out.print("Enter Start Date (YYYY-MM-DD): ");
                    leave.setStartDate(java.sql.Date.valueOf(sc.nextLine()));

                    System.out.print("Enter End Date (YYYY-MM-DD): ");
                    leave.setEndDate(java.sql.Date.valueOf(sc.nextLine()));

                    System.out.print("Enter Reason: ");
                    leave.setReason(sc.nextLine());

                    // 🔥 Calculate days
                    int days = (int) ((leave.getEndDate().getTime() - leave.getStartDate().getTime()) / (1000 * 60 * 60 * 24)) + 1;

                    // 🔥 Validation
                    if (lbDAO.hasEnoughLeave(emp.getEmployeeId(), type, days)) {

                        leaveDAO.applyLeave(leave);
                        lbDAO.updateLeaveBalance(emp.getEmployeeId(), type, days);

                        System.out.println("Leave applied and balance updated!");

                    } else {
                        System.out.println("Not enough leave balance!");
                    }

                    break;

                // 🔹 VIEW MY LEAVES
                case 3:
                    LeaveDAO leaveDAO2 = new LeaveDAO();
                    List<LeaveRequest> leaves = leaveDAO2.getLeavesByEmployeeId(emp.getEmployeeId());

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
                    break; // 🔥 IMPORTANT FIX

                // 🔹 VIEW LEAVE BALANCE
                case 4:
                    LeaveBalanceDAO lbDAO2 = new LeaveBalanceDAO();
                    LeaveBalance lb = lbDAO2.getLeaveBalance(emp.getEmployeeId());

                    if (lb != null) {
                        System.out.println("Casual Leave: " + lb.getCasualLeave());
                        System.out.println("Sick Leave: " + lb.getSickLeave());
                        System.out.println("Paid Leave: " + lb.getPaidLeave());
                    } else {
                        System.out.println("No leave balance found!");
                    }
                    break;

                // 🔹 CANCEL LEAVE
                case 5:
                    LeaveDAO leaveDAO3 = new LeaveDAO();
                    LeaveBalanceDAO lbDAO3 = new LeaveBalanceDAO();

                    System.out.print("Enter Leave ID to cancel: ");
                    int leaveId = sc.nextInt();
                    sc.nextLine(); // clear buffer

                    LeaveRequest leaveObj = leaveDAO3.getLeaveById(leaveId);

                    if (leaveObj != null
                            && leaveObj.getEmployeeId() == emp.getEmployeeId() // 🔥 SECURITY CHECK
                            && leaveObj.getStatus().equalsIgnoreCase("PENDING")) {

                        int cancelDays = (int) ((leaveObj.getEndDate().getTime() - leaveObj.getStartDate().getTime()) / (1000 * 60 * 60 * 24)) + 1;

                        lbDAO3.restoreLeaveBalance(leaveObj.getEmployeeId(), leaveObj.getLeaveType(), cancelDays);

                        leaveDAO3.cancelLeave(leaveId);

                        System.out.println("Leave cancelled and balance restored!");

                    } else {
                        System.out.println("Cannot cancel this leave!");
                    }

                    break;

                // 🔹 LOGOUT

                case 6:
                    EmployeeDAO dao7 = new EmployeeDAO();

                    System.out.print("Enter new phone: ");
                    String phone = sc.nextLine();

                    System.out.print("Enter new address: ");
                    String address = sc.nextLine();

                    dao7.updateEmployeeProfile(emp.getEmployeeId(), phone, address);

                    break;
                case 7:
                    EmployeeDAO dao8 = new EmployeeDAO();

                    int managerId = emp.getManagerId();

                    if (managerId == 0) {
                        System.out.println("No manager assigned!");
                        break;
                    }

                    Employee manager = dao8.getEmployeeById(managerId);

                    if (manager != null) {
                        System.out.println("Manager Name: " + manager.getName());
                        System.out.println("Manager Email: " + manager.getEmail());
                        System.out.println("Department: " + manager.getDepartment());
                    } else {
                        System.out.println("Manager not found!");
                    }

                    break;
                case 8:
                    HolidayDAO holidayDAO = new HolidayDAO();

                    List<Holiday> holidays = holidayDAO.getAllHolidays();

                    System.out.println("=== Company Holidays ===");

                    for (Holiday h : holidays) {
                        System.out.println(h.getHolidayDate() + " - " + h.getHolidayName());
                    }
                case 9:
                    EmployeeDAO dao10 = new EmployeeDAO();

                    List<Employee> employees = dao10.getAllEmployeeDetails();

                    System.out.println("=== Employee Directory ===");

                    for (Employee ee : employees) {
                        System.out.println("Name: " + ee.getName());
                        System.out.println("Email: " + ee.getEmail());
                        System.out.println("Department: " + ee.getDepartment());
                        System.out.println("Designation: " + ee.getDesignation());
                        System.out.println("----------------------");
                    }
                case 10:
                    PerformanceDAO pDAO = new PerformanceDAO();
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

                    pDAO.addReview(review);

                    break;
                case 11:
                    PerformanceDAO pDAO2 = new PerformanceDAO();

                    List<PerformanceReview> reviews = pDAO2.getReviewsByEmployee(emp.getEmployeeId());

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
                case 12:
                    GoalDAO gDAO = new GoalDAO();
                    Goal goal = new Goal();

                    goal.setEmployeeId(emp.getEmployeeId());

                    System.out.print("Enter goal description: ");
                    goal.setDescription(sc.nextLine());

                    System.out.print("Enter deadline (YYYY-MM-DD): ");
                    goal.setDeadline(java.sql.Date.valueOf(sc.nextLine()));

                    System.out.print("Enter priority (HIGH/MEDIUM/LOW): ");
                    goal.setPriority(sc.nextLine());

                    gDAO.addGoal(goal);

                    break;
                case 13:
                    GoalDAO gDAO2 = new GoalDAO();

                    List<Goal> goals = gDAO2.getGoalsByEmployee(emp.getEmployeeId());

                    for (Goal g : goals) {
                        System.out.println("Goal ID: " + g.getGoalId());
                        System.out.println("Description: " + g.getDescription());
                        System.out.println("Deadline: " + g.getDeadline());
                        System.out.println("Priority: " + g.getPriority());
                        System.out.println("Status: " + g.getStatus());
                        System.out.println("----------------------");
                    }
                    break;
                case 14:
                    GoalDAO gDAO3 = new GoalDAO();

                    System.out.print("Enter Goal ID: ");
                    int goalId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter new status (PENDING/IN_PROGRESS/COMPLETED): ");
                    String status = sc.nextLine();

                    gDAO3.updateGoalStatus(goalId, status);

                    break;
                    case 15:
                    System.out.println("Logged out!");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}