package com.passwordmanager.dao;

import java.util.List;
import com.passwordmanager.dto.SecurityAnswer;

public interface SecurityAnswerDao {

	// Add a security answer for a user
	boolean addAnswer(SecurityAnswer answer);

	// Update a security answer
	boolean updateAnswer(SecurityAnswer answer);

	// Get all security answers for a user
	List<SecurityAnswer> getAnswersByUser(int userId);

	// Check if the answer is correct for a question
	boolean verifyAnswer(int userId, int questionId, String answerHash);

	// Delete a specific answer
	boolean deleteAnswer(int userId, int questionId);

	// Update an answer for a specific user and question
	boolean updateAnswerByUserAndQuestion(int userId, int questionId, String newAnswer);
}
