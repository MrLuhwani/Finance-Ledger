package dev.luhwani.ledger.repos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Optional;

import dev.luhwani.ledger.customExceptions.DataAccessException;
import dev.luhwani.ledger.models.Category;
import dev.luhwani.ledger.models.Transaction2;

public class TransactionRepo {

    private final EnumSet<Category> allCategories = EnumSet.allOf(Category.class);

    public EnumSet<Category> allCategories() {
        return allCategories;
    }

    public Optional<Transaction2> returnStoredTransaction(Transaction2 transaction2) {
        String sql = "INSERT INTO transactions(date, kobo_amt, entry_type, category, description, user_id) VALUES (?, ?, ?, ?, ?, ?);";
        LocalDate date = transaction2.date();
        Long koboAmt = transaction2.koboAmt();
        String entryType = String.valueOf(transaction2.entryType());
        String category = String.valueOf(transaction2.category());
        String description = transaction2.description();
        Long userId = transaction2.userId();
        try (
                Connection conn = ConnectionManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setObject(1, date);
            pstmt.setLong(2, koboAmt);
            pstmt.setString(3, entryType);
            pstmt.setString(4, category);
            pstmt.setString(5, description);
            pstmt.setLong(6, userId);
            pstmt.executeUpdate();
            try (ResultSet generatedId = pstmt.getGeneratedKeys();) {
                if (generatedId.next()) {
                    Long id = generatedId.getLong(1);
                    Transaction2 tr = new Transaction2(id, date, koboAmt, transaction2.entryType(),
                            transaction2.category(), description, userId);
                    return Optional.of(tr);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
        return Optional.empty();
    }

    public void editTransaction(Transaction2 tr) {
        String sql = """
                UPDATE transactions
                SET date = ?, kobo_amt = ?, entry_type = ?, category = ?, description = ?, updated_at = NOW() ;""";
        LocalDate date = tr.date();
        Long koboAmt = tr.koboAmt();
        String entry = String.valueOf(tr.entryType());
        String category = String.valueOf(tr.category());
        String description = tr.description();
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, date);
            ps.setLong(2, koboAmt);
            ps.setString(3, entry);
            ps.setString(4, category);
            ps.setString(5, description);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public void removeTransaction(Long id) {
        String sql = "DELETE FROM transactions WHERE id = ?;";
        try (Connection conn = ConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }

    }
}
