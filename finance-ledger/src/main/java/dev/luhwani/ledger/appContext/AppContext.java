package dev.luhwani.ledger.appContext;

import dev.luhwani.ledger.services.TransactionService;
import dev.luhwani.ledger.services.SecurityService;
import dev.luhwani.ledger.services.UserService;

public class AppContext {
    private final SecurityService securityService;
    private final UserService userService;
    private final TransactionService transactionService;

    public AppContext(SecurityService securityService, UserService userService, TransactionService transactionService) {
        this.securityService = securityService;
        this.userService = userService;
        this.transactionService = transactionService;
    }

    public SecurityService getSecurityService() {
        return securityService;
    }

    public UserService getUserService() {
        return userService;
    }

    public TransactionService getTransactionService() {
        return transactionService;
    }
}
