package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.Services.TransactionService;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.enums.TransactionType;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;


    @GetMapping("/transactions")
    public List<TransactionDTO> getTransactions() {
        return transactionService.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toList());
    }


    @Transactional
    @PostMapping(path = "/transactions")
    public ResponseEntity<Object> transactions(

            @RequestParam double mount, @RequestParam String description, @RequestParam String numberAccountOrigin,

            @RequestParam String numberAccountDestiny, Authentication authentication) {


            Client clientAuthentication = clientService.getClientByEmail(authentication.getName());
            Account accountOrigin = accountService.getAccountByNumber(numberAccountOrigin);
            Account accountDestiny = accountService.getAccountByNumber(numberAccountDestiny);


            if (mount <= 0 || description.isEmpty() || numberAccountOrigin.isEmpty() || numberAccountDestiny.isEmpty()) {

                return new ResponseEntity<>("Falta completar el formulario", HttpStatus.FORBIDDEN);

            }

            else if (accountOrigin == null) {

                return new ResponseEntity<>("La cuenta de la que estas intentando sacar dinero no existe", HttpStatus.FORBIDDEN);

            }

            else if (accountDestiny == null) {

                return new ResponseEntity<>("La cuenta de la que estas intentando enviar dinero no existe", HttpStatus.FORBIDDEN);

            }

            else if (numberAccountOrigin.equals(numberAccountDestiny)) {

                return new ResponseEntity<>("No puedes enviar dinero a la misma cuenta que lo esta mandando", HttpStatus.FORBIDDEN);

            }

            else if (!clientAuthentication.getAccounts().contains(accountOrigin)) {

                return new ResponseEntity<>("La cuenta de la que estas intentando sacar dinero no te pertenece", HttpStatus.FORBIDDEN);

            }

            else if (accountOrigin.getBalance() < mount){

                return new ResponseEntity<>("La cuenta de la que estas intentando sacar dinero no tiene el saldo suficiente para realizar la transacción", HttpStatus.FORBIDDEN);

            } else {

                Transaction transactionOf = new Transaction(accountOrigin.getNumber(), TransactionType.DEBIT, -mount, accountOrigin.getBalance() - mount, description, LocalDateTime.now(), accountDestiny.getNumber());
                accountOrigin.addTransaction(transactionOf);
                transactionService.saveTransaction(transactionOf);

                Transaction transactionFor = new Transaction(accountDestiny.getNumber(), TransactionType.CREDIT, mount, accountDestiny.getBalance() + mount, description, LocalDateTime.now(), accountOrigin.getNumber());
                accountDestiny.addTransaction(transactionFor);
                transactionService.saveTransaction(transactionFor);

                accountOrigin.setBalance(accountOrigin.getBalance() - mount);
                accountService.saveAccount(accountOrigin);
                accountDestiny.setBalance(accountDestiny.getBalance() + mount);
                accountService.saveAccount(accountDestiny);

                return new ResponseEntity<>(HttpStatus.CREATED);

            }
    }
}
