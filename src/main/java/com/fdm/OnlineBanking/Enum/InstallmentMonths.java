package com.fdm.OnlineBanking.Enum;

public enum InstallmentMonths {

	THREE_MONTHS(3),
	SIX_MONTHS(6),
	NINE_MONTHS(9),
	TWELVE_MONTHS(12);
	
	public int numberMonths;

	InstallmentMonths(int i) {
		this.numberMonths = i;
	}
}
