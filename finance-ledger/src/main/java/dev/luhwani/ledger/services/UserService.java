package dev.luhwani.ledger.services;

import dev.luhwani.ledger.customExceptions.DataAccessException;
import dev.luhwani.ledger.customExceptions.UIException;
import dev.luhwani.ledger.repos.UserRepo;

public class UserService {

    public UserRepo repo;

    public UserService(UserRepo repo) {
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
