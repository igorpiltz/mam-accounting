package corporation.model.bookkeeping;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import corporation.model.Company;
import corporation.model.bookkeeping.convenience.BookDataFile;
import corporation.model.bookkeeping.convenience.ImportTransactionHandler;
import corporation.model.bookkeeping.convenience.TransactionFactory;

@Entity
public class Book implements java.io.Serializable {
	
	private Long id; 
	
	private Currency currency;
	private Company company;
	private List<Account> accounts = new ArrayList<Account>();
	private List<Transaction> transactions = new ArrayList<Transaction>();
	private List<BookDataFile> bookDataFiles = new ArrayList<BookDataFile>();	
	private List<ImportTransactionHandler> importTransactionHandlers = new ArrayList<ImportTransactionHandler>();
	
	public Book() {}
	
	public Book(Currency currency, Company company) {
		super();
		this.currency = currency;
		this.company = company;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	
	@OneToMany(cascade = {CascadeType.ALL}, mappedBy="book")
	public List<Account> getAccounts() {	return accounts;	}
	private void setAccounts(List<Account> accounts) {	this.accounts = accounts;	}
	
	@OneToMany(cascade = {CascadeType.ALL})
	public List<Transaction> getTransactions() {	return (transactions);	}
	private void setTransactions(List<Transaction> transactions) {	this.transactions = transactions;	}
		

	public Account findAccount(int accountCode) {
		for (Iterator<Account> it = accounts.iterator(); it.hasNext();) {
			Account account = it.next();
			if (account.getAccountCode() == accountCode)
				return account;
		}
		return null;
	}

	public Currency getCurrency() { return currency;	}
	public void setCurrency(Currency currency) { this.currency = currency; }
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Book " + currency);
		return buf.toString();
		
	}

	public void addAccount(Account account) {
		if (account.getAccountCode() != null) {
			for (Iterator<Account> it = accounts.iterator(); it.hasNext();) {
				Account temp = it.next();
				if (temp.getAccountCode() == account.getAccountCode())
					throw new IllegalArgumentException("Same Accountcode");
				if (temp.getName().equals(account.getName()))
					throw new IllegalArgumentException("Same Name");
			}
		}
		
		account.setBook(this);
		accounts.add(account);
		
	}
	
	public Account findAccount(String name) {
		for (Iterator<Account> it = accounts.iterator(); it.hasNext();) {
			Account account = it.next();
			if (account.getName().equals(name))
				return account;
		}
		return null;
	}
	
	@OneToOne(mappedBy = "book")
	public Company getCompany() { return company; }
	public void setCompany(Company company) { this.company = company; }

	public void addTransaction(Transaction transaction) throws TransactionException {
		if (transaction == null)
			throw new IllegalArgumentException("Null transaction");
		// Check that no Part.account is readonly
		for(Iterator<Part> it = transaction.getParts().iterator(); it.hasNext();) {
			if (it.next().getAccount().getReadOnly())
				throw new TransactionException("readOnly");
		}
		if (transaction.getNumber() != null) {
			for(Iterator<Transaction> it = transactions.iterator(); it.hasNext();) {
				if (it.next().getNumber().equals(transaction.getNumber()))
					throw new TransactionException("same Number");
			}
		} else {
			long number = 0;
			for(Iterator<Transaction> it = transactions.iterator(); it.hasNext();) {
				Transaction trans = it.next();
				if (trans.getNumber() > number)
					number = trans.getNumber();
			}
			transaction.setNumber(number + 1);
		}
		transactions.add(transaction);
	}
		

	/**
	 * creates a factory with a transaction ready for data entry.
	 * 
	 * @return a TransactionFactory
	 */
	@Transient
	public TransactionFactory getTransactionFactory() {
		
		return new TransactionFactory(this);
	}
	
	
	@OneToMany(cascade = {CascadeType.ALL})
	public List<BookDataFile> getBookDataFiles() {
		return bookDataFiles;
	}
	public void setBookDataFiles(List<BookDataFile> bookDataFiles) {
		this.bookDataFiles = bookDataFiles;
	}

	public void addBookDataFile(BookDataFile file) {
		file.setUploaded(new Date());
		bookDataFiles.add(file);
	}
	
	@OneToMany(cascade = {CascadeType.ALL}, fetch=FetchType.EAGER)
	public List<ImportTransactionHandler> getImportTransactionHandlers() {
		return importTransactionHandlers;
	}
	public void setImportTransactionHandlers(List<ImportTransactionHandler> importTransactionHandlers) {
		this.importTransactionHandlers = importTransactionHandlers;
	}

	public void deleteFile(long fileId) {
		for(Iterator<BookDataFile> it = bookDataFiles.iterator(); it.hasNext();) {
			BookDataFile file = it.next();
			if (file.getId().equals(fileId))
				it.remove();
		}
	}
	
	/*
	public void deleteReport(long reportId) {
		for(Iterator<Report> it = reports.iterator(); it.hasNext();) {
			Report report = it.next();
			if (report.getId().equals(reportId))
				it.remove();
		}
	}
	
	*/

}
