package corporation.dao.database;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import corporation.dao.Dao;
import corporation.gui.web.AccountController;
import corporation.model.Company;
import corporation.model.bookkeeping.Account;
import corporation.model.bookkeeping.AccountClass;
import corporation.model.bookkeeping.Book;
import corporation.model.bookkeeping.Part;
import corporation.model.bookkeeping.Transaction;
import corporation.model.bookkeeping.convenience.BookDataFile;
import corporation.model.bookkeeping.convenience.ImportTransactionHandler;

public class JpaDao implements Dao {
	static Logger log = LoggerFactory.getLogger(JpaDao.class);

	private EntityManagerFactory factory;

	private String persistenceUnit;

	public JpaDao(String persistenceUnit) {
		this.persistenceUnit = persistenceUnit;
	}
	
	@Override
	public void addCompany(Company company) {
		EntityManager em = factory.createEntityManager();
		try {
			em.getTransaction().begin();
			    
			em.persist(company);
	    
			em.getTransaction().commit();
		} finally {
			if (em != null)
				em.close();
		}
	    
	}

	@Override
	public void deleteCompany(long companyId) {
		EntityManager em = factory.createEntityManager();
		try {
			em.getTransaction().begin();
			Company company = em.find(Company.class, companyId);
		
			if (company == null)
				throw new IllegalArgumentException("Company not found");
			
			em.remove(company);
			em.getTransaction().commit();
		} finally {
			if (em != null)
				em.close();
		}
	}

	@Override
	public Company updateCompany(Company company) {
		EntityManager em = factory.createEntityManager();
		Company managed = null;
		try {
			em.getTransaction().begin();
			managed = em.merge(company);
			em.getTransaction().commit();
		} finally {
			if (em != null)
				em.close();
		}
		
		return managed;
	}	

	@Override
	public List<Company> listCompanies() {
		EntityManager em = factory.createEntityManager();
		List<Company> result = null;
		try {
			em.getTransaction().begin();
			Query q = em.createQuery ("SELECT x FROM Company x");
			result = q.getResultList();
			em.getTransaction().commit();
		} finally {
			if (em != null)
				em.close();
		}
		return result;
	}

	@Override
	public Company getCompany(long companyId) {
		EntityManager em = factory.createEntityManager();
		Company comp = null;
		try {
			em.getTransaction().begin();
			comp = em.find(Company.class, companyId);
			em.getTransaction().commit();
		} finally {
			if (em != null)
				em.close();
		}
		return comp;
	}

	@Override
	public void init() throws Exception {
		factory = Persistence.createEntityManagerFactory( persistenceUnit );
		EntityManager em = factory.createEntityManager();
		Session session = em.unwrap(Session.class);
		log.info("Underlying Hibernate session flushmode "+session.getFlushMode());
		log.info("EntityManager flushmode "+em.getFlushMode());
	
		if (em != null)
			em.close();
	
	}

	@Override
	public void destroy() throws Exception {
		factory.close();
	}

	@Override
	public Account getAccount(long accountId) {
		EntityManager em = factory.createEntityManager();
		Account account = null;
		try {
			em.getTransaction().begin();
			account = em.find(Account.class, accountId);
			em.getTransaction().commit();
		} finally {
			if (em != null)
				em.close();
		}
		
		return account;
	}

	@Override
	public void deleteAccount(long accountId) {
		EntityManager em = factory.createEntityManager();
		Account account = null;
		try {
			em.getTransaction().begin();
		
			account = em.find(Account.class, accountId);
				
			em.remove(account);
			em.getTransaction().commit();
		} finally {
			if (em != null)
				em.close();
		}
	}

	@Override
	public Book getBook(long bookId) {
		log.info("bookId: " + bookId);
		Book result = null;
		EntityManager em = factory.createEntityManager();
		try {
			em.getTransaction().begin();
			Query q = em.createQuery ("SELECT book FROM Book AS book WHERE book.id = :id");
			q.setParameter("id", bookId);
			List<Book> list = q.getResultList();
			if (!list.isEmpty()) {
				result = list.get(0);
			}
			em.getTransaction().commit();
		} finally {
			if (em != null)
				em.close();
		}
		
		return result;
	}
	
	@Override
	public List<Transaction> getTransactions(long bookId) {
		List<Transaction> result = null;
		EntityManager em = factory.createEntityManager();
		
		try {
			em.getTransaction().begin();
			Query q = em.createQuery ("SELECT DISTINCT transaction FROM Book AS book, Transaction AS transaction JOIN FETCH transaction.parts WHERE book.id = :id AND transaction MEMBER OF book.transactions ORDER BY transaction.number");
			q.setParameter("id", bookId);
			result = q.getResultList();
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		
		return result;
	}

	@Override
	public Book updateBook(Book book) {
		EntityManager em = factory.createEntityManager();
		Book managed = null;
		try {
			em.getTransaction().begin();
			managed = em.merge(book);
			em.getTransaction().commit();
		} finally {
			if (em != null)
				em.close();
		}

		return managed;

	}
	
	@Override
	public EntityManager getEm() {
		return factory.createEntityManager();
	}

	@Override
	public Account updateAccount(Account account) {
		EntityManager em = factory.createEntityManager();
		
		Account managed = null;
		try {
			em.getTransaction().begin();
			managed = em.merge(account);
			em.getTransaction().commit();
			
		} finally {
			if (em != null)
				em.close();
		}
		
		return managed;

		
	}

	@Override
	public Transaction getTransaction(long transactionId) {
		EntityManager em = factory.createEntityManager();
		Transaction trans = null;
		try {
			em.getTransaction().begin();
		
			trans = em.find(Transaction.class, transactionId);
			em.getTransaction().commit();
		} finally {
			if (em != null)
				em.close();
		}
		return trans;
	}
	
	@Override
	public List<Part> getParts(Account account) {
		EntityManager em = factory.createEntityManager();
		List<Part> result = null;
		try {
			em.getTransaction().begin();
			Query q = em.createQuery ("select part From Part as part WHERE part.account.id = :accountId");  //  where part.account.id = :id
			q.setParameter("accountId", account.getId());
			result = q.getResultList();
			em.getTransaction().commit();
		} finally {
			if (em != null)
				em.close();
		}
		return result;
	}

	@Override
	public List<Account> getAccountsByClass(Account account) {
		EntityManager em = factory.createEntityManager();
		List<Account> result = null;
		try {
			em.getTransaction().begin();
		
			Query q = em.createQuery (
				"select account From Account as account " +
				"where account.book.id = :bookId " +
				"AND account.accountClass = :accountClass " +
				"AND account.id <> :accountId " +
				"ORDER BY account.name");
			q.setParameter("bookId", account.getBook().getId());
			q.setParameter("accountClass", account.getAccountClass());
			q.setParameter("accountId", account.getId());
			result = q.getResultList();
			
			em.getTransaction().commit();
		} finally {
			if (em != null)
				em.close();
		}
		return result;
	}

	
	@Override
	public List<Account> getAccountsByClass(Book book, AccountClass accountClass) {
		EntityManager em = factory.createEntityManager();
		List<Account> result = null;
		try {
			em.getTransaction().begin();
		
			Query q = em.createQuery (
				"select account From Account as account " +
				"where account.book.id = :bookId " +
				"AND account.accountClass = :accountClass " +
				"ORDER BY account.name");
			q.setParameter("bookId", book.getId());
			q.setParameter("accountClass", accountClass);
			result = q.getResultList();
			
			em.getTransaction().commit();
		} finally {
			if (em != null)
				em.close();
		}
		return result;
	}
	
	@Override
	public long getNoTransactions(long bookId) {
		EntityManager em = factory.createEntityManager();
		Long result = null;
		try {
			em.getTransaction().begin();
			Query q = em.createQuery ("select count(trans) From Transaction as trans, Book as book WHERE book.id = :bookId AND trans MEMBER OF book.transactions");  //  where part.account.id = :id
			q.setParameter("bookId", bookId);
			result = (Long)q.getSingleResult();
		
			em.getTransaction().commit();
			
		} finally {
			if (em != null)
				em.close();
		}
		return result;
	}

	@Override
	public List<BookDataFile> getFiles(long bookId) {
		EntityManager em = factory.createEntityManager();
		List<BookDataFile> result = null;
		try {
			em.getTransaction().begin();
			Query q = em.createQuery ("select file From BookDataFile as file, Book as book where book.id = :bookId AND file MEMBER OF book.bookDataFiles");
			q.setParameter("bookId", bookId);
			result = q.getResultList();
		
			em.getTransaction().commit();
		} finally {
			if (em != null)
				em.close();
		}	
		return result;
	}

	@Override
	public void deleteFile(long fileId) {
		EntityManager em = factory.createEntityManager();
		try {
			em.getTransaction().begin();
		
			BookDataFile file = em.find(BookDataFile.class, fileId);
			if (file == null)
				throw new IllegalArgumentException("File not found");
			em.remove(file);
			em.getTransaction().commit();
		} finally {
			if (em != null)
				em.close();
		}
				
	}

	@Override
	public BookDataFile getFile(long fileId) {
		EntityManager em = factory.createEntityManager();
		BookDataFile file = null;
		
		try {
			em.getTransaction().begin();
			file = em.find(BookDataFile.class, fileId);
			em.getTransaction().commit();
		} finally {
			if (em != null)
				em.close();
		}
		
		return file;
	}

	@Override
	public Book getBookByFileId(long fileId) {
		
		EntityManager em = factory.createEntityManager();
		Book result = null;
		try {
			Query q = em.createQuery ("select book From BookDataFile as file, Book as book where file.id = :fileId AND file MEMBER OF book.bookDataFiles");
			q.setParameter("fileId", fileId);
			result = (Book) q.getSingleResult();
		} finally {
			if (em != null)
				em.close();
		}
		
		return result;
	}

	@Override
	public ImportTransactionHandler getImportTransactionHandler(Long handlerId) {
		EntityManager em = factory.createEntityManager();
		ImportTransactionHandler handler = null;
		try {
			em.getTransaction().begin();
			handler = em.find(ImportTransactionHandler.class, handlerId);
			em.getTransaction().commit();
		} finally {
			if (em != null)
				em.close();
		}
		return handler;
	}
	

	@Override 
	public BigDecimal getBalance(Account account) {
		EntityManager em = factory.createEntityManager();
		BigDecimal result = null;
		try {
			em.getTransaction().begin();
			Query q = em.createQuery (
					"select sum(part.amount) From Part as part WHERE part.account.id = :accountId");
			q.setParameter("accountId", account.getId());
			result = (BigDecimal) q.getSingleResult();
			em.getTransaction().commit();
		} finally {
			if (em != null)
				em.close();
		}

		return result;
	}

	@Override
	public long getMaxNumber(long bookId) {
		EntityManager em = factory.createEntityManager();
		Long result = null;
		try {
			em.getTransaction().begin();
			Query q = em.createQuery ("select MAX(trans.number) From Transaction as trans, Book as book WHERE book.id = :bookId AND trans MEMBER OF book.transactions");  //  where part.account.id = :id
			q.setParameter("bookId", bookId);
			result = (Long)q.getSingleResult();
			em.getTransaction().commit();
		} finally {
			if (em != null)
				em.close();
		}
		if (result == null)
			return 0;
		else return result;
	}
	
}
