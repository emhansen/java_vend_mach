package emh.orbitz.vend.domain;

import java.math.BigDecimal;

/**
 * Thrown if a purchase has been attempted but the balance is not high enough.
 * 
 * @author emhansen
 */
public class InsufficentFundsException extends Exception {

	private static final long serialVersionUID = 1826934994220708179L;

	public InsufficentFundsException(BigDecimal amountNeeded,
			BigDecimal existingBalance) {
		super(createMessage(amountNeeded, existingBalance));
	}

	private static String createMessage(BigDecimal amountNeeded,
			BigDecimal existingBalance) {
		// TODO move to a message resource bundle
		return String.format(
				"Insuffient Funds: $%,.2f needed, but only $%,.2f inserted",
				amountNeeded, existingBalance);
	}

}
