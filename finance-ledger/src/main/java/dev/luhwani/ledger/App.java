package dev.luhwani.ledger;

import java.util.Optional;
import java.util.Scanner;

import dev.luhwani.ledger.appContext.AppContext;
import dev.luhwani.ledger.customExceptions.UIException;
import dev.luhwani.ledger.models.LoginData;
import dev.luhwani.ledger.models.User;
import dev.luhwani.ledger.repos.UserRepo;
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
        AppContext context = new AppContext(securityService, userService);
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
            String response = scanner.nextLine();
            switch (response) {
                case "1" -> {
                    // run the login method
                }
                case "2" -> {
                    Optional<User> newUser = createAcct(context);
                    if (newUser.isPresent()) {
                        User user = newUser.get();
                        System.out.println("Welcome " + user.getUsername());
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

    public static Optional<User> createAcct(AppContext context) {
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
        boolean userExists;
        try {
            userExists = userService.emailFound(email);
            if (userExists) {
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

    public static void menu() {
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
    }
}
