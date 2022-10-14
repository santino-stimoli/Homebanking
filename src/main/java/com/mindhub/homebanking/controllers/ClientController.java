package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.enums.Sex;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.mindhub.homebanking.Utils.Utils.getVin;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {


    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientService.getClients().stream().map(ClientDTO::new).collect(toList());
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClientById(@PathVariable Long id){
        return new ClientDTO(clientService.getClientById(id));
    }

    @GetMapping("/clients/current")
    public ClientDTO getAuthentication(Authentication authentication){
        return new ClientDTO(clientService.getClientByEmail(authentication.getName()));
    };


    @PostMapping(path = "/clients")
    public ResponseEntity<Object> register(

            @RequestParam String name, @RequestParam String lastName, @RequestParam String email,

            @RequestParam Sex sex, @RequestParam int age, @RequestParam String password,

            @RequestParam String passwordConfirm, Authentication authentication) {


        if (name.isEmpty() || lastName.isEmpty() || sex.toString().isEmpty() || age <= 0 || email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {

            return new ResponseEntity<>("Falta completar el formulario", HttpStatus.FORBIDDEN);

        } else if (clientService.getClientByEmail(authentication.getName()) != null) {

            return new ResponseEntity<>("El mail escrito ya tiene una cuenta en Pegasus, prueba con otro mail o iniciando sesión con ese mail", HttpStatus.FORBIDDEN);

        } else if (email.contains("@admin")) {

            return new ResponseEntity<>(age + "No puedes crear un mail con ese dominio", HttpStatus.FORBIDDEN);

        } else if (age < 18){

            return new ResponseEntity<>("No puedes crear una cuenta de Pegasus siendo menor de edad", HttpStatus.FORBIDDEN);

        } else if (!password.equals(passwordConfirm)) {

            return new ResponseEntity<>("Las contraseñas no coinciden", HttpStatus.FORBIDDEN);

        } else if (password.length() < 8){

            return new ResponseEntity<>("Las contraseñas deben tener al menos 8 caracteres", HttpStatus.FORBIDDEN);

        } else {

            Client clientAuthentication = new Client(name, lastName, sex, age, email, passwordEncoder.encode(password));
            Account newAccount = new Account(getVin(), 0, LocalDate.now());

            clientAuthentication.addAccount(newAccount);

            clientService.saveClient(clientAuthentication);
            accountService.saveAccount(newAccount);

            return new ResponseEntity<>(HttpStatus.CREATED);

        }
    }
}

