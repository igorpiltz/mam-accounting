package corporation.model.bookkeeping;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TransactionTest {
	
	Transaction ts;
	Currency curr;
	Account account;
	private BigDecimal money;
	private Part part;

	@Before
	public void setUp() throws Exception {
		curr = Currency.getInstance("SEK");
		ts = new Transaction();
		account = new Account(AccountClass.ASSET, "konto", 3);
		money = new BigDecimal("3");
		part = new Part(account, money);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetBalance() {
		ts.add(part);
		assertEquals(ts.getBalance(), money.negate());
				 
	}

	@Test
	public void testGetPart() {
		ts.add(part);
		assertEquals(part, ts.getPart(3));
	}
}
