# Finance Ledger (Java CLI)
This is my first major project using Java.
A simple command-line personal finance ledger built in Java.  
It allows you to track income & expenses, update transactions, delete entries, and generate monthly summaries — all stored in a CSV file.

---

## Features
- Add new transactions  
- Edit existing transactions  
- Delete transactions  
- View entire CSV ledger  
- Auto-sorted by date (newest dates are on top)  
- Auto-generated serial IDs  
- Monthly income/expense summary  
- CSV persistence using a static initializer block  

---

## Project Structure
FinanceLedger/
│
├── src/
│   ├── com.mrLuhwani.ledger.transactionModel/
│   │   └── Transaction.java
│   └── com.mrLuhwani.ledger.utilities/
│       └── LedgerUtilities.java
│   └── com.mrLuhwani.ledger/
│       └── App.java│
├── ledger.csv
├── README.md
└── .gitignore

---

## How It Works
- All transactions are loaded from `ledger.csv` at startup.  
- Transactions are stored in a dynamic `ArrayList`.  
- IDs are regenerated after each change to maintain serial order.  
- Dates are validated to prevent future entries.  
- CSV file rewrites fully after each update (simple & safe).  

---

## Future Improvements
- Replace static methods with proper OOP services  
- Add unit tests  
- Reduce load on `LedgerUtilities`  
- Support JSON or SQLite database  
- Add filtering/search  
- Adding a GUI
