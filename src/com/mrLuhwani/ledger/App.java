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
                        1.Add transaction
                        2.Edit transaction
                        3.Delete transaction
                        4.Show monthly summary
                        5.Export to CSV
                        0.Exit
                        Response: """);
                response = scanner.next();
                switch (response) {
                    case "1":
                        LedgerUtilities.addTransaction();
                        break;
                    case "2":
                        System.out.println("editing transaction");
                        break;
                    case "3":
                        System.out.println("deleting transaction");
                        break;
                    case "4":
                        System.out.println("monthly transaction");
                        break;
                    case "5":
                        System.out.println("exporting transaction");
                        break;
                    case "0":
                        System.out.println("Exiting program...");
                        usingSystem = false;
                        break;
                    default:
                        System.out.println("Invalid input");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
