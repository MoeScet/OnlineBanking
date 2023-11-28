package com.fdm.OnlineBanking.Model;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Transient;

@Entity
public class BankAccountTransaction {
	@Id
	@SequenceGenerator(name="ug3", sequenceName="us3",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ug3")
	private long transactionId;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="FK_accountId")
	private BankAccount bankAccount;
	private String transactionType;
	private double amount;
	private LocalDateTime dateTime;
	private long receipientId;
	
	@Transient
	public String recipientIdString;
	
	public void setRIDS(long recipientId) {
		if (recipientId == 0) {
			this.recipientIdString = "NOT A TRANSFER";
		} else {
			this.recipientIdString = Long.toString(recipientId);
		}
	}
	
	@Transient
	public String senderIdString;
	
	public void setSIDS(long bankAccountId, String transactionType) {
		if (!transactionType.equals("Transfer")) {
			this.senderIdString = "NOT A TRANSFER";
		} else {
			this.senderIdString = Long.toString(bankAccountId);
		}
	}
	
	
	public BankAccountTransaction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BankAccountTransaction(BankAccount bankAccount, long receipientId, String transactionType, double amount,
			LocalDateTime dateTime) {
		super();
		
		this.bankAccount = bankAccount;
		this.transactionType = transactionType;
		this.amount = amount;
		this.dateTime = dateTime;
		this.receipientId = receipientId;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public long getReceipientId() {
		return receipientId;
	}

	public void setReceipientId(long receipientId) {
		this.receipientId = receipientId;
	}	
}

