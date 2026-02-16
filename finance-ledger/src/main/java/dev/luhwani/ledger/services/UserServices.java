package dev.luhwani.ledger.services;

import dev.luhwani.ledger.customExceptions.DataAccessException;
import dev.luhwani.ledger.customExceptions.UIException;
import dev.luhwani.ledger.repos.UserRepo;

public class UserServices {

    public UserRepo repo;

    public UserServices(UserRepo repo) {
        this.repo = repo;
    }

    public boolean emailFound(String email) {
        try {
            return repo.findEmail(email);
        } catch (DataAccessException e) {
            throw new UIException(e.getMessage(), e);
        }
    }

}
