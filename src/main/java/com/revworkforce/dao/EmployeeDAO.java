package com.revworkforce.dao;
import com.revworkforce.model.Employee;
import com.revworkforce.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

public class EmployeeDAO {
    private static final Logger logger = LogManager.getLogger(EmployeeDAO.class);
    public void addEmployee(Employee emp) {
        String sql = "INSERT INTO employee (name, email, password, phone, address, role, department, designation, salary, date_of_birth, date_of_joining, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, emp.getName());
            ps.setString(2, emp.getEmail());
            ps.setString(3, emp.getPassword());
            ps.setString(4, emp.getPhone());
            ps.setString(5, emp.getAddress());
            ps.setString(6, emp.getRole());
            ps.setString(7, emp.getDepartment());
            ps.setString(8, emp.getDesignation());
            ps.setDouble(9, emp.getSalary());
            ps.setDate(10, new java.sql.Date(emp.getDateOfBirth().getTime()));
            ps.setDate(11, new java.sql.Date(emp.getJoiningDate().getTime()));
            ps.setString(12, emp.getStatus());
            ps.executeUpdate();


        } catch (Exception e) {
            logger.warn("Error while adding employee: " + e.getMessage());
        }
    }

    public Employee getEmployeeByEmail(String email) {
        String query = "SELECT * FROM employee WHERE email = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Employee emp = new Employee();

                emp.setEmployeeId(rs.getInt("employee_id"));
                emp.setName(rs.getString("name"));
                emp.setEmail(rs.getString("email"));
                emp.setPassword(rs.getString("password"));
                emp.setPhone(rs.getString("phone"));
                emp.setAddress(rs.getString("address"));
                emp.setRole(rs.getString("role"));
                emp.setDepartment(rs.getString("department"));
                emp.setDesignation(rs.getString("designation"));
                emp.setSalary(rs.getDouble("salary"));
                emp.setStatus(rs.getString("status"));
                emp.setDateOfBirth(rs.getDate("date_of_birth"));
                emp.setJoiningDate(rs.getDate("date_of_joining"));
                emp.setManagerId(rs.getInt("manager_id"));
                emp.setEmergencyContactName(rs.getString("emergency_contact_name"));
                emp.setEmergencyContactPhone(rs.getString("emergency_contact_phone"));

                return emp;
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while fetching employee: " + e.getMessage());
        }

        return null;
    }

    public Employee getEmployeeById(int id) {
        String query = "SELECT * FROM employee WHERE employee_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Employee emp = new Employee();

                emp.setEmployeeId(rs.getInt("employee_id"));
                emp.setName(rs.getString("name"));
                emp.setEmail(rs.getString("email"));
                emp.setPhone(rs.getString("phone"));
                emp.setAddress(rs.getString("address"));
                emp.setRole(rs.getString("role"));
                emp.setDepartment(rs.getString("department"));
                emp.setDesignation(rs.getString("designation"));
                emp.setSalary(rs.getDouble("salary"));
                emp.setStatus(rs.getString("status"));
                emp.setDateOfBirth(rs.getDate("date_of_birth"));
                emp.setJoiningDate(rs.getDate("date_of_joining"));
                emp.setManagerId(rs.getInt("manager_id"));
                emp.setEmergencyContactName(rs.getString("emergency_contact_name"));
                emp.setEmergencyContactPhone(rs.getString("emergency_contact_phone"));

                return emp;
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while fetching employee: " + e.getMessage());
        }

        return null;
    }
    public void assignManager(int empId, int managerId) {
        String query = "UPDATE employee SET manager_id = ? WHERE employee_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            logger.info("Assigning manager");

            ps.setInt(1, managerId);
            ps.setInt(2, empId);

            ps.executeUpdate();

        } catch (Exception e) {
            logger.warn("Error assigning manager: " + e.getMessage());
        }
    }

    public List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        String query = "SELECT * FROM employee";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            logger.info("Fetching all employees");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Employee e = new Employee();
                e.setEmployeeId(rs.getInt("employee_id"));
                e.setName(rs.getString("name"));
                e.setRole(rs.getString("role"));
                list.add(e);
            }

        } catch (Exception e) {
            logger.warn("Error fetching employees: " + e.getMessage());
        }

        return list;
    }

    public List<Employee> searchEmployeeByName(String name) {

        List<Employee> list = new ArrayList<>();
        String query = "SELECT * FROM employee WHERE name LIKE ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            logger.info("Searching employees by name: " + name);

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

        } catch (Exception e) {
            logger.warn("Error searching employees by name: " + e.getMessage());
        }

        return list;
    }
    public List<Employee> getAllEmployeeDetails() {

        List<Employee> list = new ArrayList<>();
        String query = "SELECT * FROM employee";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            logger.info("Fetching all employee details");

            ResultSet rs = ps.executeQuery();

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
        } catch (Exception e) {
            logger.warn("Error fetching employee details: " + e.getMessage());
        }

        return list;
    }
    public void updateEmployee(Employee emp) {

        String query = "UPDATE employee SET phone=?, address=?, department=?, designation=?, salary=? WHERE employee_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            logger.info("Updating employee ID: " + emp.getEmployeeId());

            ps.setString(1, emp.getPhone());
            ps.setString(2, emp.getAddress());
            ps.setString(3, emp.getDepartment());
            ps.setString(4, emp.getDesignation());
            ps.setDouble(5, emp.getSalary());
            ps.setInt(6, emp.getEmployeeId());

            ps.executeUpdate();
        } catch (Exception e) {
            logger.warn("Error updating employee: " + e.getMessage());
        }
    }
    public void updateEmployeeStatus(int empId, String status) {

        String query = "UPDATE employee SET status = ? WHERE employee_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            logger.info("Updating status for employee ID: " + empId);

            ps.setString(1, status);
            ps.setInt(2, empId);

            ps.executeUpdate();

            logger.info("Employee status updated to " + status);

        } catch (Exception e) {
            logger.warn("Error updating employee status: " + e.getMessage());
        }
    }
    public Employee searchEmployeeById(int id) {

        String query = "SELECT * FROM employee WHERE employee_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            logger.info("Searching employee by ID: " + id);

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

        } catch (Exception e) {
            logger.warn("Error searching employee by ID: " + e.getMessage());
        }

        return null;
    }
    public List<Employee> searchByDepartment(String dept) {

        List<Employee> list = new ArrayList<>();
        String query = "SELECT * FROM employee WHERE department = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            logger.info("Searching employees by department: " + dept);

            ps.setString(1, dept);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Employee e = new Employee();

                e.setEmployeeId(rs.getInt("employee_id"));
                e.setName(rs.getString("name"));
                e.setEmail(rs.getString("email"));

                list.add(e);
            }
        } catch (Exception e) {
            logger.warn("Error searching by department: " + e.getMessage());
        }

        return list;
    }
    public List<Employee> searchByDesignation(String designation) {

        List<Employee> list = new ArrayList<>();
        String query = "SELECT * FROM employee WHERE designation = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            logger.info("Searching employees by designation: " + designation);

            ps.setString(1, designation);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Employee e = new Employee();

                e.setEmployeeId(rs.getInt("employee_id"));
                e.setName(rs.getString("name"));
                e.setEmail(rs.getString("email"));

                list.add(e);
            }
        } catch (Exception e) {
            logger.warn("Error searching by designation: " + e.getMessage());
        }

        return list;
    }
    public void updateEmployeeProfile(int empId, String phone, String address,
                                      String emergencyName, String emergencyPhone) {

        String sql = "UPDATE employee SET phone=?, address=?, emergency_contact_name=?, emergency_contact_phone=? WHERE employee_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            logger.info("Updating profile for employee ID: " + empId);

            ps.setString(1, phone);
            ps.setString(2, address);
            ps.setString(3, emergencyName);
            ps.setString(4, emergencyPhone);
            ps.setInt(5, empId);

            ps.executeUpdate();
        } catch (Exception e) {
            logger.warn("Error updating employee profile: " + e.getMessage());
        }
    }
    public List<Employee> getEmployeesByManager(int managerId) {

        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employee WHERE manager_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            logger.info("Fetching employees under manager ID: " + managerId);

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
        } catch (Exception e) {
            logger.warn("Error fetching employees by manager: " + e.getMessage());
        }

        return list;
    }
    public List<Employee> getUpcomingBirthdays() {

        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employee WHERE date_of_birth IS NOT NULL";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            logger.info("Fetching upcoming birthdays");

            while (rs.next()) {
                Employee e = new Employee();

                e.setEmployeeId(rs.getInt("employee_id"));
                e.setName(rs.getString("name"));
                e.setDateOfBirth(rs.getDate("date_of_birth"));

                list.add(e);
            }
        } catch (Exception e) {
            logger.warn("Error fetching birthdays: " + e.getMessage());
        }

        return list;
    }
    public List<Employee> getWorkAnniversaries() {

        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employee WHERE date_of_joining IS NOT NULL";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            logger.info("Fetching work anniversaries");

            while (rs.next()) {
                Employee e = new Employee();

                e.setEmployeeId(rs.getInt("employee_id"));
                e.setName(rs.getString("name"));
                e.setJoiningDate(rs.getDate("date_of_joining"));

                list.add(e);
            }

        } catch (Exception e) {
            logger.warn("Error fetching anniversaries: " + e.getMessage());
        }

        return list;
    }
    public boolean verifyPassword(int empId, String password) {

        String sql = "SELECT * FROM employee WHERE employee_id=? AND password=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            logger.info("Verifying password for employee ID: " + empId);

            ps.setInt(1, empId);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            boolean result = rs.next();
            return result;

        } catch (Exception e) {
            logger.warn("Error verifying password: " + e.getMessage());
        }

        return false;
    }

    public void updatePassword(int empId, String newPassword) {

        String sql = "UPDATE employee SET password=? WHERE employee_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            logger.info("Updating password for employee ID: " + empId);

            ps.setString(1, newPassword);
            ps.setInt(2, empId);

            ps.executeUpdate();
        } catch (Exception e) {
            logger.warn("Error updating password: " + e.getMessage());
        }
    }
    public Employee getByEmail(String email) {

        String sql = "SELECT * FROM employee WHERE email=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            logger.info("Fetching employee ID by email: " + email);

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Employee e = new Employee();
                e.setEmployeeId(rs.getInt("employee_id"));
                return e;
            }

        } catch (Exception e) {
            logger.warn("Error fetching employee by email: " + e.getMessage());
        }

        return null;
    }
}

