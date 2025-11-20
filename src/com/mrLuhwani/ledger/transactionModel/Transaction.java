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

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setAmount(double amount) {
        this.amt = amount;
    }

    public void setIsIncome(boolean isIncome) {
        this.isIncome = isIncome;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}