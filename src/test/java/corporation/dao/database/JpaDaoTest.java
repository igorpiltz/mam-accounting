package corporation.dao.database;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.GregorianCalendar;
import java.util.List;

import javax.mail.Part;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import corporation.model.Company;
import corporation.model.CompanyType;
import corporation.model.StateIdNumber;
import corporation.model.bookkeeping.Account;
import corporation.model.bookkeeping.AccountClass;
import corporation.model.bookkeeping.Book;
import corporation.model.bookkeeping.Transaction;
import corporation.model.bookkeeping.convenience.ImportTransactionHandler;
import corporation.model.bookkeeping.convenience.TransactionFactory;

public class JpaDaoTest {

	private static JpaDao dao;
	private boolean errorCompany = false;
	private Long companyId;

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
		subAccount.setReadOnly(true);
		
		book.addAccount(account);
		book.addAccount(subAccount);
		
		
		
		account.addChild(subAccount);
				
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
		
		// korrigerad transaktion
		factory = book.getTransactionFactory();
		Transaction correctedTrans = factory.getTransaction();
		calendar = new GregorianCalendar(2007, 10, 01);
		correctedTrans.setDateNoticed(calendar.getTime());
		
		factory.add(1, "44.02");
		factory.addBalance(2);
		
		trans.addCorrectedTransaction(correctedTrans);
		
		factory.addTransactionToBook();
		
		book.getImportTransactionHandlers().add(new ImportTransactionHandler("hej", "hej", "hej"));
		
		dao.addCompany(company);
		companyId = company.getId();
	}

	@After
	public void tearDown() throws Exception {
		if (errorCompany)
			return;
		if (dao.getCompany(companyId) != null)
			dao.deleteCompany(companyId);
	}

	@Test
	public void testAddCompany() {
		
		// Läggs till i setUp()
		
		EntityManager em = dao.getEm();
				
		Company comp = em.find(Company.class, companyId);
		assertEquals(comp.getName(), "emma");
		assertEquals(comp.getCompanyType(), CompanyType.PERSON);
		assertEquals(comp.getCurrency(), Currency.getInstance("SEK"));
		em.close();
	}

	/*
	@Test
	public void testDeleteCompany() {
		
		// Läggs till i setUp()
		
		EntityManager em = dao.getEm();
				
		assertNotNull(em.find(Company.class, companyId));
		em.close();
				
		dao.deleteCompany(companyId);
				
		
		em = dao.getEm();
		assertNull(em.find(Company.class, companyId));
		em.close();
	}
	
	
	@Test
	public void testUpdateCompany() {
	
		EntityManager em = dao.getEm();
		
		Company company = em.find(Company.class, companyId);
		em.close();
		
		assertNotNull(company);
		
		Long id = company.getId();
		company.setName("femma");
		dao.updateCompany(company);
					
		assertEquals(id, companyId);
		
		company = dao.getCompany(id);
		assertNotNull(company);
		
		assertEquals(company.getName(), "femma");
		assertEquals(company.getCompanyType(), CompanyType.PERSON);
		assertEquals(company.getCurrency(), Currency.getInstance("SEK"));
	}
*/

	
	@Test
	public void testGetBook() {
		
		EntityManager em = dao.getEm();
		Company comp = em.find(Company.class, companyId);
		em.close();
		
		assertNotNull(comp);
		
		Book book = dao.getBook(comp.getBook().getId());
		
		em = dao.getEm();
		book = em.merge(book);
		
		assertEquals(book.findAccount(1).getName(), "Konto");
		assertFalse(book.findAccount(1).getReadOnly());
		assertEquals(book.findAccount(2).getName(), "Kostnad");
		assertEquals(book.findAccount(1).getChildren().get(0).getName(), "underKonto");
		assertTrue(book.findAccount(3).getReadOnly());
		em.close();
	}
	
	/*
	@Test
	public void testGetBookIgor() {
	
		Long bookId = dao.getCompany(189).getBook().getId();
		assertNotNull(bookId);
		assertEquals(bookId, new Long(189));
		Book book = dao.getBook(bookId);
		assertNotNull(book);
	}
	*/

	@Test
	public void testUpdateBook() {
		
		EntityManager em = dao.getEm();
		Company comp = em.find(Company.class, companyId);
		
		Book book = comp.getBook();
		
		List<Transaction> transactions = book.getTransactions();
		assertEquals(transactions.size(), 2);
		assertEquals(transactions.get(0).getParts().size(), 2);
		em.close();
	}
	
	
	@Test
	public void testGetParts() {
		EntityManager em = dao.getEm();
		
		Query q = em.createQuery ("SELECT x FROM Company x WHERE x.name = 'emma'");
		List<Company> results = q.getResultList();
		Company comp = results.get(0);
		
		Account account = comp.getBook().findAccount(1);
		em.close();
		
		assertNotNull(account.getId());
		assertEquals(account.getName(), "Konto");
		
		List<corporation.model.bookkeeping.Part> parts = dao.getParts(account);
		
		if (parts.size() != 1)
			errorCompany = true;
		else {
			assertEquals(parts.size(), 1);
			assertEquals(parts.get(0).getAmount().toString(), "44.02");
			assertEquals(parts.get(0).getAccount().getName(), "Konto");
		}
	}
	
	@Test
	public void testGetTransactions() {
		
		EntityManager em = dao.getEm();
		Company comp = em.find(Company.class, companyId);
		em.close();
		
		
		Book book = comp.getBook();
		List<Transaction> transactions = dao.getTransactions(book.getId());
		
		assertEquals(transactions.size(), 2);
		if ( !(  (transactions.get(0).getParts().get(0).getAccount().getName().equals("Konto") ) 
				|| 
				(transactions.get(0).getParts().get(0).getAccount().getName().equals("Kostnad")) 
			) )
			Assert.fail();
		
		assertEquals(transactions.get(0).getParts().size(), 2);
		
		assertEquals(
			transactions.get(0).getCorrectedTransaction().getId(),
			transactions.get(1).getId());
		assertEquals(
				transactions.get(1).getErronousTransaction().getId(),
				transactions.get(0).getId());
		
	}
	
	@Test
	public void testGetAccountsByClass() {
		
		EntityManager em = dao.getEm();
		
		Query q = em.createQuery ("SELECT x FROM Company x WHERE x.name = 'emma'");
		List<Company> results = q.getResultList();
		Company comp = results.get(0);
		
		Book book = comp.getBook();
		Account account = book.findAccount(1);
		
		em.close();
		
		
		
		List<Account> accounts = dao.getAccountsByClass(account);
		
		assertEquals(accounts.size(), 1);
		assertEquals(accounts.get(0).getName(), "underKonto");
		
	}
	
	@Test
	public void testGetNoTransactions() {
		
		EntityManager em = dao.getEm();
				
		Company comp = em.find(Company.class, companyId);
		
		em.close();
		
		
		Book book = comp.getBook();
		
		assertEquals(dao.getNoTransactions(book.getId()), 2);
	}
	

	@Test
	public void testGetBalance() {
		
		EntityManager em = dao.getEm();
		Company comp = em.find(Company.class, companyId);
				
		Book book = comp.getBook();
		Account account = book.findAccount(1);
		assertEquals(new BigDecimal("88.04"), dao.getBalance(account));
		em.close();
	}
	
	@Test
	public void testGetMaxNumber() {
		
		EntityManager em = dao.getEm();
		Company comp = em.find(Company.class, companyId);
				
		Book book = comp.getBook();
		em.close();
		
		assertEquals(dao.getMaxNumber(book.getId()), 2);
		
	}
}
