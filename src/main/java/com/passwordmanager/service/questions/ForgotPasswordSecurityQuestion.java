package com.passwordmanager.service.questions;

import java.util.List;
import java.util.Scanner;

import com.passwordmanager.dao.SecurityAnswerDao;
import com.passwordmanager.dao.SecurityQuestionDao;
import com.passwordmanager.dao.UserInfoDao;
import com.passwordmanager.dao.implementation.SecurityAnswerDaoImp;
import com.passwordmanager.dao.implementation.SecurityQuestionDaoImp;
import com.passwordmanager.dao.implementation.UserInfoDaoImp;
import com.passwordmanager.dto.SecurityAnswer;
import com.passwordmanager.dto.SecurityQuestion;
import com.passwordmanager.util.SimpleCipherUtil;

public class ForgotPasswordSecurityQuestion {

	// Validates email format using regex
	public static boolean checkValidEmail(String email) {
		String regularExpression = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
		return email != null && email.matches(regularExpression);
	}

	// Checks whether password follows required rules
	public static boolean checkValidPassword(String password) {
		String regularExpression = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$";
		return password != null && password.matches(regularExpression);
	}

	// Verifies security questions before allowing password reset
	public static void securityQuestion(int userId) {

		// Used to take user input
		Scanner scan = new Scanner(System.in);

		// DAO objects for DB operations
		UserInfoDao userDao = new UserInfoDaoImp();
		SecurityAnswerDao answerDao = new SecurityAnswerDaoImp();
		SecurityQuestionDao questionDao = new SecurityQuestionDaoImp();

		// Get all saved answers for this user
		List<SecurityAnswer> answers = answerDao.getAnswersByUser(userId);

		if (answers.isEmpty()) {
			System.out.println("No security questions set for this user.");
			return;
		}

		// Ask each security question one by one
		for (SecurityAnswer ans : answers) {

			SecurityQuestion question = questionDao.getQuestionById(ans.getQuestionId());

			if (question == null) {
				System.out.println("Security question not found.");
				return;
			}

			System.out.println("Answer the Security Question \n" + question.getQuestionName());

			String input = scan.nextLine();

			// Decrypt stored answer for comparison
			String storedAnswer = SimpleCipherUtil.decrypt(ans.getSecurityAnswerHash());

			if (!input.equals(storedAnswer)) {
				System.out.println("Incorrect answer. Cannot recover password.");
				return;
			}
		}

		System.out.println("All answers verified. You can now reset your password.");

		System.out.println(
				"Enter new master password:(Password must minimum length of 8 and at least contains 1 Capital Letter, 1 Number, 1 symbol)");

		String newPassword = SimpleCipherUtil.encrypt(scan.nextLine());

		// Validate password strength
		if (!checkValidPassword(newPassword)) {
			System.out.println("Password condition doesn't match.");
			return;
		}

		// Update password in database
		boolean updated = userDao.updatePassword(userId, newPassword);

		if (updated) {
			System.out.println("Password reset successfully!");
		} else {
			System.out.println("Failed to update password.");
		}
	}
}
