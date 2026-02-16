package dev.luhwani.ledger.services;

import java.util.regex.Pattern;

public class Utils {
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
}
