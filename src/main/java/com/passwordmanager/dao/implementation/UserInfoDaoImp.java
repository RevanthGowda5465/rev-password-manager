package com.passwordmanager.dao.implementation;

import java.sql.*;

import com.passwordmanager.connection.ConnectorFactory;
import com.passwordmanager.dao.UserInfoDao;
import com.passwordmanager.dto.UserInfo;
import com.passwordmanager.util.SimpleCipherUtil;
import com.passwordmanager.util.LoggerFactory;

import org.apache.logging.log4j.Logger;

public class UserInfoDaoImp implements UserInfoDao {

    // Logger to track important actions and errors
    private static final Logger logger = LoggerFactory.getLogger(UserInfoDaoImp.class);

    @Override
    public boolean createUser(UserInfo user) {
        // SQL query to add a new user
        String sql = "INSERT INTO User_Info(userName, userEmail, masterPasswordHash) VALUES (?, ?, ?)";
        try (Connection conn = ConnectorFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserEmail());
            ps.setString(3, user.getMasterPasswordHash());

            // Execute the insert and check if it worked
            boolean success = ps.executeUpdate() > 0;
            if (success) {
                logger.info("Created new user '{}' with email '{}'", user.getUserName(), user.getUserEmail());
            }
            return success;

        } catch (SQLException e) {
            // Handle duplicate email error
            if (e.getErrorCode() == 1) {
                logger.warn("Failed to create user '{}', email '{}' already exists", user.getUserName(), user.getUserEmail());
                return false;
            }
            logger.error("DB error while creating user '{}'", user.getUserName(), e);
            throw new RuntimeException("DB error while creating user", e);
        }
    }

    @Override
    public UserInfo getUserByEmail(String email) {
        // SQL query to get a user by email
        String sql = "SELECT * FROM User_Info WHERE userEmail = ?";
        try (Connection conn = ConnectorFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            // Check if a user exists and return it
            if (rs.next()) {
                UserInfo user = new UserInfo();
                user.setUserId(rs.getInt("userId"));
                user.setUserName(rs.getString("userName"));
                user.setUserEmail(rs.getString("userEmail"));
                user.setMasterPasswordHash(rs.getString("masterPasswordHash"));
                logger.info("Fetched user details for email '{}'", email);
                return user;
            } else {
                logger.warn("No user found with email '{}'", email);
            }

            return null;
        } catch (SQLException e) {
            logger.error("DB error while fetching user by email '{}'", email, e);
            throw new RuntimeException("DB error while fetching user by email", e);
        }
    }

    @Override
    public UserInfo getUserById(int userId) {
        // SQL query to get a user by ID
        String sql = "SELECT * FROM User_Info WHERE userId = ?";
        try (Connection conn = ConnectorFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            // Check if a user exists and return it
            if (rs.next()) {
                UserInfo user = new UserInfo();
                user.setUserId(rs.getInt("userId"));
                user.setUserName(rs.getString("userName"));
                user.setUserEmail(rs.getString("userEmail"));
                user.setMasterPasswordHash(rs.getString("masterPasswordHash"));
                logger.info("Fetched user details for userId={}", userId);
                return user;
            } else {
                logger.warn("No user found with userId={}", userId);
            }

            return null;
        } catch (SQLException e) {
            logger.error("DB error while fetching user by userId={}", userId, e);
            throw new RuntimeException("DB error while fetching user by id", e);
        }
    }

    @Override
    public boolean updateUser(UserInfo user) {
        // SQL query to update username and email
        String sql = "UPDATE User_Info SET userName = ?, userEmail = ? WHERE userId = ?";
        try (Connection conn = ConnectorFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserEmail());
            ps.setInt(3, user.getUserId());

            // Execute update and check if it worked
            boolean success = ps.executeUpdate() > 0;
            if (success) {
                logger.info("Updated user info for userId={}: new username='{}', new email='{}'",
                        user.getUserId(), user.getUserName(), user.getUserEmail());
            } else {
                logger.warn("Failed to update user info for userId={}", user.getUserId());
            }

            return success;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1) {
                logger.warn("Failed to update userId={}, username/email already exists", user.getUserId());
                return false;
            }
            logger.error("DB error while updating userId={}", user.getUserId(), e);
            throw new RuntimeException("DB error while updating user", e);
        }
    }

    @Override
    public boolean deleteUser(int userId) {
        // SQL query to delete a user by ID
        String sql = "DELETE FROM User_Info WHERE userId = ?";
        try (Connection conn = ConnectorFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            boolean success = ps.executeUpdate() > 0;
            if (success) {
                logger.info("Deleted user with userId={}", userId);
            } else {
                logger.warn("Attempted to delete non-existing user with userId={}", userId);
            }
            return success;
        } catch (SQLException e) {
            logger.error("DB error while deleting user with userId={}", userId, e);
            throw new RuntimeException("DB error while deleting user", e);
        }
    }

    @Override
    public boolean verifyLogin(String email, String masterPassword) {
        // SQL query to check login credentials
        String sql = "SELECT 1 FROM User_Info WHERE userEmail = ? AND masterPasswordHash = ?";
        try (Connection conn = ConnectorFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, masterPassword);

            // Return true if a record exists
            boolean success = ps.executeQuery().next();
            if (!success) {
                logger.warn("Failed login attempt for email='{}'", email);
            }
            return success;
        } catch (SQLException e) {
            logger.error("DB error during login verification for email='{}'", email, e);
            throw new RuntimeException("DB error during login verification", e);
        }
    }

    @Override
    public boolean updatePassword(int userId, String newPassword) {
        // SQL query to update the password
        String sql = "UPDATE User_Info SET masterPasswordHash = ? WHERE userId = ?";
        try (Connection conn = ConnectorFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setInt(2, userId);

            boolean success = ps.executeUpdate() > 0;
            if (success) {
                logger.info("Password updated successfully for userId={}", userId);
            } else {
                logger.warn("Failed to update password for userId={}", userId);
            }
            return success;
        } catch (SQLException e) {
            logger.error("DB error while updating password for userId={}", userId, e);
            throw new RuntimeException("DB error while updating password", e);
        }
    }

    @Override
    public boolean verifyLoginByUsernameOrEmail(String input, String password) {
        // SQL to verify login using either username or email
        String sql = "SELECT COUNT(*) FROM USER_INFO WHERE (USEREMAIL = ? OR USERNAME = ?) AND MASTERPASSWORDHASH = ?";
        try (Connection con = ConnectorFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, input);
            ps.setString(2, input);
            ps.setString(3, password);

            ResultSet rs = ps.executeQuery();
            boolean success = rs.next() && rs.getInt(1) > 0;
            if (!success) {
                logger.warn("Failed login attempt for username/email '{}'", input);
            }
            return success;
        } catch (SQLException e) {
            logger.error("DB error during login verification for username/email '{}'", input, e);
            throw new RuntimeException("DB error during login verification", e);
        }
    }

    @Override
    public boolean verifyLoginByUserIdAndPassword(int userId, String plainPassword) {
        // SQL to get password hash for a userId
        String sql = "SELECT MASTERPASSWORDHASH FROM USER_INFO WHERE USERID = ?";
        try (Connection conn = ConnectorFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("MASTERPASSWORDHASH");
                String inputHash = SimpleCipherUtil.encrypt(plainPassword);
                boolean match = storedHash.equals(inputHash);
                if (!match) {
                    logger.warn("Failed login attempt by userId={}", userId);
                }
                return match;
            } else {
                logger.warn("Login attempt for non-existing userId={}", userId);
                return false;
            }
        } catch (SQLException e) {
            logger.error("DB error during login verification by userId={}", userId, e);
            throw new RuntimeException("DB error during login verification by userId", e);
        }
    }

    @Override
    public UserInfo getUserByUsernameOrEmail(String input) {
        // SQL to get user by username or email
        String sql = "SELECT * FROM USER_INFO WHERE USEREMAIL = ? OR USERNAME = ?";
        try (Connection con = ConnectorFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, input);
            ps.setString(2, input);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                UserInfo user = new UserInfo();
                user.setUserId(rs.getInt("USERID"));
                user.setUserName(rs.getString("USERNAME"));
                user.setUserEmail(rs.getString("USEREMAIL"));
                user.setMasterPasswordHash(rs.getString("MASTERPASSWORDHASH"));
                logger.info("Fetched user details for username/email '{}'", input);
                return user;
            } else {
                logger.warn("No user found with username/email '{}'", input);
            }

            return null;
        } catch (SQLException e) {
            logger.error("DB error while fetching user by username/email '{}'", input, e);
            throw new RuntimeException("DB error while fetching user by username/email", e);
        }
    }
}
