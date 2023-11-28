package com.fdm.OnlineBanking.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdm.OnlineBanking.Model.BankAccount;
import com.fdm.OnlineBanking.Model.BankAccountTransaction;
import com.fdm.OnlineBanking.Model.User;
import com.fdm.OnlineBanking.Repository.BankAccountTransactionRepository;


@Service
public class BankAccountTransactionService {
	
	@Autowired
	private BankAccountTransactionRepository bankAccountTransactionRepo;
	
	public List<BankAccountTransaction> findAllBankAccountTransactions() {
		return bankAccountTransactionRepo.findAll().stream().filter(t -> t.getDateTime().isBefore(LocalDateTime.now())).collect(Collectors.toList());		
	}
	
	public List<BankAccountTransaction> findBankAccountTransactionsByUserId(Long userId) {
		return bankAccountTransactionRepo.findByReceipientId(userId).stream().filter(t -> t.getDateTime().isBefore(LocalDateTime.now())).collect(Collectors.toList());		
	}
	
	public BankAccountTransaction findBankAccountTransaction(Long transactionId) {
		return bankAccountTransactionRepo.findById(transactionId).get();
	}
	
	public void addEntry(BankAccount bankAccount, long recipientId, LocalDateTime transactionDate, String depositOrWithdraw, double transactionAmount) {
		BankAccountTransaction newEntry = new BankAccountTransaction();
		newEntry.setBankAccount(bankAccount);
		newEntry.setReceipientId(recipientId);
		newEntry.setDateTime(transactionDate);
		newEntry.setTransactionType(depositOrWithdraw);
		newEntry.setAmount(transactionAmount);
		bankAccountTransactionRepo.save(newEntry);
	}

	public List<String> findAllMonths(List<BankAccountTransaction> bankAccountTransaction) {
		
		List<String> monthsToFilter = new ArrayList<> ();
		if (bankAccountTransaction.size() == 0) return monthsToFilter;
		
		Comparator<BankAccountTransaction> sortTransactionByTime = (t1, t2) -> t1.getDateTime().compareTo(t2.getDateTime());
		Collections.sort(bankAccountTransaction, sortTransactionByTime);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-yyyy", Locale.ENGLISH);
		String beginningDate = (bankAccountTransaction.get(0).getDateTime().toLocalDate()).format(formatter);
		String endDate = (bankAccountTransaction.get(bankAccountTransaction.size() - 1).getDateTime().toLocalDate()).format(formatter);
		
	    YearMonth startDateYM = YearMonth.parse(beginningDate, formatter);
	    YearMonth endDateYM = YearMonth.parse(endDate, formatter);

	    while(!startDateYM.isAfter(endDateYM)) {
	    	monthsToFilter.add(startDateYM.format(formatter).toString());
	    	startDateYM = startDateYM.plusMonths(1);
	    }
	    
		return monthsToFilter;
	}

	public List<BankAccountTransaction> getTransactionsOnThisMonthYear(String monthYearName) {
		
		List<BankAccountTransaction> BATransactionsOnThisMonthYear = new ArrayList<> ();
		List<BankAccountTransaction> allBATransactions = this.findAllBankAccountTransactions();
		
		Comparator<BankAccountTransaction> sortTransactionByTime = (t1, t2) -> t2.getDateTime().compareTo(t1.getDateTime());
		Collections.sort(allBATransactions, sortTransactionByTime);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-yyyy", Locale.ENGLISH);
		
		for (BankAccountTransaction transaction : allBATransactions) {
			if ((transaction.getDateTime().toLocalDate().format(formatter)).equals(monthYearName)) {
				BATransactionsOnThisMonthYear.add(transaction);
			}
		}
		return BATransactionsOnThisMonthYear;
	}
}
