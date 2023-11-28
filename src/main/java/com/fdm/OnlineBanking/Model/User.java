package com.fdm.OnlineBanking.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fdm.OnlineBanking.Enum.AnnualIncome;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.Pattern;

@Entity
@Component
public class User {
	@Id
	@SequenceGenerator(name="ug1", sequenceName="us1",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ug1")
	private long userId;
	
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String address;
	private long postalCode;
	private String mobile;
	private String email;
	private LocalDate birthDate;
	private AnnualIncome income;
	
	@OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.PERSIST)
	@JoinColumn(name = "accountId", referencedColumnName = "accountId")
	private BankAccount bankAccount;
	
	@OneToMany(mappedBy="user", fetch=FetchType.EAGER)
	private List<CreditCard> creditCard = new ArrayList<CreditCard>();
	
	private double creditLimit;
	private double availableCreditLimit;
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	} 
	
	public User(String username, String password, String firstName, String lastName, String address, long postalCode,
			String mobile, String email, LocalDate birthDate, AnnualIncome income) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.postalCode = postalCode;
		this.mobile = mobile;
		this.email = email;
		this.birthDate = birthDate;
		this.income = income;
	}
	
	public User(String username, String password, String firstName, String lastName, String address, long postalCode,
			String mobile, String email, LocalDate birthDate, AnnualIncome income, BankAccount bankAccount,
			List<CreditCard> creditCard) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.postalCode = postalCode;
		this.mobile = mobile;
		this.email = email;
		this.birthDate = birthDate;
		this.income = income;
		this.bankAccount = bankAccount;
		this.creditCard = creditCard;
	}
	
	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return this.password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstName() {
		return this.firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return this.lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getAddress() {
		return this.address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMobile() {
		return this.mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public double getCreditLimit() {
		return creditLimit;
	}
	public void setCreditLimit(double creditLimit) {
		this.creditLimit = creditLimit;
	}
	public double getAvailableCreditLimit() {
		return availableCreditLimit;
	}
	public void setAvailableCreditLimit(double availableCreditLimit) {
		this.availableCreditLimit = availableCreditLimit;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(long postalCode) {
		this.postalCode = postalCode;
	}
	public LocalDate getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}
	public AnnualIncome getIncome() {
		return income;
	}
	public void setIncome(AnnualIncome income) {
		this.income = income;
	}
	public BankAccount getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}
	public List<CreditCard> getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(List<CreditCard> creditCard) {
		this.creditCard = creditCard;
	}
	
	
}
