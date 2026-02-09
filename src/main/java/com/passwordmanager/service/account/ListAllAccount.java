package com.passwordmanager.service.account;

import java.util.List;

import com.passwordmanager.dao.AccountCredentialsDao;
import com.passwordmanager.dao.implementation.AccountCredentialsDaoImp;
import com.passwordmanager.dto.AccountCredentials;

public class ListAllAccount {

    // This method fetches and displays all accounts saved by a user
    public static void listAll(int userId) {

        // DAO object used to read account data from database
        AccountCredentialsDao accountDao = new AccountCredentialsDaoImp();

        // Get all accounts linked to this user
        List<AccountCredentials> accounts = accountDao.getAllAccountsByUser(userId);

        // Serial number for displaying the list
        int slno = 1;

        // If no accounts exist, show message to user
        if (accounts.isEmpty()) {
            System.out.println("No accounts stored.");
        } else {

            // Print header before showing account names
            System.out.println("Accounts:");
            System.out.println("Sl. No. | Name: ");

            // Loop through accounts and print each name
            for (AccountCredentials acc : accounts) {
                System.out.println(slno + "\t| " + acc.getAccountName());
                slno++;
            }
        }
    }
}
