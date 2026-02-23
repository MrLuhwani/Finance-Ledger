package dev.luhwani.ledger.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Transaction(
                Long id,
                LocalDate date,
                Long koboAmt,
                EntryType entryType,
                Category category,
                String description,
                Long userId) {
        @Override
        public String toString() {
                if (entryType.equals(EntryType.EXPENSE)) {
                        return date + ", " + "-" + nairaAmt() + ", " + category + ", " + description;
                }
                return date + ", " + nairaAmt() + ", " + category + ", " + description;
        }

        private BigDecimal nairaAmt() {
                return BigDecimal.valueOf(koboAmt).divide(BigDecimal.valueOf(100));
        }
}
