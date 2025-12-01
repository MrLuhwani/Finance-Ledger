package com.mrLuhwani.ledger;

import java.util.Scanner;

import com.mrLuhwani.ledger.utilities.LedgerUtilities;

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
                    case "1" -> LedgerUtilities.getCSV();
                    case "2" -> LedgerUtilities.addTransaction();
                    case "3" -> LedgerUtilities.editTransaction();
                    case "4" -> LedgerUtilities.deleteTransaction();
                    case "5" -> LedgerUtilities.monthlySummary();
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
