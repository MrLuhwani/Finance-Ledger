package dev.luhwani.ledger.models;

public record LoginData(
                Long id,
                String email,
                String username,
                byte[] passwordHash,
                String salt) {
}