package dev.luhwani.ledger.services;

import java.util.regex.Pattern;

public final class Utils {

    private Utils() {}
    
    public static boolean validEmail(String email) {
        Pattern p = Pattern.compile("^[A-Za-z0-9]+@[A-za-z-]+\\.[A-Za-z]{2,}$");
        return p.matcher(email).matches();
    }

    public static boolean validPassword(String password) {
        Pattern p = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9\\s])[^\\s]{7,20}$");
        return p.matcher(password).matches();
    }

    public static boolean validUsername(String username) {
        Pattern p = Pattern.compile("^(?=.*[a-zA-Z])[a-zA-Z0-9_-]{5,14}$");
        return p.matcher(username).matches();
    }

    public static int validIntChoice(String choice, int range) {
        int choiceInt = Integer.parseInt(choice);
        if (choiceInt > 0 && choiceInt <= range) {
            return choiceInt;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }
}
