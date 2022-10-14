package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static com.mindhub.homebanking.Utils.Utils.getVin;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientController clientController;


    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.getAccounts().stream().map(AccountDTO::new).collect(toList());
    }

    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getAuthentication(Authentication authentication) {

        return new ClientDTO(clientService.getClientByEmail(authentication.getName())).getAccounts();
    };


    @PostMapping(path = "/clients/current/accounts")
    public ResponseEntity<Object> accounts(Authentication authentication) {

        Client clientAuthentication = clientService.getClientByEmail(authentication.getName());


        if (clientAuthentication.getAccounts().size() >= 3){

            return new ResponseEntity<>("Haz llegado al límite de cuentas creadas", HttpStatus.FORBIDDEN);

        } else {

            String vin = getVin();
            while (true) {
                if (accountService.getAccountByNumber(vin) == null) {
                    break;
                } else {
                    vin = getVin();
                }
            }

            Account newAccount = new Account(vin, 0, LocalDate.now());

            clientAuthentication.addAccount(newAccount);

            clientService.saveClient(clientAuthentication);
            accountService.saveAccount(newAccount);

            return new ResponseEntity<>(HttpStatus.CREATED);

        }
    }
}