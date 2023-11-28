package com.fdm.OnlineBanking.Model;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
public class CreditCardTransaction {
	@Id
	@SequenceGenerator(name = "CardHistorySequence", sequenceName = "CardHistorySequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CardHistorySequence")
	@Column(name = "PK_card_id")
	private long transactionId;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	private CreditCard card;
	
	@Column(name = "transaction_type", nullable = false, length = 20)
	private String transactionType;
	
	@Column(name = "amount", nullable = false)
	private double amount;
	
	@Column(name = "transaction_time", nullable = false)
	private LocalDateTime transactionTime;
	
	@Column(name = "merchant", nullable = false, length = 50)
	private String merchant;

	public CreditCardTransaction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CreditCardTransaction(CreditCard card, String transactionType, double amount, LocalDateTime transactionTime,
			String merchant) {
		super();
		this.card = card;
		this.transactionType = transactionType;
		this.amount = amount;
		this.transactionTime = transactionTime;
		this.merchant = merchant;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public CreditCard getCard() {
		return card;
	}

	public void setCard(CreditCard card) {
		this.card = card;
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

	public LocalDateTime getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(LocalDateTime transactionTime) {
		this.transactionTime = transactionTime;
	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

}
