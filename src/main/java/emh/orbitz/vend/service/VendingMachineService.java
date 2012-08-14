package emh.orbitz.vend.service;

import java.math.BigDecimal;
import java.util.Map;

import javax.annotation.security.RolesAllowed;

import emh.orbitz.vend.domain.UsDenomination;
import emh.orbitz.vend.domain.InsufficentFundsException;
import emh.orbitz.vend.domain.InvalidDenominationException;
import emh.orbitz.vend.domain.OutOfProductException;
import emh.orbitz.vend.domain.Product;

/**
 * Provides various operations that can take place on a vending machine
 * 
 * 
 * @author emhansen
 */
public interface VendingMachineService {

	/**
	 * Adds product to a machine
	 * 
	 * @param product
	 *            the type of product
	 * @param amount
	 *            the amount of product
	 */
	@RolesAllowed("ROLE_MAINTENANCE")
	public void stock(Product product, int amount);

	/**
	 * @return an unmodifiable Map of the stock
	 */
	public Map<Product, Integer> checkStock();

	/**
	 * Allows a customer or a vending machine operator to insert currency
	 * 
	 * @param coinOrBill
	 *            the currency inserted, could be a coin or a bill
	 * @throws InvalidDenominationException
	 *             if an invalid denomination is put in.
	 */
	public void insertDenomination(UsDenomination coinOrBill)
			throws InvalidDenominationException;

	/**
	 * Allows a customer of a vending machine to select a product.
	 * 
	 * @param product
	 *            The product desired
	 * @return the product selected
	 * @throws InsufficentFundsException
	 *             if the balance is not high enough to purchase the selected
	 *             product
	 * @throws OutOfProductException
	 *             if there is no more of the requested type of product
	 */
	public Product purchaseProduct(Product product)
			throws InsufficentFundsException, OutOfProductException;

	/**
	 * Orders the vending machine to return the remaining balance using the
	 * minimal amount of coins, and no bills
	 * 
	 * @return CurrencyAmount the remaining balance in the vending machine
	 */
	public Map<UsDenomination, Integer> coinReturn();

	/**
	 * @return the current balance that the user has left in the machine
	 */
	public BigDecimal getCurrentBalance();

}
