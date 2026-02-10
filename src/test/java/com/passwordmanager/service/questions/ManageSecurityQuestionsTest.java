package com.passwordmanager.service.questions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.passwordmanager.dto.SecurityQuestion;
import com.passwordmanager.dto.SecurityAnswer;
import com.passwordmanager.util.SimpleCipherUtil;

import java.util.*;

public class ManageSecurityQuestionsTest {

    @Test
    public void testAddAndUpdateSecurityAnswerInMemory() {
        Map<Integer, SecurityQuestion> questions = new HashMap<>();
        Map<String, SecurityAnswer> answers = new HashMap<>();

        SecurityQuestion question = new SecurityQuestion();
        question.setQuestionId(1);
        question.setQuestionName("What is your pet's name?");
        questions.put(1, question);

        SecurityAnswer answer = new SecurityAnswer();
        answer.setUserId(1);
        answer.setQuestionId(1);
        answer.setSecurityAnswerHash(SimpleCipherUtil.encrypt("Fluffy"));
        answers.put("1:1", answer);

        // Update
        answers.get("1:1").setSecurityAnswerHash(SimpleCipherUtil.encrypt("Max"));

        assertEquals("Max", SimpleCipherUtil.decrypt(answers.get("1:1").getSecurityAnswerHash()));
    }
}
