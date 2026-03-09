package com.tyrone.controllers;

import com.tyrone.classes.BankAccount;
import com.tyrone.threads.UserThread;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {
    @FXML
    private javafx.scene.control.Button resetButton;
    @FXML
    private TableView<BankAccount> table;
    @FXML
    private TableColumn<BankAccount, Integer> bankNumberColumn;
    @FXML
    private TableColumn<BankAccount, String> bankNameColumn;
    @FXML
    private TableColumn<BankAccount, Double> bankAmountColumn;
    @FXML
    private Text bankTotal;
    @FXML
    private ObservableList<BankAccount> villagerBank;

    @FXML
    private ListView<String> transactionLog;

    private ObservableList<String> transactionLogData = FXCollections.observableArrayList();

    @FXML
    private LineChart<Number, Number> balanceChart;

    private XYChart.Series<Number, Number> librarianSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> nitwitSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> mendingSeries = new XYChart.Series<>();

    private int librarianCounter = 0;
    private int nitwitCounter = 0;
    private int mendingCounter = 0;

    private List<UserThread> runningThreads = new ArrayList<>();

    // Just for formatting
    DecimalFormat formatter = new DecimalFormat("#,###.##");

    BankAccount Librarian = new BankAccount(01, "Librarian Villager Number 5", 100000.00);
    BankAccount Nitwit = new BankAccount(02, "Nitwit Villager Number 1", 80000.75);
    BankAccount Mending = new BankAccount(03, "Mending Villager", 120000.09);

    // Initialize and show the table data
    public void initialize() {
        balanceChart.getStylesheets().add(getClass().getResource("/fxml/chart.css").toExternalForm());
        transactionLog.setItems(transactionLogData);

        bankNumberColumn.setCellValueFactory(data -> data.getValue().bankNumberProperty().asObject());
        bankNameColumn.setCellValueFactory(data -> data.getValue().nameProperty());
        bankAmountColumn.setCellValueFactory(data -> data.getValue().balanceProperty().asObject());

        bankAmountColumn.setCellFactory(column -> new javafx.scene.control.TableCell<BankAccount, Double>() {

            @Override
            protected void updateItem(Double balance, boolean empty) {
                super.updateItem(balance, empty);
                if (empty || balance == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", balance)); // 2 decimal places
                    BankAccount account = getTableView().getItems().get(getIndex());

                    // colors the table view so we can see what happened to the
                    // account
                    if ("DEPOSIT".equals(account.getLastTransaction())) {
                        setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white;");
                    } else if ("WITHDRAW".equals(account.getLastTransaction())) {
                        setStyle("-fx-background-color: #c62828; -fx-text-fill: white;");
                    } else {
                        setStyle("-fx-background-color: #FFF; -fx-text-fill: black;");
                    }
                }
            }

        });

        librarianSeries.setName("Librarian");
        nitwitSeries.setName("Nitwit");
        mendingSeries.setName("Mending");

        balanceChart.getData().addAll(librarianSeries, nitwitSeries, mendingSeries);

        // Add initial points
        librarianSeries.getData().add(new XYChart.Data<>(librarianCounter++, Librarian.getBalance()));
        nitwitSeries.getData().add(new XYChart.Data<>(nitwitCounter++, Nitwit.getBalance()));
        mendingSeries.getData().add(new XYChart.Data<>(mendingCounter++, Mending.getBalance()));

        // Librarian
        Librarian.balanceProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                librarianSeries.getData().add(new XYChart.Data<>(librarianCounter++, newVal.doubleValue()));
            });
        });

        // Nitwit
        Nitwit.balanceProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                nitwitSeries.getData().add(new XYChart.Data<>(nitwitCounter++, newVal.doubleValue()));
            });
        });

        // Mending
        Mending.balanceProperty().addListener((obs, oldVal, newVal) -> {
            Platform.runLater(() -> {
                mendingSeries.getData().add(new XYChart.Data<>(mendingCounter++, newVal.doubleValue()));
            });
        });

        villagerBank = FXCollections.observableArrayList(
                Librarian, Nitwit, Mending);

        table.setItems(villagerBank);
        bindTotalToAccounts();

        // Initialize the total value of every account in the bank

        double total = 0;
        for (BankAccount account : table.getItems()) {
            total += account.getBalance();
        }
        // format the amount to only show 2 decimal places
        String text = formatter.format(total);
        bankTotal.setText(text);

        transactionLog.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.contains("DEPOSIT")) {
                        setStyle("-fx-text-fill: green;");
                    } else if (item.contains("WITHDRAW")) {
                        setStyle("-fx-text-fill: red;");
                    } else {
                        setStyle("-fx-text-fill: black;");
                    }
                }
            }
        });
    }

    private void updateTotal() {
        double total = villagerBank.stream()
                .mapToDouble(BankAccount::getBalance)
                .sum();
        bankTotal.setText(formatter.format(total));
    }

    private void bindTotalToAccounts() {
        for (BankAccount account : villagerBank) {
            account.balanceProperty().addListener((obs, oldVal, newVal) -> updateTotal());
        }

        // Also handle accounts added later
        villagerBank.addListener((ListChangeListener<BankAccount>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (BankAccount acc : change.getAddedSubList()) {
                        acc.balanceProperty().addListener((obs, oldVal, newVal) -> updateTotal());
                    }
                }
            }
            updateTotal();
        });

        updateTotal(); // calculate initial total
    }

    public void logTransaction(String userName, String action, double amount, BankAccount account) {
        String logEntry = userName + " " + action + " " + String.format("%,.2f", amount)
                + " to " + account.getName();
        Platform.runLater(() -> {
            transactionLogData.add(logEntry);
            transactionLog.scrollTo(transactionLogData.size() - 1);
        });
    }

    // Starts the Bank simulation
    public void simulate(ActionEvent e) {
        System.out.println("Simulation Started");
        resetButton.setDisable(true);
        runningThreads.clear();
        System.out.println("Threads created: ");
        UserThread steveUser = new UserThread(Librarian, "Steve", this);
        UserThread creeperUser = new UserThread(Librarian, "Creeper", this);
        UserThread alexUser = new UserThread(Nitwit, "Alex", this);
        UserThread endermanUser = new UserThread(Mending, "Enderman", this);
        UserThread squidUser = new UserThread(Mending, "Squid", this);
        UserThread magmaUser = new UserThread(Nitwit, "Magma", this);

        runningThreads.addAll(Arrays.asList(
                steveUser, creeperUser, alexUser, endermanUser, squidUser, magmaUser));
        // run the threads
        runningThreads.forEach(Thread::start);

        new Thread(() -> {
            for (Thread t : runningThreads) {
                try {
                    t.join(); // wait for thread to finish
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            // Once all threads are done, re-enable Reset button on UI thread
            Platform.runLater(() -> resetButton.setDisable(false));
        }).start();
    }

    public void reset(ActionEvent e) {
        System.out.println("Reset Pressed");
        // Clear transaction log
        transactionLogData.clear();

        // Reset bank accounts to their original balances
        Librarian.setBalance(100000.00);
        Librarian.lastTransactionProperty().set("NONE");

        Nitwit.setBalance(80000.75);
        Nitwit.lastTransactionProperty().set("NONE");

        Mending.setBalance(120000.09);
        Mending.lastTransactionProperty().set("NONE");

        // Clear chart series
        librarianSeries.getData().clear();
        nitwitSeries.getData().clear();
        mendingSeries.getData().clear();

        // Reset counters
        librarianCounter = 0;
        nitwitCounter = 0;
        mendingCounter = 0;

        // Add initial points again
        librarianSeries.getData().add(new XYChart.Data<>(librarianCounter++, Librarian.getBalance()));
        nitwitSeries.getData().add(new XYChart.Data<>(nitwitCounter++, Nitwit.getBalance()));
        mendingSeries.getData().add(new XYChart.Data<>(mendingCounter++, Mending.getBalance()));

        // Reset TableView (refresh to update colors)
        table.refresh();

        // Update total
        updateTotal();
    }
}
