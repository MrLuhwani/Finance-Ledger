# Finance Ledger (CLI) — Java + PostgreSQL

A simple **command-line finance ledger** that lets users create accounts, log in, and manage income/expense transactions stored in **PostgreSQL**.

> **Goal:** practice Java fundamentals + JDBC + clean layering (Repo → Service → UI).  
> **Planned next:** Spring integration + better concurrency patterns.

---

## Features

- **Auth**
  - Create account (email + username + salted SHA-256 password hash)
  - Login
  - Change password
  - Delete account (cascades transactions)

- **Transactions**
  - Add transaction (income/expense)
  - Edit transaction
  - Delete transaction
  - View history (sorted by date desc)
  - Filter by category

- **Monthly summary**
  - Total income, total expense, net balance
  - Highest income transaction and highest expense transaction (single tx)
  - Export month to CSV (includes a summary section)

---

## Tech Stack

- Java `21`
- Maven
- PostgreSQL
- JDBC (no ORM)
- CSV export via `java.nio.file`

---

## Project Structure
```text
README.md
.gitignore
finance-ledger/
  pom.xml
  src/
    main/
      java/
        dev/luhwani/ledger/
          App.java
          appContext/
            AppContext.java
          customExceptions/
            DataAccessException.java
            UIException.java
          models/
            Category.java
            EntryType.java
            LoginData.java
            MonthSummary.java
            Transaction.java
            User.java
          repos/
            ConnectionManager.java
            TransactionRepo.java
            UserRepo.java
          services/
            ExportService.java
            FileNameService.java
            SecurityService.java
            TransactionService.java
            UserService.java
            Utils.java
      resources/
        schema.sql
exports/
```
---

## Setup

- **Prerequisites**
  - Java 21 installed
  - PostgreSQL running locally
  - A database created (example: ledger_db)

- **Configure DB connection**
  - Edit constraints in src/main/java/dev/luhwani/ledger/repos/ConnectionManager.java

- **Create tables**
  - You have schema.sql here: src/main/resources/schema.sql
  - Option A — run it in a SQL editor (psql / pgAdmin).
  - Option B — use ConnectionManager.initDB() method. If you do this, call it once at app startup, then remove the call.

---

## Run the App

From the project root **(finance-ledger/)**:
- mvn clean compile
- mvn exec:java -Dexec.mainClass="dev.luhwani.ledger.App"

If you don’t have the exec plugin, you can also run from VS Code (Run App.main) after Maven imports dependencies.

---

## Usage Walkthrough

- **When you run the app:**
  - Create Account
  - Validates email format
  - Validates password rules
  - Generates salt and hashes (password + salt) with SHA-256
  - Login
  - Loads stored hash/salt from DB
  - Hashes input and compares with MessageDigest.isEqual
  - After login (__Finance Ledger__ menu)
  - View history
  - Filter by category
  - Add / edit / delete transactions
  - Monthly summary + export CSV
  - Change password
  - Delete account

---

## Data Model Notes

- **Amount handling**
  - Amounts are stored as kobo (Long kobo_amt) to avoid floating point issues.
  - Input: user enters naira as string (e.g. 1200.50)
  - Validate scale ≤ 2
  - Convert to kobo: naira * 100

- **Expenses display**
   Expenses are stored as positive kobo values, but displayed with a minus sign in Transaction.toString().

---

## Export Output

Exports to: exports/summary-YYYY-MM.csv

If the file already exists, a unique filename is created (e.g. summary-2026-02 (1).csv).

- **CSV includes:**
  - All transactions for the chosen month
  - A **SUMMARY** section (totals, net, highest income/expense, top categories)

---

## Known Limitations / TODO

- Spring integration (planned)
  - Convert services to Spring @Service
  - Move DB config into application.properties
  - Use JdbcTemplate or Spring Data JDBC
  - Add a REST API (in consideration) + later a frontend

- Concurrency (Current app is single-threaded CLI, but concurrency can still matter for:)
  - Exporting CSV in a background thread
  - Running summaries without blocking the UI
  - Safe access to shared lists (User.transactions)

- Other Possible upgrades:
  - Using ExecutorService for export tasks
  - Making transaction list updates thread-safe (or avoid shared mutable state)
  - Adding proper transaction boundaries + rollback on SQL failure

---

## License

Built as a learning project to practice Java + JDBC + PostgreSQL. It is therefore open to contribution and improvements as it was strictly for learning purposes.
