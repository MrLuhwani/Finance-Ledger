package dev.luhwani.ledger.utilities;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

import dev.luhwani.ledger.models.Transaction;

public class LedgerUtils {

    private static Scanner scanner = new Scanner(System.in);
    static List<Transaction> transactions = new ArrayList<>();

    public static void addTransaction() {
        Transaction t = getTransactionDetails();
        newOrOldT(t, true, -1);
        CsvUtils.csvWriter();
        System.out.println("Successfully added new transaction!");
    }

    public static void editTransaction() {
        String ledgerPath = "C:\\Users\\tolut\\Desktop\\backend dev\\Projects\\FinanceLedger\\ledger.csv";
        int rows = 0;
        try (BufferedReader bReader = new BufferedReader(new FileReader(ledgerPath))) {
            while ((bReader.readLine()) != null) {
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
            Transaction t = getTransactionDetails();
            newOrOldT(t, false, index);
            CsvUtils.csvWriter();
            System.out.println("Successfully edited transaction!");
        }
    }

    public static void deleteTransaction() {
        String ledgerPath = "C:\\Users\\tolut\\Desktop\\backend dev\\Projects\\FinanceLedger\\ledger.csv";
        int rows = 0;
        try (BufferedReader bReader = new BufferedReader(new FileReader(ledgerPath))) {
            while ((bReader.readLine()) != null) {
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
            CsvUtils.csvWriter();
            System.out.println("Deletion Successfully!");
        }
    }

    public static Transaction getTransactionDetails() {
        LocalDate todayDate = LocalDate.now();
        LocalDate date;
        boolean isIncome;
        double amount;
        String type;
        String category;
        String description;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
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
                if (description.trim().isEmpty() ) {
                    System.out.println("Please enter a short description for your transaction! ");
                } else {
                    break;
                }
            }
            return new Transaction(0, date, amount, isIncome, category, description);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void newOrOldT(Transaction t, boolean isNewTransaction, int index) {
        if (isNewTransaction && index == -1) {
            transactions.add(t);
        } else {
            transactions.set(index, t);
        }
        sortDatesAndIds();
    }

    private static void sortDatesAndIds() {
        transactions.sort(Comparator.comparing(Transaction::getDate).reversed());
        int id = 1;
        for (Transaction tr : transactions) {
            tr.setId(id);
            id++;
        }
    }

    public static void monthlySummary() {
        String ledgerPath = "C:\\Users\\tolut\\Desktop\\backend dev\\Projects\\FinanceLedger\\ledger.csv";
        int rows = 0;
        try (BufferedReader bReader = new BufferedReader(new FileReader(ledgerPath))) {
            while ((bReader.readLine()) != null) {
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
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            Double amount;
            Boolean isIncome;
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