package emh.orbitz.vend.service;

import static emh.orbitz.vend.domain.Product.COKE;
import static emh.orbitz.vend.domain.Product.ICE_MOUNTAIN_WATER;
import static emh.orbitz.vend.domain.Product.SPRITE;
import static emh.orbitz.vend.domain.UsDenomination.DIME;
import static emh.orbitz.vend.domain.UsDenomination.DOLLAR_BILL;
import static emh.orbitz.vend.domain.UsDenomination.DOLLAR_COIN;
import static emh.orbitz.vend.domain.UsDenomination.FIVE_BILL;
import static emh.orbitz.vend.domain.UsDenomination.NICKEL;
import static emh.orbitz.vend.domain.UsDenomination.PENNY;
import static emh.orbitz.vend.domain.UsDenomination.QUARTER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import emh.orbitz.vend.domain.InsufficentFundsException;
import emh.orbitz.vend.domain.InvalidDenominationException;
import emh.orbitz.vend.domain.OutOfProductException;
import emh.orbitz.vend.domain.Product;
import emh.orbitz.vend.domain.UsDenomination;

/**
 * Tests the VendingMachine default implementation
 * 
 * @author emhansen
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
public class VendingMachineServiceImplTest {
	private static final String MAINTENANCE_ROLE = "ROLE_MAINTENANCE";
	private static final String CUSTOMER_ROLE = "ROLE_CUSTOMER";

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * @return a new instance of the vending machine service
	 */
	private VendingMachineService getVms() {
		return (VendingMachineService) applicationContext
				.getBean("vendingMachineService");
	}

	private void authorizeAs(String role) {
		UsernamePasswordAuthenticationToken token = null;
		if (role != null) {
			token = new UsernamePasswordAuthenticationToken(role, "password");
		}
		SecurityContextHolder.getContext().setAuthentication(token);

	}

	/**
	 * resets the authorization before each test
	 */
	@Before
	public void resetAuth() {
		authorizeAs(null);
	}

	@Test
	public void testCoinReturn() throws InvalidDenominationException {
		VendingMachineService vms = getVms();
		vms.insertDenomination(DIME);
		assertEquals("balance should be $0.10", new BigDecimal("0.10"), vms
				.getCurrentBalance());

		Map<UsDenomination, Integer> actual = vms.coinReturn();
		assertNotNull("coin return should always return a object", actual);
		assertEquals("expected dime", new Integer(1), actual.get(DIME));

		assertEquals("balance should be $0.00", new BigDecimal("0.00"), vms
				.getCurrentBalance());
	}

	@Test
	public void testInsertDenominationDime()
			throws InvalidDenominationException {
		VendingMachineService vms = getVms();
		vms.insertDenomination(DIME);

		assertEquals("balance should be $0.10", new BigDecimal("0.10"), vms
				.getCurrentBalance());
	}

	@Test
	public void testInsertDenominationNickel()
			throws InvalidDenominationException {
		VendingMachineService vms = getVms();
		vms.insertDenomination(NICKEL);
		assertEquals("balance should be $0.05", new BigDecimal("0.05"), vms
				.getCurrentBalance());

	}

	@Test
	public void testInsertDenominationQuarter()
			throws InvalidDenominationException {
		VendingMachineService vms = getVms();
		vms.insertDenomination(QUARTER);
		assertEquals("balance should be $0.25", new BigDecimal("0.25"), vms
				.getCurrentBalance());

	}

	@Test(expected = InvalidDenominationException.class)
	public void testInsertDenominationPenny()
			throws InvalidDenominationException {
		VendingMachineService vms = getVms();

		vms.insertDenomination(PENNY);

	}

	@Test(expected = InvalidDenominationException.class)
	public void testInsertDenominationDollarBill()
			throws InvalidDenominationException {
		VendingMachineService vms = getVms();

		vms.insertDenomination(DOLLAR_BILL);

	}

	@Test(expected = InvalidDenominationException.class)
	public void testInsertDenominationDollarCoin()
			throws InvalidDenominationException {
		VendingMachineService vms = getVms();

		vms.insertDenomination(DOLLAR_COIN);

	}

	@Test(expected = InvalidDenominationException.class)
	public void testInsertDenominationFiveBill()
			throws InvalidDenominationException {
		VendingMachineService vms = getVms();
		vms.insertDenomination(FIVE_BILL);

	}

	@Test
	public void testInsertDenominationMultiValid()
			throws InvalidDenominationException {
		VendingMachineService vms = getVms();

		vms.insertDenomination(QUARTER);
		vms.insertDenomination(NICKEL);
		vms.insertDenomination(DIME);
		vms.insertDenomination(NICKEL);

		assertEquals("balance should be $0.45", new BigDecimal("0.45"), vms
				.getCurrentBalance());

	}

	@Test
	public void testInsertDenominationMultiValidAndInvalid() {
		VendingMachineService vms = getVms();
		InvalidDenominationException e = null;

		try {
			vms.insertDenomination(QUARTER);
			vms.insertDenomination(NICKEL);
		} catch (InvalidDenominationException ice) {
			fail(ice.toString());
		}

		try {
			vms.insertDenomination(PENNY);
		} catch (InvalidDenominationException ice) {
			e = ice;
		}
		assertNotNull(
				"Machine should have notified that Denomination is invalid", e);

		try {
			vms.insertDenomination(DIME);
			vms.insertDenomination(NICKEL);
		} catch (InvalidDenominationException ice) {
			fail(ice.toString());
		}

		assertEquals("balance should be $0.45", new BigDecimal("0.45"), vms
				.getCurrentBalance());

	}

	@Test
	public void testPurchaseProduct() throws InvalidDenominationException,
			InsufficentFundsException, OutOfProductException {
		VendingMachineService vms = getVms();
		authorizeAs(MAINTENANCE_ROLE);
		vms.stock(COKE, 24);
		authorizeAs(null);

		vms.insertDenomination(QUARTER);
		vms.insertDenomination(QUARTER);
		vms.insertDenomination(QUARTER);
		vms.insertDenomination(QUARTER);
		vms.insertDenomination(QUARTER);
		Product p = null;
		p = vms.purchaseProduct(COKE);
		assertNotNull("Should have gotten a coke", p);
	}

	@Test(expected = OutOfProductException.class)
	public void testPurchaseProductShortProduct()
			throws InvalidDenominationException, InsufficentFundsException,
			OutOfProductException {
		VendingMachineService vms = getVms();
		authorizeAs(MAINTENANCE_ROLE);
		vms.stock(COKE, 24);
		authorizeAs(null);

		vms.insertDenomination(QUARTER);
		vms.insertDenomination(QUARTER);
		vms.insertDenomination(QUARTER);
		vms.insertDenomination(QUARTER);
		vms.insertDenomination(QUARTER);

		vms.purchaseProduct(SPRITE);
	}

	@Test(expected = InsufficentFundsException.class)
	public void testPurchaseProductShortMoney()
			throws InvalidDenominationException, InsufficentFundsException,
			OutOfProductException {
		VendingMachineService vms = getVms();
		authorizeAs(MAINTENANCE_ROLE);
		vms.stock(COKE, 24);
		authorizeAs(null);

		vms.insertDenomination(QUARTER);
		vms.insertDenomination(QUARTER);
		vms.insertDenomination(QUARTER);
		vms.insertDenomination(QUARTER);

		vms.purchaseProduct(COKE);
	}

	@Test
	public void testStock() {
		VendingMachineService vms = getVms();
		authorizeAs(MAINTENANCE_ROLE);
		vms.stock(COKE, 24);
		assertEquals("should be 24", new Integer(24), vms.checkStock()
				.get(COKE));
	}

	@Test
	public void testStockMulti() {
		VendingMachineService vms = getVms();
		authorizeAs(MAINTENANCE_ROLE);
		vms.stock(COKE, 24);
		vms.stock(ICE_MOUNTAIN_WATER, 12);

		assertEquals("coke should be 24", new Integer(24), vms.checkStock()
				.get(COKE));
		assertEquals("ice mountain water should be 12", new Integer(12), vms
				.checkStock().get(ICE_MOUNTAIN_WATER));

		vms.stock(ICE_MOUNTAIN_WATER, 12);
		assertEquals("coke should be 24", new Integer(24), vms.checkStock()
				.get(COKE));
		assertEquals("ice mountain water should be 24", new Integer(24), vms
				.checkStock().get(ICE_MOUNTAIN_WATER));

	}

	@Test
	public void testFullLifeCycle() {
		VendingMachineService vms = getVms();
		authorizeAs(MAINTENANCE_ROLE);
		vms.stock(COKE, 24);
		authorizeAs(null);
		try {
			vms.insertDenomination(QUARTER);
			vms.insertDenomination(QUARTER);
			vms.insertDenomination(QUARTER);
			vms.insertDenomination(DIME);
			vms.insertDenomination(NICKEL);
		} catch (InvalidDenominationException ice) {
			fail("should have all been valid currencies");
		}
		InvalidDenominationException e = null;
		try {
			vms.insertDenomination(PENNY);
		} catch (InvalidDenominationException ice) {
			e = ice;
		}
		assertNotNull("Should have been told penny not accepted", e);
		try {
			vms.insertDenomination(NICKEL);
			vms.insertDenomination(QUARTER);
			vms.insertDenomination(NICKEL);
			vms.insertDenomination(QUARTER);
			vms.insertDenomination(QUARTER);
			vms.insertDenomination(QUARTER);
			vms.insertDenomination(NICKEL);
			vms.insertDenomination(NICKEL);

		} catch (InvalidDenominationException ice) {
			fail("all valid currencies");
		}
		assertEquals("balance should is off", new BigDecimal("2.10"), vms
				.getCurrentBalance());
		try {
			Product p = vms.purchaseProduct(COKE);
			assertEquals("should have been a coke", COKE, p);

		} catch (InsufficentFundsException ife) {
			fail("should have enough funds");
		} catch (OutOfProductException oope) {
			fail("should have enough products");
		}

		assertEquals("balance is off", new BigDecimal("0.85"), vms
				.getCurrentBalance());

		InsufficentFundsException eActual = null;

		try {
			vms.purchaseProduct(COKE);
		} catch (InsufficentFundsException ife) {
			eActual = ife;
		} catch (OutOfProductException oope) {
			fail("should have enough products");
		}
		assertNotNull("Should have been told insuffient funds", eActual);

		Map<UsDenomination, Integer> monies = vms.coinReturn();
		assertEquals("Two quarters", new Integer(3), monies.get(QUARTER));
		assertEquals("One dime", new Integer(1), monies.get(DIME));

		assertEquals("No balance left", new BigDecimal("0.00"), vms
				.getCurrentBalance());

	}

	@Test(expected = AuthenticationCredentialsNotFoundException.class)
	public void testNoCredentials() {
		getVms().stock(COKE, 12);
	}

	@Test(expected = BadCredentialsException.class)
	public void testBadCredentials() {
		authorizeAs("not a real user");
		getVms().stock(COKE, 12);
	}

	@Test(expected = AccessDeniedException.class)
	public void testUnAuthorized() {
		authorizeAs(CUSTOMER_ROLE);
		getVms().stock(COKE, 12);
	}
}
