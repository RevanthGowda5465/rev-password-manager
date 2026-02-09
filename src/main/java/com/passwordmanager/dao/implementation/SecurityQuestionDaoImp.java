package com.passwordmanager.dao.implementation;

import java.sql.*;
import java.util.*;

import com.passwordmanager.connection.ConnectorFactory;
import com.passwordmanager.dao.SecurityQuestionDao;
import com.passwordmanager.dto.SecurityQuestion;
import com.passwordmanager.util.LoggerFactory;
import org.apache.logging.log4j.Logger;

public class SecurityQuestionDaoImp implements SecurityQuestionDao {

    // Logger used to record actions and database errors
    private static final Logger logger = LoggerFactory.getLogger(SecurityQuestionDaoImp.class);

    @Override
    public int addQuestion(SecurityQuestion question) {
        // SQL query to insert a new security question
        String sql = "INSERT INTO Security_Question(questionName) VALUES (?)";

        try (Connection conn = ConnectorFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"questionId"})) {

            ps.setString(1, question.getQuestionName());
            ps.executeUpdate();

            // Get the auto-generated question ID from the database
            ResultSet rs = ps.getGeneratedKeys();
            int questionId = rs.next() ? rs.getInt(1) : -1;

            if (questionId != -1) {
                logger.info("Added security question with id={} name={}", questionId, question.getQuestionName());
            } else {
                logger.warn("Failed to retrieve generated questionId for name={}", question.getQuestionName());
            }

            return questionId;

        } catch (SQLException e) {
            logger.error("DB error while adding security question name={}", question.getQuestionName(), e);
            throw new RuntimeException("DB error while adding security question", e);
        }
    }

    @Override
    public List<SecurityQuestion> getAllQuestions() {
        // SQL query to fetch a limited list of questions
        String sql = "SELECT * FROM Security_Question ORDER BY questionId FETCH FIRST 12 ROWS ONLY";
        List<SecurityQuestion> list = new ArrayList<>();

        try (Connection conn = ConnectorFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Convert each row into a SecurityQuestion object
            while (rs.next()) {
                SecurityQuestion q = new SecurityQuestion();
                q.setQuestionId(rs.getInt("questionId"));
                q.setQuestionName(rs.getString("questionName"));
                list.add(q);
            }

            logger.info("Fetched {} security questions", list.size());

        } catch (SQLException e) {
            logger.error("DB error while fetching all security questions", e);
            throw new RuntimeException("DB error while fetching questions", e);
        }

        return list;
    }

    @Override
    public SecurityQuestion getQuestionById(int questionId) {
        // SQL query to fetch a question using its ID
        String sql = "SELECT * FROM Security_Question WHERE questionId=?";

        try (Connection conn = ConnectorFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, questionId);
            ResultSet rs = ps.executeQuery();

            // If a record is found, build and return the object
            if (rs.next()) {
                SecurityQuestion q = new SecurityQuestion();
                q.setQuestionId(rs.getInt("questionId"));
                q.setQuestionName(rs.getString("questionName"));
                logger.info("Fetched security question id={} name={}", questionId, q.getQuestionName());
                return q;
            } else {
                logger.warn("No security question found with id={}", questionId);
            }

        } catch (SQLException e) {
            logger.error("DB error while fetching security question id={}", questionId, e);
            throw new RuntimeException("DB error while fetching question", e);
        }

        return null;
    }

    @Override
    public boolean deleteQuestion(int questionId) {
        // SQL query to remove a question from the table
        String sql = "DELETE FROM Security_Question WHERE questionId=?";

        try (Connection conn = ConnectorFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, questionId);
            boolean success = ps.executeUpdate() > 0;

            if (success) {
                logger.info("Deleted security question id={}", questionId);
            } else {
                logger.warn("Attempted to delete non-existing security question id={}", questionId);
            }

            return success;

        } catch (SQLException e) {
            logger.error("DB error while deleting security question id={}", questionId, e);
            throw new RuntimeException("DB error while deleting question", e);
        }
    }

    @Override
    public List<SecurityQuestion> getQuestionsByUserId(int userId) {
        // SQL query to get all questions selected by a user
        String sql = "SELECT q.questionId,q.questionName FROM Security_Question q JOIN Security_Answer a ON q.questionId=a.questionId WHERE a.userId=?";
        List<SecurityQuestion> list = new ArrayList<>();

        try (Connection conn = ConnectorFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            // Convert result rows into objects
            while (rs.next()) {
                SecurityQuestion q = new SecurityQuestion();
                q.setQuestionId(rs.getInt("questionId"));
                q.setQuestionName(rs.getString("questionName"));
                list.add(q);
            }

            logger.info("Fetched {} security questions for userId={}", list.size(), userId);

        } catch (SQLException e) {
            logger.error("DB error while fetching security questions for userId={}", userId, e);
            throw new RuntimeException("DB error while fetching user questions", e);
        }

        return list;
    }
}
