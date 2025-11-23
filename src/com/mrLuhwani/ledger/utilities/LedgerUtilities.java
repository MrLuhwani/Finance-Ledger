package com.mrLuhwani.ledger.utilities;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.mrLuhwani.ledger.transactionModel.Transaction;

public class LedgerUtilities {
    static Scanner scanner = new Scanner(System.in);
    static int id;
    static LocalDate date;
    static String type;
    static boolean isIncome;
    static double amount;
    static String category;
    static String description;
    static LocalDate todayDate = LocalDate.now();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    static List<Transaction> transactions = new ArrayList<>();

    protected static void getTransactionDetails() {
        try {
            while (true) {
                try {
                    System.out.print("Enter the date of the transaction (dd/MM/yyyy): ");
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
            System.out.println("Modify the fields as needed: ");
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
                    scanner.nextLine();
                    System.out.println("Invalid input!");
                } catch (Exception e) {
                    System.out.println("Something went wrong");
                }
            }
            scanner.nextLine();
            while (true) {
                System.out.print("Enter a category for your transaction, (e.g salary, utility bill, random expense): ");
                category = scanner.nextLine();
                if (category.trim().isEmpty()) {
                    System.out.println("Please enter a simple category for your transaction! ");
                } else {
                    break;
                }
            }
            while (true) {
                System.out.print("Enter a short description for your transaction: ");
                description = scanner.nextLine();
                if (description.trim().isEmpty()) {
                    System.out.println("Please enter a short description for your transaction! ");
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected static void sortDatesAndIds(){
        transactions.sort(Comparator.comparing(Transaction::getDate).reversed());
            for (Transaction t : transactions) {
                t.setId(id);
                id++;
            }
    }

    protected static void newOrOldT(boolean isNewTransaction, int index) {
        if (isNewTransaction && index == -1) {
            id = 1;
            transactions.add(new Transaction(id, date, amount, isIncome, category, description));
            sortDatesAndIds();
            for (Transaction t : transactions) {
                System.out.println("ID: " + t.getId() + ", Date: " + t.getDate().format(formatter) + ", Type: "
                        + (t.getIsIncome() ? "Income" : "Expense") + ", Amount: " + t.getAmount() + ", Category: "
                        + t.getCategory() + ", Description: " + t.getDescription());
            }
        } else {
            id = 1;
            transactions.set(index, new Transaction(id, date, amount, isIncome, category, description));
            sortDatesAndIds();
        }

    }

    public static void addTransaction() {
        getTransactionDetails();
        newOrOldT(true,-1);
        System.out.println("Successfully added new transaction!");
        /*export to csv once a new transction is added */
    }

    protected static void getCSV() {
        String ledgerPath = "C:\\Users\\tolut\\Desktop\\backend dev\\Projects\\FinanceLedger\\ledger.csv";
        try (BufferedReader bReader = new BufferedReader(new FileReader(ledgerPath))) {
            String line;
            while ((line = bReader.readLine()) != null) {
                System.out.println(line);
                System.out.println("_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Incorrect File Path Error!");
        } catch (IOException e) {
            System.out.println("Something went wrong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void editTransaction() {
        getCSV();
        boolean validId = false;
        int index = 0;
        while (!validId) {
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.print("Enter the id of the transaction you wish to change: ");
                id = scanner.nextInt();
                for (int i = 0; i < transactions.size(); i++) {
                    if (transactions.get(i).getId() == id) {
                        index = i;
                        validId = true;
                        break;
                    }
                }
                if (!validId) {
                    System.out.println("Invalid id!");
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Invalid input!");
            } catch (Exception e) {
                System.out.println("Something went wrong!");
            }
        }
        getTransactionDetails();
        newOrOldT(false,index);
        System.out.println("Successfully edited transaction!");
        /*edit transaction directly into the csv, rather than just changing the objects in our list */
    }

}