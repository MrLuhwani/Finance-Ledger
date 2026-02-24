package dev.luhwani.ledger.services;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.YearMonth;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import dev.luhwani.ledger.models.Category;
import dev.luhwani.ledger.models.EntryType;
import dev.luhwani.ledger.models.MonthSummary;
import dev.luhwani.ledger.models.Transaction;

public class ExportService {

    private final SummaryCalculator calculator = new SummaryCalculator();

    public void exportMonthlySummaryCsv(List<Transaction> monthTxns, YearMonth ym, Path outFile) throws IOException {
        
        MonthSummary summary = calculator.summarize(monthTxns);

        try (BufferedWriter w = Files.newBufferedWriter(outFile, StandardCharsets.UTF_8)) {
            w.write("date,amount_naira,category,description");
            w.newLine();

            for (Transaction t : monthTxns) {
                String amount = (t.entryType() == EntryType.EXPENSE ? "-" : "")
                        + BigDecimal.valueOf(t.koboAmt()).divide(BigDecimal.valueOf(100)).toPlainString();
                w.write(String.join(",",
                        t.date().toString(),
                        amount,
                        t.category().name(),
                        csvEscape(t.description())));
                w.newLine();
            }

            w.newLine();
            w.write("SUMMARY,,,,");
            w.newLine();
            writeSummaryRow(w, "Total Income", koboToNairaString(summary.totalIncomeKobo()));
            writeSummaryRow(w, "Total Expense", koboToNairaString(summary.totalExpenseKobo()));
            writeSummaryRow(w, "Net Balance", koboToNairaString(summary.netBalanceKobo()));
            if (summary.highestIncomeTxn() != null) {
                writeSummaryRow(w, "Highest Income", koboToNairaString(summary.highestIncomeTxn().koboAmt()));
            } else {
                writeSummaryRow(w, "Highest Income", "0");
            }
            if (summary.highestExpenseTxn() != null) {
                writeSummaryRow(w, "Highest Expense", koboToNairaString(-summary.highestExpenseTxn().koboAmt()));
            } else {
                writeSummaryRow(w, "Highest Expense", "0");
            }
            if (summary.topIncomeCategory() != null) {
                w.write(String.join(",",
                        "Top Income Category",
                        summary.topIncomeCategory().name(),
                        koboToNairaString(summary.topIncomeCategoryTotalKobo()),
                        ""));
                w.newLine();
            } else {
                w.write(String.join(",",
                        "Top Income Category",
                        "None",
                        "0",
                        ""));
                w.newLine();
            }
            if (summary.topExpenseCategory() != null) {
                w.write(String.join(",",
                        "Top Expense Category",
                        summary.topExpenseCategory().name(),
                        koboToNairaString(-summary.topExpenseCategoryTotalKobo()),
                        ""));
                w.newLine();
            } else {
                w.write(String.join(",",
                        "Top Expense Category",
                        "None",
                        "0",
                        ""));
                w.newLine();
            }
        }
    }

    /*
     * Helper to escape CSV fields. Quotes the field if it contains a comma, quote,
     * newline, or carriage return,
     * because those are the characters that can break CSV parsing. Also doubles
     * internal quotes to escape them properly.
     */
    private String csvEscape(String s) {
        if (s == null)
            return "";
        boolean needsQuotes = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        String escaped = s.replace("\"", "\"\"");
        return needsQuotes ? "\"" + escaped + "\"" : escaped;
    }

    private void writeSummaryRow(BufferedWriter w, String label, String value) throws IOException {
        w.write(String.join(",", label, value, "", ""));
        w.newLine();
    }

    private String koboToNairaString(long kobo) {
        BigDecimal naira = BigDecimal.valueOf(kobo).divide(BigDecimal.valueOf(100));
        return naira.toPlainString();
    }
}

class SummaryCalculator {

    public MonthSummary summarize(List<Transaction> txns) {
        long totalIncome = 0;
        long totalExpense = 0;

        Map<Category, Long> incomeByCategory = new HashMap<>();
        Map<Category, Long> expenseByCategory = new HashMap<>();

        Transaction highestIncomeTxn = null;
        Transaction highestExpenseTxn = null;

        for (Transaction t : txns) {
            long amt = t.koboAmt();

            if (t.entryType() == EntryType.INCOME) {
                totalIncome += amt;

                if (highestIncomeTxn == null || amt > highestIncomeTxn.koboAmt()) {
                    highestIncomeTxn = t;
                }
                incomeByCategory.merge(t.category(), amt, Long::sum);

            } else if (t.entryType() == EntryType.EXPENSE) {
                totalExpense += amt; // kept expense as positive total

                if (highestExpenseTxn == null || amt > highestExpenseTxn.koboAmt()) {
                    highestExpenseTxn = t;
                }
                expenseByCategory.merge(t.category(), amt, Long::sum);
            }
        }

        long netBalance = totalIncome - totalExpense;

        // Find top income category
        Category topIncomeCat = null;
        long topIncomeTotal = 0;
        for (var e : incomeByCategory.entrySet()) {
            if (topIncomeCat == null || e.getValue() > topIncomeTotal) {
                topIncomeCat = e.getKey();
                topIncomeTotal = e.getValue();
            }
        }

        // Find top expense category
        Category topExpenseCat = null;
        long topExpenseTotal = 0;
        for (var e : expenseByCategory.entrySet()) {
            if (topExpenseCat == null || e.getValue() > topExpenseTotal) {
                topExpenseCat = e.getKey();
                topExpenseTotal = e.getValue();
            }
        }

        return new MonthSummary(
                totalIncome,
                totalExpense,
                netBalance,
                topIncomeCat,
                topIncomeTotal,
                topExpenseCat,
                topExpenseTotal, highestIncomeTxn, highestExpenseTxn);
    }
}