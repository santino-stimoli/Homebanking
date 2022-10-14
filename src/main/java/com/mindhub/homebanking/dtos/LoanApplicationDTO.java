package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Loan;

public class LoanApplicationDTO {

    private long id;
    private double amount;
    private Integer payments;
    private String numberAccountDeposit;

    public LoanApplicationDTO() {};

    public long getId() {
        return id;
    }
    public double getAmount() {
        return amount;
    }
    public Integer getPayments() {
        return payments;
    }

    public String getNumberAccountDeposit() {
        return numberAccountDeposit;
    }

}
