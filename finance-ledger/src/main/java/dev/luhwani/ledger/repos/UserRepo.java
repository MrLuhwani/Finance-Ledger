package dev.luhwani.ledger.repos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dev.luhwani.ledger.customExceptions.DataAccessException;

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
}
