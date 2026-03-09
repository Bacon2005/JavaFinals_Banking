package com.tyrone.threads;

import com.tyrone.controllers.Controller;
import com.tyrone.Exceptions.InsufficientFundsException;
import com.tyrone.classes.BankAccount;

import javafx.application.Platform;

import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

public class UserThread extends Thread {
    private BankAccount account;
    private String name;
    private Controller controller;

    public UserThread(BankAccount account, String name, Controller controller) {
        this.account = account;
        this.name = name;
        this.controller = controller;
    }

    String actionDone;

    // Thread runs
    @Override
    public void run() {
        for (int i = 0; i <= 5; i++) {

            // Thread gets a random number for either deposit, withdraw or checkBalance

            int action = ThreadLocalRandom.current().nextInt(100);

            // random amount to deposit or withdraw
            double amount = ThreadLocalRandom.current().nextDouble(10000, 50000);

            if (action < 40) {
                // System.out.println("User: " + name + " is depositing: " + amount + " to " +
                // account.getName());
                Platform.runLater(() -> {
                    account.deposit(amount);
                    account.setLastTransaction("DEPOSIT");
                    actionDone = "DEPOSIT";
                });
                // update the amount seen on the table

            } else if (action < 80) {

                try {
                    // System.out.println("User: " + name + " is withdrawing: " + amount + " from "
                    // + account.getName());
                    Platform.runLater(() -> {
                        try {
                            account.withdraw(amount);
                            account.setLastTransaction("WITHDRAW");
                            actionDone = "WITHDRAW";
                        } catch (InsufficientFundsException e) {
                            System.out.println("Caught Exception! " + name + ": " + e.getMessage());
                        }
                    });
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else {
                // DecimalFormat formatter = new DecimalFormat("#,###.##");
                // System.out.println("User: " + name + " Checked balance");
                // String balance = formatter.format(account.getBalance());
                // System.out.println("Balance is: " + balance);

                account.setLastTransaction("CHECK");
                actionDone = "CHECK";
            }

            Platform.runLater(() -> {
                controller.logTransaction(name, actionDone, amount, account);
            });
            // 1 second pause to simulate thinking time
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(name + ": Done running");

        // temp debugging
        // DecimalFormat formatter = new DecimalFormat("#,###.##");
        // System.out.println("Final Balance: " +
        // formatter.format(account.getBalance()));
    }
}
