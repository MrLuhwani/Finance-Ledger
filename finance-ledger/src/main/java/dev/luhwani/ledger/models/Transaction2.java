package dev.luhwani.ledger.models;

import java.time.LocalDate;

//this is going to be the actual transaction object.
// this class was just made so that I can still work without having
//to deal with too many ompiler warnings if I change the original class
public record Transaction2(
        LocalDate date,
        Long koboAmt,
        EntryType entryType,
        Category category,
        String description,
        Long userId
) {}
