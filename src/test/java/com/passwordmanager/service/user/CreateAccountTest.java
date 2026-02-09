package com.passwordmanager.service.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CreateAccountTest {

    @Test
    public void testValidEmail() {

        // Check that proper email formats are accepted
        assertTrue(CreateAccount.checkValidEmail("test@example.com"));

        // Ensure incorrect email formats are rejected
        assertFalse(CreateAccount.checkValidEmail("invalid-email"));
    }

    @Test
    public void testValidPassword() {

        // Strong password should pass validation
        assertTrue(CreateAccount.checkValidPassword("Abc123$%"));

        // Weak password should fail the rule check
        assertFalse(CreateAccount.checkValidPassword("password"));
    }
}
