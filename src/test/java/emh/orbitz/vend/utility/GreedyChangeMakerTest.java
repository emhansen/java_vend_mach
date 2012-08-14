package emh.orbitz.vend.utility;

import static emh.orbitz.vend.domain.UsDenomination.DIME;
import static emh.orbitz.vend.domain.UsDenomination.NICKEL;
import static emh.orbitz.vend.domain.UsDenomination.QUARTER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.EnumSet;

import org.junit.Test;

import emh.orbitz.vend.domain.UsDenomination;

/**
 * Tests the greedy change maker works for nickels dimes and quarters
 * 
 * @author emhansen
 */
public class GreedyChangeMakerTest {
	private final static EnumSet<UsDenomination> DENOMS = EnumSet.of(QUARTER,
			DIME, NICKEL);

	@Test
	public void test100() {
		ChangeMaker cm = new GreedyChangeMaker(DENOMS);

		EnumMap<UsDenomination, Integer> result = cm.makeChange(new BigDecimal(
				"1.00"));
		assertEquals(new Integer(4), result.get(QUARTER));
		assertNull(result.get(DIME));
		assertNull(result.get(NICKEL));

	}

	@Test
	public void test35() {
		ChangeMaker cm = new GreedyChangeMaker(DENOMS);

		EnumMap<UsDenomination, Integer> result = cm.makeChange(new BigDecimal(
				"0.35"));
		assertEquals(new Integer(1), result.get(QUARTER));
		assertEquals(new Integer(1), result.get(DIME));
		assertNull(result.get(NICKEL));

	}

	@Test
	public void test115() {
		ChangeMaker cm = new GreedyChangeMaker(DENOMS);

		EnumMap<UsDenomination, Integer> result = cm.makeChange(new BigDecimal(
				"1.15"));
		assertEquals(new Integer(4), result.get(QUARTER));
		assertEquals(new Integer(1), result.get(DIME));
		assertEquals(new Integer(1), result.get(NICKEL));

	}

	@Test
	public void test145() {
		ChangeMaker cm = new GreedyChangeMaker(DENOMS);

		EnumMap<UsDenomination, Integer> result = cm.makeChange(new BigDecimal(
				"1.45"));
		assertEquals(new Integer(5), result.get(QUARTER));
		assertEquals(new Integer(2), result.get(DIME));
		assertNull(result.get(NICKEL));

	}

	@Test
	public void test150() {
		ChangeMaker cm = new GreedyChangeMaker(DENOMS);

		EnumMap<UsDenomination, Integer> result = cm.makeChange(new BigDecimal(
				"1.50"));
		assertEquals(new Integer(6), result.get(QUARTER));
		assertNull(result.get(DIME));
		assertNull(result.get(NICKEL));

	}

	@Test
	public void test155() {
		ChangeMaker cm = new GreedyChangeMaker(DENOMS);

		EnumMap<UsDenomination, Integer> result = cm.makeChange(new BigDecimal(
				"1.55"));
		assertEquals(new Integer(6), result.get(QUARTER));
		assertNull(result.get(DIME));
		assertEquals(new Integer(1), result.get(NICKEL));

	}

	@Test
	public void test05() {
		ChangeMaker cm = new GreedyChangeMaker(DENOMS);

		EnumMap<UsDenomination, Integer> result = cm.makeChange(new BigDecimal(
				"0.05"));
		assertNull(result.get(QUARTER));
		assertNull(result.get(DIME));
		assertEquals(new Integer(1), result.get(NICKEL));

	}

	@Test
	public void test10() {
		ChangeMaker cm = new GreedyChangeMaker(DENOMS);

		EnumMap<UsDenomination, Integer> result = cm.makeChange(new BigDecimal(
				"0.10"));
		assertNull(result.get(QUARTER));
		assertEquals(new Integer(1), result.get(DIME));
		assertNull(result.get(NICKEL));

	}

}
