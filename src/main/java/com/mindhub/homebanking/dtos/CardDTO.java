package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.enums.CardType;
import com.mindhub.homebanking.enums.Color;
import com.mindhub.homebanking.models.Card;

import java.time.LocalDate;

public class CardDTO {

    private long id;
    private String cardHolder, number;
    private int cvv;
    private CardType type;
    private Color color;
    private LocalDate fromDate, thruDate;

    public CardDTO(Card card){
        this.id = card.getId();
        this.cardHolder = card.getCardHolder();
        this.type = card.getType();
        this.color = card.getColor();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
    }

    public long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getNumber() {
        return number;
    }

    public int getCvv() {
        return cvv;
    }

    public CardType getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

}
