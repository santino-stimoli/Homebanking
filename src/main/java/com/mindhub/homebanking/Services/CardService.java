package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.models.Card;

import java.util.List;

public interface CardService {

    List<Card> getCards();

    Card getCardByNumber(String number);

    void saveCard(Card card);

}
