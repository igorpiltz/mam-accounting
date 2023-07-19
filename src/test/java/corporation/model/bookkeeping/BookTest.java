package corporation.model.bookkeeping;

import static org.junit.Assert.*;

import java.util.Currency;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import corporation.model.Company;
import corporation.model.CompanyType;
import corporation.model.StateIdNumber;

public class BookTest {
	
	Book book;
	Account account;
	
	@Before
	public void setUp() throws Exception {
		Company comp = new Company("namnet",
				"641223-4855", 
				CompanyType.AKTIEBOLAG, 
				Currency.getInstance("SEK"));
		
		book = comp.getBook();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetAccount() {
		account = new Account(AccountClass.ASSET, "konto", 23);
		book.addAccount(account);
		assertEquals(account, book.findAccount(23));
	}
	
	@Test(expected=Exception.class)
	public void testAddAccount() {
		account = new Account(AccountClass.ASSET, "konto", 23);
		book.addAccount(account);
		book.addAccount(account);
	}
	

	@Test
	public void testAddAccountNull() {
		account = new Account(AccountClass.ASSET, "konto", null);
		book.addAccount(account);
		book.addAccount(account);
	}
}
