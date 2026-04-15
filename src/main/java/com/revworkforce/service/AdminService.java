package com.revworkforce.service;

import com.revworkforce.dao.*;
import com.revworkforce.exception.*;
import com.revworkforce.model.*;

import java.util.List;
import java.util.logging.Logger;

public class AdminService {

    private static final Logger logger = Logger.getLogger(AdminService.class.getName());

    private static final List<String> VALID_STATUSES    = List.of("ACTIVE", "INACTIVE");
    private static final List<String> VALID_LEAVE_TYPES = List.of("CL", "SL", "PL", "PR");

    private final EmployeeDAO    employeeDAO;
    private final LeaveBalanceDAO leaveBalanceDAO;
    private final LeaveDAO        leaveDAO;
    private final AnnouncementDAO announcementDAO;
    private final HolidayDAO      holidayDAO;
    private final DepartmentDAO   departmentDAO;
    private final DesignationDAO  designationDAO;

    public AdminService(EmployeeDAO employeeDAO, LeaveBalanceDAO leaveBalanceDAO,
                        LeaveDAO leaveDAO, AnnouncementDAO announcementDAO,
                        HolidayDAO holidayDAO, DepartmentDAO departmentDAO,
                        DesignationDAO designationDAO) {
        this.employeeDAO     = employeeDAO;
        this.leaveBalanceDAO = leaveBalanceDAO;
        this.leaveDAO        = leaveDAO;
        this.announcementDAO = announcementDAO;
        this.holidayDAO      = holidayDAO;
        this.departmentDAO   = departmentDAO;
        this.designationDAO  = designationDAO;
    }

    public AdminService() {
        this(new EmployeeDAO(), new LeaveBalanceDAO(), new LeaveDAO(),
                new AnnouncementDAO(), new HolidayDAO(), new DepartmentDAO(), new DesignationDAO());
    }

    public void addEmployee(Employee emp) {
        logger.info("Adding new employee");

        if (emp == null) throw new InvalidInputException("Employee object cannot be null");
        if (emp.getName() == null || emp.getName().trim().isEmpty())
            throw new InvalidInputException("Employee name cannot be empty");
        if (emp.getEmail() == null || !emp.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
            throw new InvalidInputException("Invalid email format: " + emp.getEmail());
        if (emp.getPhone() == null || !emp.getPhone().matches("\\d{10}"))
            throw new InvalidInputException("Phone number must be exactly 10 digits");
        if (emp.getSalary() <= 0)
            throw new InvalidInputException("Salary must be greater than zero");
        if (emp.getDepartment() == null || emp.getDepartment().trim().isEmpty())
            throw new InvalidInputException("Department cannot be empty");
        if (emp.getDesignation() == null || emp.getDesignation().trim().isEmpty())
            throw new InvalidInputException("Designation cannot be empty");
        if (emp.getPassword() == null || emp.getPassword().length() < 6)
            throw new InvalidInputException("Password must be at least 6 characters");

        try {
            Employee existing = employeeDAO.getEmployeeByEmail(emp.getEmail());
            if (existing != null) throw new DuplicateEmailException(emp.getEmail());
            employeeDAO.addEmployee(emp);
            logger.info("Employee added successfully: " + emp.getEmail());
        } catch (DuplicateEmailException e) {
            throw e; // re-throw business exceptions as-is
        } catch (DatabaseException e) {
            logger.severe("DB error while adding employee: " + e.getMessage());
            throw e;
        }
    }

    public List<Employee> getAllEmployeesBasic() {
        logger.info("Fetching all employees (basic)");
        try {
            return employeeDAO.getAllEmployees();
        } catch (DatabaseException e) {
            logger.severe("DB error fetching employees: " + e.getMessage());
            throw e;
        }
    }

    public List<Employee> getAllEmployeesDetailed() {
        logger.info("Fetching all employees (detailed)");
        try {
            return employeeDAO.getAllEmployeeDetails();
        } catch (DatabaseException e) {
            logger.severe("DB error fetching detailed employees: " + e.getMessage());
            throw e;
        }
    }
    public void assignManager(int empId, int managerId) {
        logger.info("Assigning manager " + managerId + " to employee " + empId);

        if (empId <= 0)    throw new InvalidInputException("Employee ID must be a positive number");
        if (managerId <= 0) throw new InvalidInputException("Manager ID must be a positive number");
        if (empId == managerId) throw new InvalidInputException("An employee cannot be their own manager");

        try {
            Employee emp = employeeDAO.getEmployeeById(empId);
            if (emp == null) throw new EmployeeNotFoundException(empId);

            Employee manager = employeeDAO.getEmployeeById(managerId);
            if (manager == null) throw new EmployeeNotFoundException("Manager not found with ID: " + managerId);

            employeeDAO.assignManager(empId, managerId);
            logger.info("Manager assigned successfully");
        } catch (EmployeeNotFoundException e) {
            throw e;
        } catch (DatabaseException e) {
            logger.severe("DB error during assignManager: " + e.getMessage());
            throw e;
        }
    }

    public void updateEmployee(Employee emp) {
        logger.info("Updating employee");

        if (emp == null) throw new InvalidInputException("Employee object cannot be null");
        if (emp.getEmployeeId() <= 0) throw new InvalidInputException("Employee ID must be a positive number");
        if (emp.getName() == null || emp.getName().trim().isEmpty())
            throw new InvalidInputException("Employee name cannot be empty");
        if (emp.getEmail() == null || !emp.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
            throw new InvalidInputException("Invalid email format: " + emp.getEmail());
        if (emp.getPhone() != null && !emp.getPhone().matches("\\d{10}"))
            throw new InvalidInputException("Phone number must be exactly 10 digits");

        try {
            employeeDAO.updateEmployee(emp);
            logger.info("Employee updated successfully");
        } catch (DatabaseException e) {
            logger.severe("DB error while updating employee: " + e.getMessage());
            throw e;
        }
    }

    public void updateStatus(int empId, String status) {
        logger.info("Updating status for employee ID: " + empId);

        if (empId <= 0) throw new InvalidInputException("Employee ID must be a positive number");
        if (status == null || status.trim().isEmpty()) throw new InvalidInputException("Status cannot be empty");
        if (!VALID_STATUSES.contains(status.toUpperCase()))
            throw new InvalidInputException("Status must be one of: " + VALID_STATUSES);

        try {
            Employee emp = employeeDAO.getEmployeeById(empId);
            if (emp == null) throw new EmployeeNotFoundException(empId);
            employeeDAO.updateEmployeeStatus(empId, status.toUpperCase());
            logger.info("Employee status updated to: " + status);
        } catch (EmployeeNotFoundException e) {
            throw e;
        } catch (DatabaseException e) {
            logger.severe("DB error while updating status: " + e.getMessage());
            throw e;
        }
    }

    public Employee searchById(int id) {
        logger.info("Searching employee by ID: " + id);

        if (id <= 0) throw new InvalidInputException("Employee ID must be a positive number");

        try {
            Employee emp = employeeDAO.searchEmployeeById(id);
            if (emp == null) throw new EmployeeNotFoundException(id);
            return emp;
        } catch (EmployeeNotFoundException e) {
            throw e;
        } catch (DatabaseException e) {
            logger.severe("DB error during searchById: " + e.getMessage());
            throw e;
        }
    }

    public List<Employee> searchByName(String name) {
        logger.info("Searching employee by name: " + name);
        if (name == null || name.trim().isEmpty()) throw new InvalidInputException("Name cannot be empty");
        try {
            return employeeDAO.searchEmployeeByName(name);
        } catch (DatabaseException e) {
            logger.severe("DB error during searchByName: " + e.getMessage());
            throw e;
        }
    }

    public List<Employee> searchByDept(String dept) {
        logger.info("Searching employee by department: " + dept);
        if (dept == null || dept.trim().isEmpty()) throw new InvalidInputException("Department cannot be empty");
        try {
            return employeeDAO.searchByDepartment(dept);
        } catch (DatabaseException e) {
            logger.severe("DB error during searchByDept: " + e.getMessage());
            throw e;
        }
    }

    public List<Employee> searchByDesignation(String designation) {
        logger.info("Searching employee by designation: " + designation);
        if (designation == null || designation.trim().isEmpty())
            throw new InvalidInputException("Designation cannot be empty");
        try {
            return employeeDAO.searchByDesignation(designation);
        } catch (DatabaseException e) {
            logger.severe("DB error during searchByDesignation: " + e.getMessage());
            throw e;
        }
    }

    public void assignLeave(int empId, int cl, int sl, int pl, int pr) {
        logger.info("Assigning leave balance to employee ID: " + empId);

        if (empId <= 0) throw new InvalidInputException("Employee ID must be a positive number");
        if (cl < 0 || sl < 0 || pl < 0 || pr < 0)
            throw new InvalidInputException("Leave days cannot be negative");
        if (cl > 20 || sl > 20 || pl >20 || pr > 20)
            throw new InvalidInputException("Leave days cannot be greater than 20");


        try {
            Employee emp = employeeDAO.getEmployeeById(empId);
            if (emp == null) throw new EmployeeNotFoundException(empId);
            leaveBalanceDAO.assignLeaveBalance(empId, cl, sl, pl, pr);
            logger.info("Leave balance assigned successfully");
        } catch (EmployeeNotFoundException e) {
            throw e;
        } catch (DatabaseException e) {
            logger.severe("DB error during assignLeave: " + e.getMessage());
            throw e;
        }
    }

    public void adjustLeave(int empId, String type, int days, boolean isAdd) {
        logger.info("Adjusting leave for employee ID: " + empId);

        if (empId <= 0) throw new InvalidInputException("Employee ID must be a positive number");
        if (type == null || type.trim().isEmpty()) throw new InvalidInputException("Leave type cannot be empty");
        if (!VALID_LEAVE_TYPES.contains(type.toUpperCase()))
            throw new InvalidInputException("Leave type must be one of: " + VALID_LEAVE_TYPES);
        if (days <= 0) throw new InvalidInputException("Number of days must be greater than zero");

        try {
            Employee emp = employeeDAO.getEmployeeById(empId);
            if (emp == null) throw new EmployeeNotFoundException(empId);
            leaveBalanceDAO.adjustLeaveBalance(empId, type.toUpperCase(), days, isAdd);
            logger.info("Leave adjusted successfully");
        } catch (EmployeeNotFoundException e) {
            throw e;
        } catch (DatabaseException e) {
            logger.severe("DB error during adjustLeave: " + e.getMessage());
            throw e;
        }
    }

    public String cancelLeave(int leaveId) {
        logger.info("Cancelling leave ID: " + leaveId);

        if (leaveId <= 0) throw new InvalidInputException("Leave ID must be a positive number");

        try {
            LeaveRequest leave = leaveDAO.getLeaveById(leaveId);
            if (leave == null) throw new LeaveNotFoundException(leaveId);
            if (leave.getStatus().equalsIgnoreCase("CANCELLED")) return "Leave is already cancelled!";

            int days = (int) ((leave.getEndDate().getTime() - leave.getStartDate().getTime())
                    / (1000 * 60 * 60 * 24)) + 1;
            leaveBalanceDAO.restoreLeaveBalance(leave.getEmployeeId(), leave.getLeaveType(), days);
            leaveDAO.adminCancelLeave(leaveId);

            logger.info("Leave cancelled successfully");
            return "Leave cancelled!";
        } catch (LeaveNotFoundException e) {
            throw e;
        } catch (DatabaseException e) {
            logger.severe("DB error during cancelLeave: " + e.getMessage());
            throw e;
        }
    }

    public List<LeaveRequest> getAllLeaves() {
        logger.info("Fetching all leave requests");
        try {
            return leaveDAO.getAllLeaves();
        } catch (DatabaseException e) {
            logger.severe("DB error fetching leaves: " + e.getMessage());
            throw e;
        }
    }

    public void getStatistics() {
        logger.info("Fetching leave statistics");
        try {
            leaveDAO.getLeaveStatistics();
        } catch (DatabaseException e) {
            logger.severe("DB error fetching statistics: " + e.getMessage());
            throw e;
        }
    }

    public void addAnnouncement(String title, String message) {
        logger.info("Adding announcement");

        if (title == null || title.trim().isEmpty())   throw new InvalidInputException("Announcement title cannot be empty");
        if (message == null || message.trim().isEmpty()) throw new InvalidInputException("Announcement message cannot be empty");

        try {
            Announcement a = new Announcement();
            a.setTitle(title.trim());
            a.setMessage(message.trim());
            announcementDAO.addAnnouncement(a);
            logger.info("Announcement added successfully");
        } catch (DatabaseException e) {
            logger.severe("DB error adding announcement: " + e.getMessage());
            throw e;
        }
    }

    public void addHoliday(String name, java.sql.Date date, String desc) {
        logger.info("Adding holiday");

        if (name == null || name.trim().isEmpty()) throw new InvalidInputException("Holiday name cannot be empty");
        if (date == null) throw new InvalidInputException("Holiday date cannot be null");

        try {
            Holiday h = new Holiday();
            h.setHolidayName(name.trim());
            h.setHolidayDate(date);
            h.setDescription(desc);
            holidayDAO.addHoliday(h);
            logger.info("Holiday added successfully");
        } catch (DatabaseException e) {
            logger.severe("DB error adding holiday: " + e.getMessage());
            throw e;
        }
    }

    public void deleteHoliday(int id) {
        logger.info("Deleting holiday ID: " + id);
        if (id <= 0) throw new InvalidInputException("Holiday ID must be a positive number");
        try {
            holidayDAO.deleteHoliday(id);
            logger.info("Holiday deleted successfully");
        } catch (DatabaseException e) {
            logger.severe("DB error deleting holiday: " + e.getMessage());
            throw e;
        }
    }
    public void addDepartment(String name) {
        logger.info("Adding department");
        if (name == null || name.trim().isEmpty()) throw new InvalidInputException("Department name cannot be empty");
        try {
            departmentDAO.addDepartment(name.trim());
            logger.info("Department added successfully");
        } catch (DatabaseException e) {
            logger.severe("DB error adding department: " + e.getMessage());
            throw e;
        }
    }

    public List<Department> getDepartments() {
        logger.info("Fetching all departments");
        try {
            return departmentDAO.getAllDepartments();
        } catch (DatabaseException e) {
            logger.severe("DB error fetching departments: " + e.getMessage());
            throw e;
        }
    }

    public void addDesignation(String name, int deptId) {
        logger.info("Adding designation");
        if (name == null || name.trim().isEmpty()) throw new InvalidInputException("Designation name cannot be empty");
        if (deptId <= 0) throw new InvalidInputException("Department ID must be a positive number");
        try {
            designationDAO.addDesignation(name.trim(), deptId);
            logger.info("Designation added successfully");
        } catch (DatabaseException e) {
            logger.severe("DB error adding designation: " + e.getMessage());
            throw e;
        }
    }
    public void resetEmployeePassword(int empId, String newPass) {
        logger.info("Resetting password for employee ID: " + empId);

        if (empId <= 0) throw new InvalidInputException("Employee ID must be a positive number");
        if (newPass == null || newPass.trim().isEmpty()) throw new InvalidInputException("New password cannot be empty");
        if (newPass.length() < 6) throw new InvalidInputException("Password must be at least 6 characters");

        try {
            Employee emp = employeeDAO.getEmployeeById(empId);
            if (emp == null) throw new EmployeeNotFoundException(empId);
            employeeDAO.updatePassword(empId, newPass);
            logger.info("Password reset successful for employee ID: " + empId);
        } catch (EmployeeNotFoundException e) {
            throw e;
        } catch (DatabaseException e) {
            logger.severe("DB error during password reset: " + e.getMessage());
            throw e;
        }
    }
}
