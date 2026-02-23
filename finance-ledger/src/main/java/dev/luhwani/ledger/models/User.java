package dev.luhwani.ledger.models;

import java.util.ArrayList;
import java.util.List;

public class User {

    private final String email;
    private final Long id;
    private final String username;
    private List<Transaction2> transactions = new ArrayList<>();

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

    public List<Transaction2> getTransactions() {
        return transactions;
    }

    public void setTransactionList(List<Transaction2> trList) {
        if (!trList.isEmpty()) {
            transactions = trList;
        }
    }

    public void addTransaction(Transaction2 t) {
        transactions.add(t);
    }
}
