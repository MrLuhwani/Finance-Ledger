package dev.luhwani.ledger.appContext;

import dev.luhwani.ledger.services.LedgerService;
import dev.luhwani.ledger.services.SecurityService;
import dev.luhwani.ledger.services.UserService;

public class AppContext {
    private final SecurityService securityService;
    private final UserService userService;
    private final LedgerService ledgerService;

    public AppContext(SecurityService securityService, UserService userService, LedgerService ledgerService) {
        this.securityService = securityService;
        this.userService = userService;
        this.ledgerService = ledgerService;
    }

    public SecurityService getSecurityService() {
        return securityService;
    }

    public UserService getUserService() {
        return userService;
    }

    public LedgerService getLedgerService() {
        return ledgerService;
    }
}
