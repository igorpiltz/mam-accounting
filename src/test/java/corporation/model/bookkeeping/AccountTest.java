package corporation.model.bookkeeping;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AccountTest {
	private Account account = null;
	private Account subAccount;
	private Book book;

	@Before
	public void setUp() throws Exception {
		account = new Account(AccountClass.ASSET, "Account", 1);
		subAccount = new Account(AccountClass.ASSET, "subAccount", 2);
		book = new Book();
		book.addAccount(account);
		book.addAccount(subAccount);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testAddChild() {
		account.addChild(subAccount);
		assertEquals(account.getChildren().size(), 1);
		assertEquals(account.getChildren().get(0), subAccount);
		assertEquals(subAccount.getParent(), account);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddChildAccountClass() {
		account.addChild(new Account(AccountClass.COST, "errAccount", 3));
	}
	
}
