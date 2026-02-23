package dev.luhwani.ledger.customExceptions;

public class UIException extends RuntimeException {

    public UIException(String message, Throwable cause) {
        super(message, cause);
    }

    public UIException(String string) {
        super(string);
    }

}
