package com.passwordmanager.service.user;

import java.util.Scanner;

import com.passwordmanager.dao.UserInfoDao;
import com.passwordmanager.dao.implementation.UserInfoDaoImp;
import com.passwordmanager.dto.UserInfo;

public class UpdateUserInfo {

    // Updates username or email for the logged-in user
    public static void updateUserInfo(int userId) {

        Scanner scan = new Scanner(System.in); // Reads user input
        UserInfoDao userDao = new UserInfoDaoImp(); // DAO for DB operations

        UserInfo user = userDao.getUserById(userId); // Fetch user details

        if (user == null) {
            System.out.println("User not found."); // Exit if no record exists
            return;
        }

        System.out.println("\n--- UPDATE PROFILE ---");
        System.out.println("1. Update Username");
        System.out.println("2. Update Email");
        System.out.print("Enter your choice: ");

        String choice = scan.nextLine().trim(); // Read menu option

        String newValue;

        switch (choice) {

            case "1":
                System.out.print("Enter new username: ");
                newValue = scan.nextLine().trim(); // Read new username

                if (newValue.isEmpty()) {
                    System.out.println("Username cannot be empty."); // Validate input
                    return;
                }

                user.setUserName(newValue); // Update username in object
                break;

            case "2":
                System.out.print("Enter new email: ");
                newValue = scan.nextLine().trim(); // Read new email

                if (newValue.isEmpty()) {
                    System.out.println("Email cannot be empty."); // Validate input
                    return;
                }

                if (!CreateAccount.checkValidEmail(newValue)) {
                    System.out.println("Invalid email format."); // Validate email pattern
                    return;
                }

                user.setUserEmail(newValue); // Update email in object
                break;

            default:
                System.out.println("Invalid option."); // Handle wrong menu choice
                return;
        }

        boolean updated = userDao.updateUser(user); // Save changes to DB

        if (updated) {
            System.out.println("Profile updated successfully!"); // Success message
        } else {
            System.out.println("Username or email already exists."); // Duplicate data case
        }
    }
}
