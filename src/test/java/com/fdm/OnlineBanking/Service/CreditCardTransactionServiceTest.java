package com.fdm.OnlineBanking.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdm.OnlineBanking.Model.CreditCard;
import com.fdm.OnlineBanking.Model.CreditCardTransaction;
import com.fdm.OnlineBanking.Model.User;
import com.fdm.OnlineBanking.Repository.CreditCardTransactionRepository;

@ExtendWith(MockitoExtension.class)
class CreditCardTransactionServiceTest {

	@InjectMocks CreditCardTransactionService creditCardTransactionService;
	@Mock CreditCardTransactionRepository creditCardTransactionRepo;
	User user;
	CreditCard creditCard;
	
	@BeforeEach 
	void setup() {
		user = new User();
		creditCard = new CreditCard();
	}
	
	@Test
	void saveTransaction_exceededCreditLimitTest() {
		user.setAvailableCreditLimit(100);
		creditCard.setUser(user);
		CreditCardTransaction transaction = new CreditCardTransaction(creditCard, "purchase", 100.1, LocalDateTime.now(), "merchant");
		
		boolean result = creditCardTransactionService.saveTransaction(transaction);
		
		assertFalse(result);
	}

	
	@Test
	void saveTransaction_notExceedCreditLimitTest() {
		user.setAvailableCreditLimit(100);
		creditCard.setUser(user);
		CreditCardTransaction transaction = new CreditCardTransaction(creditCard, "purchase", 99.9, LocalDateTime.now(), "merchant");
		
		boolean result = creditCardTransactionService.saveTransaction(transaction);
		
		verify(creditCardTransactionRepo, times(1)).save(transaction);
		assertTrue(result);
	}
}
















