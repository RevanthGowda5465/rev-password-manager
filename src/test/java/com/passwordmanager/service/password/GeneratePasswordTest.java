package com.passwordmanager.service.password;

import org.junit.jupiter.api.Test;

import com.passwordmanager.service.password.GeneratePassword;

import static org.junit.jupiter.api.Assertions.*;

public class GeneratePasswordTest {

    @Test
    public void testPasswordLength() {

        // Generate password using all character categories
        String pwd = GeneratePassword.generatePassword(12, true, true, true, true);

        assertNotNull(pwd);
        assertEquals(12, pwd.length());
    }

    @Test
    public void testNoCharacterTypeSelected() {

        // When nothing is selected, method should return null
        String pwd = GeneratePassword.generatePassword(10, false, false, false, false);

        assertNull(pwd);
    }
}
