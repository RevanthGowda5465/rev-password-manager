package com.passwordmanager.dto;

public class UserInfo {

	// Stores the unique id of the user from the database
	private int userId;

	// Stores the username chosen by the user
	private String userName;

	// Stores the hashed version of the master password
	private String masterPasswordHash;

	// Stores the email id of the user
	private String userEmail;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMasterPasswordHash() {
		return masterPasswordHash;
	}

	public void setMasterPasswordHash(String masterPasswordHash) {
		this.masterPasswordHash = masterPasswordHash;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
}
