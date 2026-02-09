package com.passwordmanager.service.account;

import java.util.Scanner;
import com.passwordmanager.dao.AccountCredentialsDao;
import com.passwordmanager.dao.UserInfoDao;
import com.passwordmanager.dao.implementation.AccountCredentialsDaoImp;
import com.passwordmanager.dao.implementation.UserInfoDaoImp;
import com.passwordmanager.dto.AccountCredentials;
import com.passwordmanager.util.SimpleCipherUtil;

public class UpdateAccountCredential {

	// This method updates the password for an existing account
	// Master password is required for security
	public static void updateCredential(int userId) {

		// Scanner for reading user input
		Scanner scan = new Scanner(System.in);

		// DAO objects to interact with database tables
		AccountCredentialsDao accountDao = new AccountCredentialsDaoImp();
		UserInfoDao userDao = new UserInfoDaoImp();

		// Ask which account should be updated
		System.out.println("Enter Account Name to update:");
		String accountName = scan.nextLine().trim();

		// Ask user to confirm master password
		System.out.println("Enter Master Password to continue:");
		String masterPassword = scan.nextLine().trim();

		// Encrypt password before verification
		String encryptedPassword = SimpleCipherUtil.encrypt(masterPassword);

		// Verify login before allowing update
		boolean verified = userDao.verifyLogin(userDao.getUserById(userId).getUserEmail(), encryptedPassword);

		if (!verified) {
			System.out.println("Invalid master password.");
			return;
		}

		// Fetch account details
		AccountCredentials account = accountDao.getAccountByName(userId, accountName);

		if (account == null) {
			System.out.println("Account not found.");
			return;
		}

		// Ask user for new password
		System.out.println("Enter new password for the account:");
		String newPassword = scan.nextLine().trim();

		// Encrypt new password before saving
		account.setAccountPasswordHash(SimpleCipherUtil.encrypt(newPassword));

		// Update account in database
		boolean updated = accountDao.updateAccount(account);

		// Show final result
		if (updated) {
			System.out.println("Account password updated successfully!");
		} else {
			System.out.println("Failed to update account.");
		}
	}
}
