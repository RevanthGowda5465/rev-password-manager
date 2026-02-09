package com.passwordmanager.service.password;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.passwordmanager.util.SimpleCipherUtil;

public class SimpleCipherUtilTest {

    @Test
    public void testEncryptAndDecrypt() {

        // Plain text value for encryption
        String original = "MySecret123!";

        String encrypted = SimpleCipherUtil.encrypt(original);
        assertNotNull(encrypted, "Encrypted output must not be null");

        // Convert encrypted value back to original
        String decrypted = SimpleCipherUtil.decrypt(encrypted);

        assertEquals(original, decrypted, "Final result should match starting value");
    }
}
