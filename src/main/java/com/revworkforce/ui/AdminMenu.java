package com.revworkforce.ui;

import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveRequest;
import com.revworkforce.service.AdminService;
import com.revworkforce.model.Department;


import java.util.List;
import java.util.Scanner;

public class AdminMenu {

    public static void show(Employee emp) {
        Scanner sc = new Scanner(System.in);
        AdminService service = new AdminService();


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
            System.out.println("13. Add Announcement");
            System.out.println("14. Add Holiday");
            System.out.println("15. Delete Holiday");
            System.out.println("16. Add Department");
            System.out.println("17. Add Designation");
            System.out.println("18. Reset Employee Password");
            System.out.println("19. Logout");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                // 🔹 ADD EMPLOYEE
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

                    sc.nextLine(); // important

                    System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
                    newEmp.setDateOfBirth(java.sql.Date.valueOf(sc.nextLine()));

                    System.out.print("Enter Date of Joining (YYYY-MM-DD): ");
                    newEmp.setJoiningDate(java.sql.Date.valueOf(sc.nextLine()));

                    newEmp.setStatus("ACTIVE");

                    service.addEmployee(newEmp);
                    System.out.println("Employee added!");
                    break;
                case 2:
                    List<Employee> employees = service.getAllEmployeesBasic();

                    for (Employee e : employees) {
                        System.out.println(e.getEmployeeId() + " - " + e.getName());
                    }
                    System.out.print("Enter Employee ID: ");
                    int empId = sc.nextInt();
                    System.out.print("Enter Manager ID: ");
                    int managerId = sc.nextInt();
                    service.assignManager(empId, managerId);
                    System.out.println("Manager assigned!");
                    break;
                case 3:
                    System.out.println("1. Name  2. ID  3. Dept  4. Designation");
                    int s = sc.nextInt();
                    sc.nextLine();

                    switch (s) {
                        case 1:
                            List<Employee> list = service.searchByName(sc.nextLine());
                            list.forEach(e -> System.out.println(e.getEmployeeId() + " - " + e.getName()));
                            break;

                        case 2:
                            Employee e = service.searchById(sc.nextInt());
                            if (e != null)
                                System.out.println(e.getName() + " - " + e.getEmail());
                            break;

                        case 3:
                            service.searchByDept(sc.nextLine())
                                    .forEach(em -> System.out.println(em.getEmployeeId() + " - " + em.getName()));
                            break;

                        case 4:
                            service.searchByDesignation(sc.nextLine())
                                    .forEach(empp -> System.out.println(empp.getEmployeeId() + " - " + empp.getName()));
                            break;
                    }
                    break;
                case 4:
                    service.getAllEmployeesDetailed().forEach(e -> {
                        System.out.println(e.getEmployeeId() + " - " + e.getName() + " - " + e.getRole());
                    });
                    break;
                case 5:
                    System.out.print("Enter ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    Employee upd = new Employee();
                    upd.setEmployeeId(id);

                    System.out.print("Phone: ");
                    upd.setPhone(sc.nextLine());

                    System.out.print("Address: ");
                    upd.setAddress(sc.nextLine());

                    System.out.print("Dept: ");
                    upd.setDepartment(sc.nextLine());

                    System.out.print("Designation: ");
                    upd.setDesignation(sc.nextLine());

                    System.out.print("Salary: ");
                    upd.setSalary(sc.nextDouble());

                    service.updateEmployee(upd);
                    System.out.println("Updated!");
                    break;

                case 6:
                    service.updateStatus(sc.nextInt(), "INACTIVE");
                    break;

                case 7:
                    service.updateStatus(sc.nextInt(), "ACTIVE");
                    break;
                case 8:
                    service.assignLeave(sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt(),sc.nextInt());
                    break;

                case 9:
                    int eid = sc.nextInt();
                    sc.nextLine();
                    String type = sc.nextLine();
                    int days = sc.nextInt();
                    sc.nextLine();
                    boolean isAdd = sc.nextLine().equalsIgnoreCase("add");

                    service.adjustLeave(eid, type, days, isAdd);
                    break;

                // 🔹 CANCEL LEAVE
                case 10:
                    String msg = service.cancelLeave(sc.nextInt());
                    System.out.println(msg);
                    break;

                // 🔹 VIEW LEAVES
                case 11:
                    List<LeaveRequest> leaves = service.getAllLeaves();

                    for (LeaveRequest l : leaves) {
                        System.out.println(l.getLeaveId() + " - " + l.getStatus());
                    }
                    break;

                // 🔹 STATS
                case 12:
                    service.getStatistics();
                    break;

                // 🔹 LOGOUT

                case 13:
                    System.out.print("Enter Title: ");
                    String title = sc.nextLine();

                    System.out.print("Enter Message: ");
                    String message = sc.nextLine();

                    service.addAnnouncement(title, message);
                    System.out.println("Announcement added!");
                    break;
                case 14:
                    System.out.print("Enter Holiday Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Date (YYYY-MM-DD): ");
                    java.sql.Date date = java.sql.Date.valueOf(sc.nextLine());

                    System.out.print("Enter Description: ");
                    String desc = sc.nextLine();

                    service.addHoliday(name, date, desc);
                    System.out.println("Holiday added!");
                    break;

                case 15:
                    System.out.print("Enter Holiday ID to delete: ");
                    int hid = sc.nextInt();

                    service.deleteHoliday(hid);
                    System.out.println("Holiday deleted!");
                    break;
                case 16:
                    System.out.print("Enter Department Name: ");
                    String dname = sc.nextLine();

                    service.addDepartment(dname);
                    System.out.println("Department added!");
                    break;

                case 17:
                    List<Department> depts = service.getDepartments();

                    for (Department d : depts) {
                        System.out.println(d.getDeptId() + " - " + d.getDeptName());
                    }

                    System.out.print("Enter Department ID: ");
                    int did = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Designation Name: ");
                    String desig = sc.nextLine();

                    service.addDesignation(desig, did);
                    System.out.println("Designation added!");
                    break;
                case 18:
                    System.out.print("Enter Employee ID: ");
                    int emppId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter new password: ");
                    String newPass = sc.nextLine();

                    service.resetEmployeePassword(emppId, newPass);

                    System.out.println("Password reset successfully!");
                    break;

                case 19:
                    System.out.println("Logged out!");
                    return;
                default:
                    System.out.println("Invalid!");
            }
        }
    }
}