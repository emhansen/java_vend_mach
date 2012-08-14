package emh.orbitz.vend.domain;

/**
 * Represents that currency that is not allowed has been inserted into a vending
 * machine
 * 
 * @author emhansen
 * 
 */
public class InvalidDenominationException extends Exception {
	private static final long serialVersionUID = 4226853392880059463L;

	public InvalidDenominationException(UsDenomination notAllowed) {
		super(notAllowed + " is not allowed in this machine");
	}

}
