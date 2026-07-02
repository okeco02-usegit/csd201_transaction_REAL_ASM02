package service;

import model.Account;
import model.Transaction;
import java.util.ArrayList;
import java.util.HashMap;

public class MainController {

    private HashMap<String, Account> accountDatabase;

    private final String ACCOUNT_FILE = "bank_accounts.txt";
    private final String TX_FILE = "bank_transactions.txt";

    public MainController() {
        accountDatabase = new HashMap<>();
    }

    public boolean createAccount(String accountId,
            String accountName,
            double initialBalance) {

        if (!accountId.matches("ACC\\d{3}")) {
            System.out.println("[ERROR] Account ID must follow format ACC###");
            return false;
        }

        if (initialBalance < 0) {
            System.out.println("[ERROR] Initial balance cannot be negative.");
            return false;
        }

        if (accountDatabase.containsKey(accountId)) {
            System.out.println("[ERROR] Account ID [" + accountId + "] already exists!");
            return false;
        }

        accountDatabase.put(accountId,
                new Account(accountId, accountName, initialBalance));

        System.out.println("[SUCCESS] Account created.");
        return true;
    }

    public Account findAccount(String accountId) {
        return accountDatabase.get(accountId);
    }

    public void processDeposit(String accountId, String txId, double amount, String desc) {

        Account acc = findAccount(accountId);

        if (acc == null) {
            System.out.println("[FAILED] Transaction aborted: Account not found.");
            return;
        }

        if (acc.deposit(amount)) {

            acc.addTransaction(new Transaction(txId, "Deposit", amount, desc));

            System.out.println("[SUCCESS] Deposit completed.");

        } else {

            System.out.println("[FAILED] Invalid deposit amount.");
        }
    }

    public void processWithdraw(String accountId, String txId, double amount, String desc) {
        Account acc = findAccount(accountId);
        if (acc == null) {
            System.out.println("[FAILED] Transaction aborted: Account not found.");
            return;
        }
        if (acc.withdraw(amount)) {
            acc.addTransaction(new Transaction(txId, "Withdraw", amount, desc));
            System.out.println("[SUCCESS] Withdrawal completed.");
        } else {
            System.out.println("[FAILED] Insufficient balance.");
        }
    }

    public void processTransfer(String fromId, String toId, String txId, double amount, String desc) {
        Account sender = findAccount(fromId);
        Account receiver = findAccount(toId);

        if (sender == null || receiver == null) {
            System.out.println("[FAILED] Invalid account.");
            return;
        }

        if (amount <= 0) {
            System.out.println("[FAILED] Invalid transfer amount.");
            return;
        }

        if (sender.withdraw(amount)) {
            receiver.deposit(amount);
            sender.addTransaction(new Transaction(txId, "Transfer Out", amount, "To " + toId + ": " + desc));
            receiver.addTransaction(new Transaction(txId, "Transfer In", amount, "From " + fromId + ": " + desc));
            System.out.println("[SUCCESS] Transfer completed.");
        } else {
            System.out.println("[FAILED] Insufficient balance.");
        }
        
    }

    public void saveData() {
        try (
                 java.io.PrintWriter accWriter
                = new java.io.PrintWriter(new java.io.FileWriter(ACCOUNT_FILE));  java.io.PrintWriter txWriter
                = new java.io.PrintWriter(new java.io.FileWriter(TX_FILE))) {

            // Save account information
            for (Account acc : accountDatabase.values()) {

                accWriter.println(
                        acc.getAccountId() + ","
                        + acc.getAccountName() + ","
                        + acc.getBalance());

                // Save transaction history
                for (Transaction tx : acc.getAllTransactions()) {

                    txWriter.println(
                            acc.getAccountId() + ","
                            + tx.getTransactionId() + ","
                            + tx.getType() + ","
                            + tx.getAmount() + ","
                            + tx.getTimestamp().getTime() + ","
                            + tx.getDescription());
                }
            }

            System.out.println("[SUCCESS] Data saved successfully.");

        } catch (Exception e) {

            System.out.println("[ERROR] Unable to save data.");
            e.printStackTrace();
        }
    }

    public void loadData() {
        java.io.File accountFile = new java.io.File(ACCOUNT_FILE);
        java.io.File transactionFile = new java.io.File(TX_FILE);

        if (!accountFile.exists()) {
            System.out.println("[INFO] No existing database found.");
            return;
        }

        try {

            // Load Accounts
            java.util.Scanner accScanner
                    = new java.util.Scanner(accountFile);

            while (accScanner.hasNextLine()) {

                String line = accScanner.nextLine();

                String[] parts = line.split(",", 3);

                if (parts.length == 3) {

                    String id = parts[0];
                    String name = parts[1];
                    double balance = Double.parseDouble(parts[2]);

                    Account account
                            = new Account(id, name, balance);

                    accountDatabase.put(id, account);
                }
            }

            accScanner.close();

            // Load Transactions
            if (transactionFile.exists()) {

                java.util.Scanner txScanner
                        = new java.util.Scanner(transactionFile);

                while (txScanner.hasNextLine()) {

                    String line = txScanner.nextLine();

                    String[] parts = line.split(",", 6);

                    if (parts.length == 6) {

                        String accountId = parts[0];
                        String txId = parts[1];
                        String type = parts[2];
                        double amount = Double.parseDouble(parts[3]);
                        java.util.Date timestamp
                                = new java.util.Date(
                                        Long.parseLong(parts[4]));
                        String description = parts[5];

                        Account acc
                                = accountDatabase.get(accountId);

                        if (acc != null) {

                            Transaction tx
                                    = new Transaction(
                                            txId,
                                            type,
                                            amount,
                                            timestamp,
                                            description);

                            acc.addTransaction(tx);
                        }
                    }
                }

                txScanner.close();
            }

            System.out.println("[SUCCESS] Data loaded successfully.");

        } catch (Exception e) {

            System.out.println("[ERROR] Unable to load data.");
            e.printStackTrace();
        }
    }

    public void searchTransaction(String accountId, String txId) {
        Account acc = findAccount(accountId);
        if (acc == null) {
            System.out.println("[FAILED] Account not found.");
            return;
        }

        Transaction tx = acc.findTransactionByIdLinear(txId);

        if (tx != null) {
            System.out.println(tx);
        } else {
            System.out.println("[FAILED] Transaction not found.");
        }
    }

    public void generateFinancialReport(String accountId, int month, int year) {
        Account acc = findAccount(accountId);

        if (acc == null) {
            System.out.println("[FAILED] Account not found.");
            return;
        }

        ArrayList<Transaction> monthlyTx = acc.getTransactionsByMonth(month, year);

        if (monthlyTx.isEmpty()) {
            System.out.println("[INFO] No transactions found.");
            return;
        }

        mergeSortTransactions(monthlyTx, 0, monthlyTx.size() - 1);

        double inflow = 0;
        double outflow = 0;

        System.out.println("===== SORTED MONTHLY REPORT =====");

        for (Transaction tx : monthlyTx) {
            System.out.println(tx);

            if (tx.getType().equals("Deposit") || tx.getType().equals("Transfer In")) {
                inflow += tx.getAmount();
            } else {
                outflow += tx.getAmount();
            }
        }

        System.out.printf("Total Inflow : %.2f%n", inflow);
        System.out.printf("Total Outflow: %.2f%n", outflow);
        System.out.printf("Net Cash Flow: %.2f%n", inflow - outflow);
    }

    private void mergeSortTransactions(ArrayList<Transaction> list, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSortTransactions(list, left, mid);
            mergeSortTransactions(list, mid + 1, right);
            merge(list, left, mid, right);
        }
    }

    private void merge(ArrayList<Transaction> list, int left, int mid, int right) {
        ArrayList<Transaction> L = new ArrayList<>();
        ArrayList<Transaction> R = new ArrayList<>();

        for (int i = left; i <= mid; i++) {
            L.add(list.get(i));
        }
        for (int i = mid + 1; i <= right; i++) {
            R.add(list.get(i));
        }

        int i = 0, j = 0, k = left;

        while (i < L.size() && j < R.size()) {
            if (L.get(i).getAmount() >= R.get(j).getAmount()) {
                list.set(k++, L.get(i++));
            } else {
                list.set(k++, R.get(j++));
            }
        }

        while (i < L.size()) {
            list.set(k++, L.get(i++));
        }
        while (j < R.size()) {
            list.set(k++, R.get(j++));
        }

    }

    private boolean isValidAccountId(String accountId) {
        return accountId != null
                && accountId.matches("ACC\\d{3}");
    }
}
