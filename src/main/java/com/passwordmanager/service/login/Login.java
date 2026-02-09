package com.passwordmanager.service.login;

import java.util.Scanner;
import com.passwordmanager.dao.UserInfoDao;
import com.passwordmanager.dao.implementation.UserInfoDaoImp;
import com.passwordmanager.dto.UserInfo;
import com.passwordmanager.util.SimpleCipherUtil;

public class Login {

    // This method handles user login and returns userId if successful
    // If login fails, it returns -1
    public static int login() {

        // Scanner is used to read input from console
        Scanner scan = new Scanner(System.in);

        // DAO object to interact with user table
        UserInfoDao userDao = new UserInfoDaoImp();

        // User can enter either username or email
        System.out.println("Enter Username or Email:");
        String input = scan.nextLine().trim();

        // Ask user to enter master password
        System.out.println("Enter Master Password:");
        String password = scan.nextLine().trim();

        // Encrypt the password before sending it to database
        String encryptedPassword =
                SimpleCipherUtil.encrypt(password);

        // Check whether given credentials are valid
        boolean valid =
                userDao.verifyLoginByUsernameOrEmail(
                        input,
                        encryptedPassword);

        if (valid) {

            // Fetch full user details after successful login
            UserInfo user =
                    userDao.getUserByUsernameOrEmail(input);

            System.out.println("Login successful!");

            // Return userId so that menu can continue
            return user.getUserId();

        } else {

            // Login failed case
            System.out.println("Invalid username/email or password.");
            return -1;
        }
    }
}
