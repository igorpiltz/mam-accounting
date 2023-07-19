package corporation.model.bookkeeping.convenience;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Currency;

import javax.script.ScriptException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import corporation.model.bookkeeping.Account;
import corporation.model.bookkeeping.AccountClass;
import corporation.model.bookkeeping.Book;
import corporation.model.bookkeeping.Transaction;

public class ScriptTransactionFactoryTest {

	Book book;
	ScriptTransactionFactory factory;
	
	@Before
	public void setUp() throws Exception {
		Book book = new Book(Currency.getInstance("SEK"), null);
        book.addAccount(new Account(AccountClass.REVENUE, "Sales", 1));
        book.addAccount(new Account(AccountClass.LIABILITIES, "VAT", 2));
        book.addAccount(new Account(AccountClass.ASSET, "Checking Account", 3));
        
        factory = new ScriptTransactionFactory(book);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddBalance() throws FileNotFoundException, ScriptException {
		factory.eval(new InputStreamReader(getClass().getResourceAsStream( "/salarytest.txt" )));
        Transaction transaction = factory.getTransaction();
		assertTrue(transaction.getPart(1).getAmount().compareTo(new BigDecimal("-0.68")) == 0);
		assertTrue(transaction.getPart(2).getAmount().compareTo(new BigDecimal("-0.32")) == 0);
		assertTrue(transaction.getPart(3).getAmount().compareTo(new BigDecimal("1")) == 0);
	}

}
