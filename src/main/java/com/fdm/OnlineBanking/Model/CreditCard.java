package com.fdm.OnlineBanking.Model;
import java.time.LocalDate;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fdm.OnlineBanking.Enum.CreditCardName;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
public class CreditCard {
	
	@Id
	@SequenceGenerator(name = "CreditCardSequence", sequenceName = "CreditCardSequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CreditCardSequence")
	@Column(name = "PK_card_id")
	private long CardId;
	
	@Column(name = "card_name", nullable = false)
	private CreditCardName cardName;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	private User user;
	
	@Column(name = "outstanding_balance", nullable = false)
	private double outstandingBalance;
	
	@Column(name = "available_credit", nullable = false)
	private double availableCredit;
	
	@Column(name = "bill_due_date", nullable = false)
	private LocalDate paymentDate;
	
	@Column(name = "current_amount_payable")
	private double currentAmountPayable;

	public long getCardId() {
		return CardId;
	}

	public void setCardId(long cardId) {
		CardId = cardId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public CreditCardName getCardName() {
		return cardName;
	}

	public void setCardName(CreditCardName cardName) {
		cardName = cardName;
	}

	public double getOutstandingBalance() {
		return outstandingBalance;
	}

	public void setOutstandingBalance(double outstandingBalance) {
		this.outstandingBalance = outstandingBalance;
	}

	public double getAvailableCredit() {
		return availableCredit;
	}

	public void setAvailableCredit(double availableCredit) {
		this.availableCredit = availableCredit;
	}

	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}
	
	public CreditCard() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CreditCard(User user, CreditCardName CardName, double outstandingBalance, double availableCredit, LocalDate paymentDate) {
		super();
		this.user = user;
		this.cardName = CardName;
		this.outstandingBalance = outstandingBalance;
		this.availableCredit = availableCredit;
		this.paymentDate = paymentDate;
		this.currentAmountPayable = 0;
	}

	public double getCurrentAmountPayable() {
		return currentAmountPayable;
	}

	public void setCurrentAmountPayable(double currentAmountPayable) {
		this.currentAmountPayable = currentAmountPayable;
	}
	
}
