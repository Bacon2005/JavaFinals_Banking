package com.tyrone.threads;

import com.tyrone.Exceptions.InsufficientFundsException;
import com.tyrone.classes.BankAccount;

public class UserThread implements Runnable{
    private BankAccount account;

    public UserThread(BankAccount account){
        this.account = account;
    }

    //What happens when you run the thread
    @Override
    public void run(){
        try {
            account.withdraw(500.00);
        } catch (InsufficientFundsException e) {
            System.out.println("Exception Error: " + e.getMessage());
        }
    }
}
