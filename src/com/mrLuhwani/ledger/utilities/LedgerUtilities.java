package com.mrLuhwani.ledger.utilities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.mrLuhwani.ledger.transactionModel.Transaction;

public class LedgerUtilities {
    static LocalDate todayDate = LocalDate.now();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    static ArrayList<Transaction> transactions = new ArrayList<>();

    public static void addTransaction() {
        try (Scanner scanner = new Scanner(System.in)) {
            LocalDate date;
            while (true) {
                try {
                    System.out.print("Enter the date of the transaction (dd-MM-yyyy): ");
                    String input = scanner.nextLine();
                    date = LocalDate.parse(input, formatter);
                    if (date.isAfter(todayDate)) {
                        System.out.println("Invalid date. Cannot input future dates!");
                    } else {
                        break;
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format!");
                } catch (Exception e) {
                    System.out.println("Something went wrong!");
                }
            }
            String type;
            boolean isIncome;
            while (true) {
                System.out.print("Enter the type of transaction(Income|Expense): ");
                type = scanner.nextLine().toLowerCase();
                if (type.equals("income")) {
                    isIncome = true;
                    break;
                } else if (type.equals("expense")) {
                    isIncome = false;
                    break;
                } else {
                    System.err.println("Invalid input!");
                }
            }
            double amount;
            while (true) {
                try {
                    System.out.print("Enter the amount for your transaction: ");
                    amount = scanner.nextDouble();
                    if (amount < 0) {
                        System.out.println("Amount can't be less than zero");
                    } else {
                        break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input!");
                } catch (Exception e) {
                    System.out.println("Something went wrong");
                }
            }
            scanner.nextLine();
            String category;
            while (true) {
                System.out.print("Enter a category for your transaction, (e.g salary, utility bill, random expense): ");
                category = scanner.nextLine();
                if (category.trim().isEmpty()) {
                    System.out.println("Please enter a simple category for your transaction! ");
                } else {
                    break;
                }
            }
            String description;
            while (true) {
                System.out.print("Enter a short description for your transaction: ");
                description = scanner.nextLine();
                if (description.trim().isEmpty()) {
                    System.out.println("Please enter a short description for your transaction! ");
                } else {
                    break;
                }
            }
            int id;
            if (transactions.isEmpty()) {
                id = 1;
            } else {
                id = transactions.get(transactions.size() - 1).getId() + 1;
            }
            transactions.add(new Transaction(id, date, amount, isIncome, category, description));
            System.out.println("Successfully added new transaction!");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void editTransaction() {
        try(Scanner scanner = new Scanner(System.in)){
            
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
