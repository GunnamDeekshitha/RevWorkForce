package com.revworkforce.ui;

import com.revworkforce.dao.EmployeeDAO;
import com.revworkforce.model.Employee;
import com.revworkforce.dao.LeaveBalanceDAO;
import com.revworkforce.dao.LeaveDAO;
import com.revworkforce.model.LeaveRequest;

import java.util.Scanner;
import java.util.List;

public class AdminMenu {

    public static void show(Employee emp) {
        Scanner sc = new Scanner(System.in);
        EmployeeDAO dao = new EmployeeDAO();

        while (true) {
            System.out.println("\nWelcome Admin: " + emp.getName());
            System.out.println("1. Add Employee");
            System.out.println("2. Assign Manager");
            System.out.println("3. Search Employee");
            System.out.println("4. View All Employees");
            System.out.println("5. Update Employee");
            System.out.println("6. Deactivate Employee");
            System.out.println("7. Activate Employee");
            System.out.println("8. Assign Leave Balance");
            System.out.println("9. Adjust Leave Balance");
            System.out.println("10. Cancel Leave");
            System.out.println("11. View All Leave Records");
            System.out.println("12. View Leave Statistics");
            System.out.println("13. Logout");


            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    Employee newEmp = new Employee();

                    System.out.print("Enter Name: ");
                    newEmp.setName(sc.nextLine());

                    System.out.print("Enter Email: ");
                    newEmp.setEmail(sc.nextLine());

                    System.out.print("Enter Password: ");
                    newEmp.setPassword(sc.nextLine());

                    System.out.print("Enter Phone: ");
                    newEmp.setPhone(sc.nextLine());

                    System.out.print("Enter Address: ");
                    newEmp.setAddress(sc.nextLine());

                    System.out.print("Enter Role (EMPLOYEE/MANAGER): ");
                    newEmp.setRole(sc.nextLine());

                    System.out.print("Enter Department: ");
                    newEmp.setDepartment(sc.nextLine());

                    System.out.print("Enter Designation: ");
                    newEmp.setDesignation(sc.nextLine());

                    System.out.print("Enter Salary: ");
                    newEmp.setSalary(sc.nextDouble());

                    newEmp.setStatus("ACTIVE");

                    dao.addEmployee(newEmp);
                    break;

                case 2:
                    EmployeeDAO dao2 = new EmployeeDAO();

                    List<Employee> employees = dao2.getAllEmployees();

                    System.out.println("Employees:");
                    for (Employee e : employees) {
                        System.out.println(e.getEmployeeId() + " - " + e.getName() + " (" + e.getRole() + ")");
                    }

                    System.out.print("Enter Employee ID: ");
                    int empId = sc.nextInt();

                    System.out.print("Enter Manager ID: ");
                    int managerId = sc.nextInt();

                    dao2.assignManager(empId, managerId);

                    break;
                case 3:
                    EmployeeDAO dao3 = new EmployeeDAO();

                    System.out.println("Search By:");
                    System.out.println("1. Name");
                    System.out.println("2. ID");
                    System.out.println("3. Department");
                    System.out.println("4. Designation");

                    int searchChoice = sc.nextInt();
                    sc.nextLine();

                    switch (searchChoice) {

                        case 1:
                            System.out.print("Enter name: ");
                            String name = sc.nextLine();
                            List<Employee> nameResults = dao3.searchEmployeeByName(name);

                            for (Employee e : nameResults) {
                                System.out.println(e.getEmployeeId() + " - " + e.getName());
                            }
                            break;

                        case 2:
                            System.out.print("Enter ID: ");
                            int id = sc.nextInt();
                            Employee e = dao3.searchEmployeeById(id);

                            if (e != null) {
                                System.out.println("ID: " + e.getEmployeeId());
                                System.out.println("Name: " + e.getName());
                                System.out.println("Email: " + e.getEmail());
                                System.out.println("Department: " + e.getDepartment());
                                System.out.println("Designation: " + e.getDesignation());
                                System.out.println("----------------------");
                            } else {
                                System.out.println("Not found!");
                            }
                            break;

                        case 3:
                            System.out.print("Enter department: ");
                            String dept = sc.nextLine();
                            List<Employee> deptResults = dao3.searchByDepartment(dept);

                            for (Employee empl : deptResults) {
                                System.out.println(empl.getEmployeeId() + " - " + emp.getName());
                            }
                            break;

                        case 4:
                            System.out.print("Enter designation: ");
                            String des = sc.nextLine();
                            List<Employee> desResults = dao3.searchByDesignation(des);

                            for (Employee emplo : desResults) {
                                System.out.println(emplo.getEmployeeId() + " - " + emp.getName());
                            }
                            break;

                        default:
                            System.out.println("Invalid option!");
                    }

                    break;
                case 4:
                    EmployeeDAO dao4 = new EmployeeDAO();

                    List<Employee> allEmployees = dao4.getAllEmployeeDetails();

                    for (Employee e : allEmployees) {
                        System.out.println("ID: " + e.getEmployeeId());
                        System.out.println("Name: " + e.getName());
                        System.out.println("Email: " + e.getEmail());
                        System.out.println("Dept: " + e.getDepartment());
                        System.out.println("Role: " + e.getRole());
                        System.out.println("Status: " + e.getStatus());
                        System.out.println("----------------------");
                    }
                    break;
                case 5:
                    EmployeeDAO dao5 = new EmployeeDAO();

                    System.out.print("Enter Employee ID to update: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    Employee empToUpdate = new Employee();
                    empToUpdate.setEmployeeId(id);

                    System.out.print("Enter new Phone: ");
                    empToUpdate.setPhone(sc.nextLine());

                    System.out.print("Enter new Address: ");
                    empToUpdate.setAddress(sc.nextLine());

                    System.out.print("Enter new Department: ");
                    empToUpdate.setDepartment(sc.nextLine());

                    System.out.print("Enter new Designation: ");
                    empToUpdate.setDesignation(sc.nextLine());

                    System.out.print("Enter new Salary: ");
                    empToUpdate.setSalary(sc.nextDouble());

                    dao5.updateEmployee(empToUpdate);

                    break;
                case 6:
                    EmployeeDAO dao6 = new EmployeeDAO();

                    System.out.print("Enter Employee ID to deactivate: ");
                    int deactId = sc.nextInt();

                    dao6.updateEmployeeStatus(deactId, "INACTIVE");
                    break;

                case 7:
                    EmployeeDAO dao7 = new EmployeeDAO();

                    System.out.print("Enter Employee ID to activate: ");
                    int actId = sc.nextInt();

                    dao7.updateEmployeeStatus(actId, "ACTIVE");
                    break;
                case 8:
                    LeaveBalanceDAO lbDAO = new LeaveBalanceDAO();

                    System.out.print("Enter Employee ID: ");
                    int emplId = sc.nextInt();

                    System.out.print("Enter CL: ");
                    int cl = sc.nextInt();

                    System.out.print("Enter SL: ");
                    int sl = sc.nextInt();

                    System.out.print("Enter PL: ");
                    int pl = sc.nextInt();

                    lbDAO.assignLeaveBalance(emplId, cl, sl, pl);

                    break;
                case 9:
                    LeaveBalanceDAO lbDAO2 = new LeaveBalanceDAO();

                    System.out.print("Enter Employee ID: ");
                    int empId2 = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Leave Type (CL/SL/PL): ");
                    String type = sc.nextLine();

                    System.out.print("Enter number of days: ");
                    int days = sc.nextInt();

                    System.out.print("Enter operation (add/remove): ");
                    sc.nextLine();
                    String op = sc.nextLine();

                    boolean isAdd = op.equalsIgnoreCase("add");

                    lbDAO2.adjustLeaveBalance(empId2, type, days, isAdd);

                    break;
                case 10:
                    LeaveDAO leaveDAO = new LeaveDAO();
                    LeaveBalanceDAO lbbDAO = new LeaveBalanceDAO();

                    System.out.print("Enter Leave ID to cancel: ");
                    int leaveId = sc.nextInt();

                    LeaveRequest leave = leaveDAO.getLeaveById(leaveId);

                    if (leave != null && !leave.getStatus().equalsIgnoreCase("CANCELLED")) {

                        int dayys = (int) ((leave.getEndDate().getTime() - leave.getStartDate().getTime()) / (1000 * 60 * 60 * 24)) + 1;

                        // restore balance
                        lbbDAO.restoreLeaveBalance(leave.getEmployeeId(), leave.getLeaveType(), dayys);

                        // cancel leave
                        leaveDAO.adminCancelLeave(leaveId);

                    } else {
                        System.out.println("Leave already cancelled or invalid!");
                    }

                    break;
                case 11:
                    LeaveDAO leaveDAO4 = new LeaveDAO();

                    List<LeaveRequest> allLeaves = leaveDAO4.getAllLeaves();

                    for (LeaveRequest l : allLeaves) {
                        System.out.println("Leave ID: " + l.getLeaveId());
                        System.out.println("Employee ID: " + l.getEmployeeId());
                        System.out.println("Type: " + l.getLeaveType());
                        System.out.println("From: " + l.getStartDate());
                        System.out.println("To: " + l.getEndDate());
                        System.out.println("Reason: " + l.getReason());
                        System.out.println("Status: " + l.getStatus());
                        System.out.println("Manager Comment: " + l.getManagerComment());
                        System.out.println("----------------------");
                    }

                    break;
                case 12:
                    LeaveDAO leaveDAO5 = new LeaveDAO();

                    leaveDAO5.getLeaveStatistics();

                    break;
                case 13:
                    System.out.println("Logged out!");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}