package com.passwordmanager.service.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.passwordmanager.dao.SecurityAnswerDao;
import com.passwordmanager.dao.SecurityQuestionDao;
import com.passwordmanager.dao.UserInfoDao;
import com.passwordmanager.dao.implementation.SecurityAnswerDaoImp;
import com.passwordmanager.dao.implementation.SecurityQuestionDaoImp;
import com.passwordmanager.dao.implementation.UserInfoDaoImp;
import com.passwordmanager.dto.SecurityAnswer;
import com.passwordmanager.dto.SecurityQuestion;
import com.passwordmanager.dto.UserInfo;
import com.passwordmanager.util.SimpleCipherUtil;

public class CreateAccount {

	// Validates email format using regex
	public static boolean checkValidEmail(String email) {
		String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

		return email != null && email.matches(regex);
	}

	// Checks password strength rules
	public static boolean checkValidPassword(String password) {
		String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$";

		return password != null && password.matches(regex);
	}

	// Handles full user registration process
	public static void createAccount() {

		Scanner scan = new Scanner(System.in);

		// DAO objects to perform database operations
		UserInfoDao userDao = new UserInfoDaoImp();

		SecurityQuestionDao questionDao = new SecurityQuestionDaoImp();

		SecurityAnswerDao answerDao = new SecurityAnswerDaoImp();

		UserInfo user = new UserInfo();

		// Collect username
		System.out.println("Enter Username:");
		user.setUserName(scan.nextLine().trim());

		// Collect and validate email
		System.out.println("Enter Email:");
		String email = scan.nextLine().trim();

		if (!checkValidEmail(email)) {
			System.out.println("Invalid email format.");
			return;
		}

		user.setUserEmail(email);

		// Collect and validate master password
		System.out.println("Enter Master Password:");

		String password = scan.nextLine();

		if (!checkValidPassword(password)) {
			System.out.println("Password must contain uppercase, number, symbol and 8+ chars.");
			return;
		}

		// Encrypt password before storing in database
		user.setMasterPasswordHash(SimpleCipherUtil.encrypt(password));

		// Store answers temporarily until user is created
		List<SecurityAnswer> answers = new ArrayList<>();

		// Prevent selecting same question twice
		Set<Integer> usedQuestionIds = new HashSet<>();

		System.out.println("\nSECURITY QUESTION SETUP");

		// Force user to select two different security questions
		for (int i = 0; i < 2; i++) {

			System.out.println(i == 0 ? "Security Question 1" : "Security Question 2");

			System.out.println("1. Choose existing question");

			System.out.println("2. Create new question");

			String choice = scan.nextLine();

			int questionId;

			if (choice.equals("1")) {

				// Show all available questions
				List<SecurityQuestion> questions = questionDao.getAllQuestions();

				for (SecurityQuestion q : questions) {
					System.out.println(q.getQuestionId() + ". " + q.getQuestionName());
				}

				try {
					questionId = Integer.parseInt(scan.nextLine());
				} catch (Exception e) {
					System.out.println("Invalid input.");
					return;
				}

			} else if (choice.equals("2")) {

				// Allow user to add a new question
				System.out.println("Enter new security question:");

				SecurityQuestion q = new SecurityQuestion();

				q.setQuestionName(scan.nextLine().trim());

				questionId = questionDao.addQuestion(q);

				if (questionId == -1) {
					System.out.println("Failed to add question.");
					return;
				}

			} else {
				System.out.println("Invalid option.");
				return;
			}

			// Ensure the same question is not reused
			if (usedQuestionIds.contains(questionId)) {

				System.out.println("You already selected this question. Choose another.");
				i--;
				continue;
			}

			usedQuestionIds.add(questionId);

			// Ask for answer to the selected question
			System.out.println("Enter Answer:");
			String ans = scan.nextLine().trim();

			if (ans.isEmpty()) {
				System.out.println("Answer cannot be empty.");
				i--;
				continue;
			}

			SecurityAnswer sa = new SecurityAnswer();
			sa.setQuestionId(questionId);

			// ENCRYPT answer before storing
			sa.setSecurityAnswerHash(SimpleCipherUtil.encrypt(ans));

			answers.add(sa);

		}

		// Create user record first
		boolean created = userDao.createUser(user);

		if (!created) {
			System.out.println("Username or Email already exists.");
			return;
		}

		// Fetch newly created user's id
		int userId = userDao.getUserByEmail(user.getUserEmail()).getUserId();

		// Save security answers linked to this user
		for (SecurityAnswer a : answers) {

			a.setUserId(userId);

			try {

				boolean added = answerDao.addAnswer(a);

				if (!added) {
					System.out.println("Failed saving security answers.");
					return;
				}

			} catch (RuntimeException e) {

				System.out.println(e.getMessage());

				System.out.println("Account created but security answers failed.");
				return;
			}
		}

		System.out.println("\nAccount created successfully!");
	}
}
