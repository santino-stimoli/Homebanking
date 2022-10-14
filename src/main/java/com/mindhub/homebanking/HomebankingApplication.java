package com.mindhub.homebanking;

import com.mindhub.homebanking.controllers.ClientController;
import com.mindhub.homebanking.enums.CardType;
import com.mindhub.homebanking.enums.Color;
import com.mindhub.homebanking.enums.Sex;
import com.mindhub.homebanking.enums.TransactionType;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static com.mindhub.homebanking.Utils.Utils.*;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ClientController clientController;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, CardRepository cardRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository) {

		return (args) -> {
			// F U N C I O N A L I D A D E S
			// Fechas
			LocalDateTime fechaHoraActualAyer = LocalDateTime.now().minusDays(1);
			LocalDateTime fechaHoraActual = LocalDateTime.now();
			LocalDate fechaActual = LocalDate.now();

			// Tipos de tarjeta
			CardType cardCredit = CardType.CREDIT;
			CardType cardDebit = CardType.DEBIT;

			// Color de tarjeta
			Color silver = Color.SILVER;
			Color gold = Color.GOLD;
			Color titanium = Color.TITANIUM;

			// Tipos de transacciones
			TransactionType transactionCredit = TransactionType.CREDIT;
			TransactionType transactionDebit = TransactionType.DEBIT;

			// Préstamos
			Loan loanTest1 = new Loan("Hipotecario", 500000, Arrays.asList(12,24,36,48,60));
			Loan loanTest2 = new Loan("Personal", 100000, Arrays.asList(6,12,24));
			Loan loanTest3 = new Loan("Automotriz", 300000, Arrays.asList(6,12,24,36));

			loanRepository.save(loanTest1);
			loanRepository.save(loanTest2);
			loanRepository.save(loanTest3);


			// A D M I N
			Client admin = new Client("Admin","Admin", Sex.NONE,0,"admin@admin.com", passwordEncoder.encode("123"));

			// GUARDAR
			// Cliente
			clientRepository.save(admin);

			// C L I E N T E   D E   P R U E B A   1

			// CREAR
			// Cliente
			Client clientTest1 = new Client("Melba","Lorenzo", Sex.FEMALE,52,"melbalorenzo@mindhub.com", passwordEncoder.encode("123"));

			// Cuentas
			Account client1account1 = new Account("VIN01",0, fechaActual);
			Account client1account2 = new Account("VIN02",0, fechaActual);

			// Tarjetas
			Card client1card1 = new Card(getCardHolder(clientTest1), cardCredit, silver, getCardNumber(cardCredit), getCvvNumber(), fechaActual, fechaActual.plusYears(5));
			Card client1card2 = new Card(getCardHolder(clientTest1), cardDebit, gold, getCardNumber(cardDebit), getCvvNumber(), fechaActual, fechaActual.plusYears(5));

			// Préstamos
			ClientLoan client1clientLoan1 = new ClientLoan(100000, 36, clientTest1, loanTest1);
			ClientLoan client1clientLoan2 = new ClientLoan(30000, 6, clientTest1, loanTest2);

			// Transacciones
			Transaction client1transaction1 = new Transaction(client1account1.getNumber(), transactionCredit, client1clientLoan1.getAmount(), client1account1.getBalance() + client1clientLoan1.getAmount(), client1clientLoan1.getLoan().getName(), fechaHoraActualAyer, "Pegasus");
			client1account1.setBalance(client1account1.getBalance() + client1transaction1.getAmount());

			Transaction client1transaction2 = new Transaction(client1account1.getNumber(), transactionCredit, 50000, client1account1.getBalance() + 50000, "Sueldo", fechaHoraActual, "Pegasus");
			client1account1.setBalance(client1account1.getBalance() + client1transaction2.getAmount());

			Transaction client1transaction3 = new Transaction(client1account1.getNumber(), transactionDebit, -5000, client1account1.getBalance() - 5000, "Comida", fechaHoraActual, "Pegasus");
			client1account1.setBalance(client1account1.getBalance() + client1transaction3.getAmount());

			Transaction client1transaction4 = new Transaction(client1account2.getNumber(), transactionCredit, client1clientLoan2.getAmount(), client1account2.getBalance() + client1clientLoan2.getAmount(), client1clientLoan2.getLoan().getName(), fechaHoraActualAyer, "Pegasus");
			client1account2.setBalance(client1account2.getBalance() + client1transaction4.getAmount());

			Transaction client1transaction5 = new Transaction(client1account2.getNumber(), transactionDebit, -10000, client1account2.getBalance() - 10000, "Regalo", fechaHoraActual, "Pegasus");
			client1account2.setBalance(client1account2.getBalance() + client1transaction5.getAmount());

			Transaction client1transaction6 = new Transaction(client1account2.getNumber(), transactionDebit, -15000, client1account2.getBalance() - 15000, "Álbum", fechaHoraActual, "Pegasus");
			client1account2.setBalance(client1account2.getBalance() + client1transaction6.getAmount());


			// RELACIONES
			// Cuentas
			clientTest1.addAccount(client1account1);
			clientTest1.addAccount(client1account2);

			// Tarjetas
			clientTest1.addCard(client1card1);
			clientTest1.addCard(client1card2);

			// Transacciones
			client1account1.addTransaction((client1transaction1));
			client1account1.addTransaction((client1transaction2));
			client1account1.addTransaction((client1transaction3));
			client1account2.addTransaction((client1transaction4));
			client1account2.addTransaction((client1transaction5));
			client1account2.addTransaction((client1transaction6));


			// GUARDAR
			// Cliente
			clientRepository.save(clientTest1);

			// Cuentas
			accountRepository.save(client1account1);
			accountRepository.save(client1account2);

			// Tarjetas
			cardRepository.save(client1card1);
			cardRepository.save(client1card2);

			// Préstamos
			clientLoanRepository.save(client1clientLoan1);
			clientLoanRepository.save(client1clientLoan2);

			// Transacciones
			transactionRepository.save(client1transaction1);
			transactionRepository.save(client1transaction2);
			transactionRepository.save(client1transaction3);
			transactionRepository.save(client1transaction4);
			transactionRepository.save(client1transaction5);
			transactionRepository.save(client1transaction6);

		};
	}
}
