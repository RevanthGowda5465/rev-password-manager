package com.passwordmanager.testSuite;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import com.passwordmanager.service.account.AddAccountCredentialTest;
import com.passwordmanager.service.account.DeleteAccountCredentialTest;
import com.passwordmanager.service.account.UpdateAccountCredentialTest;
import com.passwordmanager.service.account.ViewAccountDetailTest;
import com.passwordmanager.service.password.GeneratePasswordTest;
import com.passwordmanager.service.password.SimpleCipherUtilTest;
import com.passwordmanager.service.questions.ForgotPasswordTest;
import com.passwordmanager.service.questions.ManageSecurityQuestionsTest;
import com.passwordmanager.service.user.LoginTest;
import com.passwordmanager.service.user.CreateAccountTest;

// Define this class as a JUnit 5 suite
@Suite
@SelectClasses({
        AddAccountCredentialTest.class,
        DeleteAccountCredentialTest.class,
        UpdateAccountCredentialTest.class,
        ViewAccountDetailTest.class,
        GeneratePasswordTest.class,
        SimpleCipherUtilTest.class,
        ForgotPasswordTest.class,
        ManageSecurityQuestionsTest.class,
        LoginTest.class,
        CreateAccountTest.class
})
public class TestSuite {
    
}
