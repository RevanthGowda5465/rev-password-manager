package com.passwordmanager.service.user;

import java.util.Scanner;

import com.passwordmanager.service.account.AddAccountCredential;
import com.passwordmanager.service.account.DeleteAccountCredential;
import com.passwordmanager.service.account.ListAllAccount;
import com.passwordmanager.service.account.ListAllAccountsWithPassword;
import com.passwordmanager.service.account.SearchAccount;
import com.passwordmanager.service.account.UpdateAccountCredential;
import com.passwordmanager.service.account.ViewAccountDetail;
import com.passwordmanager.service.password.GeneratePassword;
import com.passwordmanager.service.questions.ManageSecurityQuestions;

public class UserMenu {

    // Shows menu options for all user features after login
    public static void getMenu(int userId) {

        Scanner scan = new Scanner(System.in); // Reads menu input
        String choice = "";

        while (true) {

            System.out.println("\n--- USER MENU ---");
            System.out.println("1. Add Account Credential");
            System.out.println("2. List All Accounts");
            System.out.println("3. List All Accounts With Password");
            System.out.println("4. View Account Details");
            System.out.println("5. Update Account Credential");
            System.out.println("6. Delete Account Credential");
            System.out.println("7. Search Account by Name");
            System.out.println("8. Generate Random Password");
            System.out.println("9. Get Details");
            System.out.println("10. Update Profile Info (Username / Email)");
            System.out.println("11. Manage Security Questions");
            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");

            choice = scan.next(); // Read user selection

            switch (choice) {

                case "1":
                    AddAccountCredential.addCredential(userId); // Add new account
                    break;

                case "2":
                    ListAllAccount.listAll(userId); // Show stored accounts
                    break;

                case "3":
                    ListAllAccountsWithPassword.listAllWithPassword(userId); // Show accounts with passwords
                    break;

                case "4":
                    ViewAccountDetail.viewAccount(userId); // Display one account
                    break;

                case "5":
                    UpdateAccountCredential.updateCredential(userId); // Change account password
                    break;

                case "6":
                    DeleteAccountCredential.deleteCredential(userId); // Remove account
                    break;

                case "7":
                    SearchAccount.searchByName(userId); // Search account
                    break;

                case "8":
                    GeneratePassword.generatePasswordConsole(); // Generate random password
                    break;

                case "9":
                    UserDetails.getUserDetails(userId); // Show profile info
                    break;

                case "10":
                    UpdateUserInfo.updateUserInfo(userId); // Update user details
                    break;

                case "11":
                    ManageSecurityQuestions.manage(userId); // Open security question menu
                    break;

                case "0":
                    System.out.println("Logging out..."); // Exit menu
                    return;

                default:
                    System.out.println("Invalid choice. Try again."); // Handle wrong input
            }
        }
    }
}
