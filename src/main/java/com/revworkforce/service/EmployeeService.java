package com.revworkforce.service;

import com.revworkforce.dao.*;
import com.revworkforce.exception.*;
import com.revworkforce.model.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class EmployeeService {

    private static final Logger logger = Logger.getLogger(EmployeeService.class.getName());

    private static final List<String> VALID_LEAVE_TYPES  = List.of("CL", "SL", "PL", "PR");
    private static final List<String> VALID_GOAL_STATUSES = List.of("NOT_STARTED", "IN_PROGRESS", "COMPLETED");

    private final EmployeeDAO    employeeDAO;
    private final LeaveDAO        leaveDAO;
    private final LeaveBalanceDAO leaveBalanceDAO;
    private final HolidayDAO      holidayDAO;
    private final PerformanceDAO  performanceDAO;
    private final GoalDAO         goalDAO;
    private final AnnouncementDAO announcementDAO;
    private final NotificationDAO notificationDAO;

    public EmployeeService(EmployeeDAO employeeDAO, LeaveDAO leaveDAO,
                           LeaveBalanceDAO leaveBalanceDAO, HolidayDAO holidayDAO,
                           PerformanceDAO performanceDAO, GoalDAO goalDAO,
                           AnnouncementDAO announcementDAO, NotificationDAO notificationDAO) {
        this.employeeDAO     = employeeDAO;
        this.leaveDAO        = leaveDAO;
        this.leaveBalanceDAO = leaveBalanceDAO;
        this.holidayDAO      = holidayDAO;
        this.performanceDAO  = performanceDAO;
        this.goalDAO         = goalDAO;
        this.announcementDAO = announcementDAO;
        this.notificationDAO = notificationDAO;
    }

    public EmployeeService() {
        this(new EmployeeDAO(), new LeaveDAO(), new LeaveBalanceDAO(), new HolidayDAO(),
                new PerformanceDAO(), new GoalDAO(), new AnnouncementDAO(), new NotificationDAO());
    }

    public Employee getProfile(int empId) {
        logger.info("Fetching profile for employee ID: " + empId);

        if (empId <= 0) throw new InvalidInputException("Employee ID must be a positive number");

        try {
            Employee emp = employeeDAO.getEmployeeById(empId);
            if (emp == null) throw new EmployeeNotFoundException(empId);
            return emp;
        } catch (EmployeeNotFoundException e) {
            throw e;
        } catch (DatabaseException e) {
            logger.severe("DB error fetching profile for " + empId + ": " + e.getMessage());
            throw e;
        }
    }
    public void updateProfile(int empId, String phone, String address,
                              String emergencyName, String emergencyPhone) {
        logger.info("Updating profile for employee ID: " + empId);

        if (empId <= 0) throw new InvalidInputException("Employee ID must be a positive number");
        if (phone != null && !phone.trim().isEmpty() && !phone.matches("\\d{10}"))
            throw new InvalidInputException("Phone number must be exactly 10 digits");
        if (emergencyPhone != null && !emergencyPhone.trim().isEmpty() && !emergencyPhone.matches("\\d{10}"))
            throw new InvalidInputException("Emergency contact phone must be exactly 10 digits");

        try {
            Employee emp = employeeDAO.getEmployeeById(empId);
            if (emp == null) throw new EmployeeNotFoundException(empId);

            if (phone != null && !phone.trim().isEmpty())         emp.setPhone(phone);
            if (address != null && !address.trim().isEmpty())     emp.setAddress(address.trim());
            if (emergencyName != null && !emergencyName.trim().isEmpty()) emp.setEmergencyContactName(emergencyName.trim());
            if (emergencyPhone != null && !emergencyPhone.trim().isEmpty()) emp.setEmergencyContactPhone(emergencyPhone);

            employeeDAO.updateEmployeeProfile(empId, emp.getPhone(), emp.getAddress(),
                    emp.getEmergencyContactName(), emp.getEmergencyContactPhone());
            logger.info("Profile updated successfully");
        } catch (EmployeeNotFoundException e) {
            throw e;
        } catch (DatabaseException e) {
            logger.severe("DB error updating profile for " + empId + ": " + e.getMessage());
            throw e;
        }
    }

    public Employee getManager(int managerId) {
        logger.info("Fetching manager with ID: " + managerId);
        if (managerId <= 0) throw new InvalidInputException("Manager ID must be a positive number");
        try {
            Employee manager = employeeDAO.getEmployeeById(managerId);
            if (manager == null) throw new EmployeeNotFoundException("Manager not found with ID: " + managerId);
            return manager;
        } catch (EmployeeNotFoundException e) {
            throw e;
        } catch (DatabaseException e) {
            logger.severe("DB error fetching manager: " + e.getMessage());
            throw e;
        }
    }

    public List<Employee> getAllEmployees() {
        logger.info("Fetching all employees");
        try {
            return employeeDAO.getAllEmployeeDetails();
        } catch (DatabaseException e) {
            logger.severe("DB error fetching all employees: " + e.getMessage());
            throw e;
        }
    }
    public String applyLeave(Employee emp, LeaveRequest leave) {
        logger.info("Applying leave for employee");

        if (emp == null)   throw new InvalidInputException("Employee cannot be null");
        if (leave == null) throw new InvalidInputException("Leave request cannot be null");
        if (leave.getStartDate() == null) throw new InvalidLeaveDateException("Leave start date cannot be null");
        if (leave.getEndDate() == null)   throw new InvalidLeaveDateException("Leave end date cannot be null");
        if (leave.getLeaveType() == null || !VALID_LEAVE_TYPES.contains(leave.getLeaveType().toUpperCase()))
            throw new InvalidInputException("Leave type must be one of: " + VALID_LEAVE_TYPES);
        if (leave.getReason() == null || leave.getReason().trim().isEmpty())
            throw new InvalidInputException("Leave reason cannot be empty");

        Date today = new Date();
        if (leave.getStartDate().before(today))
            throw new InvalidLeaveDateException("Cannot apply leave for past dates");
        if (leave.getEndDate().before(leave.getStartDate()))
            throw new InvalidLeaveDateException("End date cannot be before start date");

        int days = (int) ((leave.getEndDate().getTime() - leave.getStartDate().getTime())
                / (1000 * 60 * 60 * 24)) + 1;

        try {
            if (!leaveBalanceDAO.hasEnoughLeave(emp.getEmployeeId(), leave.getLeaveType(), days))
                throw new InsufficientLeaveBalanceException(leave.getLeaveType(), days);

            leaveDAO.applyLeave(leave);
            leaveBalanceDAO.updateLeaveBalance(emp.getEmployeeId(), leave.getLeaveType(), days);

            Notification n = new Notification();
            n.setEmployeeId(emp.getManagerId());
            n.setMessage("New leave request from " + emp.getName());
            notificationDAO.addNotification(n);

            logger.info("Leave applied successfully");
            return "Leave applied successfully!";
        } catch (InsufficientLeaveBalanceException e) {
            throw e;
        } catch (DatabaseException e) {
            logger.severe("DB error while applying leave: " + e.getMessage());
            throw e;
        }
    }

    public List<LeaveRequest> getMyLeaves(int empId) {
        logger.info("Fetching leaves for employee ID: " + empId);
        if (empId <= 0) throw new InvalidInputException("Employee ID must be a positive number");
        try {
            return leaveDAO.getLeavesByEmployeeId(empId);
        } catch (DatabaseException e) {
            logger.severe("DB error fetching leaves: " + e.getMessage());
            throw e;
        }
    }

    public LeaveBalance getLeaveBalance(int empId) {
        logger.info("Fetching leave balance for employee ID: " + empId);
        if (empId <= 0) throw new InvalidInputException("Employee ID must be a positive number");
        try {
            return leaveBalanceDAO.getLeaveBalance(empId);
        } catch (DatabaseException e) {
            logger.severe("DB error fetching leave balance: " + e.getMessage());
            throw e;
        }
    }


    public String cancelLeave(int empId, int leaveId) {
        logger.info("Cancelling leave ID: " + leaveId + " for employee ID: " + empId);

        if (empId <= 0)  throw new InvalidInputException("Employee ID must be a positive number");
        if (leaveId <= 0) throw new InvalidInputException("Leave ID must be a positive number");

        try {
            LeaveRequest leave = leaveDAO.getLeaveById(leaveId);
            if (leave == null) throw new LeaveNotFoundException(leaveId);
            if (leave.getEmployeeId() != empId)
                throw new UnauthorizedAccessException("You are not authorized to cancel this leave request");
            if (!leave.getStatus().equalsIgnoreCase("PENDING"))
                return "Only PENDING leaves can be cancelled!";

            int days = (int) ((leave.getEndDate().getTime() - leave.getStartDate().getTime())
                    / (1000 * 60 * 60 * 24)) + 1;
            leaveBalanceDAO.restoreLeaveBalance(empId, leave.getLeaveType(), days);
            leaveDAO.cancelLeave(leaveId);

            logger.info("Leave cancelled successfully");
            return "Leave cancelled!";
        } catch (LeaveNotFoundException | UnauthorizedAccessException e) {
            throw e;
        } catch (DatabaseException e) {
            logger.severe("DB error cancelling leave: " + e.getMessage());
            throw e;
        }
    }

    public List<Holiday> getHolidays() {
        logger.info("Fetching holidays");
        try {
            return holidayDAO.getAllHolidays();
        } catch (DatabaseException e) {
            logger.severe("DB error fetching holidays: " + e.getMessage());
            throw e;
        }
    }

    public void submitReview(PerformanceReview review) {
        logger.info("Submitting performance review");

        if (review == null)
            throw new InvalidInputException("Performance review cannot be null");

        if (review.getEmployeeId() <= 0)
            throw new InvalidInputException("Employee ID must be a positive number");

        if (review.getSelfAssessment() == null || review.getSelfAssessment().trim().isEmpty())
            throw new InvalidInputException("Self assessment cannot be empty");

        if (review.getRating() < 1 || review.getRating() > 5)
            throw new InvalidInputException("Rating must be between 1 and 5");

        try {
            Employee emp = employeeDAO.getEmployeeById(review.getEmployeeId());
            if (emp == null)
                throw new EmployeeNotFoundException(review.getEmployeeId());

            Calendar cal = Calendar.getInstance();
            cal.setTime(emp.getJoiningDate());
            int joiningYear = cal.get(Calendar.YEAR);

            int reviewYear = review.getYear();
            if (reviewYear < joiningYear) {
                throw new InvalidInputException("Review year cannot be before joining year");
            }

            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            if (reviewYear > currentYear) {
                throw new InvalidInputException("Review year cannot be in the future");
            }

            performanceDAO.addReview(review);
            logger.info("Review submitted successfully");

        } catch (EmployeeNotFoundException e) {
            throw e;
        } catch (DatabaseException e) {
            logger.severe("DB error submitting review: " + e.getMessage());
            throw e;
        }
    }

    public List<PerformanceReview> getReviews(int empId) {
        logger.info("Fetching reviews for employee ID: " + empId);
        if (empId <= 0) throw new InvalidInputException("Employee ID must be a positive number");
        try {
            return performanceDAO.getReviewsByEmployee(empId);
        } catch (DatabaseException e) {
            logger.severe("DB error fetching reviews: " + e.getMessage());
            throw e;
        }
    }

    public void addGoal(Goal goal) {
        logger.info("Adding goal");

        if (goal == null) throw new InvalidInputException("Goal cannot be null");
        if (goal.getEmployeeId() <= 0) throw new InvalidInputException("Employee ID must be a positive number");
        if (goal.getDescription() == null || goal.getDescription().trim().isEmpty())
            throw new InvalidInputException("Goal description cannot be empty");
        if (goal.getDeadline() == null) throw new InvalidInputException("Goal deadline cannot be null");

        try {
            goalDAO.addGoal(goal);
            logger.info("Goal added successfully");
        } catch (DatabaseException e) {
            logger.severe("DB error adding goal: " + e.getMessage());
            throw e;
        }
    }

    public List<Goal> getGoals(int empId) {
        logger.info("Fetching goals for employee ID: " + empId);
        if (empId <= 0) throw new InvalidInputException("Employee ID must be a positive number");
        try {
            return goalDAO.getGoalsByEmployee(empId);
        } catch (DatabaseException e) {
            logger.severe("DB error fetching goals: " + e.getMessage());
            throw e;
        }
    }

    public void updateGoalStatus(int goalId, String status) {
        logger.info("Updating goal status for goal ID: " + goalId);

        if (goalId <= 0) throw new InvalidInputException("Goal ID must be a positive number");
        if (status == null || status.trim().isEmpty()) throw new InvalidInputException("Status cannot be empty");
        if (!VALID_GOAL_STATUSES.contains(status.toUpperCase()))
            throw new InvalidInputException("Status must be one of: " + VALID_GOAL_STATUSES);

        try {
            goalDAO.updateGoalStatus(goalId, status.toUpperCase());
            logger.info("Goal status updated to: " + status);
        } catch (DatabaseException e) {
            logger.severe("DB error updating goal status: " + e.getMessage());
            throw e;
        }
    }
    public List<Announcement> getAnnouncements() {
        logger.info("Fetching announcements");
        try {
            return announcementDAO.getAllAnnouncements();
        } catch (DatabaseException e) {
            logger.severe("DB error fetching announcements: " + e.getMessage());
            throw e;
        }
    }

    public List<Employee> getBirthdays() {
        logger.info("Fetching upcoming birthdays");
        try {
            return employeeDAO.getUpcomingBirthdays();
        } catch (DatabaseException e) {
            logger.severe("DB error fetching birthdays: " + e.getMessage());
            throw e;
        }
    }

    public List<Employee> getAnniversaries() {
        logger.info("Fetching work anniversaries");
        try {
            return employeeDAO.getWorkAnniversaries();
        } catch (DatabaseException e) {
            logger.severe("DB error fetching anniversaries: " + e.getMessage());
            throw e;
        }
    }


    public List<Notification> getNotifications(int empId) {
        logger.info("Fetching notifications for employee ID: " + empId);
        if (empId <= 0) throw new InvalidInputException("Employee ID must be a positive number");
        try {
            return notificationDAO.getNotifications(empId);
        } catch (DatabaseException e) {
            logger.severe("DB error fetching notifications: " + e.getMessage());
            throw e;
        }
    }

    public int getUnreadCount(int empId) {
        logger.info("Fetching unread count for employee ID: " + empId);
        if (empId <= 0) throw new InvalidInputException("Employee ID must be a positive number");
        try {
            return notificationDAO.getUnreadCount(empId);
        } catch (DatabaseException e) {
            logger.severe("DB error fetching unread count: " + e.getMessage());
            throw e;
        }
    }

    public void markNotificationsRead(int empId) {
        logger.info("Marking notifications as read for employee ID: " + empId);
        if (empId <= 0) throw new InvalidInputException("Employee ID must be a positive number");
        try {
            notificationDAO.markAllAsRead(empId);
            logger.info("Notifications marked as read");
        } catch (DatabaseException e) {
            logger.severe("DB error marking notifications: " + e.getMessage());
            throw e;
        }
    }
}
