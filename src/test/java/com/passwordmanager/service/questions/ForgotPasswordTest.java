package com.passwordmanager.service.questions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.passwordmanager.dao.UserInfoDao;
import com.passwordmanager.dao.implementation.UserInfoDaoImp;
import com.passwordmanager.dto.UserInfo;
import com.passwordmanager.service.password.ForgotPassword;
import com.passwordmanager.util.SimpleCipherUtil;

public class ForgotPasswordTest {

    @Test
    public void testCheckValidEmail() {

        // Email validation scenarios
        assertTrue(ForgotPassword.checkValidEmail("user@example.com"));
        assertFalse(ForgotPassword.checkValidEmail("userexample.com"));
    }

    @Test
    public void testCheckValidPassword() {

        // Strong vs weak password checks
        assertTrue(ForgotPassword.checkValidPassword("Abc123$%"));
        assertFalse(ForgotPassword.checkValidPassword("password"));
    }

    @Test
    public void testResetPasswordLogic() {

        UserInfoDao userDao = new UserInfoDaoImp();

        // Insert dummy user for password-reset testing
        UserInfo user = new UserInfo();
        user.setUserName("forgotTest");
        user.setUserEmail("forgot@example.com");
        user.setMasterPasswordHash(SimpleCipherUtil.encrypt("OldPass123$"));
        userDao.createUser(user);

        // Change the existing master password
        boolean updated = userDao.updatePassword(
                userDao.getUserByEmail("forgot@example.com").getUserId(),
                SimpleCipherUtil.encrypt("NewPass456$")
        );

        assertTrue(updated);

        // Attempt login using updated password
        boolean loginSuccess =
                userDao.verifyLoginByUsernameOrEmail(
                        "forgotTest",
                        SimpleCipherUtil.encrypt("NewPass456$")
                );

        assertTrue(loginSuccess);
    }
}
