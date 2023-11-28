package com.fdm.OnlineBanking.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdm.OnlineBanking.Model.BankAccount;
import com.fdm.OnlineBanking.Model.User;
import com.fdm.OnlineBanking.Repository.BankAccountRepository;


@Service
public class BankAccountService {
	
	@Autowired
	private BankAccountRepository bankAccountRepo;
	
	public BankAccount findBankAccount(Long accountId) {
		return bankAccountRepo.findById(accountId).get();
	}
	
	public List<BankAccount> findAllBankAccounts() {
		return bankAccountRepo.findAll();		
	}
	
	public void saveAccount(BankAccount bankAccount) {
		// TODO Auto-generated method stub
		bankAccountRepo.save(bankAccount);
	}
	
	public Optional<BankAccount> findAccountTransfer(Long accountId){
		return bankAccountRepo.findById(accountId);
		
	}

	public Optional<BankAccount> findByBankAccNo(long bankAccNo) {
		return bankAccountRepo.findByBankAccNo(bankAccNo);
	}
	
}
