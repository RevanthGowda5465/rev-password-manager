package com.passwordmanager.dto;

public class AccountCredentials {

	// Stores the account id from the database
	private int accountId;

	// Stores the name of the account like Gmail, Facebook, etc.
	private String accountName;

	// Stores the encrypted or hashed password for the account
	private String accountPasswordHash;

	// Stores the user id to whom this account belongs
	private int userId;

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountPasswordHash() {
		return accountPasswordHash;
	}

	public void setAccountPasswordHash(String accountPasswordHash) {
		this.accountPasswordHash = accountPasswordHash;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
