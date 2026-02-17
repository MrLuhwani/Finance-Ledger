package dev.luhwani.ledger.models;

import java.util.ArrayList;
import java.util.List;

public class User {

    private final String email;
    private final Long id;
    private String username;
    private List<Transaction> transactions = new ArrayList<>();

    public User(Long id, String email, String username) {
        this.email = email;
        this.id = id;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }
}
