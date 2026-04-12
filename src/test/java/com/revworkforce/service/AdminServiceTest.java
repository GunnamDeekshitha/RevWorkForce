package com.revworkforce.service;

import com.revworkforce.dao.*;
import com.revworkforce.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminServiceTest {

    private EmployeeDAO employeeDAO;
    private LeaveBalanceDAO leaveBalanceDAO;
    private LeaveDAO leaveDAO;
    private AnnouncementDAO announcementDAO;
    private HolidayDAO holidayDAO;
    private DepartmentDAO departmentDAO;
    private DesignationDAO designationDAO;

    private AdminService adminService;

    @BeforeEach
    void setup() {
        employeeDAO = mock(EmployeeDAO.class);
        leaveBalanceDAO = mock(LeaveBalanceDAO.class);
        leaveDAO = mock(LeaveDAO.class);
        announcementDAO = mock(AnnouncementDAO.class);
        holidayDAO = mock(HolidayDAO.class);
        departmentDAO = mock(DepartmentDAO.class);
        designationDAO = mock(DesignationDAO.class);

        // ✅ constructor injection (clean)
        adminService = new AdminService(
                employeeDAO,
                leaveBalanceDAO,
                leaveDAO,
                announcementDAO,
                holidayDAO,
                departmentDAO,
                designationDAO
        );
    }

    // 🔹 ADD EMPLOYEE
    @Test
    void testAddEmployeeSuccess() {
        Employee emp = new Employee();

        adminService.addEmployee(emp);

        verify(employeeDAO).addEmployee(emp);
    }

    @Test
    void testAddEmployeeNull() {
        assertThrows(RuntimeException.class, () -> adminService.addEmployee(null));
    }

    // 🔹 ASSIGN MANAGER
    @Test
    void testAssignManagerSuccess() {
        adminService.assignManager(1, 2);

        verify(employeeDAO).assignManager(1, 2);
    }

    @Test
    void testAssignManagerInvalid() {
        assertThrows(RuntimeException.class,
                () -> adminService.assignManager(0, 2));
    }

    // 🔹 SEARCH
    @Test
    void testSearchByIdSuccess() {
        Employee emp = new Employee();
        when(employeeDAO.searchEmployeeById(1)).thenReturn(emp);

        Employee result = adminService.searchById(1);

        assertNotNull(result);
    }

    @Test
    void testSearchByIdInvalid() {
        assertThrows(RuntimeException.class, () -> adminService.searchById(0));
    }

    // 🔹 LEAVE CANCEL
    @Test
    void testCancelLeaveSuccess() {
        LeaveRequest leave = new LeaveRequest();
        leave.setEmployeeId(1);
        leave.setLeaveType("CL");
        leave.setStatus("APPROVED");
        leave.setStartDate(new Date(System.currentTimeMillis()));
        leave.setEndDate(new Date(System.currentTimeMillis()));

        when(leaveDAO.getLeaveById(1)).thenReturn(leave);

        String result = adminService.cancelLeave(1);

        assertEquals("Leave cancelled!", result);
        verify(leaveDAO).adminCancelLeave(1);
    }

    @Test
    void testCancelLeaveInvalid() {
        when(leaveDAO.getLeaveById(1)).thenReturn(null);

        String result = adminService.cancelLeave(1);

        assertEquals("Leave already cancelled or invalid!", result);
    }

    // 🔹 ANNOUNCEMENT
    @Test
    void testAddAnnouncementSuccess() {
        adminService.addAnnouncement("Title", "Message");

        verify(announcementDAO).addAnnouncement(any(Announcement.class));
    }

    @Test
    void testAddAnnouncementInvalid() {
        assertThrows(RuntimeException.class,
                () -> adminService.addAnnouncement(null, null));
    }

    // 🔹 HOLIDAY
    @Test
    void testAddHolidaySuccess() {
        adminService.addHoliday("Holiday",
                new Date(System.currentTimeMillis()), "desc");

        verify(holidayDAO).addHoliday(any(Holiday.class));
    }

    @Test
    void testDeleteHolidayInvalid() {
        assertThrows(RuntimeException.class,
                () -> adminService.deleteHoliday(0));
    }

    // 🔹 DEPARTMENT
    @Test
    void testAddDepartmentSuccess() {
        adminService.addDepartment("IT");

        verify(departmentDAO).addDepartment("IT");
    }

    @Test
    void testAddDepartmentInvalid() {
        assertThrows(RuntimeException.class,
                () -> adminService.addDepartment(""));
    }

    // 🔹 DESIGNATION
    @Test
    void testAddDesignationSuccess() {
        adminService.addDesignation("Dev", 1);

        verify(designationDAO).addDesignation("Dev", 1);
    }

    @Test
    void testAddDesignationInvalid() {
        assertThrows(RuntimeException.class,
                () -> adminService.addDesignation("", 0));
    }

    // 🔹 PASSWORD RESET
    @Test
    void testResetPasswordSuccess() {
        adminService.resetEmployeePassword(1, "newpass");

        verify(employeeDAO).updatePassword(1, "newpass");
    }

    @Test
    void testResetPasswordInvalid() {
        assertThrows(RuntimeException.class,
                () -> adminService.resetEmployeePassword(0, ""));
    }
}