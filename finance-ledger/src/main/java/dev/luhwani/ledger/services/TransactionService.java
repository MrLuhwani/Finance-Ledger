package dev.luhwani.ledger.services;

import java.util.EnumSet;
import java.util.Optional;

import dev.luhwani.ledger.customExceptions.DataAccessException;
import dev.luhwani.ledger.customExceptions.UIException;
import dev.luhwani.ledger.models.Category;
import dev.luhwani.ledger.models.Transaction;
import dev.luhwani.ledger.models.User;
import dev.luhwani.ledger.repos.TransactionRepo;

public class TransactionService {

    private final TransactionRepo repo;

    public TransactionService(TransactionRepo repo) {
        this.repo = repo;
    }

    public EnumSet<Category> getCategories() {
        return repo.allCategories();
    }

    public void addTransaction(User user, Transaction transaction) {
        try {
            Optional<Transaction> tr = repo.returnStoredTransaction(transaction);
            if (tr.isPresent()) {
                user.addTransaction(tr.get());
                return;
            }
            throw new UIException("ID couldn't be retrieved while storing transaction");
        } catch (DataAccessException e) {
            throw new UIException(e.getMessage(), e);
        }
    }

    public void editTransaction(Transaction transaction) {
        try {
            repo.editTransaction(transaction);
        } catch (DataAccessException e) {
            throw new UIException(e.getMessage(), e);
        }
    }

    public void deleteTransaction(Long id) {
        try {
            repo.removeTransaction(id);
        } catch (DataAccessException e) {
            throw new UIException(e.getMessage(), e);
        }
    }
}
