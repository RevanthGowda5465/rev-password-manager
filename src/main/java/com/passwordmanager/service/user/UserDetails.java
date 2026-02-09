package com.passwordmanager.service.user;

import com.passwordmanager.dao.UserInfoDao;
import com.passwordmanager.dao.implementation.UserInfoDaoImp;
import com.passwordmanager.dto.UserInfo;

public class UserDetails {

	// Displays basic profile details of the logged-in user
	public static void getUserDetails(int userId) {

		UserInfoDao userDao = new UserInfoDaoImp(); // DAO for fetching user data

		UserInfo user = userDao.getUserById(userId); // Load user record

		if (user == null) {
			System.out.println("User details not found."); // Stop if user does not exist
			return;
		}

		System.out.println("\n--- USER PROFILE ---");
		System.out.println("Username : " + user.getUserName());
		System.out.println("Email    : " + user.getUserEmail());
	}
}
