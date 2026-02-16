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

    public boolean findEmail(String email) {
        String sql = "SELECT email FROM users;";
        try (
                Connection conn = ConnectionManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);) {
            String result;
            while (rs.next()) {
                result = rs.getString("email");
                if (result.equals(email)) {
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public Optional<Long> registerUser(LoginData loginData) {
        String sql = "INSERT INTO users (email, salt, username, password_hash) VALUES (?, ?, ?, ?);";
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
}
