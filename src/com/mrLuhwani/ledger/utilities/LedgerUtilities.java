package com.mrLuhwani.ledger.utilities;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.mrLuhwani.ledger.transactionModel.Transaction;

public class LedgerUtilities {
    static Scanner scanner = new Scanner(System.in);
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
            String header = "ID,Date,Amount(₦),Category,Description";
            while ((line = bReader.readLine()) != null) {
                if (!line.equals(header)) {
                    column = line.split(",");
                    amount = Math.abs(Double.parseDouble(column[2]));
                    if (column[2].startsWith("-")) {
                        isIncome = false;
                    } else {
                        isIncome = true;
                    }
                    transactions.add(new Transaction(Integer.parseInt(column[0]), LocalDate.parse(column[1], formatter),
                            amount, isIncome, column[3], column[4]));
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

    private static void getTransactionDetails() {
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

    private static void sortDatesAndIds() {
        transactions.sort(Comparator.comparing(Transaction::getDate).reversed());
        int id = 1;
        for (Transaction tr : transactions) {
            tr.setId(id);
            id++;
        }
    }

    private static void newOrOldT(boolean isNewTransaction, int index) {
        if (isNewTransaction && index == -1) {
            t = new Transaction(0, date, amount, isIncome, category, description);
            transactions.add(t);
            sortDatesAndIds();
        } else {
            t = new Transaction(0, date, amount, isIncome, category, description);
            transactions.set(index, t);
            sortDatesAndIds();
        }

    }

    private static void csvEditor() {
        String strId;
        try (BufferedWriter bWriter = new BufferedWriter(new FileWriter(ledgerPath))) {
            String header = "ID,Date,Amount(₦),Category,Description";
            String strDate;
            String strAmt;
            bWriter.write(header);
            bWriter.write("\n");
            for (Transaction tr : transactions) {
                strId = String.valueOf(tr.getId());
                strDate = tr.getDate().format(formatter);
                strAmt = String.valueOf(tr.getAmount());
                if (!tr.getIsIncome()) {
                    strAmt = "-" + strAmt;
                }
                String[] row = { strId, strDate, strAmt, tr.getCategory(), tr.getDescription() };
                bWriter.write(String.join(",", row));
                bWriter.write("\n");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Invalid file path");
        } catch (IOException e) {
            System.out.println("Something went wrong");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void addTransaction() {
        getTransactionDetails();
        newOrOldT(true, -1);
        csvEditor();
        System.out.println("Successfully added new transaction!");
    }

    public static int getCSV() {
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
        int rows = 0;
        try (BufferedReader bReader = new BufferedReader(new FileReader(ledgerPath))) {
            String ln;
            while ((ln = bReader.readLine()) != null) {
                rows++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Invalid file path");
        } catch (IOException e) {
            System.out.println("Something went wrong");
        } catch (Exception e) {
            e.printStackTrace();
        }
        int id;
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
                    e.printStackTrace();
                }
            }
            scanner.nextLine();
            getTransactionDetails();
            newOrOldT(false, index);
            csvEditor();
            System.out.println("Successfully edited transaction!");
        }
    }

    public static void deleteTransaction() {
        int rows = 0;
        try (BufferedReader bReader = new BufferedReader(new FileReader(ledgerPath))) {
            String ln;
            while ((ln = bReader.readLine()) != null) {
                rows++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Invalid file path");
        } catch (IOException e) {
            System.out.println("Something went wrong");
        } catch (Exception e) {
            e.printStackTrace();
        }
        int id;
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
                    e.printStackTrace();
                }
            }
            transactions.remove(index);
            sortDatesAndIds();
            csvEditor();
            System.out.println("Deletion Successfully!");
        }
    }

    public static void monthlySummary() {
        int rows = 0;
        try (BufferedReader bReader = new BufferedReader(new FileReader(ledgerPath))) {
            String ln;
            while ((ln = bReader.readLine()) != null) {
                rows++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Invalid file path");
        } catch (IOException e) {
            System.out.println("Something went wrong");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (rows == 1) {
            System.out.println("No transactions in CSV");
        } else {
            int currentYear = LocalDate.now().getYear();
            int monthNum;
            while (true) {
                try {
                    System.out.print("Enter the number of the monthNum you wish to check (1-12): ");
                    monthNum = scanner.nextInt();
                    if (1 <= monthNum && monthNum <= 12) {
                        break;
                    } else {
                        System.out.println("Invalid month input!");
                    }
                } catch (InputMismatchException e) {
                    scanner.nextLine();
                    System.out.println("Invalid input!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            scanner.nextLine();
            String[] column;
            ArrayList<String> monthRecords = new ArrayList<>();
            ArrayList<Transaction> recordObjects = new ArrayList<>();
            String ln;
            try (BufferedReader bReader = new BufferedReader(new FileReader(ledgerPath))) {
                bReader.readLine();
                while ((ln = bReader.readLine()) != null) {
                    column = ln.split(",");
                    LocalDate date = LocalDate.parse(column[1], formatter);
                    if (date.getMonthValue() == monthNum && date.getYear() == currentYear) {
                        monthRecords.add(ln);
                        amount = Math.abs(Double.parseDouble(column[2]));
                        if (column[2].startsWith("-")) {
                            isIncome = false;
                        } else {
                            isIncome = true;
                        }
                        recordObjects.add(new Transaction(Integer.parseInt(column[0]),
                                LocalDate.parse(column[1], formatter), amount, isIncome, column[3], column[4]));
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("Invalid file path");
            } catch (IOException e) {
                System.out.println("Something went wrong");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (monthRecords.isEmpty()) {
                System.out.println("No records for this month!");
            } else {
                String monthName = Month.of(monthNum).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                System.out.println("Summary for the Month of " + monthName + ": ");
                System.out.println("ID,Date,Amount(₦),Category,Description");
                for (String str : monthRecords) {
                    System.out.println(str);
                }
                double expense = 0;
                double income = 0;
                for (Transaction tr : recordObjects) {
                    if (tr.getIsIncome() == true) {
                        income += tr.getAmount();
                    } else {
                        expense += tr.getAmount();
                    }
                }
                System.out.println("Total income: " + income);
                System.out.println("Total Expense: " + expense);
                double net = income - expense;
                System.out.println("Net Profit: " + net);
                System.out.println("Summary Complete");
            }
        }
    }
}