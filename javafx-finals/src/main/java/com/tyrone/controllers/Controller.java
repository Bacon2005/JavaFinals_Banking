package com.tyrone.controllers;

import com.tyrone.classes.BankAccount;
import com.tyrone.threads.UserThread;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

public class Controller {

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

    BankAccount Librarian = new BankAccount(01, "Librarian Villager Number 5", 200.99);
        BankAccount Nitwit = new BankAccount(02, "Nitwit Villager Number 1", 122.15);
        BankAccount Mending = new BankAccount(03, "Mending Villager", 519.18);

    //Initialize and show the table data
    public void initialize(){
        bankNumberColumn.setCellValueFactory(new PropertyValueFactory<>("bankNumber"));
        bankNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        bankAmountColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));

        ObservableList<BankAccount> villagerBank = FXCollections.observableArrayList(
        Librarian, Nitwit, Mending
        );


        table.setItems(villagerBank);

        double total = 0;
        for (BankAccount account : table.getItems()){
            total += account.getBalance();
        }
        //format the amount to only show 2 decimal places 
        String text = String.format("%.2f",total);
        bankTotal.setText(text);
    }

    public double getTotalAmount(String currentTotal){ 
        //gets the string of the total value then parse to double value
        double total = Double.parseDouble(currentTotal);
        return total;
    }

    public void updateTotal(double currentTotal){
        String text = String.format("%.2f",currentTotal);
        bankTotal.setText(text);
    }
    
    public void simulate(ActionEvent e){
        System.out.println("Simulation Started");
        UserThread steveUser = new UserThread(Librarian);
    }
}
