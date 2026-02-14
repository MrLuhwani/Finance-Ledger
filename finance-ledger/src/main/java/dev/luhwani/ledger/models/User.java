package dev.luhwani.ledger.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User {
    
    private String email;
    private final Long id;
    private String username;
    private String password;
    private List<Transaction> transactions = new ArrayList<>();

    //these two might not be actual object fields, but are here for the time-being as
    //refernces while building

    private String salt;
    private LocalDateTime createdAt;

    public User(String email, Long id, String username, String password) {
        this.email = email;
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Transaction> geTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }
}
