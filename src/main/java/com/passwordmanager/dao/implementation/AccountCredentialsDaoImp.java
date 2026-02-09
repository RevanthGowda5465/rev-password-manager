package com.passwordmanager.dao.implementation;

import java.sql.*;
import java.util.*;
import com.passwordmanager.connection.ConnectorFactory;
import com.passwordmanager.dao.AccountCredentialsDao;
import com.passwordmanager.dto.AccountCredentials;
import com.passwordmanager.util.LoggerFactory;

import org.apache.logging.log4j.Logger;

public class AccountCredentialsDaoImp implements AccountCredentialsDao {

    // Logger used to track database actions and errors
    private static final Logger logger = LoggerFactory.getLogger(AccountCredentialsDaoImp.class);

    @Override
    public boolean addAccount(AccountCredentials account) {
        // SQL query to insert a new account record
        String sql = "INSERT INTO Account_Credential(accountName, accountPasswordhash, userID) VALUES (?, ?, ?)";

        try (Connection conn = ConnectorFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, account.getAccountName());
            ps.setString(2, account.getAccountPasswordHash());
            ps.setInt(3, account.getUserId());

            // Execute insert and check if it was successful
            boolean success = ps.executeUpdate() > 0;

            if (success) {
                logger.info("Added account '{}' for userId={}", account.getAccountName(), account.getUserId());
            }

            return success;

        } catch (SQLException e) {
            // Handle duplicate account name case
            if (e.getErrorCode() == 1) {
                logger.warn("Failed to add account '{}', already exists for userId={}", account.getAccountName(), account.getUserId());
                return false;
            }
            logger.error("DB error while adding account '{}'", account.getAccountName(), e);
            throw new RuntimeException("DB error while adding account", e);
        }
    }

    @Override
    public boolean updateAccount(AccountCredentials account) {
        // SQL query to update account name and password
        String sql = "UPDATE Account_Credential SET accountName=?, accountPasswordhash=? WHERE accountId=?";

        try (Connection conn = ConnectorFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, account.getAccountName());
            ps.setString(2, account.getAccountPasswordHash());
            ps.setInt(3, account.getAccountId());

            boolean success = ps.executeUpdate() > 0;

            if (success) {
                logger.info("Updated account '{}' (accountId={})", account.getAccountName(), account.getAccountId());
            }

            return success;

        } catch (SQLException e) {
            logger.error("DB error while updating account '{}' (accountId={})", account.getAccountName(), account.getAccountId(), e);
            throw new RuntimeException("DB error while updating account", e);
        }
    }

    @Override
    public boolean deleteAccount(int userId, String accountName) {
        // SQL query to remove an account for a user
        String sql = "DELETE FROM Account_Credential WHERE userId=? AND accountName=?";

        try (Connection conn = ConnectorFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, accountName);

            boolean success = ps.executeUpdate() > 0;

            if (success) {
                logger.info("Deleted account '{}' for userId={}", accountName, userId);
            } else {
                logger.warn("Attempted to delete non-existing account '{}' for userId={}", accountName, userId);
            }

            return success;

        } catch (SQLException e) {
            logger.error("DB error while deleting account '{}' for userId={}", accountName, userId, e);
            throw new RuntimeException("DB error while deleting account", e);
        }
    }

    @Override
    public List<AccountCredentials> getAllAccountsByUser(int userId) {
        // SQL query to fetch all accounts for a user
        String sql = "SELECT * FROM Account_Credential WHERE userID=?";
        List<AccountCredentials> list = new ArrayList<>();

        try (Connection conn = ConnectorFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            // Convert each row into an AccountCredentials object
            while (rs.next()) {
                AccountCredentials account = new AccountCredentials();
                account.setAccountId(rs.getInt("accountId"));
                account.setAccountName(rs.getString("accountName"));
                account.setAccountPasswordHash(rs.getString("accountPasswordhash"));
                account.setUserId(rs.getInt("userID"));
                list.add(account);
            }

        } catch (SQLException e) {
            logger.error("DB error while fetching accounts for userId={}", userId, e);
            throw new RuntimeException("DB error while fetching accounts", e);
        }

        return list;
    }

    @Override
    public AccountCredentials getAccountByName(int userId, String accountName) {
        // SQL query to find an account by name for a user
        String sql = "SELECT * FROM Account_Credential WHERE userID=? AND UPPER(accountName)=UPPER(?)";

        try (Connection conn = ConnectorFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, accountName);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                AccountCredentials account = new AccountCredentials();
                account.setAccountId(rs.getInt("accountId"));
                account.setAccountName(rs.getString("accountName"));
                account.setAccountPasswordHash(rs.getString("accountPasswordhash"));
                account.setUserId(rs.getInt("userID"));
                return account;
            }

        } catch (SQLException e) {
            logger.error("DB error while fetching account '{}' for userId={}", accountName, userId, e);
            throw new RuntimeException("DB error while fetching account by name", e);
        }

        return null;
    }

    @Override
    public AccountCredentials getAccountById(int accountId) {
        // SQL query to fetch an account using its ID
        String sql = "SELECT * FROM Account_Credential WHERE accountId=?";

        try (Connection conn = ConnectorFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                AccountCredentials account = new AccountCredentials();
                account.setAccountId(rs.getInt("accountId"));
                account.setAccountName(rs.getString("accountName"));
                account.setAccountPasswordHash(rs.getString("accountPasswordhash"));
                account.setUserId(rs.getInt("userID"));
                return account;
            }

        } catch (SQLException e) {
            logger.error("DB error while fetching account by id={}", accountId, e);
            throw new RuntimeException("DB error while fetching account by id", e);
        }

        return null;
    }
}
