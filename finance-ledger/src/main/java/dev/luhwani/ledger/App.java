package dev.luhwani.ledger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import dev.luhwani.ledger.appContext.AppContext;
import dev.luhwani.ledger.customExceptions.UIException;
import dev.luhwani.ledger.models.Category;
import dev.luhwani.ledger.models.EntryType;
import dev.luhwani.ledger.models.LoginData;
import dev.luhwani.ledger.models.Transaction2;
import dev.luhwani.ledger.models.User;
import dev.luhwani.ledger.repos.TransactionRepo;
import dev.luhwani.ledger.repos.UserRepo;
import dev.luhwani.ledger.services.TransactionService;
import dev.luhwani.ledger.services.SecurityService;
import dev.luhwani.ledger.services.UserService;
import dev.luhwani.ledger.services.Utils;
import dev.luhwani.ledger.utilities.CsvUtils;
import dev.luhwani.ledger.utilities.LedgerUtils;

public class App {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        UserRepo userRepo = new UserRepo();
        UserService userService = new UserService(userRepo);
        SecurityService securityService = new SecurityService();
        TransactionRepo transactionRepo = new TransactionRepo();
        TransactionService transactionService = new TransactionService(transactionRepo);
        AppContext context = new AppContext(securityService, userService, transactionService);
        startApp(context);
    }

    private static void startApp(AppContext context) {
        boolean usingSystem = true;
        while (usingSystem) {
            System.out.println("""
                    1.Login
                    2.Create Account
                    3.Exit""");
            System.out.print("Response: ");
            String response = scanner.nextLine().trim();
            switch (response) {
                case "1" -> {
                    Optional<User> user = login(context);
                    if (user.isPresent()) {
                        System.out.println("Welcome " + user.get().getUsername());
                        menu(user.get(), context);
                    }
                }
                case "2" -> {
                    Optional<User> newUser = createAcct(context);
                    if (newUser.isPresent()) {
                        System.out.println("Welcome " + newUser.get().getUsername());
                        menu(newUser.get(), context);
                    }

                }
                case "3" -> {
                    scanner.close();
                    usingSystem = false;
                    System.out.println("Exitting interface...");
                }
                default -> System.out.println("Invalid input");
            }
        }
    }

    private static Optional<User> createAcct(AppContext context) {
        String email;
        while (true) {
            System.out.print("Enter your email: ");
            email = scanner.nextLine();
            if (Utils.validEmail(email)) {
                break;
            }
            System.out.println("Invalid email");
        }
        UserService userService = context.getUserService();
        try {
            Optional<LoginData> userExists = userService.emailFound(email);
            if (userExists.isPresent()) {
                System.out.println("This email is already in use");
                return Optional.empty();
            }
        } catch (UIException e) {
            System.err.println(e.getMessage());
        }
        String password;
        System.out.println("""
                Set new Password:
                Password requirements:
                1.Between 7 - 20 characters long
                2.No space
                3.Contains both letters and numbers
                4.Contains at least one symbol""");
        while (true) {
            System.out.print("Response: ");
            password = scanner.nextLine();
            if (Utils.validPassword(password)) {
                break;
            }
            System.out.println("Invalid Password");
        }
        SecurityService securityService = context.getSecurityService();
        String salt = securityService.saltGenerator();
        String passwordAndSalt = password + salt;
        byte[] passwordHash = securityService.hashText(passwordAndSalt);
        String username;
        System.out.println("""
                Set new Username:
                1.Between 5 - 14 characters long
                2.No space
                3.Can contain numbers
                4.Underscores and hyphens accepted
                5.Can't contain other symbols""");
        while (true) {
            System.out.print("Response: ");
            username = scanner.nextLine();
            if (Utils.validUsername(username)) {
                break;
            }
            System.out.println("Invalid Username");
        }
        LoginData loginData = new LoginData(0L, email, username, passwordHash, salt);
        try {
            Optional<User> newUser = userService.createUser(loginData);
            if (newUser.isPresent()) {
                System.out.println("Account created successfully");
                return newUser;   
            }
            return Optional.empty();
        } catch (UIException e) {
            System.err.println(e.getMessage());
            return Optional.empty();
        }
    }

    private static Optional<User> login(AppContext context) {
        String email;
        while (true) {
            System.out.print("Enter email: ");
            email = scanner.nextLine();
            if (Utils.validEmail(email)) {
                break;
            }
            System.out.println("Invalid email");
        }
        UserService userService = context.getUserService();
        Optional<LoginData> loginData = Optional.empty();
        try {
            loginData = userService.emailFound(email);
            if (!loginData.isPresent()) {
                System.out.println("This email is not registered");
                return Optional.empty();
            }
        } catch (UIException e) {
            System.err.println(e.getMessage());
            return Optional.empty();
        }
        byte[] passwordHash = loginData.get().passwordHash();
        String salt = loginData.get().salt();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        String passwordAndSalt = password + salt;
        SecurityService securityService = context.getSecurityService();
        try {
            byte[] inputHash = securityService.hashText(passwordAndSalt);
            if (!securityService.passwordsMatch(passwordHash, inputHash)) {
                System.out.println("Invalid password");
                return Optional.empty();
            }
            Optional<User> user = userService.login(loginData.get());
            return user;
        } catch (UIException e) {
            System.err.println(e.getMessage());
            return Optional.empty();
        }

    }

    private static void menu(User user, AppContext context) {
        System.out.println("__Finance Ledger__");
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
                    6.Export monthly sommary to CSV
                    7.Change Password
                    8.Delete Account
                    0.Exit
                    Response: """);
            response = scanner.nextLine().trim();
            switch (response) {
                case "1" -> CsvUtils.getCSV();
                case "2" -> addTransaction(context.getTransactionService(), user);
                case "3" -> editTransaction(context.getTransactionService(), user);
                case "4" -> deleteTransaction(context.getTransactionService(), user);
                case "5" -> LedgerUtils.monthlySummary();
                case "6" -> {
                    System.out.println("Expor to CSV");
                }
                case "7" -> changePassword(user, context);
                case "8" -> {
                    deleteAcct(user, context.getUserService());
                    usingSystem = false;
                    System.out.println("Exitting program...");
                }
                case "0" -> {
                    usingSystem = false;
                    System.out.println("Exiting program...");
                }
                default -> System.out.println("Invalid input");
            }
        }
    }

    private static void addTransaction(TransactionService transactionService, User user) {
        Transaction2 transaction2 = getTransactionDetails(user, transactionService, Optional.empty());
        try {
            transactionService.addTransaction(user, transaction2);
            System.out.println("Transaction successfully saved!");
        } catch (UIException | NullPointerException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void editTransaction(TransactionService transactionService, User user) {
        List<Transaction2> transactions = user.getTransactions();
        int choiceInt = chooseTransaction(transactions);
        Transaction2 t2 = transactions.get(choiceInt - 1);
        System.out.println("Edit the required fields: ");
        Transaction2 tr = getTransactionDetails(user, transactionService, Optional.of(t2.id()));
        try {
            transactionService.editTransaction(tr);
            transactions.remove(choiceInt - 1);
            transactions.add(tr);
            System.out.println("Transaction successfully saved!");
        } catch (UIException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void deleteTransaction(TransactionService transactionService, User user) {
        List<Transaction2> transactions = user.getTransactions();
        int choiceInt = chooseTransaction(transactions);
        while (true) {
            System.out.println("""
                Input number to confirm transaction deletion:
                1.Confirm
                2.Exit""");
            System.out.print("Response: ");
            String choice = scanner.nextLine().trim();
            if (choice.equals("1")) {
                break;
            } else if (choice.equals("2")) {
                System.out.println("Operation cancelled...");
                return;
            } else {
                System.out.println("Invalid input!");
            }
        }
        Transaction2 t = transactions.get(choiceInt - 1);
        try{
            transactionService.deleteTransaction(t.id());
            transactions.remove(choiceInt - 1);
            System.out.println("Transaction successfully deleted");
        } catch (UIException e) {
            System.err.println(e.getMessage());
        }

    }

    private static Transaction2 getTransactionDetails(User user, TransactionService transactionService, Optional<Long> transactionId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date;
        while (true) {
            try {
                LocalDate todayDate = LocalDate.now();
                System.out.print("Enter the date of the transaction (yyyy-MM-dd): ");
                String input = scanner.nextLine().trim();
                date = LocalDate.parse(input, formatter);
                if (!date.isAfter(todayDate)) {
                    break;
                }
                System.out.println("Invalid date. Cannot input future dates!");
            } catch (DateTimeParseException e) {
                System.err.println("Invalid date format!");
            }
        }
        EntryType entryType;
        System.out.println("Modify the fields as needed: ");
        while (true) {
            System.out.print("Enter the type of transaction(Income|Expense): ");
            String type = scanner.nextLine().trim().toLowerCase();
            if (type.equals("income")) {
                entryType = EntryType.INCOME;
                break;
            } else if (type.equals("expense")) {
                entryType = EntryType.EXPENSE;
                break;
            } else {
                System.out.println("Invalid input!");
            }
        }
        Long koboAmt;
        while (true) {
            System.out.print("Enter the amount for your transaction: ");
            String amtString = scanner.nextLine().trim();
            try{
                BigDecimal nairaAmt = new BigDecimal(amtString);
                if (nairaAmt.scale() > 2) {
                   System.out.println("Amount must be in two decimal places"); 
                } else if (nairaAmt.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Amount can't be less than or equal to zero");
                } else {
                    koboAmt = nairaAmt.multiply(BigDecimal.valueOf(100)).longValueExact();
                    break;
                }
            } catch (NumberFormatException | ArithmeticException e) {
                System.err.println("Invalid input!");
            }
        }
        Category category;
        EnumSet<Category> categorySet = transactionService.getCategories();
        int count = 0;
        System.out.println("__Categories__");
        for (Category c : categorySet) {
            count++;
            System.out.println(count + "." + c);
        }
        while (true) {
            String categoryChoice;
            System.out.print("Enter the number for the category for your transaction: ");
            categoryChoice = scanner.nextLine().trim();
            if (categoryChoice.isEmpty()) {
                System.out.println("Please enter a valid category for your transaction! ");
                continue;
            }
            List<Category> categoryList = new ArrayList<>(categorySet);
            int choiceInt;
            try {
                choiceInt = Utils.validIntChoice(categoryChoice, categoryList.size());
                category = categoryList.get(choiceInt - 1);
                break;
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
               System.err.println("Invalid input");
            }
        }
        String description;
        //hopefully the frontend can implement a way to show the num of chars
        //as the user is typing
        while (true) {
            System.out.print("Enter a short description for your transaction (not more than 50 chars): ");
            description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                System.out.println("Please enter a short description for your transaction! ");
            } else if (description.length() > 50){
                System.out.println("Description has exceeded character limit");
            } else {
                break;
            }
        }
        if (transactionId.isPresent()) {
            return new Transaction2(transactionId.get(), date, koboAmt, entryType, category, description, user.getId());
        }
        return new Transaction2(0L, date, koboAmt, entryType, category, description, user.getId());
    }

    private static int chooseTransaction(List<Transaction2> transactionList) {
        if (transactionList.isEmpty()) {
            System.out.println("No transactions have been added yet");
        } else {
            System.out.println("id, Date, Amount, Category, Description");
            int count = 1;
            for (Transaction2 tr : transactionList) {
                System.out.println(count + ", " + tr.toString());
            }
        }
        int choiceInt;
        while (true) {
            System.out.print("Enter the id for the transaction: ");
            String choice = scanner.nextLine().trim();
            try {
                choiceInt = Utils.validIntChoice(choice, transactionList.size());
                break;
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                System.err.println("Invalid input");
            }
        }
        return choiceInt;
    }

    private static void changePassword(User user, AppContext context) {
        Optional<byte[]> oldPasswordHash;
        Optional<String> salt;
        UserService userService = context.getUserService();
        SecurityService securityService = context.getSecurityService();
        try {
            oldPasswordHash = userService.getPassword(user.getId());
            salt = userService.getSalt(user.getId());
            if (oldPasswordHash.isEmpty() || salt.isEmpty()) {
                System.out.println("Error while getting password");
                return;
            }
            while (true) {
                System.out.print("Enter old password: ");
                String oldPassword = scanner.nextLine();
                String oldPasswordAndSalt = oldPassword + salt.get();
                byte[] oldPasswordHashInput = securityService.hashText(oldPasswordAndSalt);
                if (securityService.passwordsMatch(oldPasswordHash.get(), oldPasswordHashInput)) {
                    break;
                }
                System.out.println("Invalid password");
            }
        } catch (UIException e) {
            System.err.println(e.getMessage());
        }
        String newPassword;
        System.out.println("""
                Set new Password:
                Password requirements:
                1.Between 7 - 20 characters long
                2.No space
                3.Contains both letters and numbers
                4.Contains at least one symbol""");
        while (true) {
            System.out.print("Response: ");
            newPassword = scanner.nextLine();
            if (Utils.validPassword(newPassword)) {
                break;
            }
            System.out.println("Invalid Password");
        }
        String newSalt = securityService.saltGenerator();
        String newPasswordAndSalt = newPassword + newSalt;
        try {
            byte[] newPasswordHash = securityService.hashText(newPasswordAndSalt);
            userService.updatePassword(user.getId(), newPasswordHash, newSalt);
            System.out.println("Password updated successfully");
        } catch (UIException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void deleteAcct(User user, UserService userService) {
        while (true) {
            System.out.println("""
                    Are you sure you wish to delete your account?!
                    Enter the number for your choice:
                    1.Confirm
                    2.Cancel""");
            System.out.print("Response: ");
            String choice = scanner.nextLine().trim();
            if (choice.equals("1")) {
                break;
            } else if (choice.equals("2")) {
                System.out.println("Operation cancelled...");
                return;
            } else {
                System.out.println("Invalid input!");
            }
        }
        userService.deleteUser(user.getId());
    }
}
