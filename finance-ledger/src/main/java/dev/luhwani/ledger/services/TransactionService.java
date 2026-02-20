package dev.luhwani.ledger.services;

import java.util.EnumSet;

import dev.luhwani.ledger.models.Category;
import dev.luhwani.ledger.repos.LedgerRepo;

public class TransactionService {
    
    private final LedgerRepo repo;
    
    public TransactionService(LedgerRepo repo) {
        this.repo = repo;
    }

    public EnumSet<Category> getCategories() {
        return repo.allCategories();
    }
}
