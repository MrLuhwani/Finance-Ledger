package dev.luhwani.ledger.services;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import dev.luhwani.ledger.customExceptions.DataAccessException;
import dev.luhwani.ledger.customExceptions.UIException;
import dev.luhwani.ledger.models.LoginData;
import dev.luhwani.ledger.models.Transaction;
import dev.luhwani.ledger.models.User;
import dev.luhwani.ledger.repos.UserRepo;

public class UserService {

    private final UserRepo repo;

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
            List<Transaction> transactionList = repo.getUserTransactions(loginData.id());
            repo.setLastLogin(loginData.id());
            Optional<User> user = Optional.of(new User(loginData.id(), loginData.email(), loginData.username()));
            transactionList.sort(Comparator.comparing(Transaction::date).reversed());
            user.get().setTransactionList(transactionList);
            return user;
        } catch (DataAccessException e) {
            throw new UIException(e.getMessage(), e);
        }
    }

    public Optional<byte[]> getPassword(long userId) {
        try {
            return repo.getPassword(userId);
        } catch (DataAccessException e) {
            throw new UIException(e.getMessage(), e);
        }
    }

    public Optional<String> getSalt(long userId) {
        try {
            return repo.getSalt(userId);
        } catch (DataAccessException e) {
            throw new UIException(e.getMessage(), e);
        }
    }

    public void updatePassword(Long userId, byte[] passwordHash, String salt) {
        try {
            repo.updatePassword(userId, passwordHash, salt);
        } catch (DataAccessException e) {
            throw new UIException(e.getMessage(), e);
        }
    }

    public void deleteUser(Long userId) {
        try {
            repo.deleteAcct(userId);
        } catch (DataAccessException e) {
            throw new UIException(e.getMessage(), e);
        }
    }
}
