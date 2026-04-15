package com.revworkforce.dao;

import com.revworkforce.exception.DatabaseException;
import com.revworkforce.model.Employee;
import com.revworkforce.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class EmployeeDAO {

    private static final Logger logger = Logger.getLogger(EmployeeDAO.class.getName());

    private Employee mapFull(ResultSet rs) throws SQLException {
        Employee e = new Employee();
        e.setEmployeeId(rs.getInt("employee_id"));
        e.setName(rs.getString("name"));
        e.setEmail(rs.getString("email"));
        e.setPassword(rs.getString("password"));
        e.setPhone(rs.getString("phone"));
        e.setAddress(rs.getString("address"));
        e.setRole(rs.getString("role"));
        e.setDepartment(rs.getString("department"));
        e.setDesignation(rs.getString("designation"));
        e.setSalary(rs.getDouble("salary"));
        e.setStatus(rs.getString("status"));
        e.setDateOfBirth(rs.getDate("date_of_birth"));
        e.setJoiningDate(rs.getDate("date_of_joining"));
        e.setManagerId(rs.getInt("manager_id"));
        e.setEmergencyContactName(rs.getString("emergency_contact_name"));
        e.setEmergencyContactPhone(rs.getString("emergency_contact_phone"));
        return e;
    }

    public void addEmployee(Employee emp) {
        String sql = "INSERT INTO employee (name, email, password, phone, address, role, " +
                "department, designation, salary, date_of_birth, date_of_joining, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1,  emp.getName());
            ps.setString(2,  emp.getEmail());
            ps.setString(3,  emp.getPassword());
            ps.setString(4,  emp.getPhone());
            ps.setString(5,  emp.getAddress());
            ps.setString(6,  emp.getRole());
            ps.setString(7,  emp.getDepartment());
            ps.setString(8,  emp.getDesignation());
            ps.setDouble(9,  emp.getSalary());
            ps.setDate(10,   new java.sql.Date(emp.getDateOfBirth().getTime()));
            ps.setDate(11,   new java.sql.Date(emp.getJoiningDate().getTime()));
            ps.setString(12, emp.getStatus());
            ps.executeUpdate();
            logger.info("Employee added: " + emp.getEmail());

        } catch (SQLException e) {
            logger.severe("DB error adding employee: " + e.getMessage());
            throw new DatabaseException("Failed to add employee: " + emp.getEmail(), e);
        }
    }

    public Employee getEmployeeByEmail(String email) {
        String sql = "SELECT * FROM employee WHERE email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? mapFull(rs) : null;

        } catch (SQLException e) {
            logger.severe("DB error fetching employee by email: " + e.getMessage());
            throw new DatabaseException("Failed to fetch employee by email: " + email, e);
        }
    }

    public Employee getByEmail(String email) {
        String sql = "SELECT * FROM employee WHERE email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Employee e = new Employee();
                e.setEmployeeId(rs.getInt("employee_id"));
                e.setEmail(rs.getString("email"));
                return e;
            }
            return null;

        } catch (SQLException e) {
            logger.severe("DB error fetching employee by email (getByEmail): " + e.getMessage());
            throw new DatabaseException("Failed to fetch employee by email: " + email, e);
        }
    }

    public Employee getEmployeeById(int id) {
        String sql = "SELECT * FROM employee WHERE employee_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? mapFull(rs) : null;

        } catch (SQLException e) {
            logger.severe("DB error fetching employee by ID " + id + ": " + e.getMessage());
            throw new DatabaseException("Failed to fetch employee with ID: " + id, e);
        }
    }

    public void assignManager(int empId, int managerId) {
        String sql = "UPDATE employee SET manager_id = ? WHERE employee_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, managerId);
            ps.setInt(2, empId);
            ps.executeUpdate();
            logger.info("Manager " + managerId + " assigned to employee " + empId);

        } catch (SQLException e) {
            logger.severe("DB error assigning manager: " + e.getMessage());
            throw new DatabaseException("Failed to assign manager " + managerId + " to employee " + empId, e);
        }
    }

    public List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employee";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Employee e = new Employee();
                e.setEmployeeId(rs.getInt("employee_id"));
                e.setName(rs.getString("name"));
                e.setRole(rs.getString("role"));
                list.add(e);
            }
            return list;

        } catch (SQLException e) {
            logger.severe("DB error fetching all employees: " + e.getMessage());
            throw new DatabaseException("Failed to fetch employee list", e);
        }
    }

    public List<Employee> getAllEmployeeDetails() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employee";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Employee e = new Employee();
                e.setEmployeeId(rs.getInt("employee_id"));
                e.setName(rs.getString("name"));
                e.setEmail(rs.getString("email"));
                e.setPhone(rs.getString("phone"));
                e.setDepartment(rs.getString("department"));
                e.setDesignation(rs.getString("designation"));
                e.setRole(rs.getString("role"));
                e.setStatus(rs.getString("status"));
                list.add(e);
            }
            return list;

        } catch (SQLException e) {
            logger.severe("DB error fetching all employee details: " + e.getMessage());
            throw new DatabaseException("Failed to fetch detailed employee list", e);
        }
    }

    public void updateEmployee(Employee emp) {
        String sql = "UPDATE employee SET phone=?, address=?, department=?, designation=?, salary=? WHERE employee_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, emp.getPhone());
            ps.setString(2, emp.getAddress());
            ps.setString(3, emp.getDepartment());
            ps.setString(4, emp.getDesignation());
            ps.setDouble(5, emp.getSalary());
            ps.setInt(6, emp.getEmployeeId());
            ps.executeUpdate();
            logger.info("Employee updated: " + emp.getEmployeeId());

        } catch (SQLException e) {
            logger.severe("DB error updating employee " + emp.getEmployeeId() + ": " + e.getMessage());
            throw new DatabaseException("Failed to update employee with ID: " + emp.getEmployeeId(), e);
        }
    }

    public void updateEmployeeStatus(int empId, String status) {
        String sql = "UPDATE employee SET status = ? WHERE employee_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, empId);
            ps.executeUpdate();
            logger.info("Status updated for employee " + empId + " → " + status);

        } catch (SQLException e) {
            logger.severe("DB error updating status for employee " + empId + ": " + e.getMessage());
            throw new DatabaseException("Failed to update status for employee ID: " + empId, e);
        }
    }

    public Employee searchEmployeeById(int id) {
        String sql = "SELECT * FROM employee WHERE employee_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Employee e = new Employee();
                e.setEmployeeId(rs.getInt("employee_id"));
                e.setName(rs.getString("name"));
                e.setEmail(rs.getString("email"));
                e.setDepartment(rs.getString("department"));
                e.setDesignation(rs.getString("designation"));
                return e;
            }
            return null;

        } catch (SQLException e) {
            logger.severe("DB error searching employee by ID " + id + ": " + e.getMessage());
            throw new DatabaseException("Failed to search employee with ID: " + id, e);
        }
    }

    public List<Employee> searchEmployeeByName(String name) {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employee WHERE name LIKE ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Employee e = new Employee();
                e.setEmployeeId(rs.getInt("employee_id"));
                e.setName(rs.getString("name"));
                e.setEmail(rs.getString("email"));
                e.setDepartment(rs.getString("department"));
                e.setDesignation(rs.getString("designation"));
                list.add(e);
            }
            return list;

        } catch (SQLException e) {
            logger.severe("DB error searching employees by name: " + e.getMessage());
            throw new DatabaseException("Failed to search employees by name: " + name, e);
        }
    }

    public List<Employee> searchByDepartment(String dept) {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employee WHERE department = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dept);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Employee e = new Employee();
                e.setEmployeeId(rs.getInt("employee_id"));
                e.setName(rs.getString("name"));
                e.setEmail(rs.getString("email"));
                list.add(e);
            }
            return list;

        } catch (SQLException e) {
            logger.severe("DB error searching by department: " + e.getMessage());
            throw new DatabaseException("Failed to search employees by department: " + dept, e);
        }
    }

    public List<Employee> searchByDesignation(String designation) {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employee WHERE designation = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, designation);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Employee e = new Employee();
                e.setEmployeeId(rs.getInt("employee_id"));
                e.setName(rs.getString("name"));
                e.setEmail(rs.getString("email"));
                list.add(e);
            }
            return list;

        } catch (SQLException e) {
            logger.severe("DB error searching by designation: " + e.getMessage());
            throw new DatabaseException("Failed to search employees by designation: " + designation, e);
        }
    }

    public void updateEmployeeProfile(int empId, String phone, String address,
                                      String emergencyName, String emergencyPhone) {
        String sql = "UPDATE employee SET phone=?, address=?, emergency_contact_name=?, " +
                "emergency_contact_phone=? WHERE employee_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, phone);
            ps.setString(2, address);
            ps.setString(3, emergencyName);
            ps.setString(4, emergencyPhone);
            ps.setInt(5, empId);
            ps.executeUpdate();
            logger.info("Profile updated for employee " + empId);

        } catch (SQLException e) {
            logger.severe("DB error updating profile for employee " + empId + ": " + e.getMessage());
            throw new DatabaseException("Failed to update profile for employee ID: " + empId, e);
        }
    }

    public List<Employee> getEmployeesByManager(int managerId) {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employee WHERE manager_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, managerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Employee emp = new Employee();
                emp.setEmployeeId(rs.getInt("employee_id"));
                emp.setName(rs.getString("name"));
                emp.setEmail(rs.getString("email"));
                emp.setDepartment(rs.getString("department"));
                emp.setDesignation(rs.getString("designation"));
                emp.setEmergencyContactName(rs.getString("emergency_contact_name"));
                emp.setEmergencyContactPhone(rs.getString("emergency_contact_phone"));
                list.add(emp);
            }
            return list;

        } catch (SQLException e) {
            logger.severe("DB error fetching employees by manager " + managerId + ": " + e.getMessage());
            throw new DatabaseException("Failed to fetch team members for manager ID: " + managerId, e);
        }
    }

    public List<Employee> getUpcomingBirthdays() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employee WHERE date_of_birth IS NOT NULL";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Employee e = new Employee();
                e.setEmployeeId(rs.getInt("employee_id"));
                e.setName(rs.getString("name"));
                e.setDateOfBirth(rs.getDate("date_of_birth"));
                list.add(e);
            }
            return list;

        } catch (SQLException e) {
            logger.severe("DB error fetching birthdays: " + e.getMessage());
            throw new DatabaseException("Failed to fetch upcoming birthdays", e);
        }
    }

    public List<Employee> getWorkAnniversaries() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employee WHERE date_of_joining IS NOT NULL";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Employee e = new Employee();
                e.setEmployeeId(rs.getInt("employee_id"));
                e.setName(rs.getString("name"));
                e.setJoiningDate(rs.getDate("date_of_joining"));
                list.add(e);
            }
            return list;

        } catch (SQLException e) {
            logger.severe("DB error fetching work anniversaries: " + e.getMessage());
            throw new DatabaseException("Failed to fetch work anniversaries", e);
        }
    }

    public boolean verifyPassword(int empId, String password) {
        String sql = "SELECT 1 FROM employee WHERE employee_id=? AND password=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ps.setString(2, password);
            return ps.executeQuery().next();

        } catch (SQLException e) {
            logger.severe("DB error verifying password for employee " + empId + ": " + e.getMessage());
            throw new DatabaseException("Failed to verify password for employee ID: " + empId, e);
        }
    }

    public void updatePassword(int empId, String newPassword) {
        String sql = "UPDATE employee SET password=? WHERE employee_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setInt(2, empId);
            ps.executeUpdate();
            logger.info("Password updated for employee " + empId);

        } catch (SQLException e) {
            logger.severe("DB error updating password for employee " + empId + ": " + e.getMessage());
            throw new DatabaseException("Failed to update password for employee ID: " + empId, e);
        }
    }
}
