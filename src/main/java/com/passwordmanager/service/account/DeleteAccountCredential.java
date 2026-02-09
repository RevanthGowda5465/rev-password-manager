package com.passwordmanager.service.account;

import java.util.Scanner;
import com.passwordmanager.dao.AccountCredentialsDao;
import com.passwordmanager.dao.UserInfoDao;
import com.passwordmanager.dao.implementation.AccountCredentialsDaoImp;
import com.passwordmanager.dao.implementation.UserInfoDaoImp;

public class DeleteAccountCredential {

    // This method deletes an account credential after verifying the user's master password
    public static void deleteCredential(int userId) {

        // Scanner for reading user input
        Scanner scan = new Scanner(System.in);

        // DAO objects to interact with database tables
        AccountCredentialsDao accountDao = new AccountCredentialsDaoImp();
        UserInfoDao userDao = new UserInfoDaoImp();

        // Ask user which account should be deleted
        System.out.println("Enter Account Name to delete:");
        String accountName = scan.nextLine().trim();

        // Ask for master password to confirm the action
        System.out.println("Enter Master Password to continue:");
        String masterPassword = scan.nextLine().trim();

        // Encrypt the entered password before verification
        String encryptedPassword =
                com.passwordmanager.util.SimpleCipherUtil.encrypt(masterPassword);

        // Verify login using stored email and encrypted password
        boolean verified =
                userDao.verifyLogin(
                        userDao.getUserById(userId).getUserEmail(),
                        encryptedPassword);

        // If password is wrong, stop the deletion process
        if (!verified) {
            System.out.println("Invalid master password.");
            return;
        }

        // Delete the account from database
        boolean deleted = accountDao.deleteAccount(userId, accountName);

        // Show result message to user
        if (deleted) {
            System.out.println("Account deleted successfully!");
        } else {
            System.out.println("Failed to delete account. Account may not exist.");
        }
    }
}
