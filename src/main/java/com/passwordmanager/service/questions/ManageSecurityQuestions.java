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

public class ManageSecurityQuestions {

	// Shared scanner for menu input
	private static final Scanner scan = new Scanner(System.in);

	// DAO objects reused in this class
	private static final SecurityQuestionDao questionDao = new SecurityQuestionDaoImp();
	private static final SecurityAnswerDao answerDao = new SecurityAnswerDaoImp();
	private static final UserInfoDao userDao = new UserInfoDaoImp();

	// Main menu loop for managing questions
	public static void manage(int userId) {

		while (true) {

			System.out.println("\n--- MANAGE SECURITY QUESTIONS ---");
			System.out.println("1. View security question and answer");
			System.out.println("2. Update existing question answers");
			System.out.println("3. Replace a question with a new question");
			System.out.println("0. Back to User Menu");
			System.out.print("Enter your choice: ");

			String choice = scan.nextLine().trim();

			switch (choice) {
			case "1":
				viewSecurityQuestions(userId);
				break;

			case "2":
				UpdateUserSecurityQuestion.updateUserSecurityQuestion(userId);
				break;

			case "3":
				replaceSecurityQuestion(userId);
				break;

			case "0":
				return;

			default:
				System.out.println("Invalid choice. Try again.");
			}
		}
	}

	// Shows questions and answers after verifying password
	private static void viewSecurityQuestions(int userId) {

		System.out.println("Enter Master Password for verification:");

		String masterPassword = scan.nextLine().trim();

		// DAO handles encryption internally
		if (!userDao.verifyLoginByUserIdAndPassword(userId, masterPassword)) {

			System.out.println("Invalid master password.");
			return;
		}

		List<SecurityAnswer> answers = answerDao.getAnswersByUser(userId);

		if (answers.isEmpty()) {
			System.out.println("No security questions set.");
			return;
		}

		System.out.println("\n--- SECURITY QUESTIONS & ANSWERS ---");

		for (SecurityAnswer ans : answers) {

			SecurityQuestion q = questionDao.getQuestionById(ans.getQuestionId());

			if (q != null) {

				System.out.println("Q: " + q.getQuestionName());

				// Decrypt answer before displaying
				System.out.println("A: " + SimpleCipherUtil.decrypt(ans.getSecurityAnswerHash()));
			}
		}
	}

	// Replaces one existing question with a new one
	private static void replaceSecurityQuestion(int userId) {

		List<SecurityAnswer> answers = answerDao.getAnswersByUser(userId);

		if (answers.isEmpty()) {
			System.out.println("No security questions set for this user.");
			return;
		}

		System.out.println("\n--- YOUR SECURITY QUESTIONS ---");

		for (SecurityAnswer ans : answers) {

			SecurityQuestion q = questionDao.getQuestionById(ans.getQuestionId());

			if (q != null) {
				System.out.println(q.getQuestionId() + ". " + q.getQuestionName());
			}
		}

		System.out.print("Enter the question id to replace: ");

		int oldQId;

		try {
			oldQId = Integer.parseInt(scan.nextLine().trim());
		} catch (Exception e) {
			System.out.println("Invalid input.");
			return;
		}

		System.out.println("\n--- SELECT NEW QUESTION ---");

		List<SecurityQuestion> allQuestions = questionDao.getAllQuestions();

		for (SecurityQuestion q : allQuestions) {
			System.out.println(q.getQuestionId() + ". " + q.getQuestionName());
		}

		System.out.print("Enter new question id: ");

		int newQId;

		try {
			newQId = Integer.parseInt(scan.nextLine().trim());
		} catch (Exception e) {
			System.out.println("Invalid input.");
			return;
		}

		System.out.print("Enter answer for the new question: ");

		String newAnswer = scan.nextLine().trim();

		if (newAnswer.isEmpty()) {
			System.out.println("Answer cannot be empty.");
			return;
		}

		// Remove old answer from DB
		answerDao.deleteAnswer(userId, oldQId);

		// Store new answer after encryption
		SecurityAnswer sa = new SecurityAnswer();

		sa.setUserId(userId);
		sa.setQuestionId(newQId);
		sa.setSecurityAnswerHash(SimpleCipherUtil.encrypt(newAnswer));

		answerDao.addAnswer(sa);

		System.out.println("Question replaced successfully!");
	}
}
