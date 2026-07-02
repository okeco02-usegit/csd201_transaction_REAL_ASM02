package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class Account {

    private String accountId;
    private String accountName;
    private double balance;

    private Node head;
    private Node tail;

    public Account(String accountId, String accountName, double initialBalance) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.balance = initialBalance;
        this.head = null;
        this.tail = null;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public double getBalance() {
        return balance;
    }

    public boolean deposit(double amount) {

        if (amount <= 0) {
            return false;
        }

        this.balance += amount;
        return true;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && this.balance >= amount) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    public void addTransaction(Transaction t) {
        Node newNode = new Node(t);
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
    }

    public void displayHistory() {
        if (head == null) {
            System.out.println("No transactions found for this account.");
            return;
        }
        Node current = head;
        while (current != null) {
            System.out.println(current.data.toString());
            current = current.next;
        }
    }

    public Transaction findTransactionByIdLinear(String txId) {
        Node current = head;
        while (current != null) {
            if (current.data.getTransactionId().equalsIgnoreCase(txId)) {
                return current.data;
            }
            current = current.next;
        }
        return null;
    }

    public ArrayList<Transaction> getTransactionsByMonth(int month, int year) {
        ArrayList<Transaction> filteredList = new ArrayList<>();
        Node current = head;
        Calendar cal = Calendar.getInstance();

        while (current != null) {
            cal.setTime(current.data.getTimestamp());
            int tMonth = cal.get(Calendar.MONTH) + 1;
            int tYear = cal.get(Calendar.YEAR);

            if (tMonth == month && tYear == year) {
                filteredList.add(current.data);
            }
            current = current.next;
        }
        return filteredList;
    }

    public ArrayList<Transaction> getAllTransactions() {
        ArrayList<Transaction> list = new ArrayList<>();
        Node current = head;
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        return list;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Account account = (Account) obj;
        return Objects.equals(accountId, account.accountId);
    }
}
