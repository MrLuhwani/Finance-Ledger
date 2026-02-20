package dev.luhwani.ledger.services;

import java.util.EnumSet;

import dev.luhwani.ledger.models.Category;
import dev.luhwani.ledger.repos.TransactionRepo;

public class TransactionService {
    
    private final TransactionRepo repo;
    
    public TransactionService(TransactionRepo repo) {
        this.repo = repo;
    }

    public EnumSet<Category> getCategories() {
        return repo.allCategories();
    }
}
