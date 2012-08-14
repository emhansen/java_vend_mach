package emh.orbitz.vend.domain;

/**
 * The vending machine does not have enough product
 * 
 * @author emhansen
 * 
 */
public class OutOfProductException extends Exception {
	private static final long serialVersionUID = 4592489503916340373L;

	public OutOfProductException(Product p) {
		super("Out of " + p.toString());
	}

}
