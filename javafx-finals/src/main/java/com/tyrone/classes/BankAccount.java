package com.tyrone.classes;

import com.tyrone.Exceptions.InsufficientFundsException;
import com.tyrone.Interface.Transaction;


public class BankAccount implements Transaction {
    private int bankNumber;
    private String name;
    protected double balance;

    //constructors for the bank account
    public BankAccount(){
        bankNumber = 1000;
        name = "UserName";
        balance = 5000.00;
    }

    public BankAccount(int bankNumber, String name, Double balance){
        this.balance = balance;
        this.name = name;
        this.bankNumber = bankNumber;
    }

    public double getBalance(){
        return balance;
    }

    public String getName() {
        return name;
    }

    public int getBankNumber() {
        return bankNumber;
    }

    
    @Override
    public synchronized void withdraw(double amount) throws InsufficientFundsException{
        if(balance < amount){
            throw new InsufficientFundsException("Not enough money for withdrawal");
        } else{
            balance -= amount;
        }
    }

    @Override
    public synchronized void deposit(double amount) {
        balance += amount;
    }
}
