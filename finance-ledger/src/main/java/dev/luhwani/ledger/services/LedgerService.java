package dev.luhwani.ledger.services;

import dev.luhwani.ledger.repos.LedgerRepo;

public class LedgerService {
    
    private final LedgerRepo repo;
    public LedgerService(LedgerRepo repo) {
        this.repo = repo;
    }
}
