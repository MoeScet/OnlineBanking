package com.fdm.OnlineBanking.Enum;

public enum CreditCardName {

	CARD1("Smart Credit Card"),
	CARD2("Titanium Rewards Credit Card"),
	CARD3("Infinite Credit Card");
	

	private final String creditCardName;
	
	CreditCardName(String creditCardName) {
		this.creditCardName = creditCardName;
	}
	
	public String getCreditCardName() {
		return creditCardName;
	}
	
	
}
