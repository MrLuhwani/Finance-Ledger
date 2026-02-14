package dev.luhwani.ledger.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import dev.luhwani.ledger.models.Transaction;

public class CsvUtils {

    static {
        String ledgerPath = "C:\\Users\\tolut\\Desktop\\backend dev\\Projects\\FinanceLedger\\ledger.csv";
        double amount;
        boolean isIncome;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try (BufferedReader bReader = new BufferedReader(new FileReader(ledgerPath))) {
            String[] column;
            String line;
            String header = "ID,Date,Amount(₦),Category,Description";
            while ((line = bReader.readLine()) != null) {
                if (!line.equals(header)) {
                    column = line.split(",");
                    amount = Math.abs(Double.parseDouble(column[2]));
                    if (column[2].startsWith("-")) {
                        isIncome = false;
                    } else {
                        isIncome = true;
                    }
                    LedgerUtils.transactions
                            .add(new Transaction(Integer.parseInt(column[0]), LocalDate.parse(column[1], formatter),
                                    amount, isIncome, column[3], column[4]));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Invalid file path");
        } catch (IOException e) {
            System.out.println("Something went wrong");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getCSV() {
        String ledgerPath = "C:\\Users\\tolut\\Desktop\\backend dev\\Projects\\FinanceLedger\\ledger.csv";
        String line;
        int rows = 0;
        try (BufferedReader bReader = new BufferedReader(new FileReader(ledgerPath))) {
            while ((line = bReader.readLine()) != null) {
                System.out.println(line);
                rows += 1;
                System.out.println("_ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ ");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Incorrect File Path Error!");
        } catch (IOException e) {
            System.out.println("Something went wrong!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (rows == 1) {
            System.out.println("No transaction has been added yet");
        }
    }
    
    static void csvWriter() {
        String ledgerPath = "C:\\Users\\tolut\\Desktop\\backend dev\\Projects\\FinanceLedger\\ledger.csv";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String strId;
        try (BufferedWriter bWriter = new BufferedWriter(new FileWriter(ledgerPath))) {
            String header = "ID,Date,Amount(₦),Category,Description";
            String strDate;
            String strAmt;
            bWriter.write(header);
            bWriter.write("\n");
            for (Transaction tr : LedgerUtils.transactions) {
                strId = String.valueOf(tr.getId());
                strDate = tr.getDate().format(formatter);
                strAmt = String.valueOf(tr.getAmount());
                if (!tr.getIsIncome()) {
                    strAmt = "-" + strAmt;
                }
                String[] row = { strId, strDate, strAmt, tr.getCategory(), tr.getDescription() };
                bWriter.write(String.join(",", row));
                bWriter.write("\n");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Invalid file path");
        } catch (IOException e) {
            System.out.println("Something went wrong");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
