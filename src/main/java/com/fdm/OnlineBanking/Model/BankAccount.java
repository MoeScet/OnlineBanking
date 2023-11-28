package com.fdm.OnlineBanking.Model;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.Size;

@Entity
public class BankAccount {
	@Id
	@SequenceGenerator(name="ug2", sequenceName="us2",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ug2")
	private Long accountId;
	
	private Long bankAccNo;
		
	@OneToOne(mappedBy = "bankAccount",cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinColumn(name = "userId", referencedColumnName = "userId")
	private User user;
	
	@OneToMany(mappedBy="bankAccount", fetch=FetchType.EAGER)
	private List<BankAccountTransaction> bankAccountTransactions = new ArrayList<BankAccountTransaction>();
	
	private double balance;
	
	public void addBalance(double add) {
		this.balance = this.balance + add;
	}
	
	public void minusBalance(double minus) {
		this.balance = this.balance - minus;
	}
	

	public BankAccount(User user) {
		super();
		this.user = user;
	}

	public BankAccount() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getAccountId() {
		return accountId;
	}
	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public double getBalance() {
		return this.balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public Long getBankAccNo() {
		return this.bankAccNo;
	}

	public void setBankAccNo(Long bankAccNo) {
		this.bankAccNo = bankAccNo;
	}
    
}
