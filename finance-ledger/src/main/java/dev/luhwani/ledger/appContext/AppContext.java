package dev.luhwani.ledger.appContext;

import dev.luhwani.ledger.services.SecurityService;
import dev.luhwani.ledger.services.UserService;

public class AppContext {
    public SecurityService securityService;
    public UserService userService;

    public AppContext(SecurityService securityService, UserService userService) {
        this.securityService = securityService;
        this.userService = userService;
    }

    public SecurityService getSecurityService() {
        return securityService;
    }

    public UserService getUserService() {
        return userService;
    }
}
