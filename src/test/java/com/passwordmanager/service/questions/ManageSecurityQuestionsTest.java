package com.passwordmanager.service.questions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.passwordmanager.dao.SecurityQuestionDao;
import com.passwordmanager.dao.implementation.SecurityQuestionDaoImp;
import com.passwordmanager.dao.SecurityAnswerDao;
import com.passwordmanager.dao.implementation.SecurityAnswerDaoImp;
import com.passwordmanager.dto.SecurityQuestion;
import com.passwordmanager.dto.SecurityAnswer;
import com.passwordmanager.util.SimpleCipherUtil;

public class ManageSecurityQuestionsTest {

    @Test
    public void testAddAndUpdateSecurityAnswer() {

        SecurityQuestionDao questionDao = new SecurityQuestionDaoImp();
        SecurityAnswerDao answerDao = new SecurityAnswerDaoImp();

        // Insert a new security question for testing
        SecurityQuestion question = new SecurityQuestion();
        question.setQuestionName("What is your pet's name?");

        int qId = questionDao.addQuestion(question);
        assertTrue(qId > 0);

        // Store encrypted answer for the user
        SecurityAnswer answer = new SecurityAnswer();
        answer.setUserId(1);
        answer.setQuestionId(qId);
        answer.setSecurityAnswerHash(SimpleCipherUtil.encrypt("Fluffy"));

        boolean added = answerDao.addAnswer(answer);
        assertTrue(added);

        // Modify answer with new value
        boolean updated =
                answerDao.updateAnswerByUserAndQuestion(
                        1,
                        qId,
                        SimpleCipherUtil.encrypt("Max")
                );

        assertTrue(updated);

        // Retrieve and validate the latest stored answer
        SecurityAnswer fetched =
                answerDao.getAnswersByUser(1).stream()
                        .filter(a -> a.getQuestionId() == qId)
                        .findFirst()
                        .orElse(null);

        assertNotNull(fetched);
        assertEquals("Max", SimpleCipherUtil.decrypt(fetched.getSecurityAnswerHash()));
    }
}
