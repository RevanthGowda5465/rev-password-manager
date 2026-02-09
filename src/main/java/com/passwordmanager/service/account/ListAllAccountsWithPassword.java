package com.passwordmanager.service.account;

import java.util.List;
import java.util.Scanner;

import com.passwordmanager.dao.AccountCredentialsDao;
import com.passwordmanager.dao.UserInfoDao;
import com.passwordmanager.dao.implementation.AccountCredentialsDaoImp;
import com.passwordmanager.dao.implementation.UserInfoDaoImp;
import com.passwordmanager.dto.AccountCredentials;
import com.passwordmanager.util.SimpleCipherUtil;

public class ListAllAccountsWithPassword {

    // This method shows all saved accounts along with decrypted passwords
    // User must enter master password to continue
    public static void listAllWithPassword(int userId) {

        // Scanner for reading console input
        Scanner scan = new Scanner(System.in);

        // DAO objects to interact with user and account tables
        UserInfoDao userDao = new UserInfoDaoImp();
        AccountCredentialsDao accountDao = new AccountCredentialsDaoImp();

        // Ask user to confirm master password
        System.out.println("Enter Master Password to continue:");
        String masterPassword = scan.nextLine().trim();

        // Verify master password using userId
        if (!userDao.verifyLoginByUserIdAndPassword(userId, masterPassword)) {
            System.out.println("Invalid master password.");
            return;
        }

        // Fetch all accounts for this user
        List<AccountCredentials> accounts = accountDao.getAllAccountsByUser(userId);

        // If no accounts are found, stop here
        if (accounts.isEmpty()) {
            System.out.println("No accounts stored.");
            return;
        }

        // Print header before displaying accounts
        System.out.println("\n--- STORED ACCOUNTS ---");
        System.out.println("Sl. No | Account Name | Password");

        int slNo = 1;

        // Loop through each account and display details
        for (AccountCredentials acc : accounts) {

            // Decrypt the password before showing it to user
            String decryptedPassword =
                    SimpleCipherUtil.decrypt(acc.getAccountPasswordHash());

            System.out.println(
                    slNo + " | " + acc.getAccountName() + " | " + decryptedPassword);

            slNo++;
        }
    }
}
