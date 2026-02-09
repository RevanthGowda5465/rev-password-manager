package com.passwordmanager.dao;

import java.util.List;
import com.passwordmanager.dto.SecurityQuestion;

public interface SecurityQuestionDao {

	// Add a new security question
	int addQuestion(SecurityQuestion question);

	// Get all available security questions
	List<SecurityQuestion> getAllQuestions();

	// Get a security question by its ID
	SecurityQuestion getQuestionById(int questionId);

	// Delete a security question by ID
	boolean deleteQuestion(int questionId);

	// Get all questions that a user has selected
	List<SecurityQuestion> getQuestionsByUserId(int userId);
}
