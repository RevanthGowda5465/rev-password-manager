package com.passwordmanager.service.account;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.passwordmanager.dto.AccountCredentials;
import com.passwordmanager.util.SimpleCipherUtil;

import java.util.HashMap;
import java.util.Map;

public class UpdateAccountCredentialTest {

    @Test
    void testUpdatePasswordInMemory() {

        // In-memory "database"
        Map<String, AccountCredentials> accountStore = new HashMap<>();

        // Create initial credential entry
        AccountCredentials account = new AccountCredentials();
        account.setUserId(1);
        account.setAccountName("Facebook");
        account.setAccountPasswordHash(SimpleCipherUtil.encrypt("OldPass123!"));
        accountStore.put(account.getUserId() + ":" + account.getAccountName(), account);

        // Update password
        AccountCredentials toUpdate = accountStore.get("1:Facebook");
        toUpdate.setAccountPasswordHash(SimpleCipherUtil.encrypt("NewPass456$"));
        accountStore.put("1:Facebook", toUpdate); // simulate DAO update

        // Verify updated password
        AccountCredentials fetched = accountStore.get("1:Facebook");
        assertEquals(SimpleCipherUtil.encrypt("NewPass456$"), fetched.getAccountPasswordHash());

        // Extra check: ensure old password is not present
        assertNotEquals(SimpleCipherUtil.encrypt("OldPass123!"), fetched.getAccountPasswordHash());
    }
}
