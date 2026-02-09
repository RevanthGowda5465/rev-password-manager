package com.passwordmanager.service.account;

import com.passwordmanager.dao.implementation.AccountCredentialsDaoImp;
import com.passwordmanager.dto.AccountCredentials;
import com.passwordmanager.util.SimpleCipherUtil;
import org.junit.jupiter.api.Test;

public class AddAccountCredentialTest {

    @Test
    public void testAddCredentialDirect() {

        // User id used for this test run
        int userId = 1;

        // Preparing dummy account data
        AccountCredentials account = new AccountCredentials();
        account.setUserId(userId);
        account.setAccountName("Gmail");
        account.setAccountPasswordHash(SimpleCipherUtil.encrypt("MyPass123!"));

        // DAO object calling real database
        AccountCredentialsDaoImp dao = new AccountCredentialsDaoImp();

        boolean success = dao.addAccount(account);   // performs actual insert

        if (success) {
            System.out.println("Test: Account inserted correctly!");
        } else {
            System.out.println("Test: Insert failed (record may already exist).");
        }
    }
}
