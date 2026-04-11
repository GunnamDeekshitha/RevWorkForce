package com.revworkforce.ui;

import com.revworkforce.model.Employee;
import com.revworkforce.service.AuthService;

import java.util.Scanner;

public class MainMenu {

    public static void start() {
        Scanner sc = new Scanner(System.in);
        AuthService authService = new AuthService();

        while (true) {

            System.out.println("\n==== RevWorkForce ====");
            System.out.println("1. Login");
            System.out.println("2. Forgot Password");
            System.out.println("3. Exit");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice) {

                case 1:
                    System.out.print("Enter Email: ");
                    String email = sc.nextLine();

                    System.out.print("Enter Password: ");
                    String password = sc.nextLine();

                    Employee emp = authService.login(email, password);

                    if (emp != null) {
                        System.out.println("Login Successful!");

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
                    } else {
                        System.out.println("Invalid credentials!");
                    }
                    break;
                case 2:
                    System.out.println("Password reset requires admin approval.");
                    System.out.println("This feature is restricted.");
                    break;


                case 3:
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}