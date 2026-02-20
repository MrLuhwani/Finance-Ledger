# Finance Ledger (Java CLI)
A simple command-line personal finance ledger built in Java.
It allows multiple users to track income & expenses, update transactions, filter expenses, delete entries, and generate monthly summaries.
The ledger can also be exported in a csv

---

## Current Features  
- User Account creation methods
- Add new transactions  
- Edit existing transactions  
- Delete transactions  
- View entire CSV ledger  
- Auto-sorted by date (newest dates are on top)  
- Monthly income/expense summary  
- CSV persistence using a static initializer block  
- Persistence with DB

---

## Planned Features  
- Supporting multiple users
- Filter by different criteria
- Export to csv

---

## Project Structure
FinanceLedger  
├─ finance-ledger/  
│  ├─ src/  
│  │  └─ main/  
│  │     ├─ java/  
│  │     │  └─ dev/  
│  │     │     └─ luhwani/  
│  │     │        └─ ledger/  
│  │     │           ├─ appContext/  
│  │     │           │  └─ AppContext.java  
│  │     │           ├─ customExceptions/  
│  │     │           │  ├─ DataAccessException.java  
│  │     │           │  └─ UIException.java  
│  │     │           ├─ models/  
│  │     │           │  ├─ Category.java  
│  │     │           │  ├─ EntryType.java  
│  │     │           │  ├─ LoginData.java  
│  │     │           │  ├─ Transaction.java  
│  │     │           │  ├─ Transaction2.java  
│  │     │           │  └─ User.java  
│  │     │           ├─ repos/  
│  │     │           │  ├─ ConnectionManager.java  
│  │     │           │  ├─ TransactionRepo.java  
│  │     │           │  └─ UserRepo.java  
│  │     │           ├─ services/  
│  │     │           │  ├─ TransactionService.java  
│  │     │           │  ├─ SecurityService.java  
│  │     │           │  ├─ UserService.java  
│  │     │           │  └─ Utils.java  
│  │     │           ├─ utilities/  
│  │     │           │  ├─ CsvUtils.java  
│  │     │           │  └─ LedgerUtils.java  
│  │     │           └─ App.java  
│  │     └─ resources/  
│  │         └─ ledger.csv  
│  │         └─ schema.sql
│  └── pom.xml  
├─ .gitignore  
└─ README.md  
---

## Future Improvements
- Add unit tests  
- Integrate Spring boot
- Add a frontend or UI
