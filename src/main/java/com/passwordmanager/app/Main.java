package com.passwordmanager.app;

import java.util.Scanner;

import com.passwordmanager.service.login.Login;
import com.passwordmanager.service.password.ForgotPassword;
import com.passwordmanager.service.user.CreateAccount;
import com.passwordmanager.service.user.UserMenu;

public class Main {
    // Scanner to read user input from console
    private static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        // Variable to store user choice
        String choice = "";

        // Loop to show the menu until user exits
        while (true) {
            System.out.println("\nPASSWORD MANAGER");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Forgot Password");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scan.nextLine();

            // Check what the user selected
            switch (choice) {
                case "1":
                    // Call method to create a new account
                    CreateAccount.createAccount();
                    break;

                case "2":
                    // Call method to login and get user ID
                    int userId = Login.login();
                    // If login is successful, show user menu
                    if (userId != -1) {
                        UserMenu.getMenu(userId);
                    }
                    break;

                case "3":
                    // Call method to recover forgotten password
                    ForgotPassword.recoverPassword();
                    break;

                case "0":
                    // Exit the program
                    System.out.println("Exiting Password Manager");
                    System.exit(0);

                default:
                    // If user enters something wrong
                    System.out.println("Invalid choice. Try again");
            }
        }
    }
}
