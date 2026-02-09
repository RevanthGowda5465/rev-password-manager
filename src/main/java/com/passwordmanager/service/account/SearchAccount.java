package com.passwordmanager.service.account;

import java.util.Scanner;

import com.passwordmanager.dao.AccountCredentialsDao;
import com.passwordmanager.dao.implementation.AccountCredentialsDaoImp;
import com.passwordmanager.dto.AccountCredentials;

public class SearchAccount {

	// This method searches for an account using its name
	public static void searchByName(int userId) {

		// Scanner to read user input
		Scanner scan = new Scanner(System.in);

		// DAO used to fetch account details
		AccountCredentialsDao accountDao = new AccountCredentialsDaoImp();

		// Ask user for account name
		System.out.println("Enter Account Name to search:");
		String accountName = scan.nextLine();

		// Get account from database
		AccountCredentials account = accountDao.getAccountByName(userId, accountName);

		// Show result based on whether account exists
		if (account != null) {
			System.out.println("Account Found!");
			System.out.println("Account Name: " + account.getAccountName());
		} else {
			System.out.println("Account not found.");
		}
	}
}
