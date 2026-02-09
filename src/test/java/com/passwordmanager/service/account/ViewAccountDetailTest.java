package com.passwordmanager.service.account;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.passwordmanager.dao.AccountCredentialsDao;
import com.passwordmanager.dao.implementation.AccountCredentialsDaoImp;
import com.passwordmanager.dto.AccountCredentials;
import com.passwordmanager.util.SimpleCipherUtil;

public class ViewAccountDetailTest {

    @Test
    public void testViewAccount() {

        AccountCredentialsDao accountDao = new AccountCredentialsDaoImp();

        // Add a record that will be fetched later
        AccountCredentials account = new AccountCredentials();
        account.setUserId(1);
        account.setAccountName("ViewTest");
        account.setAccountPasswordHash(SimpleCipherUtil.encrypt("View123$"));
        accountDao.addAccount(account);

        // Retrieve stored account information
        AccountCredentials fetched = accountDao.getAccountByName(1, "ViewTest");

        // Validate returned values
        assertNotNull(fetched);
        assertEquals("ViewTest", fetched.getAccountName());
        assertEquals("View123$", SimpleCipherUtil.decrypt(fetched.getAccountPasswordHash()));
    }
}
