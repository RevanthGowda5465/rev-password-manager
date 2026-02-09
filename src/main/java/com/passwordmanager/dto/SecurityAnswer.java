package com.passwordmanager.dto;

public class SecurityAnswer {

    // Stores the hashed version of the security answer
    private String securityAnswerHash;

    // Stores the user id for whom this answer belongs
    private int userId;

    // Stores the related security question id
    private int questionId;

    public String getSecurityAnswerHash() {
        return securityAnswerHash;
    }

    public void setSecurityAnswerHash(String securityAnswerHash) {
        this.securityAnswerHash = securityAnswerHash;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
}
