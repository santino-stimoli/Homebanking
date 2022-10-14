package com.mindhub.homebanking.Utils;

import com.mindhub.homebanking.enums.CardType;
import com.mindhub.homebanking.models.Client;

public final class Utils {

    public static String getNumberDebitCredit(CardType cardType) {

        if (cardType.toString().equals("CREDIT")){
            return "4531";
        } else {
            return "4518";
        }

    }

    public static String getFourNumbers() {
        int number = (int) ((Math.random() * (9999 - 1)) + 1);
        return String.format("%04d",number);
    }

    // Externas

    public static String getVin() {
        int number = (int) ((Math.random() * (99999999 - 10000)) + 10000);
        return "VIN" + String.format("%08d",number);
    }

    public static int getCvvNumber() {
        return  (int) (Math.random() * (999 - 100)) + 100;
    }

    public static String getCardNumber(CardType cardType){
        return getNumberDebitCredit(cardType) + " " + getFourNumbers() + " " + getFourNumbers() + " " + getFourNumbers();
    }

    public static String getCardHolder(Client client){
        return client.getName().split(" ")[0] + " " + client.getLastName().split(" ")[0];
    }

}
