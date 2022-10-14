package com.mindhub.homebanking.models;

import com.mindhub.homebanking.enums.TransactionType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private TransactionType transactionType;
    private double amount, actualAccountBalance;
    private String description, connection, accountAppointed;
    private LocalDateTime creation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="account_id")
    private Account account;

    public Transaction() {};

    public Transaction(String accountAppointed, TransactionType transactionType, double amount, double actualAccountBalance, String description, LocalDateTime creation, String connection) {
        this.accountAppointed = accountAppointed;
        this.transactionType = transactionType;
        this.amount = amount;
        this.actualAccountBalance = actualAccountBalance;
        this.description = description;
        this.creation = creation;
        this.connection = connection;
    }

    public long getId() {
        return id;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getActualAccountBalance() {
        return actualAccountBalance;
    }

    public void setActualAccountBalance(double actualAccountBalance) {
        this.actualAccountBalance = actualAccountBalance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getAccountAppointed() {
        return accountAppointed;
    }

    public void setAccountAppointed(String accountAppointed) {
        this.accountAppointed = accountAppointed;
    }

    public LocalDateTime getCreation() {
        return creation;
    }

    public void setCreation(LocalDateTime creation) {
        this.creation = creation;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
