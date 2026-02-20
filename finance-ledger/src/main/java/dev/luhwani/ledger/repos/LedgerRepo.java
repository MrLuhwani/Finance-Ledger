package dev.luhwani.ledger.repos;

import java.util.EnumSet;

import dev.luhwani.ledger.models.Category;

public class LedgerRepo {
    
    private EnumSet<Category> allCategories = EnumSet.allOf(Category.class);

    public EnumSet<Category> allCategories(){
        return allCategories;
    }
}
