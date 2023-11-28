package com.fdm.OnlineBanking.Enum;

public enum CreditLimit {

	lowIncome(5000),
	MiddleIncome(10000),
	HighIncome(15000),
	HighestIncome(20000);
	
	private double creditLimit;
	
	CreditLimit(double creditLimit) {
		this.creditLimit = creditLimit;
	}
	
	public double getCreditLimit() {
		return creditLimit;
	}

	
	
	
	
}
