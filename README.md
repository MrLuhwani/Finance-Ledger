# Finance Ledger (Java CLI)
A simple command-line personal finance ledger built in Java.
It allows multiple users to track income & expenses, update transactions, filter expenses, delete entries, and generate monthly summaries.
The ledger can also be exported in a csv

---

## Current Features  
- Add new transactions  
- Edit existing transactions  
- Delete transactions  
- View entire CSV ledger  
- Auto-sorted by date (newest dates are on top)  
- Monthly income/expense summary  
- CSV persistence using a static initializer block  

---

## Planned Features  
- Supporting multiple users
- Filter by different criteria
- Persistence with DB
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
│  │     │           ├─ models/  
│  │     │           │  └─ Transaction.java  
│  │     │           ├─ utilities/  
│  │     │           │  ├─ CsvUtils.java  
│  │     │           │  └─ LedgerUtils.java  
│  │     │           └─ App.java  
│  │     └─ resources/  
│  │         └─ ledger.csv  
│  └── pom.xml  
├─ .gitignore  
└─ README.md  
---

## Future Improvements
- Add unit tests  
- Integrate Spring boot
- Add a frontend or UI
