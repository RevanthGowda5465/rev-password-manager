package com.passwordmanager.dao.implementation;

import java.sql.*;
import java.util.*;

import com.passwordmanager.connection.ConnectorFactory;
import com.passwordmanager.dao.SecurityAnswerDao;
import com.passwordmanager.dto.SecurityAnswer;
import com.passwordmanager.util.LoggerFactory;
import org.apache.logging.log4j.Logger;

public class SecurityAnswerDaoImp implements SecurityAnswerDao {

    // Logger used to record database operations and errors
    private static final Logger logger = LoggerFactory.getLogger(SecurityAnswerDaoImp.class);

    @Override
    public boolean addAnswer(SecurityAnswer answer) {
        // SQL query to insert a new security answer
        String sql = "INSERT INTO Security_Answer(securityAnswerHash,userId,questionId) VALUES(?,?,?)";
        try (Connection conn = ConnectorFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, answer.getSecurityAnswerHash());
            ps.setInt(2, answer.getUserId());
            ps.setInt(3, answer.getQuestionId());

            // Execute insert and check if record was added
            boolean success = ps.executeUpdate() > 0;
            if (success) {
                logger.info("Added security answer for userId={} questionId={}",
                            answer.getUserId(), answer.getQuestionId());
            }
            return success;

        } catch (SQLException e) {
            logger.error("DB error while adding security answer for userId={} questionId={}",
                         answer.getUserId(), answer.getQuestionId(), e);
            throw new RuntimeException("DB error while adding security answer", e);
        }
    }

    @Override
    public boolean updateAnswer(SecurityAnswer answer) {
        // SQL query to update an existing security answer
        String sql = "UPDATE Security_Answer SET securityAnswerHash=? WHERE userId=? AND questionId=?";
        try (Connection conn = ConnectorFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, answer.getSecurityAnswerHash());
            ps.setInt(2, answer.getUserId());
            ps.setInt(3, answer.getQuestionId());

            boolean success = ps.executeUpdate() > 0;
            if (success) {
                logger.info("Updated security answer for userId={} questionId={}",
                            answer.getUserId(), answer.getQuestionId());
            } else {
                logger.warn("Failed to update security answer for userId={} questionId={}",
                            answer.getUserId(), answer.getQuestionId());
            }
            return success;

        } catch (SQLException e) {
            logger.error("DB error while updating security answer for userId={} questionId={}",
                         answer.getUserId(), answer.getQuestionId(), e);
            throw new RuntimeException("DB error while updating security answer", e);
        }
    }

    @Override
    public boolean updateAnswerByUserAndQuestion(int userId, int questionId, String newAnswer) {
        // SQL query to update answer using userId and questionId
        String sql = "UPDATE Security_Answer SET securityAnswerHash=? WHERE userId=? AND questionId=?";
        try (Connection conn = ConnectorFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newAnswer);
            ps.setInt(2, userId);
            ps.setInt(3, questionId);

            boolean success = ps.executeUpdate() > 0;
            if (success) {
                logger.info("Updated security answer by userId={} questionId={}", userId, questionId);
            } else {
                logger.warn("Failed to update security answer by userId={} questionId={}", userId, questionId);
            }
            return success;

        } catch (SQLException e) {
            logger.error("DB error while updating security answer by userId={} questionId={}", userId, questionId, e);
            throw new RuntimeException("DB error while updating answer", e);
        }
    }

    @Override
    public List<SecurityAnswer> getAnswersByUser(int userId) {
        // SQL query to get all answers for a specific user
        String sql = "SELECT * FROM Security_Answer WHERE userId=?";
        List<SecurityAnswer> list = new ArrayList<>();
        try (Connection conn = ConnectorFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            // Convert each row into a SecurityAnswer object
            while (rs.next()) {
                SecurityAnswer ans = new SecurityAnswer();
                ans.setSecurityAnswerHash(rs.getString("securityAnswerHash"));
                ans.setUserId(rs.getInt("userId"));
                ans.setQuestionId(rs.getInt("questionId"));
                list.add(ans);
            }

            logger.info("Fetched {} security answers for userId={}", list.size(), userId);
        } catch (SQLException e) {
            logger.error("DB error while fetching security answers for userId={}", userId, e);
            throw new RuntimeException("DB error while fetching security answers", e);
        }

        return list;
    }

    @Override
    public boolean verifyAnswer(int userId, int questionId, String answerHash) {
        // SQL query to check if the answer matches
        String sql = "SELECT 1 FROM Security_Answer WHERE userId=? AND questionId=? AND securityAnswerHash=?";
        try (Connection conn = ConnectorFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, questionId);
            ps.setString(3, answerHash);

            boolean success = ps.executeQuery().next();
            if (!success) {
                logger.warn("Failed verification for userId={} questionId={}", userId, questionId);
            } else {
                logger.info("Verified security answer for userId={} questionId={}", userId, questionId);
            }

            return success;

        } catch (SQLException e) {
            logger.error("DB error while verifying security answer for userId={} questionId={}", userId, questionId, e);
            throw new RuntimeException("DB error while verifying answer", e);
        }
    }

    @Override
    public boolean deleteAnswer(int userId, int questionId) {
        // SQL query to delete an answer for a user and question
        String sql = "DELETE FROM Security_Answer WHERE userId=? AND questionId=?";
        try (Connection conn = ConnectorFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            boolean success = ps.executeUpdate() > 0;
            if (success) {
                logger.info("Deleted security answer for userId={} questionId={}", userId, questionId);
            } else {
                logger.warn("Attempted to delete non-existing security answer for userId={} questionId={}", userId, questionId);
            }
            return success;

        } catch (SQLException e) {
            logger.error("DB error while deleting security answer for userId={} questionId={}", userId, questionId, e);
            throw new RuntimeException("DB error while deleting answer", e);
        }
    }
}
