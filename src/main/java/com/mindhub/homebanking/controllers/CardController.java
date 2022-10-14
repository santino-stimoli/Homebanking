package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.CardService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.enums.CardType;
import com.mindhub.homebanking.enums.Color;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.Utils.Utils.*;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientController clientController;


    @GetMapping("/cards")
    public List<CardDTO> getCards(){
        return cardService.getCards().stream().map(CardDTO::new).collect(toList());
    }

    @GetMapping("/clients/current/cards")
    public Set<CardDTO> getAuthentication(Authentication authentication) {
        return new ClientDTO(clientService.getClientByEmail(authentication.getName())).getCards();
    };


    @PostMapping(path = "/clients/current/cards")
    public ResponseEntity<Object> cards(@RequestParam CardType cardType, @RequestParam Color color, Authentication authentication) {

        Client clientAuthentication = clientService.getClientByEmail(authentication.getName());


        if (clientAuthentication.getCards().stream().filter(card -> card.getColor().equals(color)).collect(Collectors.toSet()).size() >= 2) {

            return new ResponseEntity<>("Haz llegado al limite de tarjetas de tipo " + cardType.toString().toLowerCase() + "o en tu cuenta de Pegasus", HttpStatus.FORBIDDEN);

        } else if (clientAuthentication.getCards().stream().filter(card -> card.getType().equals(cardType)).collect(Collectors.toSet()).size() >= 3) {

            return new ResponseEntity<>("Haz llegado al limite de tarjetas " + color.toString().toLowerCase() + " en tu cuenta de Pegasus", HttpStatus.FORBIDDEN);

        } else if (clientAuthentication.getCards().stream().filter(card -> card.getType().equals(cardType) && card.getColor().equals(color)).collect(Collectors.toSet()).size() == 1) {

            return new ResponseEntity<>("Ya tienes una tarjeta " + color.toString().toLowerCase() + " de " + cardType.toString().toLowerCase() + "o en tu cuenta", HttpStatus.FORBIDDEN);

        } else  {

                String number = getCardNumber(cardType);
                while (true) {
                    if (cardService.getCardByNumber(number) == null) {
                        break;
                    } else {
                        number = getCardNumber(cardType);
                    }
                }

                Card newCard = new Card(getCardHolder(clientAuthentication), cardType, color, number, getCvvNumber(), LocalDate.now(), LocalDate.now().plusYears(5));

                clientAuthentication.addCard(newCard);

                cardService.saveCard(newCard);
                clientService.saveClient(clientAuthentication);

            return new ResponseEntity<>(HttpStatus.CREATED);

        }
    }
}
