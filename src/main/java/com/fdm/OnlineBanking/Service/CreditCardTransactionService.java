package com.fdm.OnlineBanking.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdm.OnlineBanking.Model.BankAccount;
import com.fdm.OnlineBanking.Model.BankAccountTransaction;
import com.fdm.OnlineBanking.Model.CreditCard;
import com.fdm.OnlineBanking.Model.CreditCardTransaction;
import com.fdm.OnlineBanking.Model.User;
import com.fdm.OnlineBanking.Repository.CreditCardTransactionRepository;

@Service
public class CreditCardTransactionService {
	
	@Autowired
	private CreditCardTransactionRepository creditCardTransactionRepo;
	
	public List<CreditCardTransaction> findAllCreditCardTransactions(Long creditCardId) {
		List<CreditCardTransaction>  allCCTransaction = creditCardTransactionRepo.findAll();
		List<CreditCardTransaction> CCTransactionWithGivenCCName = new ArrayList<> ();
		for (CreditCardTransaction cct : allCCTransaction) {
			if (cct.getCard().getCardId() == creditCardId) CCTransactionWithGivenCCName.add(cct);
		}
		
		return CCTransactionWithGivenCCName.stream().filter(t -> t.getTransactionTime().isBefore(LocalDateTime.now())).collect(Collectors.toList());
	}
	
	public CreditCardTransaction findCreditCardTransaction(Long transactionId) {
		return creditCardTransactionRepo.findById(transactionId).get();
	}
	
	public void addEntry(CreditCard creditCard, LocalDateTime transactionDate, String payableOrPayment, String merchantName, double transactionAmount) {
		CreditCardTransaction newEntry = new CreditCardTransaction();
		newEntry.setCard(creditCard);
		newEntry.setTransactionTime(transactionDate);
		newEntry.setTransactionType(payableOrPayment);
		newEntry.setMerchant(merchantName);
		newEntry.setAmount(transactionAmount);
		creditCardTransactionRepo.save(newEntry);
	}

	public List<String> findAllMonths(List<CreditCardTransaction> creditCardTransaction) {
		List<String> monthsToFilter = new ArrayList<> ();
		
		if (creditCardTransaction.size() == 0) return monthsToFilter;
		
		Comparator<CreditCardTransaction> sortTransactionByTime = (t1, t2) -> t1.getTransactionTime().compareTo(t2.getTransactionTime());
		Collections.sort(creditCardTransaction, sortTransactionByTime);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-yyyy", Locale.ENGLISH);
		String beginningDate = (creditCardTransaction.get(0).getTransactionTime().toLocalDate()).format(formatter);
		String endDate = (creditCardTransaction.get(creditCardTransaction.size() - 1).getTransactionTime().toLocalDate()).format(formatter);
		
	    YearMonth startDateYM = YearMonth.parse(beginningDate, formatter);
	    YearMonth endDateYM = YearMonth.parse(endDate, formatter);

	    while(!startDateYM.isAfter(endDateYM)) {
	    	monthsToFilter.add(startDateYM.format(formatter).toString());
	    	startDateYM = startDateYM.plusMonths(1);
	    }
	    
		return monthsToFilter;
	}

	public List<CreditCardTransaction> getTransactionsOnThisMonthYear(String monthYearName, List<CreditCardTransaction> allCCTransactions) {
		List<CreditCardTransaction> CCTransactionsOnThisMonthYear = new ArrayList<> ();
		
		Comparator<CreditCardTransaction> sortTransactionByTime = (t1, t2) -> t2.getTransactionTime().compareTo(t1.getTransactionTime());
		Collections.sort(allCCTransactions, sortTransactionByTime);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-yyyy", Locale.ENGLISH);
		
		for (CreditCardTransaction transaction : allCCTransactions) {
			if ((transaction.getTransactionTime().toLocalDate().format(formatter)).equals(monthYearName)) {
				CCTransactionsOnThisMonthYear.add(transaction);
			}
		}
		return CCTransactionsOnThisMonthYear;
		
	}
	
	// to add new transaction
	public boolean saveTransaction(CreditCardTransaction transaction) {
		double availableCreditLimit = transaction.getCard().getUser().getAvailableCreditLimit();
		double amount = transaction.getAmount();
		double outstandingBalance = transaction.getCard().getOutstandingBalance();
		
		// to check if the amount does not exceed credit limit
		if (availableCreditLimit >= amount) {
			double newCreditLimit = availableCreditLimit - amount;
			outstandingBalance += amount;
			
			transaction.getCard().getUser().setAvailableCreditLimit(newCreditLimit);
			transaction.getCard().setOutstandingBalance(outstandingBalance);
			
			creditCardTransactionRepo.save(transaction);
			return true;
		}
		else {
			return false;
		}
	}
	
	@Autowired
	CreditCardService creditCardService;
	
	public void updateUserAndCardAvailableLimits(CreditCard creditCardSelected, User currentUser, double amountSpent) {
		
		double currentAvailableCredit = creditCardSelected.getAvailableCredit();
		double updatedAvailableCredit = currentAvailableCredit - amountSpent;
		
		currentUser.setAvailableCreditLimit(updatedAvailableCredit);
		
		List<CreditCard> creditCardsUnderUser = creditCardService.findAllCreditCardsByUser(currentUser);
		for (CreditCard card: creditCardsUnderUser ) {
			card.setAvailableCredit(updatedAvailableCredit);
		}
		
		double currentOutstandingBalance = creditCardSelected.getOutstandingBalance();
		double updatedOutstandingBalance = currentOutstandingBalance + amountSpent;
		creditCardSelected.setOutstandingBalance(updatedOutstandingBalance);
		
	}
}
