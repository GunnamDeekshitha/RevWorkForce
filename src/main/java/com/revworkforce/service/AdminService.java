package com.revworkforce.service;

import com.revworkforce.dao.*;
import com.revworkforce.model.Employee;
import com.revworkforce.model.LeaveRequest;
import com.revworkforce.model.Announcement;
import java.util.List;
import com.revworkforce.model.*;
import com.revworkforce.model.Department;


public class AdminService {

    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private LeaveBalanceDAO leaveBalanceDAO = new LeaveBalanceDAO();
    private LeaveDAO leaveDAO = new LeaveDAO();
    private AnnouncementDAO announcementDAO = new AnnouncementDAO();
    private HolidayDAO holidayDAO = new HolidayDAO();
    private DepartmentDAO departmentDAO = new DepartmentDAO();
    private DesignationDAO designationDAO = new DesignationDAO();

    public void addEmployee(Employee emp) {
        employeeDAO.addEmployee(emp);
    }

    public List<Employee> getAllEmployeesBasic() {
        return employeeDAO.getAllEmployees();
    }

    public List<Employee> getAllEmployeesDetailed() {
        return employeeDAO.getAllEmployeeDetails();
    }

    public void assignManager(int empId, int managerId) {
        employeeDAO.assignManager(empId, managerId);
    }

    public void updateEmployee(Employee emp) {
        employeeDAO.updateEmployee(emp);
    }

    public void updateStatus(int empId, String status) {
        employeeDAO.updateEmployeeStatus(empId, status);
    }

    public Employee searchById(int id) {
        return employeeDAO.searchEmployeeById(id);
    }

    public List<Employee> searchByName(String name) {
        return employeeDAO.searchEmployeeByName(name);
    }

    public List<Employee> searchByDept(String dept) {
        return employeeDAO.searchByDepartment(dept);
    }

    public List<Employee> searchByDesignation(String des) {
        return employeeDAO.searchByDesignation(des);
    }

    // 🔹 LEAVE BALANCE
    public void assignLeave(int empId, int cl, int sl, int pl,int pr) {
        leaveBalanceDAO.assignLeaveBalance(empId, cl, sl, pl,pr);
    }

    public void adjustLeave(int empId, String type, int days, boolean isAdd) {
        leaveBalanceDAO.adjustLeaveBalance(empId, type, days, isAdd);
    }

    // 🔹 LEAVE ADMIN ACTIONS
    public String cancelLeave(int leaveId) {

        LeaveRequest leave = leaveDAO.getLeaveById(leaveId);

        if (leave != null && !leave.getStatus().equalsIgnoreCase("CANCELLED")) {

            int days = (int) ((leave.getEndDate().getTime() - leave.getStartDate().getTime())
                    / (1000 * 60 * 60 * 24)) + 1;

            leaveBalanceDAO.restoreLeaveBalance(leave.getEmployeeId(), leave.getLeaveType(), days);
            leaveDAO.adminCancelLeave(leaveId);

            return "Leave cancelled!";
        }

        return "Leave already cancelled or invalid!";
    }

    public List<LeaveRequest> getAllLeaves() {
        return leaveDAO.getAllLeaves();
    }

    public void getStatistics() {
        leaveDAO.getLeaveStatistics();
    }
    public void addAnnouncement(String title, String message) {
        Announcement a = new Announcement();
        a.setTitle(title);
        a.setMessage(message);
        announcementDAO.addAnnouncement(a);
    }
    public void addHoliday(String name, java.sql.Date date, String desc) {
        Holiday h = new Holiday();
        h.setHolidayName(name);
        h.setHolidayDate(date);
        h.setDescription(desc);

        holidayDAO.addHoliday(h);
    }

    public void deleteHoliday(int id) {
        holidayDAO.deleteHoliday(id);
    }
    public void addDepartment(String name) {
        departmentDAO.addDepartment(name);
    }

    public List<Department> getDepartments() {
        return departmentDAO.getAllDepartments();
    }

    public void addDesignation(String name, int deptId) {
        designationDAO.addDesignation(name, deptId);
    }
    public void resetEmployeePassword(int empId, String newPass) {
        employeeDAO.updatePassword(empId, newPass);
    }

}