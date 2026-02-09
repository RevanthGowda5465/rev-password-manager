package com.passwordmanager.dao;

import java.util.List;
import com.passwordmanager.dto.AccountCredentials;

public interface AccountCredentialsDao {

	// Add a new account for the user
	boolean addAccount(AccountCredentials account);

	// Update an existing account
	boolean updateAccount(AccountCredentials account);

	// Delete an account using ID and name
	boolean deleteAccount(int accountId, String accountName);

	// Get all accounts for a specific user
	List<AccountCredentials> getAllAccountsByUser(int userId);

	// Get a specific account by name for a user
	AccountCredentials getAccountByName(int userId, String accountName);

	// Get a specific account by its ID
	AccountCredentials getAccountById(int accountId);
}
