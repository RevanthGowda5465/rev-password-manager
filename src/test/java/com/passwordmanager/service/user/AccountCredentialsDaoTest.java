package com.passwordmanager.service.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.passwordmanager.dao.AccountCredentialsDao;
import com.passwordmanager.dao.implementation.AccountCredentialsDaoImp;
import com.passwordmanager.dto.AccountCredentials;
import com.passwordmanager.util.SimpleCipherUtil;

public class AccountCredentialsDaoTest {

    @Test
    public void testAddAndFetchAccount() {

        // Create DAO object to work with account records
        AccountCredentialsDao accountDao = new AccountCredentialsDaoImp();

        // Prepare sample account data for testing
        AccountCredentials account = new AccountCredentials();
        account.setUserId(1);
        account.setAccountName("Gmail");
        account.setAccountPasswordHash(SimpleCipherUtil.encrypt("MyPass123!"));

        // Try inserting the account into database
        boolean added = accountDao.addAccount(account);
        assertTrue(added);

        // Read the same account back using name
        AccountCredentials fetched = accountDao.getAccountByName(1, "Gmail");
        assertNotNull(fetched);
        assertEquals("Gmail", fetched.getAccountName());
    }
}
