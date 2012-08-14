package emh.orbitz.vend.service;

import static emh.orbitz.vend.domain.UsDenomination.DIME;
import static emh.orbitz.vend.domain.UsDenomination.NICKEL;
import static emh.orbitz.vend.domain.UsDenomination.QUARTER;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import emh.orbitz.vend.domain.InsufficentFundsException;
import emh.orbitz.vend.domain.InvalidDenominationException;
import emh.orbitz.vend.domain.OutOfProductException;
import emh.orbitz.vend.domain.Product;
import emh.orbitz.vend.domain.UsDenomination;
import emh.orbitz.vend.utility.ChangeMaker;
import emh.orbitz.vend.utility.GreedyChangeMaker;

/**
 * Implements the VendingMachineService, uses in memory data store. Normally
 * creating a dao layer is best practice but because this is such a simple case
 * I am doing this for now... also just covering the sychronization issues with
 * method synchronized keywords even though the methods don't need to be locked
 * that coursely. Normally I'd use JTA to take care of that.
 * 
 * Uses the prototype scoping so every bean that is requested from the
 * application context is independent of one another.
 * 
 * @author emhansen
 */
@Service("vendingMachineService")
@Scope("prototype")
public class VendingMachineServiceImpl implements VendingMachineService {
	private static final EnumSet<UsDenomination> VALID_Denomination_SET = EnumSet
			.of(QUARTER, DIME, NICKEL);
	private final ChangeMaker changeMaker = new GreedyChangeMaker(
			VALID_Denomination_SET);
	private final Map<Product, Integer> productMap = new ConcurrentHashMap<Product, Integer>();

	private BigDecimal balance = new BigDecimal("0.00");

	@Override
	public synchronized EnumMap<UsDenomination, Integer> coinReturn() {
		EnumMap<UsDenomination, Integer> result = changeMaker
				.makeChange(balance);
		// could just set to zero but this is safer
		for (Map.Entry<UsDenomination, Integer> e : result.entrySet()) {
			balance = balance.subtract(e.getKey().getValue().multiply(
					new BigDecimal(e.getValue())));
		}
		return result;
	}

	@Override
	public synchronized void insertDenomination(UsDenomination coinOrBill)
			throws InvalidDenominationException {

		if (!VALID_Denomination_SET.contains(coinOrBill)) {
			throw new InvalidDenominationException(coinOrBill);
		}

		balance = balance.add(coinOrBill.getValue());
	}

	@Override
	public synchronized Product purchaseProduct(Product product)
			throws InsufficentFundsException, OutOfProductException {
		Integer amount = productMap.get(product);
		if (amount == null) {
			throw new OutOfProductException(product);
		}

		if (product.getCost().compareTo(balance) == 1) {
			throw new InsufficentFundsException(product.getCost(), balance);
		}

		balance = balance.subtract(product.getCost());

		productMap.put(product, productMap.get(product) - 1);

		return product;
	}

	@Override
	public synchronized void stock(Product product, int amount) {
		Integer existing = productMap.get(product);
		if (existing == null) {
			productMap.put(product, amount);
		} else {
			productMap.put(product, existing + amount);
		}

	}

	public Map<Product, Integer> checkStock() {
		return Collections.unmodifiableMap(productMap);
	}

	@Override
	public synchronized BigDecimal getCurrentBalance() {
		return balance;
	}

}
