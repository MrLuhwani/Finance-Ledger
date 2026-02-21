package dev.luhwani.ledger.services;

import java.util.EnumSet;

import dev.luhwani.ledger.customExceptions.DataAccessException;
import dev.luhwani.ledger.customExceptions.UIException;
import dev.luhwani.ledger.models.Category;
import dev.luhwani.ledger.models.Transaction2;
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

    public void addTransaction(User user,Transaction2 transaction2) {
        try {
            repo.storeTransaction(transaction2);
            user.addTransaction(transaction2);
        } catch (DataAccessException e) {
            throw new UIException(e.getMessage(), e);
        }
    }
}
