package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.models.Account;

import java.util.List;

public interface AccountService {

    List<Account> getAccounts();

    Account getAccountByNumber(String number);

    void saveAccount(Account account);

}
