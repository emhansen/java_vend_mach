package emh.orbitz.vend.utility;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

import emh.orbitz.vend.domain.UsDenomination;

/**
 * Greedy algorithm to make change for some amount.  Not always optimal solution but this is for 
 * US Denominations.  This depends on the {@link UsDenomination} enum being ordered from highest to lowest.
 * 
 * @author emhansen
 */
public class GreedyChangeMaker implements ChangeMaker {

	EnumMap<UsDenomination, Integer> currencyAvailable;

	/**
	 * Create a new change maker using the demonination passed in, remember this implementation
	 * will not be optimal for all denomination sets.  Assumes there is an infinite
	 * amount of each of the denominations passed in.
	 * 
	 * @param denominationsAvailable the set of denominations to use
	 */
	public GreedyChangeMaker(EnumSet<UsDenomination> denominationsAvailable) {
		this.currencyAvailable = new EnumMap<UsDenomination, Integer>(UsDenomination.class);
		for (UsDenomination c : denominationsAvailable) {
			this.currencyAvailable.put(c, null);
		}
	}

	/* (non-Javadoc)
	 * doesn't have to be synchronized if we assume infinite change, only if we'll 
	 * be decrementing the amount
	 * 
	 * @see emh.orbitz.vend.utility.ChangeMaker#makeChange(java.math.BigDecimal)
	 */
	@Override
	public EnumMap<UsDenomination, Integer> makeChange(BigDecimal amount) {
		EnumMap<UsDenomination, Integer> result = new EnumMap<UsDenomination, Integer>(
				UsDenomination.class);

		for (Map.Entry<UsDenomination, Integer> e : currencyAvailable.entrySet()) {
			// if the amount is greater than or equal to the denomination amount
			while (amount.compareTo(e.getKey().getValue()) > -1
					&& (e.getValue() == null || e.getValue() > 0)) {

				amount = amount.subtract(e.getKey().getValue());
				
				result.put(e.getKey(), nullSafeAdd(result.get(e.getKey()), 1));
//                              experimental, a non-infinite coin source
//				if (e.getValue() != null) {
//					e.setValue(e.getValue() - 1);
//				}
			}

		}
		return result;
	}

	private Integer nullSafeAdd(Integer arg1, Integer arg2) {
		if (arg1 == null)
			arg1 = 0;
		if (arg2 == null)
			arg2 = 0;

		return arg1 + arg2;
	}
}
