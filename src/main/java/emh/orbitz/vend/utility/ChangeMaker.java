package emh.orbitz.vend.utility;

import java.math.BigDecimal;
import java.util.EnumMap;

import emh.orbitz.vend.domain.UsDenomination;

/**
 * Interface for the an algorithm that can determine the minimum change for the
 * amount requested
 * 
 * @author emhansen
 */
public interface ChangeMaker {

	/**
	 * Finds the optimal way to make change, that is using the fewest number of
	 * coins and bills.
	 * 
	 * @param amount
	 * @return
	 */
	public EnumMap<UsDenomination, Integer> makeChange(BigDecimal amount);

}
