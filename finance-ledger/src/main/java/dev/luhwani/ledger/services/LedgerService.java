package dev.luhwani.ledger.services;

import java.util.EnumSet;

import dev.luhwani.ledger.models.Category;
import dev.luhwani.ledger.repos.LedgerRepo;

public class LedgerService {
    
    private final LedgerRepo repo;
    
    public LedgerService(LedgerRepo repo) {
        this.repo = repo;
    }

    public EnumSet<Category> getCategories() {
        return repo.allCategories;
    }
}
