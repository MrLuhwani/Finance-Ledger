package dev.luhwani.ledger.repos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dev.luhwani.ledger.customExceptions.DataAccessException;
import dev.luhwani.ledger.models.Category;
import dev.luhwani.ledger.models.EntryType;
import dev.luhwani.ledger.models.LoginData;
import dev.luhwani.ledger.models.Transaction2;

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

    public List<Transaction2> getUserTransactions(Long userId) {
        String sql = "SELECT id, date, kobo_amt, entry_type, category, description, user_id FROM transactions WHERE user_id = ?;";
        try (
            Connection conn = ConnectionManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setLong(1, userId);
            List<Transaction2> trs = new ArrayList<>();
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Long id = rs.getLong("id");
                    LocalDate date = (LocalDate) rs.getObject("date");
                    Long koboAmt = rs.getLong("kobo_amt");
                    EntryType entryType = EntryType.valueOf(rs.getString("entry_type"));
                    Category category= Category.valueOf(rs.getString("category"));
                    String description = rs.getString("description");
                    Transaction2 tr = new Transaction2(id, date, koboAmt, entryType, category, description, userId);
                    trs.add(tr);
                }
            }
            return trs;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public void setLastLogin(Long userId) {
        String sql = "UPDATE users SET last_login = NOW() WHERE id = ?;";
        try (Connection conn = ConnectionManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setLong(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public Optional<byte[]> getPassword(Long userId) {
        byte[] passwordHash;
        String sql = "SELECT password_hash FROM users WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    passwordHash = rs.getBytes("password_hash");
                    return Optional.of(passwordHash);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Optional<String> getSalt(Long userId) {
        String salt;
        String sql = "SELECT password_salt FROM users WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    salt = rs.getString("password_salt");
                    return Optional.ofNullable(salt);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
        return Optional.empty();
    }

    public void updatePassword(Long userId, byte[] passwordHash, String salt) {
        String sql = "UPDATE users SET password_hash = ?, password_salt = ? WHERE id = ?";
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBytes(1, passwordHash);
            pstmt.setString(2, salt);
            pstmt.setLong(3, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
