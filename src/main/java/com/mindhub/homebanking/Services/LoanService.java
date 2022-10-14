package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {

    List<Loan> getLoans();

    Loan getLoanById(Long id);

    void saveLoan(Loan loan);

}
