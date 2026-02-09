package com.passwordmanager.service.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.passwordmanager.dto.UserInfo;
import com.passwordmanager.dao.UserInfoDao;
import com.passwordmanager.dao.implementation.UserInfoDaoImp;
import com.passwordmanager.util.SimpleCipherUtil;

public class LoginTest {

    @Test
    public void testLogin() {

        // Create DAO instance for user operations
        UserInfoDao userDao = new UserInfoDaoImp();

        // Insert a sample user for authentication test
        UserInfo user = new UserInfo();
        user.setUserName("tester");
        user.setUserEmail("tester@example.com");
        user.setMasterPasswordHash(SimpleCipherUtil.encrypt("Test123$"));
        userDao.createUser(user);

        // Attempt login with correct credentials
        boolean loginSuccess =
                userDao.verifyLoginByUsernameOrEmail(
                        "tester",
                        SimpleCipherUtil.encrypt("Test123$")
                );
        assertTrue(loginSuccess);

        // Try login using wrong password
        boolean loginFail =
                userDao.verifyLoginByUsernameOrEmail(
                        "tester",
                        SimpleCipherUtil.encrypt("WrongPass")
                );
        assertFalse(loginFail);
    }
}
