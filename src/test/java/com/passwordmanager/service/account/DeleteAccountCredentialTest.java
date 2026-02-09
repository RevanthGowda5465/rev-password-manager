package com.passwordmanager.service.account;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.passwordmanager.dao.AccountCredentialsDao;
import com.passwordmanager.dao.implementation.AccountCredentialsDaoImp;
import com.passwordmanager.dto.AccountCredentials;
import com.passwordmanager.util.SimpleCipherUtil;

public class DeleteAccountCredentialTest {

    @Test
    public void testDeleteAccount() {

        AccountCredentialsDao accountDao = new AccountCredentialsDaoImp();

        // Insert a temporary record for deletion test
        AccountCredentials account = new AccountCredentials();
        account.setUserId(1);
        account.setAccountName("TestDelete");
        account.setAccountPasswordHash(SimpleCipherUtil.encrypt("Delete123$"));
        accountDao.addAccount(account);

        // Confirm record is available before deleting
        AccountCredentials fetched = accountDao.getAccountByName(1, "TestDelete");
        assertNotNull(fetched);

        // Execute delete operation
        boolean deleted = accountDao.deleteAccount(1, "TestDelete");
        assertTrue(deleted);

        // Check that the record no longer exists
        AccountCredentials deletedAccount = accountDao.getAccountByName(1, "TestDelete");
        assertNull(deletedAccount);
    }
}
