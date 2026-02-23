package dev.luhwani.ledger.repos;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionManager {

    private static final String URL = "jdbc:postgresql://localhost:5432/ledger_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "password";

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    /*
     * run this method when you want to create the db automatically without using an
     * sql Editor. So run this once to create your tables, then you can delete the
     * call from App.java
     */
    private String loadSchema() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("schema.sql")) {
            if (inputStream == null) {
                throw new RuntimeException("Schema.sql not found in resources");
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load schema.sql", e);
        }
    }

    public void initDB() {
        String[] statements = loadSchema().split(";");
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement();) {
            for (String sql : statements) {
                if (!sql.trim().isEmpty()) {
                    stmt.execute(sql);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB initialization failed", e);
        }
        System.out.println("DB sucessfullly initialized");
    }
}
