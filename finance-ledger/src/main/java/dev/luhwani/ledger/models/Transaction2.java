package dev.luhwani.ledger.models;

import java.math.BigDecimal;
import java.time.LocalDate;

//this is going to be the actual transaction object.
// this class was just made so that I can still work without having
//to deal with too many ompiler warnings if I change the original class
public record Transaction2(
        Long id,
        LocalDate date,
        Long koboAmt,
        EntryType entryType,
        Category category,
        String description,
        Long userId
) {
        @Override
        public String toString() {
                if (entryType.equals(EntryType.EXPENSE)) {
                        return date + ", " + "-" + nairaAmt() + ", " +  category + ", " + description; 
                }
                return date + ", " + nairaAmt() + ", " + category + ", " + description;
        }

        private BigDecimal nairaAmt() {
                return BigDecimal.valueOf(koboAmt).divide(BigDecimal.valueOf(100));
        }
}
