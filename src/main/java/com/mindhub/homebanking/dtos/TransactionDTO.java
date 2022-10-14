package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.enums.TransactionType;

import java.time.LocalDateTime;

public class TransactionDTO {
    private long id;
    private TransactionType transactionType;
    private double amount, actualAccountBalance;
    private String description, connection, accountAppointed;
    private LocalDateTime creation;

    public TransactionDTO(Transaction transaction) {
        this.accountAppointed =transaction.getAccountAppointed();
        this.id = transaction.getId();
        this.transactionType = transaction.getTransactionType();
        this.amount = transaction.getAmount();
        this.actualAccountBalance = transaction.getActualAccountBalance();
        this.description = transaction.getDescription();
        this.creation = transaction.getCreation();
        this.connection = transaction.getConnection();
    }

    public long getId() {
        return id;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public double getActualAccountBalance() {
        return actualAccountBalance;
    }

    public String getDescription() {
        return description;
    }

    public String getConnection() {
        return connection;
    }

    public String getAccountAppointed() {
        return accountAppointed;
    }

    public LocalDateTime getCreation() {
        return creation;
    }
}