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

                            int empId = sc.nextInt();
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

                            System.out.println("1.Name 2.ID 3.Dept 4.Designation");
                            int s = sc.nextInt();
                            sc.nextLine();

                            switch (s) {
                                case 1:
                                    service.searchByName(sc.nextLine())
                                            .forEach(e -> System.out.println(e.getName()));
                                    break;
                                case 2:
                                    Employee e = service.searchById(sc.nextInt());
                                    if (e != null)
                                        System.out.println(e.getName());
                                    break;
                                case 3:
                                    service.searchByDept(sc.nextLine())
                                            .forEach(em -> System.out.println(em.getName()));
                                    break;
                                case 4:
                                    service.searchByDesignation(sc.nextLine())
                                            .forEach(empp -> System.out.println(empp.getName()));
                                    break;
                            }
                        } catch (Exception e) {
                            logger.severe(e.getMessage());
                            System.out.println("Error searching!");
                        }
                        break;

                    case 4:
                        try {
                            logger.info("View Employees");
                            service.getAllEmployeesDetailed().forEach(e ->
                                    System.out.println(e.getName()));
                        } catch (Exception e) {
                            logger.severe(e.getMessage());
                        }
                        break;

                    case 5:
                        try {
                            logger.info("Update Employee");

                            int id = sc.nextInt();
                            sc.nextLine();

                            Employee upd = new Employee();
                            upd.setEmployeeId(id);

                            upd.setPhone(sc.nextLine());
                            upd.setAddress(sc.nextLine());
                            upd.setDepartment(sc.nextLine());
                            upd.setDesignation(sc.nextLine());
                            upd.setSalary(sc.nextDouble());

                            service.updateEmployee(upd);
                            System.out.println("Updated!");
                        } catch (Exception e) {
                            logger.severe(e.getMessage());
                            System.out.println("Error updating!");
                        }
                        break;

                    case 6:
                        try {
                            logger.info("Deactivate Employee");
                            service.updateStatus(sc.nextInt(), "INACTIVE");
                        } catch (Exception e) {
                            logger.severe(e.getMessage());
                        }
                        break;

                    case 7:
                        try {
                            logger.info("Activate Employee");
                            service.updateStatus(sc.nextInt(), "ACTIVE");
                        } catch (Exception e) {
                            logger.severe(e.getMessage());
                        }
                        break;

                    case 8:
                        try {
                            logger.info("Assign Leave");
                            service.assignLeave(sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt());
                        } catch (Exception e) {
                            logger.severe(e.getMessage());
                        }
                        break;

                    case 9:
                        try {
                            logger.info("Adjust Leave");
                            int eid = sc.nextInt();
                            sc.nextLine();
                            String type = sc.nextLine();
                            int days = sc.nextInt();
                            sc.nextLine();
                            boolean isAdd = sc.nextLine().equalsIgnoreCase("add");

                            service.adjustLeave(eid, type, days, isAdd);
                        } catch (Exception e) {
                            logger.severe(e.getMessage());
                        }
                        break;

                    case 10:
                        try {
                            logger.info("Cancel Leave");
                            System.out.println(service.cancelLeave(sc.nextInt()));
                        } catch (Exception e) {
                            logger.severe(e.getMessage());
                        }
                        break;

                    case 11:
                        try {
                            logger.info("View Leaves");
                            List<LeaveRequest> leaves = service.getAllLeaves();
                            for (LeaveRequest l : leaves) {
                                System.out.println(l.getLeaveId() + "-" + l.getStatus());
                            }
                        } catch (Exception e) {
                            logger.severe(e.getMessage());
                        }
                        break;

                    case 12:
                        try {
                            logger.info("View Stats");
                            service.getStatistics();
                        } catch (Exception e) {
                            logger.severe(e.getMessage());
                        }
                        break;

                    case 13:
                        try {
                            logger.info("Add Announcement");
                            String title = sc.nextLine();
                            String msg = sc.nextLine();
                            service.addAnnouncement(title, msg);
                        } catch (Exception e) {
                            logger.severe(e.getMessage());
                        }
                        break;

                    case 14:
                        try {
                            logger.info("Add Holiday");
                            String name = sc.nextLine();
                            java.sql.Date date = java.sql.Date.valueOf(sc.nextLine());
                            String desc = sc.nextLine();
                            service.addHoliday(name, date, desc);
                        } catch (Exception e) {
                            logger.severe(e.getMessage());
                        }
                        break;

                    case 15:
                        try {
                            logger.info("Delete Holiday");
                            service.deleteHoliday(sc.nextInt());
                        } catch (Exception e) {
                            logger.severe(e.getMessage());
                        }
                        break;

                    case 16:
                        try {
                            logger.info("Add Department");
                            service.addDepartment(sc.nextLine());
                        } catch (Exception e) {
                            logger.severe(e.getMessage());
                        }
                        break;

                    case 17:
                        try {
                            logger.info("Add Designation");

                            List<Department> depts = service.getDepartments();
                            for (Department d : depts) {
                                System.out.println(d.getDeptId() + "-" + d.getDeptName());
                            }

                            int did = sc.nextInt();
                            sc.nextLine();
                            String desig = sc.nextLine();

                            service.addDesignation(desig, did);
                        } catch (Exception e) {
                            logger.severe(e.getMessage());
                        }
                        break;

                    case 18:
                        try {
                            logger.info("Reset Password");
                            int id = sc.nextInt();
                            sc.nextLine();
                            String pass = sc.nextLine();

                            service.resetEmployeePassword(id, pass);
                        } catch (Exception e) {
                            logger.severe(e.getMessage());
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
