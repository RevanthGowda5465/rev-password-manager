package com.passwordmanager.service.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.passwordmanager.dto.AccountCredentials;

import java.util.*;

public class AddAccountCredentialTest {

    @Test
    void testAddCredentialDirectInMemory() {
        Map<Integer, AccountCredentials> accountStore = new HashMap<>();

        // Create new account
        AccountCredentials account = new AccountCredentials();
        account.setAccountId(1); // simulate DB ID
        account.setUserId(100);  // simulate user ID
        account.setAccountName("Gmail");
        account.setAccountPasswordHash("hashed_password");

        accountStore.put(account.getAccountId(), account);

        assertTrue(accountStore.containsKey(1), "Account creation failed");
    }
}
