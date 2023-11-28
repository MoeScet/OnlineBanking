package com.fdm.OnlineBanking.Enum;

public enum AnnualIncome {
	
	lowIncome("Annual Income below $30,000"),
	MiddleIncome("Annual Income between $30,000 and $100,000"),
	HighIncome("Annual Income between $100,000 and $150,000"),
	HighestIncome("Annual Income above $150,000");

	private String displayName;
	
	AnnualIncome(String displayName) {
		// TODO Auto-generated constructor stub
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
	    return displayName;
	  }
	
}
