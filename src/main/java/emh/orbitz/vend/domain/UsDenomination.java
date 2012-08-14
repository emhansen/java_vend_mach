package emh.orbitz.vend.domain;

import java.math.BigDecimal;

/**
 * Represents US Denomination
 * 
 * @author emhansen
 */
public enum UsDenomination {
	FIVE_BILL(500), DOLLAR_BILL(100), DOLLAR_COIN(100), QUARTER(25), DIME(10), NICKEL(
			5), PENNY(1);

	private BigDecimal value;

	UsDenomination(int value) {
		this.value = new BigDecimal(value).movePointLeft(2);
	}

	/**
	 * @return the value of this denomination.
	 */
	public BigDecimal getValue() {
		return this.value;
	}
}
