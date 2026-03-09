package com.tyrone.classes;

import com.tyrone.Exceptions.InsufficientFundsException;
import com.tyrone.Interface.Transaction;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BankAccount implements Transaction {
    private IntegerProperty bankNumber;
    private StringProperty name;
    private DoubleProperty balance;

    private StringProperty lastTransaction = new SimpleStringProperty("NONE");

    public BankAccount(int bankNumber, String name, double balance) {
        this.bankNumber = new SimpleIntegerProperty(bankNumber);
        this.name = new SimpleStringProperty(name);
        this.balance = new SimpleDoubleProperty(balance);
    }

    // Properties for TableView binding
    public IntegerProperty bankNumberProperty() {
        return bankNumber;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public DoubleProperty balanceProperty() {
        return balance;
    }

    public StringProperty lastTransactionProperty() {
        return lastTransaction;
    }

    // Getters / Setters
    public String getLastTransaction() {
        return lastTransaction.get();
    }

    public void setLastTransaction(String lastTransaction) {
        this.lastTransaction.set(lastTransaction);
    }

    public int getBankNumber() {
        return bankNumber.get();
    }

    public void setBankNumber(int value) {
        bankNumber.set(value);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String value) {
        name.set(value);
    }

    public double getBalance() {
        lastTransaction.set("CHECK");
        return balance.get();
    }

    public void setBalance(double value) {
        balance.set(value);
    }

    // Thread-safe transaction methods
    @Override
    public synchronized void withdraw(double amount) throws InsufficientFundsException {
        if (balance.get() < amount) {
            throw new InsufficientFundsException("Insufficient Funds Exception");
        } else {
            balance.set(balance.get() - amount);
        }
    }

    @Override
    public synchronized void deposit(double amount) {
        balance.set(balance.get() + amount);
        lastTransaction.set("DEPOSIT");
    }
}