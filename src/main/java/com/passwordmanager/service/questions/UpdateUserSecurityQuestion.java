package com.passwordmanager.service.questions;

import java.util.List;
import java.util.Scanner;

import com.passwordmanager.dao.SecurityAnswerDao;
import com.passwordmanager.dao.SecurityQuestionDao;
import com.passwordmanager.dao.UserInfoDao;
import com.passwordmanager.dao.implementation.SecurityAnswerDaoImp;
import com.passwordmanager.dao.implementation.SecurityQuestionDaoImp;
import com.passwordmanager.dao.implementation.UserInfoDaoImp;
import com.passwordmanager.dto.SecurityQuestion;
import com.passwordmanager.util.SimpleCipherUtil;

public class UpdateUserSecurityQuestion {

	// Shared Scanner for user input
	private static final Scanner scan = new Scanner(System.in);

	// DAO objects used to talk to database
	private static final SecurityQuestionDao questionDao = new SecurityQuestionDaoImp();

	private static final SecurityAnswerDao answerDao = new SecurityAnswerDaoImp();

	private static final UserInfoDao userDao = new UserInfoDaoImp();

	// Allows user to update one of their existing security answers
	public static void updateUserSecurityQuestion(int userId) {

		// Ask master password before allowing update
		System.out.println("Enter Master Password to continue:");

		String masterPassword = scan.nextLine().trim();

		// DAO verifies password (handles encryption internally)
		if (!userDao.verifyLoginByUserIdAndPassword(userId, masterPassword)) {

			System.out.println("Invalid master password.");
			return;
		}

		// Get only this user's security questions
		List<SecurityQuestion> userQuestions = questionDao.getQuestionsByUserId(userId);

		if (userQuestions.isEmpty()) {
			System.out.println("No security questions found for this user.");
			return;
		}

		// Display questions so user can select one
		System.out.println("\n--- YOUR SECURITY QUESTIONS ---");

		for (SecurityQuestion q : userQuestions) {
			System.out.println(q.getQuestionId() + ". " + q.getQuestionName());
		}

		// Ask which question to update
		System.out.print("Enter question id to update: ");

		int questionId;

		try {
			questionId = Integer.parseInt(scan.nextLine().trim());
		} catch (Exception e) {
			System.out.println("Invalid input.");
			return;
		}

		// Take new answer from user
		System.out.print("Enter new answer: ");

		String newAnswer = scan.nextLine().trim();

		if (newAnswer.isEmpty()) {
			System.out.println("Answer cannot be empty.");
			return;
		}

		// Encrypt answer before storing in database
		boolean updated = answerDao.updateAnswerByUserAndQuestion(userId, questionId,
				SimpleCipherUtil.encrypt(newAnswer));

		if (updated) {
			System.out.println("Security answer updated successfully!");
		} else {
			System.out.println("Failed to update answer.");
		}
	}
}
