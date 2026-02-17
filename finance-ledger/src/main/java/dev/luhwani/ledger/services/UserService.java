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

    public Optional<LoginData> emailFound(String email) {
        try {
            return repo.findEmail(email);
        } catch (DataAccessException e) {
            throw new UIException(e.getMessage(), e);
        }
    }

    public Optional<User> createUser(LoginData loginData) {
        try {
            Optional<Long> userId = repo.registerUser(loginData);
            if (userId.isPresent()) {
                return Optional.of(new User(userId.get(), loginData.email(), loginData.username()));
            }
            return Optional.empty();
        } catch (DataAccessException e) {
            throw new UIException(e.getMessage(), e);
        }
    }

    public Optional<User> login(LoginData loginData) {
        try {
            repo.setLastLogin(loginData.id());
            Optional<User> user = Optional.of(new User(loginData.id(), loginData.email(), loginData.username()));
            return user;
        } catch (DataAccessException e) {
            throw new UIException(e.getMessage(), e);
        }
    }

}
