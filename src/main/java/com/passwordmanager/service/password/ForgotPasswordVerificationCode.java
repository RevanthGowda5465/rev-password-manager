package com.passwordmanager.service.password;

import java.util.Random;
import java.util.Scanner;

import com.passwordmanager.dao.UserInfoDao;
import com.passwordmanager.dao.implementation.UserInfoDaoImp;
import com.passwordmanager.util.SimpleCipherUtil;

public class ForgotPasswordVerificationCode {

    // This method handles password reset using OTP verification
    public static void verificationCode(int userId) {

        // Scanner is used to read user input
        Scanner scan = new Scanner(System.in);

        // DAO used to update user password
        UserInfoDao userDao = new UserInfoDaoImp();

        // Generate a random 6 digit OTP
        int generatedOtp =
                100000 + new Random().nextInt(900000);

        System.out.println("OTP sent to registered mail.");
        System.out.println("(DEBUG OTP: " + generatedOtp + ")");

        // Ask user to enter received OTP
        System.out.print("Enter the OTP: ");
        int enteredOtp;

        try {
            enteredOtp =
                    Integer.parseInt(scan.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid OTP format.");
            return;
        }

        // Check whether OTP matches
        if (enteredOtp != generatedOtp) {
            System.out.println("Incorrect OTP.");
            return;
        }

        System.out.println("OTP verified successfully.");

        // Ask user to enter new master password
        System.out.println(
                "Enter new master password:(Password must minimum length of 8 and at least contains 1 Capital Letter, 1 Number, 1 symbol)");

        String newPassword =
                SimpleCipherUtil.encrypt(
                        scan.nextLine());

        // Validate password strength rules
        if (!ForgotPassword.checkValidPassword(
                newPassword)) {
            System.out.println(
                    "Password condition doesn't match.");
            return;
        }

        // Update password in database
        boolean updated =
                userDao.updatePassword(
                        userId,
                        newPassword);

        if (updated) {
            System.out.println(
                    "Password reset successfully!");
        } else {
            System.out.println(
                    "Failed to update password.");
        }
    }
}
