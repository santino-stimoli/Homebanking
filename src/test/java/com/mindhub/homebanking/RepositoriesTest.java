package com.mindhub.homebanking;

import com.mindhub.homebanking.enums.CardType;
import com.mindhub.homebanking.enums.TransactionType;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

// @DataJpaTest // Solo Postgres, orientado a JPA
@SpringBootTest // Solo H2, en general
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTest {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    TransactionRepository transactionRepository;


    // T E S T S
    // Accounts
    @Test
    public void existAccounts(){

        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, is(not(empty())));

    }

    @Test
    public void existCreationAccounts(){

        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, hasItem(hasProperty("creation", is(LocalDate.now()))));

    }

    // Cards
    @Test
    public void existCards(){

        List<Card> cards = cardRepository.findAll();
        assertThat(cards, is(not(empty())));

    }

    @Test
    public void existCardTypeCredit(){

        List<Card> cards = cardRepository.findAll();
        assertThat(cards, hasItem(hasProperty("type", is(CardType.CREDIT))));

    }

    // Clients
    @Test
    public void existClients(){

        List<Client> clients = clientRepository.findAll();
        assertThat(clients, is(not(empty())));

    }

    @Test
    public void existAdminClient(){

        List<Client> clients = clientRepository.findAll();
        assertThat(clients, hasItem(hasProperty("email", is("admin@admin.com"))));

    }

    // Loans
    @Test
    public void existLoans(){

        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, is(not(empty())));

    }

    @Test
    public void existHipotecarioLoan(){

        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Hipotecario"))));

    }

    // Transactions
    @Test
    public void existTransactions(){

        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, is(not(empty())));

    }

    @Test
    public void existTransactionTypeCredit(){

        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, hasItem(hasProperty("transactionType", is(TransactionType.CREDIT))));

    }

}
