package com.revworkforce.ui;

import com.revworkforce.model.Employee;
import com.revworkforce.service.AuthService;

import java.util.Scanner;

public class MainMenu {

    public static void start() {
        Scanner sc = new Scanner(System.in);
        AuthService authService = new AuthService();

        System.out.println("==== RevWorkForce Login ====");

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        Employee emp = authService.login(email, password);

        if (emp != null) {
            switch (emp.getRole()) {
                case "EMPLOYEE":
                    EmployeeMenu.show(emp);
                    break;
                case "MANAGER":
                    ManagerMenu.show(emp);
                    break;
                case "ADMIN":
                    AdminMenu.show(emp);
                    break;
                default:
                    System.out.println("Invalid role!");
            }
        }
    }
}