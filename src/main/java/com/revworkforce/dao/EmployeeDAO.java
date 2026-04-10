package com.revworkforce.dao;
import com.revworkforce.model.Employee;
import com.revworkforce.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.ArrayList;

public class EmployeeDAO {
    private static final Logger logger = LogManager.getLogger(EmployeeDAO.class);
    // 🔹 INSERT EMPLOYEE
    public void addEmployee(Employee emp) {
        String query = "INSERT INTO employee (name, email, password, phone, address, role, department, designation, salary, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, emp.getName());
            ps.setString(2, emp.getEmail());
            ps.setString(3, emp.getPassword());
            ps.setString(4, emp.getPhone());
            ps.setString(5, emp.getAddress());
            ps.setString(6, emp.getRole());
            ps.setString(7, emp.getDepartment());
            ps.setString(8, emp.getDesignation());
            ps.setDouble(9, emp.getSalary());
            ps.setString(10, emp.getStatus());

            ps.executeUpdate();
            logger.info("Employee Added Successfully!");

        } catch (Exception e) {
            logger.error("Error while adding employee: " + e.getMessage());
        }
    }

    // 🔹 GET EMPLOYEE BY EMAIL (for login later)
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
                emp.setManagerId(rs.getInt("manager_id"));

                return emp;
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while fetching employee: " + e.getMessage());
        }

        return null;
    }
    // 🔹 GET EMPLOYEE BY ID
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
                emp.setManagerId(rs.getInt("manager_id"));

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

            ps.setInt(1, managerId);
            ps.setInt(2, empId);

            ps.executeUpdate();

            System.out.println("Manager assigned successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Employee> getAllEmployees() {

        List<Employee> list = new ArrayList<>();

        String query = "SELECT * FROM employee";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Employee e = new Employee();

                e.setEmployeeId(rs.getInt("employee_id"));
                e.setName(rs.getString("name"));
                e.setRole(rs.getString("role"));

                list.add(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public List<Employee> searchEmployeeByName(String name) {

        List<Employee> list = new ArrayList<>();

        String query = "SELECT * FROM employee WHERE name LIKE ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

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
            e.printStackTrace();
        }

        return list;
    }
    public List<Employee> getAllEmployeeDetails() {

        List<Employee> list = new ArrayList<>();

        String query = "SELECT * FROM employee";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

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
            e.printStackTrace();
        }

        return list;
    }
    public void updateEmployee(Employee emp) {

        String query = "UPDATE employee SET phone=?, address=?, department=?, designation=?, salary=? WHERE employee_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, emp.getPhone());
            ps.setString(2, emp.getAddress());
            ps.setString(3, emp.getDepartment());
            ps.setString(4, emp.getDesignation());
            ps.setDouble(5, emp.getSalary());
            ps.setInt(6, emp.getEmployeeId());

            ps.executeUpdate();

            System.out.println("Employee updated successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateEmployeeStatus(int empId, String status) {

        String query = "UPDATE employee SET status = ? WHERE employee_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, status);
            ps.setInt(2, empId);

            ps.executeUpdate();

            System.out.println("Employee status updated to " + status);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Employee searchEmployeeById(int id) {

        String query = "SELECT * FROM employee WHERE employee_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

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
            e.printStackTrace();
        }

        return null;
    }
    public List<Employee> searchByDepartment(String dept) {

        List<Employee> list = new ArrayList<>();

        String query = "SELECT * FROM employee WHERE department = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

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
            e.printStackTrace();
        }

        return list;
    }
    public List<Employee> searchByDesignation(String designation) {

        List<Employee> list = new ArrayList<>();

        String query = "SELECT * FROM employee WHERE designation = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

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
            e.printStackTrace();
        }

        return list;
    }
    public void updateEmployeeProfile(int empId, String phone, String address) {

        String query = "UPDATE employee SET phone = ?, address = ? WHERE employee_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, phone);
            ps.setString(2, address);
            ps.setInt(3, empId);

            ps.executeUpdate();

            System.out.println("Profile updated successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}