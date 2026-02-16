package dev.luhwani.ledger.services;

import java.util.Optional;

import dev.luhwani.ledger.customExceptions.DataAccessException;
import dev.luhwani.ledger.customExceptions.UIException;
import dev.luhwani.ledger.models.LoginData;
import dev.luhwani.ledger.models.User;
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

    public Optional<User> createNewAcct(LoginData loginData) {
        try {
            Optional<Long> userId = repo.registerUser(loginData);
            if (userId.isPresent()) {
                return Optional.of(new User(loginData.email(), userId.get(), loginData.username()));
            }
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new UIException(e.getMessage(), e);
        }
    }

}
