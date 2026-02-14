package dev.luhwani.ledger;

import java.util.Scanner;

import dev.luhwani.ledger.utilities.CsvUtils;
import dev.luhwani.ledger.utilities.LedgerUtils;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("__Finance Ledger__");
        try (Scanner scanner = new Scanner(System.in)) {
            boolean usingSystem = true;
            while (usingSystem) {
                String response;
                System.out.print("""
                        Enter the number for what you want to choose
                        1.Show Transaction History
                        2.Add transaction
                        3.Edit transaction
                        4.Delete transaction
                        5.Show monthly summary
                        0.Exit
                        Response: """);
                response = scanner.next();
                switch (response) {
                    case "1" -> CsvUtils.getCSV();
                    case "2" -> LedgerUtils.addTransaction();
                    case "3" -> LedgerUtils.editTransaction();
                    case "4" -> LedgerUtils.deleteTransaction();
                    case "5" -> LedgerUtils.monthlySummary();
                    case "0" -> {
                        usingSystem = false;
                        System.out.println("Exiting program...");
                    }
                    default -> System.out.println("Invalid input");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
