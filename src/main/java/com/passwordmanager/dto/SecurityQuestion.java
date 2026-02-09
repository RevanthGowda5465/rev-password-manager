package com.passwordmanager.dto;

public class SecurityQuestion {

	// Stores the id of the security question
	private int questionId;

	// Stores the actual question text
	private String questionName;

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public String getQuestionName() {
		return questionName;
	}

	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}
}
