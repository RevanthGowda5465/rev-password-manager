package com.passwordmanager.service.account;

import java.util.Scanner;

import com.passwordmanager.dao.AccountCredentialsDao;
import com.passwordmanager.dao.implementation.AccountCredentialsDaoImp;
import com.passwordmanager.dto.AccountCredentials;
import com.passwordmanager.service.password.GeneratePassword;
import com.passwordmanager.util.SimpleCipherUtil;

public class AddAccountCredential {

	// This method is used to add a new account for a given user
	public static void addCredential(int userId) {

		// Scanner is used to read user input from the console
		Scanner scan = new Scanner(System.in);

		// DAO object to perform database operations
		AccountCredentialsDao accountDao = new AccountCredentialsDaoImp();

		// DTO object to store account details
		AccountCredentials account = new AccountCredentials();

		// Set the logged-in user's id for this account
		account.setUserId(userId);

		String accountName;

		// Keep asking until a valid account name is entered
		while (true) {

			System.out.println("Enter Account Name (e.g., Gmail, Facebook):");
			accountName = scan.nextLine().trim();

			if (!accountName.isEmpty()) {
				break;
			}

			System.out.println("Account name cannot be empty. Try again.");
		}

		// Save account name into DTO object
		account.setAccountName(accountName);

		String password = null;

		// Loop to allow the user to choose how the password should be created
		while (true) {

			System.out.println("\nChoose password option:");
			System.out.println("1. Enter your own password");
			System.out.println("2. Generate random password");
			System.out.print("Enter choice: ");

			String choice = scan.nextLine().trim();

			switch (choice) {

			case "1":

				// User enters their own password
				while (true) {
					System.out.println("Enter Account Password:");
					password = scan.nextLine();

					if (!password.trim().isEmpty()) {
						break;
					}

					System.out.println("Password cannot be empty.");
				}
				break;

			case "2":

				int length;

				// Ask for password length and validate the input
				while (true) {
					System.out.println("Enter desired password length:");

					try {
						length = Integer.parseInt(scan.nextLine());

						if (length < 6) {
							System.out.println("Password length must be at least 6.");
							continue;
						}

						break;

					} catch (NumberFormatException e) {
						System.out.println("Please enter a valid number.");
					}
				}

				// Generate a strong random password
				password = GeneratePassword.generatePassword(length, true, true, true, true);

				System.out.println("Generated Password: " + password);
				break;

			default:
				System.out.println("Invalid choice. Please select 1 or 2.");
				continue;
			}

			break;
		}

		// Encrypt the password before storing it in the database
		account.setAccountPasswordHash(SimpleCipherUtil.encrypt(password));

		boolean success;

		try {
			// Save account details into database
			success = accountDao.addAccount(account);
		} catch (Exception e) {
			System.out.println("Something went wrong while saving account.");
			return;
		}

		// Show result message to the user
		if (success) {
			System.out.println("Account credential added successfully!");
		} else {
			System.out.println("Failed to add credential. Account name might already exist.");
		}
	}
}
