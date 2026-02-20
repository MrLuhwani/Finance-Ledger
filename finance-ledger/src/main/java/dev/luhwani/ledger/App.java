package dev.luhwani.ledger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Scanner;

import dev.luhwani.ledger.appContext.AppContext;
import dev.luhwani.ledger.customExceptions.UIException;
import dev.luhwani.ledger.models.Category;
import dev.luhwani.ledger.models.EntryType;
import dev.luhwani.ledger.models.LoginData;
import dev.luhwani.ledger.models.User;
import dev.luhwani.ledger.repos.LedgerRepo;
import dev.luhwani.ledger.repos.UserRepo;
import dev.luhwani.ledger.services.LedgerService;
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
        LedgerRepo ledgerRepo = new LedgerRepo();
        LedgerService ledgerService = new LedgerService(ledgerRepo);
        AppContext context = new AppContext(securityService, userService, ledgerService);
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
                3.Contains letters
                4.Can contain numbers
                5.Underscores and hyphens accepted""");
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
            System.out.println("Account created successfully");
            return newUser;
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
                    6.Change Password
                    0.Exit
                    Response: """);
            response = scanner.nextLine().trim();
            switch (response) {
                case "1" -> CsvUtils.getCSV();
                case "2" -> LedgerUtils.addTransaction();
                case "3" -> LedgerUtils.editTransaction();
                case "4" -> LedgerUtils.deleteTransaction();
                case "5" -> LedgerUtils.monthlySummary();
                case "6" -> changePassword(user, context);
                case "0" -> {
                    usingSystem = false;
                    System.out.println("Exiting program...");
                }
                default -> System.out.println("Invalid input");
            }
        }
    }

    private static void addTransaction(AppContext context, User user) {
        
    }

    private static void getTransactionDetails(User user, AppContext context) {
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
                System.out.println("Invalid date format!");
            }
        }
        EntryType entryType;
        System.out.println("Modify the fields as needed: ");
        while (true) {
            System.out.print("Enter the type of transaction(Income|Expense): ");
            String type = scanner.nextLine().toLowerCase();
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
                BigDecimal nairaAmt = new BigDecimal(amtString).setScale(2, RoundingMode.HALF_UP);
                if (nairaAmt.scale() > 2) {
                   System.out.println("Amount must be in two decimal places"); 
                } else if (nairaAmt.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("Amount can't be less than zero");
                } else {
                    koboAmt = nairaAmt.multiply(BigDecimal.valueOf(100)).longValueExact();
                    break;
                }
            } catch (NumberFormatException | ArithmeticException e) {
                System.out.println("Invalid input!");
            }
        }
        EnumSet<Category> categories = context.getLedgerService().getCategories();
        int count = 0;
        System.out.println("__Categories__");
        for (Category category : categories) {
            count++;
            System.out.println(count + "." + category);
        }
        String categoryChoice;
        while (true) {
            System.out.print("Enter a category for your transaction: ");
            categoryChoice = scanner.nextLine().trim().toUpperCase();
            if (categoryChoice.isEmpty()) {
                System.out.println("Please enter a category for your transaction! ");
            } 
            // else if(){
            //     break;
            // }
        }
    }

    private static void changePassword(User user, AppContext context) {
        Optional<byte[]> oldPasswordHash;
        Optional<String> salt;
        UserService userService = context.getUserService();
        SecurityService securityService = context.getSecurityService();
        try {
            oldPasswordHash = userService.getPassword(user.getId());
            salt = userService.getSalt(user.getId());
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
}
