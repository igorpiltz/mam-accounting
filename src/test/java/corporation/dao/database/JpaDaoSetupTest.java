package corporation.dao.database;

import static org.junit.Assert.*;

import java.util.Currency;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import corporation.model.Company;
import corporation.model.CompanyType;
import corporation.model.bookkeeping.Account;
import corporation.model.bookkeeping.AccountClass;
import corporation.model.bookkeeping.Book;
import corporation.model.bookkeeping.Transaction;
import corporation.model.bookkeeping.convenience.TransactionFactory;

public class JpaDaoSetupTest {

	private static JpaDao dao;
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		dao = new JpaDao("rut-test");
		dao.init();
	}
	
	@AfterClass
	public static void tearDownClass() throws Exception {
		dao.destroy();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		Company company = new Company("emma", 
				"790524-1415", 
				CompanyType.PERSON,
				Currency.getInstance("SEK")
				);
		Book book = company.getBook();
		
		Account account = new Account(
				AccountClass.ASSET,
				"Konto",
				1);
		Account subAccount = new Account(
				AccountClass.ASSET,
				"underKonto",
				3);
		
		book.addAccount(account);
		book.addAccount(subAccount);
		assertNull(account.getId());
		
		dao.addCompany(company);
		
		assertNotNull(account.getId());
		assertNotNull(subAccount.getId());
		
		account.addChild(subAccount);
		dao.updateCompany(company);
		
		/*		
		book.addAccount(
				new Account(AccountClass.COST,
							"Kostnad",
							2));
				 		
		
		TransactionFactory factory = book.getTransactionFactory();
		Transaction trans = factory.getTransaction();
		GregorianCalendar calendar = new GregorianCalendar(2007, 10, 01);
		trans.setDateNoticed(calendar.getTime());
		
		factory.add(1, "44.02");
		factory.addBalance(2);
		
		factory.addTransactionToBook();
		
		dao.updateBook(book);
		*/
	}

}
