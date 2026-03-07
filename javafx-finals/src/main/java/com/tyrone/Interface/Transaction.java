package com.tyrone.Interface;

import com.tyrone.Exceptions.InsufficientFundsException;

public interface Transaction {
    void withdraw(double amount) throws InsufficientFundsException;
    void deposit(double amount);
}
