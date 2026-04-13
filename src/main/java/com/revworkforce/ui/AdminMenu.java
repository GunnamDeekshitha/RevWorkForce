package com.revworkforce.ui;

import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveRequest;
import com.revworkforce.service.AdminService;
import com.revworkforce.model.Department;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class AdminMenu {

    private static final Logger logger = Logger.getLogger(AdminMenu.class.getName());
    public static void show(Employee emp) {
        Scanner sc = new Scanner(System.in);
        AdminService service = new AdminService();
        while (true) {
            try {
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
                    case 1:
                        try {
                            logger.info("Add Employee");
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

                            System.out.print("Enter Role: ");
                            newEmp.setRole(sc.nextLine());

                            System.out.print("Enter Department: ");
                            newEmp.setDepartment(sc.nextLine());

                            System.out.print("Enter Designation: ");
                            newEmp.setDesignation(sc.nextLine());

                            System.out.print("Enter Salary: ");
                            newEmp.setSalary(sc.nextDouble());
                            sc.nextLine();

                            System.out.print("Enter DOB (YYYY-MM-DD) : ");
                            newEmp.setDateOfBirth(java.sql.Date.valueOf(sc.nextLine()));

                            System.out.print("Enter DOJ (YYYY-MM-DD) : ");
                            newEmp.setJoiningDate(java.sql.Date.valueOf(sc.nextLine()));

                            newEmp.setStatus("ACTIVE");

                            service.addEmployee(newEmp);
                            System.out.println("Employee added!");
                        } catch (Exception e) {
                            logger.severe("Error: " + e.getMessage());
                            System.out.println("Error adding employee!");
                        }
                        break;

                    case 2:
                        try {
                            logger.info("Assign Manager");
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
                        } catch (Exception e) {
                            logger.severe(e.getMessage());
                            System.out.println("Error assigning manager!");
                        }
                        break;

                    case 3:
                        try {
                            logger.info("Search Employee");

                            System.out.println("Search By:");
                            System.out.println("1. Name");
                            System.out.println("2. ID");
                            System.out.println("3. Department");
                            System.out.println("4. Designation");

                            System.out.print("Enter choice: ");
                            int s = sc.nextInt();
                            sc.nextLine();

                            switch (s) {

                                case 1:
                                    System.out.print("Enter Name: ");
                                    String name = sc.nextLine();

                                    List<Employee> list = service.searchByName(name);

                                    if (list.isEmpty()) {
                                        System.out.println("No employees found!");
                                    } else {
                                        for (Employee emp1 : list) {
                                            System.out.println(emp1.getEmployeeId() + " - " + emp1.getName());
                                        }
                                    }
                                    break;

                                case 2:
                                    System.out.print("Enter Employee ID: ");
                                    int id = sc.nextInt();
                                    sc.nextLine(); // IMPORTANT

                                    Employee e = service.searchById(id);

                                    if (e != null) {
                                        System.out.println("Name: " + e.getName());
                                        System.out.println("Email: " + e.getEmail());
                                        System.out.println("Department: " + e.getDepartment());
                                        System.out.println("Designation: " + e.getDesignation());
                                    } else {
                                        System.out.println("Employee not found!");
                                    }
                                    break;

                                case 3:
                                    System.out.print("Enter Department: ");
                                    String dept = sc.nextLine();

                                    List<Employee> deptList = service.searchByDept(dept);

                                    if (deptList.isEmpty()) {
                                        System.out.println("No employees found!");
                                    } else {
                                        for (Employee emp2 : deptList) {
                                            System.out.println(emp2.getEmployeeId() + " - " + emp2.getName());
                                        }
                                    }
                                    break;

                                case 4:
                                    System.out.print("Enter Designation: ");
                                    String des = sc.nextLine();

                                    List<Employee> desList = service.searchByDesignation(des);

                                    if (desList.isEmpty()) {
                                        System.out.println("No employees found!");
                                    } else {
                                        for (Employee emp3 : desList) {
                                            System.out.println(emp3.getEmployeeId() + " - " + emp3.getName());
                                        }
                                    }
                                    break;

                                default:
                                    System.out.println("Invalid choice!");
                            }

                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                            System.out.println("Error searching employee!");
                        }
                        break;
                    case 4:
                        try {
                            logger.info("View Employees");

                            List<Employee> employees = service.getAllEmployeesDetailed();

                            if (employees.isEmpty()) {
                                System.out.println("No employees found!");
                            } else {
                                System.out.println("=== Employee List ===");

                                for (Employee e : employees) {
                                    System.out.println("ID: " + e.getEmployeeId());
                                    System.out.println("Name: " + e.getName());
                                    System.out.println("Email: " + e.getEmail());
                                    System.out.println("Department: " + e.getDepartment());
                                    System.out.println("Designation: " + e.getDesignation());
                                    System.out.println("Role: " + e.getRole());
                                    System.out.println("Status: " + e.getStatus());
                                    System.out.println("----------------------");
                                }
                            }

                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                            System.out.println("Error fetching employees!");
                        }
                        break;

                    case 5:
                        try {
                            logger.info("Update Employee");

                            System.out.print("Enter Employee ID: ");
                            int id = sc.nextInt();
                            sc.nextLine();

                            Employee upd = new Employee();
                            upd.setEmployeeId(id);

                            System.out.print("Enter Phone: ");
                            String phone = sc.nextLine();

                            if (!phone.isEmpty() && !phone.matches("\\d{10}")) {
                                System.out.println("Invalid phone number! Must be 10 digits.");
                                break;
                            }
                            upd.setPhone(phone);

                            System.out.print("Enter Address: ");
                            upd.setAddress(sc.nextLine());

                            System.out.print("Enter Department: ");
                            upd.setDepartment(sc.nextLine());

                            System.out.print("Enter Designation: ");
                            upd.setDesignation(sc.nextLine());

                            System.out.print("Enter Salary: ");
                            double salary = sc.nextDouble();
                            sc.nextLine();

                            if (salary < 0) {
                                System.out.println("Salary cannot be negative!");
                                break;
                            }
                            upd.setSalary(salary);

                            service.updateEmployee(upd);

                            System.out.println("Employee updated successfully!");

                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                            System.out.println("Error updating employee!");
                        }
                        break;

                    case 6:
                        try {
                            logger.info("Deactivate Employee");

                            System.out.print("Enter Employee ID: ");
                            int empId = sc.nextInt();
                            sc.nextLine();

                            service.updateStatus(empId, "INACTIVE");

                            System.out.println("Employee deactivated successfully!");

                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                            System.out.println("Error deactivating employee!");
                        }
                        break;

                    case 7:
                        try {
                            logger.info("Activate Employee");

                            System.out.print("Enter Employee ID: ");
                            int empId = sc.nextInt();
                            sc.nextLine();

                            service.updateStatus(empId, "ACTIVE");

                            System.out.println("Employee activated successfully!");

                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                            System.out.println("Error activating employee!");
                        }
                        break;

                    case 8:
                        try {
                            logger.info("Assign Leave");

                            System.out.print("Enter Employee ID: ");
                            int empId = sc.nextInt();

                            System.out.print("Enter Casual Leave (CL): ");
                            int cl = sc.nextInt();

                            System.out.print("Enter Sick Leave (SL): ");
                            int sl = sc.nextInt();

                            System.out.print("Enter Paid Leave (PL): ");
                            int pl = sc.nextInt();

                            System.out.print("Enter Privilege Leave (PR): ");
                            int pr = sc.nextInt();
                            sc.nextLine();

                            service.assignLeave(empId, cl, sl, pl, pr);

                            System.out.println("Leave assigned successfully!");

                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                            System.out.println("Error assigning leave!");
                        }
                        break;

                    case 9:
                        try {
                            logger.info("Adjust Leave");

                            System.out.print("Enter Employee ID: ");
                            int eid = sc.nextInt();
                            sc.nextLine();

                            System.out.print("Enter Leave Type (CL/SL/PL/PR): ");
                            String type = sc.nextLine();

                            System.out.print("Enter number of days: ");
                            int days = sc.nextInt();
                            sc.nextLine();

                            System.out.print("Enter action (add/remove): ");
                            String action = sc.nextLine();

                            boolean isAdd = action.equalsIgnoreCase("add");

                            service.adjustLeave(eid, type, days, isAdd);

                            System.out.println("Leave adjusted successfully!");

                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                            System.out.println("Error adjusting leave!");
                        }
                        break;

                    case 10:
                        try {
                            logger.info("Cancel Leave");

                            System.out.print("Enter Leave ID to cancel: ");
                            int leaveId = sc.nextInt();
                            sc.nextLine(); // IMPORTANT

                            String msg = service.cancelLeave(leaveId);

                            System.out.println(msg);

                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                            System.out.println("Error cancelling leave!");
                        }
                        break;

                    case 11:
                        try {
                            logger.info("View Leaves");

                            List<LeaveRequest> leaves = service.getAllLeaves();

                            if (leaves.isEmpty()) {
                                System.out.println("No leave records found!");
                            } else {
                                for (LeaveRequest l : leaves) {
                                    System.out.println("Leave ID: " + l.getLeaveId());
                                    System.out.println("Employee ID: " + l.getEmployeeId());
                                    System.out.println("Type: " + l.getLeaveType());
                                    System.out.println("From: " + l.getStartDate());
                                    System.out.println("To: " + l.getEndDate());
                                    System.out.println("Status: " + l.getStatus());
                                    System.out.println("----------------------");
                                }
                            }

                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                            System.out.println("Error fetching leave records!");
                        }
                        break;

                    case 12:
                        try {
                            logger.info("View Leave Statistics");

                            service.getStatistics();

                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                            System.out.println("Error fetching statistics!");
                        }
                        break;

                    case 13:
                        try {
                            logger.info("Add Announcement");

                            System.out.print("Enter Title: ");
                            String title = sc.nextLine();

                            System.out.print("Enter Message: ");
                            String message = sc.nextLine();

                            service.addAnnouncement(title, message);

                            System.out.println("Announcement added!");

                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                            System.out.println("Error adding announcement!");
                        }
                        break;

                    case 14:
                        try {
                            logger.info("Add Holiday");

                            System.out.print("Enter Holiday Name: ");
                            String name = sc.nextLine();

                            System.out.print("Enter Date (YYYY-MM-DD): ");
                            java.sql.Date date = java.sql.Date.valueOf(sc.nextLine());

                            System.out.print("Enter Description: ");
                            String desc = sc.nextLine();

                            service.addHoliday(name, date, desc);

                            System.out.println("Holiday added!");

                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                            System.out.println("Error adding holiday!");
                        }
                        break;

                    case 15:
                        try {
                            logger.info("Delete Holiday");

                            System.out.print("Enter Holiday ID to delete: ");
                            int hid = sc.nextInt();
                            sc.nextLine(); // IMPORTANT

                            service.deleteHoliday(hid);

                            System.out.println("Holiday deleted!");

                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                            System.out.println("Error deleting holiday!");
                        }
                        break;

                    case 16:
                        try {
                            logger.info("Add Department");

                            System.out.print("Enter Department Name: ");
                            String dname = sc.nextLine();

                            service.addDepartment(dname);

                            System.out.println("Department added!");

                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                            System.out.println("Error adding department!");
                        }
                        break;

                    case 17:
                        try {
                            logger.info("Add Designation");

                            List<Department> depts = service.getDepartments();

                            if (depts.isEmpty()) {
                                System.out.println("No departments found!");
                                break;
                            }

                            System.out.println("=== Departments ===");
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

                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                            System.out.println("Error adding designation!");
                        }
                        break;

                    case 18:
                        try {
                            logger.info("Reset Employee Password");

                            System.out.print("Enter Employee ID: ");
                            int empId = sc.nextInt();
                            sc.nextLine();

                            System.out.print("Enter new password: ");
                            String newPass = sc.nextLine();

                            service.resetEmployeePassword(empId, newPass);

                            System.out.println("Password reset successfully!");

                        } catch (Exception e) {
                            logger.warning(e.getMessage());
                            System.out.println("Error resetting password!");
                        }
                        break;

                    case 19:
                        logger.info("Logout");
                        System.out.println("Logged out!");
                        return;

                    default:
                        System.out.println("Invalid!");
                }

            } catch (Exception e) {
                logger.severe("Menu error: " + e.getMessage());
                System.out.println("Something went wrong!");
                sc.nextLine();
            }
        }
    }
}
