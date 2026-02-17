package dev.luhwani.ledger.repos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import dev.luhwani.ledger.customExceptions.DataAccessException;
import dev.luhwani.ledger.models.LoginData;

public class UserRepo {

    public Optional<LoginData> findEmail(String email) {
        String sql = "SELECT id, email, username, password_hash, password_salt FROM users WHERE email = ?;";
        try (
                Connection conn = ConnectionManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery();) {
                if (rs.next()) {
                    Long id = rs.getLong("id");
                    String emailResult = rs.getString("email");
                    String usernameResult = rs.getString("username");
                    byte[] passwordHash = rs.getBytes("password_hash");
                    String salt = rs.getString("password_salt");
                    return Optional.of(new LoginData(id, emailResult, usernameResult, passwordHash, salt));
                }
                return Optional.empty();
            } 
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public Optional<Long> registerUser(LoginData loginData) {
        String sql = "INSERT INTO users (email, password_salt, username, password_hash, last_login) VALUES (?, ?, ?, ?, NOW());";
        String email = loginData.email();
        String salt = loginData.salt();
        String username = loginData.username();
        byte[] passwordHash = loginData.passwordHash();
        try (
                Connection conn = ConnectionManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            pstmt.setString(1, email);
            pstmt.setString(2, salt);
            pstmt.setString(3, username);
            pstmt.setBytes(4, passwordHash);
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong(1);
                    return Optional.of(id);
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public void setLastLogin(Long userId) {
        String sql = "UPDATE users SET last_login = NOW() WHERE id = ?;";
        try(Connection conn = ConnectionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setLong(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
