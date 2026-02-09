package com.passwordmanager.service.account;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.passwordmanager.dao.AccountCredentialsDao;
import com.passwordmanager.dao.implementation.AccountCredentialsDaoImp;
import com.passwordmanager.dto.AccountCredentials;
import com.passwordmanager.util.SimpleCipherUtil;

public class UpdateAccountCredentialTest {

    @Test
    public void testUpdatePassword() {

        AccountCredentialsDao accountDao = new AccountCredentialsDaoImp();

        // Create initial credential entry
        AccountCredentials account = new AccountCredentials();
        account.setUserId(1);
        account.setAccountName("Facebook");
        account.setAccountPasswordHash(SimpleCipherUtil.encrypt("OldPass123!"));
        accountDao.addAccount(account);

        // Change password and update in DB
        account.setAccountPasswordHash(SimpleCipherUtil.encrypt("NewPass456$"));
        boolean updated = accountDao.updateAccount(account);
        assertTrue(updated);

        // Fetch updated record for verification
        AccountCredentials fetched = accountDao.getAccountByName(1, "Facebook");
        assertEquals(SimpleCipherUtil.encrypt("NewPass456$"), fetched.getAccountPasswordHash());
    }
}
