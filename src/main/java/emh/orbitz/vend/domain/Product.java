/**
 * 
 */
package emh.orbitz.vend.domain;

import java.math.BigDecimal;

/**
 * Represents the types of Products a vending machine has
 * 
 * @author emhansen
 */
public enum Product {
	COKE(125), DIET_COKE(125), SPRITE(125), FANTA_ORANGE(125), ICE_MOUNTAIN_WATER(
			100);

	private BigDecimal cost;

	Product(int priceInCents) {
		cost = new BigDecimal(priceInCents).movePointLeft(2);
	}

	public BigDecimal getCost() {
		return cost;
	}
}
