package corporation.dao;

import java.util.List;

import javax.persistence.EntityManager;

import java.math.BigDecimal;
import corporation.model.Company;
import corporation.model.bookkeeping.Account;
import corporation.model.bookkeeping.AccountClass;
import corporation.model.bookkeeping.Book;
import corporation.model.bookkeeping.Part;
import corporation.model.bookkeeping.Transaction;
import corporation.model.bookkeeping.convenience.BookDataFile;


public interface Dao {
	public void addCompany(Company company);
	public void deleteCompany(long companyId);
	public Company updateCompany(Company company);
	public List<Company> listCompanies();
	public Company getCompany(long companyId);
	
	public void init() throws Exception;
	public void destroy() throws Exception;
	public Account getAccount(long accountId);
	public void deleteAccount(long accountId);
	public Book getBook(long bookId);
	public Book updateBook(Book book);
	public Account updateAccount(Account account);
	public Transaction getTransaction(long transactionId);
	List<Part> getParts(Account account);
	List<Transaction> getTransactions(long bookId);
	EntityManager getEm();
	public List<Account> getAccountsByClass(Account account);
	public long getNoTransactions(long bookId);
	public List<BookDataFile> getFiles(long bookId);
	public void deleteFile(long fileId);
	public BookDataFile getFile(long fileId);
	public Book getBookByFileId(long fileId);
	List<Account> getAccountsByClass(Book book, AccountClass accountClass);
	public corporation.model.bookkeeping.convenience.ImportTransactionHandler getImportTransactionHandler(Long handlerId);
	public BigDecimal getBalance(Account account);
	public long getMaxNumber(long bookId);
}
