package com.passwordmanager.service.account;

import java.util.Scanner;
import com.passwordmanager.dao.AccountCredentialsDao;
import com.passwordmanager.dao.UserInfoDao;
import com.passwordmanager.dao.implementation.AccountCredentialsDaoImp;
import com.passwordmanager.dao.implementation.UserInfoDaoImp;
import com.passwordmanager.dto.AccountCredentials;
import com.passwordmanager.util.SimpleCipherUtil;

public class ViewAccountDetail {

    // This method allows user to view one specific account's details
    // User must confirm master password before data is shown
    public static void viewAccount(int userId) {

        // Scanner is used to read input from console
        Scanner scan = new Scanner(System.in);

        // DAO objects for accessing account and user tables
        AccountCredentialsDao accountDao = new AccountCredentialsDaoImp();
        UserInfoDao userDao = new UserInfoDaoImp();

        // Ask user for account name to search
        System.out.println("Enter Account Name to view details:");
        String accountName = scan.nextLine().trim();

        // Ask user to re-enter master password for security check
        System.out.println("Re-enter Master Password:");
        String masterPassword = scan.nextLine().trim();

        // Encrypt entered password before verification
        String encryptedPassword =
                SimpleCipherUtil.encrypt(masterPassword);

        // Verify master password using email linked to this userId
        boolean verified =
                userDao.verifyLogin(
                        userDao.getUserById(userId).getUserEmail(),
                        encryptedPassword);

        // Stop execution if password is wrong
        if (!verified) {
            System.out.println("Invalid master password.");
            return;
        }

        // Fetch account details from database
        AccountCredentials account =
                accountDao.getAccountByName(userId, accountName);

        // If account is not found, inform user
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        // Display account details after successful verification
        System.out.println("\n--- ACCOUNT DETAILS ---");
        System.out.println("Account Name: " + account.getAccountName());

        // Decrypt password before showing it
        System.out.println(
                "Account Password: " +
                SimpleCipherUtil.decrypt(
                        account.getAccountPasswordHash()));
    }
}
