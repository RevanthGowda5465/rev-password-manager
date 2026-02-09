package com.passwordmanager.dao;

import com.passwordmanager.dto.UserInfo;

public interface UserInfoDao {

	// Create a new user account
	boolean createUser(UserInfo user);

	// Get user details by email
	UserInfo getUserByEmail(String email);

	// Get user details by ID
	UserInfo getUserById(int userId);

	// Update user information
	boolean updateUser(UserInfo user);

	// Delete a user by ID
	boolean deleteUser(int userId);

	// Check login with email and password
	boolean verifyLogin(String email, String masterPassword);

	// Change user password
	boolean updatePassword(int userId, String newPassword);

	// Verify login using either username or email
	boolean verifyLoginByUsernameOrEmail(String input, String password);

	// Verify login using user ID and password
	boolean verifyLoginByUserIdAndPassword(int userId, String plainPassword);

	// Get user by username or email
	UserInfo getUserByUsernameOrEmail(String input);
}
