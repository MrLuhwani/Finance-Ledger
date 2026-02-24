package dev.luhwani.ledger.models;

public record MonthSummary(
        long totalIncomeKobo,
        long totalExpenseKobo,
        long netBalanceKobo,

        Category topIncomeCategory,
        long topIncomeCategoryTotalKobo,

        Category topExpenseCategory,
        long topExpenseCategoryTotalKobo,

        Transaction highestIncomeTxn,
        Transaction highestExpenseTxn) {
}
