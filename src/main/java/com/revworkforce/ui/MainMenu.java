package com.revworkforce.ui;

import com.revworkforce.model.Employee;
import com.revworkforce.service.AuthService;
import java.util.Scanner;
import java.util.logging.Logger;

public class MainMenu {

    private static final Logger logger = Logger.getLogger(MainMenu.class.getName());
    public static void start() {
        Scanner sc = new Scanner(System.in);
        AuthService authService = new AuthService();
        while (true) {
            try {
                System.out.println("\n==== RevWorkForce ====");
                System.out.println("1. Login");
                System.out.println("2. Forgot Password");
                System.out.println("3. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {

                    case 1:
                        try {
                            System.out.print("Enter Email: ");
                            String email = sc.nextLine();

                            System.out.print("Enter Password: ");
                            String password = sc.nextLine();

                            // 🔥 LOG AFTER INPUT (FIX)
                            logger.info("Login attempt for email: " + email);

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

                        } catch (Exception e) {
                            logger.warning("Login error: " + e.getMessage());
                            System.out.println("Error during login!");
                        }
                        break;

                    case 2:
                        try {
                            logger.info("Forgot password selected");
                            System.out.println("Password reset requires admin approval.");
                            System.out.println("This feature is restricted.");
                        } catch (Exception e) {
                            logger.warning("Error in forgot password: " + e.getMessage());
                        }
                        break;

                    case 3:
                        logger.info("Application exit");
                        System.out.println("Exiting...");
                        return;

                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (Exception e) {
                logger.warning("Main menu error: " + e.getMessage());
                System.out.println("Something went wrong!");
                sc.nextLine();
            }
        }
    }
}