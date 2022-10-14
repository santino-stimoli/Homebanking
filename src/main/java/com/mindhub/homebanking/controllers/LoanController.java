package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.*;
import com.mindhub.homebanking.dtos.*;
import com.mindhub.homebanking.enums.TransactionType;
import com.mindhub.homebanking.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private LoanService loanService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ClientLoanService clientLoanService;


    @GetMapping("/loans")
    public List<LoanDTO> getLoans() {
        return loanService.getLoans().stream().map(LoanDTO::new).collect(toList());
    }

    @GetMapping("/clients/current/loans")
    public Set<ClientLoanDTO> getAuthentication(Authentication authentication) {
        return new ClientDTO(clientService.getClientByEmail(authentication.getName())).getLoans();
    };


    @PostMapping(path = "/loans")
    public ResponseEntity<Object> accounts(@RequestBody LoanApplicationDTO loan, Authentication authentication) {

        Client clientAuthentication = clientService.getClientByEmail(authentication.getName());
        Loan loanSelected = loanService.getLoanById(loan.getId());
        Account accountSelected = accountService.getAccountByNumber(loan.getNumberAccountDeposit());


        if (loan.getAmount() <= 0 || loan.getPayments() <= 0 || loan.getNumberAccountDeposit().isEmpty()) {

            return new ResponseEntity<>("Falta completar el formulario", HttpStatus.FORBIDDEN);

        } else if (loanSelected == null) {

            return new ResponseEntity<>("El préstamo solicitado no existe, intenta de nuevo mas tarde", HttpStatus.FORBIDDEN);

        } else if (loan.getAmount() > loanSelected.getMaxMount()) {

            return new ResponseEntity<>("EL monto solicitado esta fuera del rango del préstamo seleccionado", HttpStatus.FORBIDDEN);

        } else if (!loanSelected.getPayments().contains(loan.getPayments())) {

            return new ResponseEntity<>("La cantidad de pagos seleccionados no está dentro de las posibilidades del préstamo", HttpStatus.FORBIDDEN);

        } else if (accountSelected == null) {

            return new ResponseEntity<>("La cuenta seleccionada no existe", HttpStatus.FORBIDDEN);

        } else if (!clientAuthentication.getAccounts().contains(accountSelected)) {

            return new ResponseEntity<>("La cuenta seleccionada no le pertenece", HttpStatus.FORBIDDEN);

        } else {

            ClientLoan clientLoan = new ClientLoan(loan.getAmount() * 1.2, loan.getPayments(), clientAuthentication, loanSelected);
            clientLoanService.saveClientLoan(clientLoan);

            accountSelected.setBalance(accountSelected.getBalance() + loan.getAmount());
            Transaction transaction = new Transaction(accountSelected.getNumber(), TransactionType.CREDIT, loan.getAmount(), accountSelected.getBalance(), "Préstamo " + loanSelected.getName() , LocalDateTime.now(), "Pegasus");
            accountSelected.addTransaction(transaction);
            transactionService.saveTransaction(transaction);

            accountService.saveAccount(accountSelected);

            return new ResponseEntity<>(HttpStatus.CREATED);

        }
    }
}
