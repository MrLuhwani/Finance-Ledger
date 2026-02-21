package dev.luhwani.ledger.repos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.EnumSet;

import dev.luhwani.ledger.customExceptions.DataAccessException;
import dev.luhwani.ledger.models.Category;
import dev.luhwani.ledger.models.Transaction2;

public class TransactionRepo {
    
    private EnumSet<Category> allCategories = EnumSet.allOf(Category.class);

    public EnumSet<Category> allCategories(){
        return allCategories;
    }

    public void storeTransaction(Transaction2 transaction2) {
        String sql = "INSERT INTO transactions(date, koboAmt, entry_type, category, description, user_id) VALUES (?, ?, ?, ?, ?, ?);";
        LocalDate date = transaction2.date();
        Long koboAmt = transaction2.koboAmt();
        String entryType = String.valueOf(transaction2.entryType());
        String category = String.valueOf(transaction2.category());
        String description = transaction2.description();
        Long userId = transaction2.userId();
        try (
                Connection conn = ConnectionManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, date);
            pstmt.setLong(2, koboAmt);
            pstmt.setString(3, entryType);
            pstmt.setString(4, category);
            pstmt.setString(5, description);
            pstmt.setLong(6,userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
