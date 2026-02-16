package dev.luhwani.ledger.services;

import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.util.Random;

import dev.luhwani.ledger.customExceptions.UIException;

public class SecurityService {
    
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new Random();
    
    //the upper and lower boundaries represent the range of values for a generated salt
    private static final int SALTLOWERLIMIT = 5;
    private static final int SALTUPPERLIMIT = 10;

    public static byte[] hashText(String input) {
        try {
            MessageDigest digester = MessageDigest.getInstance("SHA-256");
            byte[] inputByte =  input.getBytes();
            byte[] hashByte = digester.digest(inputByte);
            return hashByte;
        } catch (NoSuchAlgorithmException e) {
            throw new UIException(e.getMessage(), e);
        }
    }

    public static String saltGenerator() {
        int saltLength = RANDOM.nextInt(SALTLOWERLIMIT, SALTUPPERLIMIT);
        StringBuilder sb = new StringBuilder(saltLength);
        for (int i = 0; i < saltLength; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public static boolean passwordsMatch(byte[] b1, byte[] b2 ) {
        return MessageDigest.isEqual(b1, b2);
    }
}
