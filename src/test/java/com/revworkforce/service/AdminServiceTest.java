package com.revworkforce.service;

import com.revworkforce.dao.*;
import com.revworkforce.exception.*;
import com.revworkforce.model.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import java.sql.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
        employeeDAO    = Mockito.mock(EmployeeDAO.class);
        leaveBalanceDAO = Mockito.mock(LeaveBalanceDAO.class);
        leaveDAO       = Mockito.mock(LeaveDAO.class);
        announcementDAO = Mockito.mock(AnnouncementDAO.class);
        holidayDAO     = Mockito.mock(HolidayDAO.class);
        departmentDAO  = Mockito.mock(DepartmentDAO.class);
        designationDAO = Mockito.mock(DesignationDAO.class);

        adminService = new AdminService(
                employeeDAO, leaveBalanceDAO, leaveDAO,
                announcementDAO, holidayDAO, departmentDAO, designationDAO
        );
    }

    private Employee buildValidEmployee() {
        Employee emp = new Employee();
        emp.setName("Test User");
        emp.setEmail("testuser@mail.com");
        emp.setPassword("securePass");
        emp.setPhone("9876543210");
        emp.setDepartment("IT");
        emp.setDesignation("Developer");
        emp.setRole("EMPLOYEE");
        emp.setStatus("ACTIVE");
        emp.setSalary(50000);
        return emp;
    }

    // addEmployee()

    @Test
    @Order(1)
    void addEmployee_WhenValidEmployeeProvided_ShouldSaveSuccessfully() {
        Employee emp = buildValidEmployee();
        when(employeeDAO.getEmployeeByEmail(emp.getEmail())).thenReturn(null);

        assertDoesNotThrow(() -> adminService.addEmployee(emp));
        verify(employeeDAO).addEmployee(emp);
    }

    @Test
    @Order(2)
    void addEmployee_WhenEmployeeIsNull_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> adminService.addEmployee(null));
    }

    @Test
    @Order(3)
    void addEmployee_WhenNameIsEmpty_ShouldThrowInvalidInputException() {
        Employee emp = buildValidEmployee();
        emp.setName("");
        assertThrows(InvalidInputException.class, () -> adminService.addEmployee(emp));
    }

    @Test
    @Order(4)
    void addEmployee_WhenEmailIsInvalid_ShouldThrowInvalidInputException() {
        Employee emp = buildValidEmployee();
        emp.setEmail("not-an-email");
        assertThrows(InvalidInputException.class, () -> adminService.addEmployee(emp));
    }

    @Test
    @Order(5)
    void addEmployee_WhenEmailAlreadyExists_ShouldThrowDuplicateEmailException() {
        Employee emp = buildValidEmployee();
        when(employeeDAO.getEmployeeByEmail(emp.getEmail())).thenReturn(emp);
        assertThrows(DuplicateEmailException.class, () -> adminService.addEmployee(emp));
    }

    @Test
    @Order(6)
    void addEmployee_WhenPhoneIsInvalid_ShouldThrowInvalidInputException() {
        Employee emp = buildValidEmployee();
        emp.setPhone("123");
        assertThrows(InvalidInputException.class, () -> adminService.addEmployee(emp));
    }

    @Test
    @Order(7)
    void addEmployee_WhenSalaryIsZero_ShouldThrowInvalidInputException() {
        Employee emp = buildValidEmployee();
        emp.setSalary(0);
        assertThrows(InvalidInputException.class, () -> adminService.addEmployee(emp));
    }

    @Test
    @Order(8)
    void addEmployee_WhenPasswordIsTooShort_ShouldThrowInvalidInputException() {
        Employee emp = buildValidEmployee();
        emp.setPassword("abc");
        assertThrows(InvalidInputException.class, () -> adminService.addEmployee(emp));
    }


    // assignManager()
    @Test
    @Order(9)
    void assignManager_WhenValidIdsProvided_ShouldAssignSuccessfully() {
        Employee emp     = new Employee(); emp.setEmployeeId(1);
        Employee manager = new Employee(); manager.setEmployeeId(2);

        when(employeeDAO.getEmployeeById(1)).thenReturn(emp);
        when(employeeDAO.getEmployeeById(2)).thenReturn(manager);

        assertDoesNotThrow(() -> adminService.assignManager(1, 2));
        verify(employeeDAO).assignManager(1, 2);
    }

    @Test
    @Order(10)
    void assignManager_WhenEmployeeIdIsInvalid_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> adminService.assignManager(0, 2));
    }

    @Test
    @Order(11)
    void assignManager_WhenManagerIdIsInvalid_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> adminService.assignManager(1, 0));
    }

    @Test
    @Order(12)
    void assignManager_WhenEmployeeAndManagerAreSame_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> adminService.assignManager(1, 1));
    }

    @Test
    @Order(13)
    void assignManager_WhenEmployeeNotFound_ShouldThrowEmployeeNotFoundException() {
        when(employeeDAO.getEmployeeById(99)).thenReturn(null);
        assertThrows(EmployeeNotFoundException.class, () -> adminService.assignManager(99, 2));
    }


    // searchById()

    @Test
    @Order(14)
    void searchById_WhenValidIdProvided_ShouldReturnEmployee() {
        Employee emp = new Employee(); emp.setEmployeeId(6);
        when(employeeDAO.searchEmployeeById(6)).thenReturn(emp);

        Employee result = adminService.searchById(6);
        assertNotNull(result);
        assertEquals(6, result.getEmployeeId());
    }

    @Test
    @Order(15)
    void searchById_WhenInvalidId_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> adminService.searchById(0));
    }

    @Test
    @Order(16)
    void searchById_WhenEmployeeNotFound_ShouldThrowEmployeeNotFoundException() {
        when(employeeDAO.searchEmployeeById(999)).thenReturn(null);
        assertThrows(EmployeeNotFoundException.class, () -> adminService.searchById(999));
    }

    // updateStatus()

    @Test
    @Order(17)
    void updateStatus_WhenValidStatusProvided_ShouldUpdateSuccessfully() {
        Employee emp = new Employee(); emp.setEmployeeId(1);
        when(employeeDAO.getEmployeeById(1)).thenReturn(emp);

        assertDoesNotThrow(() -> adminService.updateStatus(1, "INACTIVE"));
        verify(employeeDAO).updateEmployeeStatus(1, "INACTIVE");
    }

    @Test
    @Order(18)
    void updateStatus_WhenStatusIsInvalid_ShouldThrowInvalidInputException() {
        Employee emp = new Employee(); emp.setEmployeeId(1);
        when(employeeDAO.getEmployeeById(1)).thenReturn(emp);
        assertThrows(InvalidInputException.class, () -> adminService.updateStatus(1, "SUSPENDED"));
    }

    // cancelLeave()

    @Test
    @Order(19)
    void cancelLeave_WhenValidLeaveExists_ShouldCancelSuccessfully() {
        LeaveRequest leave = new LeaveRequest();
        leave.setLeaveId(1);
        leave.setEmployeeId(2);
        leave.setLeaveType("CL");
        leave.setStatus("PENDING");
        leave.setStartDate(Date.valueOf("2026-05-01"));
        leave.setEndDate(Date.valueOf("2026-05-02"));

        when(leaveDAO.getLeaveById(1)).thenReturn(leave);

        String result = adminService.cancelLeave(1);
        assertEquals("Leave cancelled!", result);
    }

    @Test
    @Order(20)
    void cancelLeave_WhenLeaveAlreadyCancelled_ShouldReturnAlreadyCancelledMessage() {
        LeaveRequest leave = new LeaveRequest();
        leave.setLeaveId(1);
        leave.setStatus("CANCELLED");
        leave.setStartDate(Date.valueOf("2026-05-01"));
        leave.setEndDate(Date.valueOf("2026-05-02"));

        when(leaveDAO.getLeaveById(1)).thenReturn(leave);

        String result = adminService.cancelLeave(1);
        assertEquals("Leave is already cancelled!", result);
    }

    @Test
    @Order(21)
    void cancelLeave_WhenLeaveNotFound_ShouldThrowLeaveNotFoundException() {
        when(leaveDAO.getLeaveById(999)).thenReturn(null);
        assertThrows(LeaveNotFoundException.class, () -> adminService.cancelLeave(999));
    }

    @Test
    @Order(22)
    void cancelLeave_WhenLeaveIdIsInvalid_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> adminService.cancelLeave(0));
    }

    // addAnnouncement()

    @Test
    @Order(23)
    void addAnnouncement_WhenValidDataProvided_ShouldSaveSuccessfully() {
        assertDoesNotThrow(() -> adminService.addAnnouncement("Title", "Message body"));
        verify(announcementDAO).addAnnouncement(any(Announcement.class));
    }

    @Test
    @Order(24)
    void addAnnouncement_WhenTitleIsEmpty_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> adminService.addAnnouncement("", "Message"));
    }

    @Test
    @Order(25)
    void addAnnouncement_WhenMessageIsEmpty_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> adminService.addAnnouncement("Title", ""));
    }

    // ──────────────────────────────────────────────
    // addHoliday()
    // ──────────────────────────────────────────────

    @Test
    @Order(26)
    void addHoliday_WhenValidDataProvided_ShouldSaveSuccessfully() {
        assertDoesNotThrow(() ->
                adminService.addHoliday("Diwali", Date.valueOf("2026-10-20"), "Festival of Lights"));
        verify(holidayDAO).addHoliday(any(Holiday.class));
    }

    @Test
    @Order(27)
    void addHoliday_WhenNameIsEmpty_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () ->
                adminService.addHoliday("", Date.valueOf("2026-10-20"), "desc"));
    }

    @Test
    @Order(28)
    void addHoliday_WhenDateIsNull_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () ->
                adminService.addHoliday("Diwali", null, "desc"));
    }

    // resetEmployeePassword()

    @Test
    @Order(29)
    void resetEmployeePassword_WhenValidDataProvided_ShouldResetSuccessfully() {
        Employee emp = new Employee(); emp.setEmployeeId(1);
        when(employeeDAO.getEmployeeById(1)).thenReturn(emp);

        assertDoesNotThrow(() -> adminService.resetEmployeePassword(1, "newPass1"));
        verify(employeeDAO).updatePassword(1, "newPass1");
    }

    @Test
    @Order(30)
    void resetEmployeePassword_WhenPasswordIsTooShort_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> adminService.resetEmployeePassword(1, "abc"));
    }

    @Test
    @Order(31)
    void resetEmployeePassword_WhenEmployeeNotFound_ShouldThrowEmployeeNotFoundException() {
        when(employeeDAO.getEmployeeById(99)).thenReturn(null);
        assertThrows(EmployeeNotFoundException.class, () -> adminService.resetEmployeePassword(99, "validPass"));
    }

    // adjustLeave()

    @Test
    @Order(32)
    void adjustLeave_WhenValidDataProvided_ShouldAdjustSuccessfully() {
        Employee emp = new Employee(); emp.setEmployeeId(1);
        when(employeeDAO.getEmployeeById(1)).thenReturn(emp);

        assertDoesNotThrow(() -> adminService.adjustLeave(1, "CL", 2, true));
        verify(leaveBalanceDAO).adjustLeaveBalance(1, "CL", 2, true);
    }

    @Test
    @Order(33)
    void adjustLeave_WhenLeaveTypeIsInvalid_ShouldThrowInvalidInputException() {
        Employee emp = new Employee(); emp.setEmployeeId(1);
        when(employeeDAO.getEmployeeById(1)).thenReturn(emp);
        assertThrows(InvalidInputException.class, () -> adminService.adjustLeave(1, "XL", 2, true));
    }

    @Test
    @Order(34)
    void adjustLeave_WhenDaysIsZero_ShouldThrowInvalidInputException() {
        assertThrows(InvalidInputException.class, () -> adminService.adjustLeave(1, "CL", 0, true));
    }


}
