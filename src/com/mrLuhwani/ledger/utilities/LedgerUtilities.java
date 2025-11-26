package com.mrLuhwani.ledger.utilities;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
    static String ledgerPath = "C:\\Users\\tolut\\Desktop\\backend dev\\Projects\\FinanceLedger\\ledger.csv";
    static Transaction t;

    static {
        try (BufferedReader bReader = new BufferedReader(new FileReader(ledgerPath))) {
            String[] column;
            String line;
            String header = "ID,Date,Amount,Category,Description";
            while ((line = bReader.readLine()) != null) {
                if (!line.equals(header)) {
                    column = line.split(",");
                    id = Integer.parseInt(column[0]);
                    date = LocalDate.parse(column[1], formatter);
                    amount = Math.abs(Double.parseDouble(column[2]));

                    if (column[2].startsWith("-")) {
                        isIncome = false;
                    } else {
                        isIncome = true;
                    }
                    transactions.add(new Transaction(id, date, amount, isIncome, column[3], column[4]));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Invalid file path");
        } catch (IOException e) {
            System.out.println("Something went wrong");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
                    e.printStackTrace();
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
                    System.out.println("Invalid input!");
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
                    e.printStackTrace();
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

    protected static void sortDatesAndIds() {
        transactions.sort(Comparator.comparing(Transaction::getDate).reversed());
        for (Transaction tr : transactions) {
            tr.setId(id);
            id++;
        }
    }

    protected static void newOrOldT(boolean isNewTransaction, int index) {
        if (isNewTransaction && index == -1) {
            id = 1;
            t = new Transaction(id, date, amount, isIncome, category, description);
            transactions.add(t);
            sortDatesAndIds();
        } else {
            id = 1;
            t = new Transaction(id, date, amount, isIncome, category, description);
            transactions.set(index, t);
            sortDatesAndIds();
        }

    }

    protected static void csvEditor(Transaction t, boolean isNew, boolean del) {
        String targetId = String.valueOf(t.getId());
        String amt;
        String id;
        String date;
        ArrayList<String> row = new ArrayList<>();
        if (!del) {
            if (t.getIsIncome() == false) {
                amt = "-" + String.valueOf(t.getAmount());
            } else {
                amt = String.valueOf(t.getAmount());
            }
            id = String.valueOf(t.getId());
            date = t.getDate().format(formatter);
            row.add(id);
            row.add(date);
            row.add(amt);
            row.add(t.getCategory());
            row.add(t.getDescription());
        }
        if (isNew) {
            try (BufferedWriter bWriter = new BufferedWriter(new FileWriter(ledgerPath, true))) {
                bWriter.append("\n");
                bWriter.append(String.join(",", row));
            } catch (FileNotFoundException e) {
                System.out.println("Invalid file path");
            } catch (IOException e) {
                System.out.println("Something went wrong!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ArrayList<String> updatedLines = new ArrayList<>();
            String[] columns;
            String ln;
            try (BufferedReader bReader = new BufferedReader(new FileReader(ledgerPath))) {
                if (del) {
                    while ((ln = bReader.readLine()) != null) {
                        columns = ln.split(",");
                        if (columns[0] != targetId) {
                            updatedLines.add(ln);
                        }
                    }
                } else {
                    while ((ln = bReader.readLine()) != null) {
                        columns = ln.split(",");
                        if (columns[0] == targetId) {
                            updatedLines.add(String.join(",", row));
                        } else {
                            updatedLines.add(ln);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("Invalid file path");
            } catch (IOException e) {
                System.out.println("Something went wrong");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try (BufferedWriter bWriter = new BufferedWriter(new FileWriter(ledgerPath))) {
                for (String st : updatedLines) {
                    bWriter.append(st);
                    bWriter.append("\n");
                }
            } catch (FileNotFoundException e) {
                System.out.println("Invalid file path");
            } catch (IOException e) {
                System.out.println("Something went wrong");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void addTransaction() {
        getTransactionDetails();
        newOrOldT(true, -1);
        csvEditor(t, true, false);
        System.out.println("Successfully added new transaction!");
    }

    protected static int getCSV() {
        String line;
        int rows = 0;
        try (BufferedReader bReader = new BufferedReader(new FileReader(ledgerPath))) {
            while ((line = bReader.readLine()) != null) {
                System.out.println(line);
                rows += 1;
                System.out.println("_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Incorrect File Path Error!");
        } catch (IOException e) {
            System.out.println("Something went wrong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }

    public static void editTransaction() {
        int rows = getCSV();
        if (rows == 1) {
            System.out.println("No transactions in CSV");
        } else {
            boolean validId = false;
            int index = 0;
            while (!validId) {
                try {
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
                scanner.nextLine();
            }
            getTransactionDetails();
            newOrOldT(false, index);
            csvEditor(t, false, false);
            System.out.println("Successfully edited transaction!");
        }
    }

    public static void deleteTransaction() {
        int rows = getCSV();
        if (rows == 1) {
            System.out.println("No transactions in CSV");
        } else {
            boolean validId = false;
            int index = 0;
            while (!validId) {
                try {
                    System.out.print("Enter the id of the transaction you wish to delete: ");
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
                scanner.nextLine();
            }
            t = transactions.remove(index);
            sortDatesAndIds();
            csvEditor(t, false, true);
            System.out.println("Transaction successfully deleted");
        }
    }
}