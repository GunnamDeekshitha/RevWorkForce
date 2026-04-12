package com.revworkforce.service;

import com.revworkforce.dao.*;
import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveRequest;
import com.revworkforce.model.Announcement;
import java.util.List;
import com.revworkforce.model.*;
import com.revworkforce.model.Department;
import java.util.logging.Logger;

public class AdminService {
    private static final Logger logger = Logger.getLogger(AdminService.class.getName());
    private EmployeeDAO employeeDAO;
    private LeaveBalanceDAO leaveBalanceDAO;
    private LeaveDAO leaveDAO;
    private AnnouncementDAO announcementDAO;
    private HolidayDAO holidayDAO;
    private DepartmentDAO departmentDAO;
    private DesignationDAO designationDAO;
    public AdminService(EmployeeDAO employeeDAO,
                        LeaveBalanceDAO leaveBalanceDAO,
                        LeaveDAO leaveDAO,
                        AnnouncementDAO announcementDAO,
                        HolidayDAO holidayDAO,
                        DepartmentDAO departmentDAO,
                        DesignationDAO designationDAO) {

        this.employeeDAO = employeeDAO;
        this.leaveBalanceDAO = leaveBalanceDAO;
        this.leaveDAO = leaveDAO;
        this.announcementDAO = announcementDAO;
        this.holidayDAO = holidayDAO;
        this.departmentDAO = departmentDAO;
        this.designationDAO = designationDAO;
    }
    public AdminService() {
        this.employeeDAO = new EmployeeDAO();
        this.leaveBalanceDAO = new LeaveBalanceDAO();
        this.leaveDAO = new LeaveDAO();
        this.announcementDAO = new AnnouncementDAO();
        this.holidayDAO = new HolidayDAO();
        this.departmentDAO = new DepartmentDAO();
        this.designationDAO = new DesignationDAO();
    }

    public void addEmployee(Employee emp) {
        logger.info("Adding new employee");
        if (emp == null) {
            logger.severe("Employee object is null");
            throw new RuntimeException("Employee cannot be null");
        }
        employeeDAO.addEmployee(emp);
        logger.info("Employee added successfully");
    }

    public List<Employee> getAllEmployeesBasic() {
        logger.info("Fetching all employees (basic)");
        return employeeDAO.getAllEmployees();
    }

    public List<Employee> getAllEmployeesDetailed() {
        logger.info("Fetching all employees (detailed)");
        return employeeDAO.getAllEmployeeDetails();
    }

    public void assignManager(int empId, int managerId) {
        logger.info("Assigning manager " + managerId + " to employee " + empId);
        if (empId <= 0 || managerId <= 0) {
            logger.severe("Invalid employee or manager ID");
            throw new RuntimeException("Invalid IDs");
        }
        employeeDAO.assignManager(empId, managerId);
        logger.info("Manager assigned successfully");
    }

    public void updateEmployee(Employee emp) {
        logger.info("Updating employee");
        if (emp == null) {
            logger.severe("Employee object is null");
            throw new RuntimeException("Employee cannot be null");
        }
        employeeDAO.updateEmployee(emp);
        logger.info("Employee updated successfully");
    }

    public void updateStatus(int empId, String status) {
        logger.info("Updating status for employee ID: " + empId);
        if (empId <= 0 || status == null) {
            logger.severe("Invalid input for status update");
            throw new RuntimeException("Invalid input");
        }
        employeeDAO.updateEmployeeStatus(empId, status);
        logger.info("Employee status updated");
    }

    public Employee searchById(int id) {
        logger.info("Searching employee by ID: " + id);
        if (id <= 0) {
            logger.severe("Invalid employee ID");
            throw new RuntimeException("Invalid employee ID");
        }
        return employeeDAO.searchEmployeeById(id);
    }

    public List<Employee> searchByName(String name) {
        logger.info("Searching employee by name");
        if (name == null || name.isEmpty()) {
            logger.severe("Name is empty");
            throw new RuntimeException("Name cannot be empty");
        }
        return employeeDAO.searchEmployeeByName(name);
    }

    public List<Employee> searchByDept(String dept) {
        logger.info("Searching employee by department");
        if (dept == null || dept.isEmpty()) {
            logger.severe("Department is empty");
            throw new RuntimeException("Department cannot be empty");
        }
        return employeeDAO.searchByDepartment(dept);
    }

    public List<Employee> searchByDesignation(String des) {
        logger.info("Searching employee by designation");
        if (des == null || des.isEmpty()) {
            logger.severe("Designation is empty");
            throw new RuntimeException("Designation cannot be empty");
        }
        return employeeDAO.searchByDesignation(des);
    }

    public void assignLeave(int empId, int cl, int sl, int pl, int pr) {
        logger.info("Assigning leave balance to employee ID: " + empId);
        if (empId <= 0) {
            logger.severe("Invalid employee ID");
            throw new RuntimeException("Invalid employee ID");
        }
        leaveBalanceDAO.assignLeaveBalance(empId, cl, sl, pl, pr);
        logger.info("Leave balance assigned");
    }

    public void adjustLeave(int empId, String type, int days, boolean isAdd) {
        logger.info("Adjusting leave for employee ID: " + empId);
        if (empId <= 0 || type == null || days < 0) {
            logger.severe("Invalid input for leave adjustment");
            throw new RuntimeException("Invalid input");
        }
        leaveBalanceDAO.adjustLeaveBalance(empId, type, days, isAdd);
        logger.info("Leave adjusted successfully");
    }

    public String cancelLeave(int leaveId) {
        logger.info("Cancelling leave ID: " + leaveId);
        if (leaveId <= 0) {
            logger.severe("Invalid leave ID");
            throw new RuntimeException("Invalid leave ID");
        }
        LeaveRequest leave = leaveDAO.getLeaveById(leaveId);
        if (leave != null && !leave.getStatus().equalsIgnoreCase("CANCELLED")) {
            int days = (int) ((leave.getEndDate().getTime() - leave.getStartDate().getTime())
                    / (1000 * 60 * 60 * 24)) + 1;
            leaveBalanceDAO.restoreLeaveBalance(leave.getEmployeeId(), leave.getLeaveType(), days);
            leaveDAO.adminCancelLeave(leaveId);
            logger.info("Leave cancelled successfully");
            return "Leave cancelled!";
        }
        logger.warning("Leave already cancelled or invalid");
        return "Leave already cancelled or invalid!";
    }

    public List<LeaveRequest> getAllLeaves() {
        logger.info("Fetching all leave requests");
        return leaveDAO.getAllLeaves();
    }

    public void getStatistics() {
        logger.info("Fetching leave statistics");
        leaveDAO.getLeaveStatistics();
    }

    public void addAnnouncement(String title, String message) {
        logger.info("Adding announcement");
        if (title == null || message == null) {
            logger.severe("Title or message is null");
            throw new RuntimeException("Invalid announcement data");
        }
        Announcement a = new Announcement();
        a.setTitle(title);
        a.setMessage(message);
        announcementDAO.addAnnouncement(a);
        logger.info("Announcement added");
    }

    public void addHoliday(String name, java.sql.Date date, String desc) {
        logger.info("Adding holiday");
        if (name == null || date == null) {
            logger.severe("Invalid holiday data");
            throw new RuntimeException("Invalid holiday data");
        }
        Holiday h = new Holiday();
        h.setHolidayName(name);
        h.setHolidayDate(date);
        h.setDescription(desc);
        holidayDAO.addHoliday(h);
        logger.info("Holiday added");
    }

    public void deleteHoliday(int id) {
        logger.info("Deleting holiday ID: " + id);
        if (id <= 0) {
            logger.severe("Invalid holiday ID");
            throw new RuntimeException("Invalid holiday ID");
        }
        holidayDAO.deleteHoliday(id);
        logger.info("Holiday deleted");
    }

    public void addDepartment(String name) {
        logger.info("Adding department");
        if (name == null || name.isEmpty()) {
            logger.severe("Department name is empty");
            throw new RuntimeException("Department name cannot be empty");
        }
        departmentDAO.addDepartment(name);
        logger.info("Department added");
    }

    public List<Department> getDepartments() {
        logger.info("Fetching all departments");
        return departmentDAO.getAllDepartments();
    }

    public void addDesignation(String name, int deptId) {
        logger.info("Adding designation");
        if (name == null || name.isEmpty() || deptId <= 0) {
            logger.severe("Invalid designation data");
            throw new RuntimeException("Invalid designation data");
        }
        designationDAO.addDesignation(name, deptId);
        logger.info("Designation added");
    }

    public void resetEmployeePassword(int empId, String newPass) {
        logger.info("Resetting password for employee ID: " + empId);
        if (empId <= 0 || newPass == null || newPass.isEmpty()) {
            logger.severe("Invalid password reset data");
            throw new RuntimeException("Invalid input");
        }
        employeeDAO.updatePassword(empId, newPass);
        logger.info("Password reset successful");
    }
}