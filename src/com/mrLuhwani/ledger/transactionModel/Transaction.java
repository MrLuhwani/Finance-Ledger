package com.mrLuhwani.ledger.transactionModel;

import java.time.LocalDate;

public class Transaction {

    int id;
    LocalDate date;
    double amt;
    boolean isIncome;
    String category;
    String description;

    public Transaction(int id, LocalDate date, double amt, boolean isIncome, String category, String description) {
        this.id = id;
        this.date = date;
        this.amt = amt;
        this.isIncome = isIncome;
        this.category = category;
        this.description = description;
    }

    public int getId() {
        return this.id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public double getAmount() {
        return this.amt;
    }

    public boolean getIsIncome() {
        return this.isIncome;
    }

    public String getCategory() {
        return this.category;
    }

    public String getDescription() {
        return this.description;
    }

    public void setId(int id) {
        this.id = id;
    }
}