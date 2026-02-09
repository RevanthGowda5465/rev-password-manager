package com.passwordmanager.service.password;

import java.util.Scanner;
import com.passwordmanager.dao.SecurityAnswerDao;
import com.passwordmanager.dao.SecurityQuestionDao;
import com.passwordmanager.dao.UserInfoDao;
import com.passwordmanager.dao.implementation.SecurityAnswerDaoImp;
import com.passwordmanager.dao.implementation.SecurityQuestionDaoImp;
import com.passwordmanager.dao.implementation.UserInfoDaoImp;
import com.passwordmanager.service.questions.ForgotPasswordSecurityQuestion;

public class ForgotPassword {

    // Checks whether the given email follows proper email format
    public static boolean checkValidEmail(String email) {
        String regularExpression =
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null &&
               email.matches(regularExpression);
    }

    // Checks whether password has uppercase, digit,
    // special character and minimum length of 8
    public static boolean checkValidPassword(String password) {
        String regularExpression =
                "^(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}$";
        return password != null &&
               password.matches(regularExpression);
    }

    // Main flow for recovering forgotten password
    public static void recoverPassword() {

        // Scanner for reading user input
        Scanner scan = new Scanner(System.in);

        // DAO objects for user, question and answer tables
        UserInfoDao userDao = new UserInfoDaoImp();
        SecurityAnswerDao answerDao = new SecurityAnswerDaoImp();
        SecurityQuestionDao questionDao = new SecurityQuestionDaoImp();

        // Ask for registered email
        System.out.println("Enter your registered Email:");
        String email = scan.nextLine();

        // Validate email format first
        if (!checkValidEmail(email)) {
            System.out.println("Invaild mail entered.");
            return;
        }

        // Check whether email exists in database
        if (userDao.getUserByEmail(email) == null) {
            System.out.println("Email not found.");
            return;
        }

        // Let user choose recovery method
        System.out.println("Reset via:");
        System.out.println("1. Verification code");
        System.out.println("2. Security question");
        System.out.print("Enter the option: ");

        // Get userId for this email
        int userId =
                userDao.getUserByEmail(email)
                       .getUserId();

        String choice = "";
        choice = scan.nextLine();

        // Route user based on selected option
        switch (choice) {

        case "1":
            ForgotPasswordVerificationCode
                    .verificationCode(userId);
            break;

        case "2":
            ForgotPasswordSecurityQuestion
                    .securityQuestion(userId);
            break;

        default:
            System.out.println("");
        }
    }
}
