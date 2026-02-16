package dev.luhwani.ledger.repos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
     * sql Editor
     * Also note that this will return an exception after running because there was
     * no return value
     * of the query. So run this once to create your tables, then you can delete the
     * call from main
     */
    public static void initDB() throws SQLException, IOException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        String sql = Files.readString(Paths.get("finance-ledger\\src\\main\\resources\\schema.sql"));
        stmt.executeQuery(sql);
        conn.close();
        System.out.println("Connected sucessfullly");
    }
}
