package corporation.model.bookkeeping.convenience;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import corporation.model.bookkeeping.Account;
import corporation.model.bookkeeping.Book;
import corporation.model.bookkeeping.Part;
import corporation.model.bookkeeping.Transaction;
import corporation.model.bookkeeping.TransactionException;

public class TransactionFactory {
	private Book book;
	protected Transaction transaction;
	private int fracDigits;
	
	public TransactionFactory(Book book) {
		this.book = book;
		if (book == null)
			throw new IllegalArgumentException("Book null");
		
		transaction = new Transaction();
		fracDigits = book.getCurrency().getDefaultFractionDigits();
	}
	
	public void add(int accountCode, String amount) {
		Account account = book.findAccount(accountCode);
		BigDecimal money = new BigDecimal(amount);
		money = money.setScale(fracDigits, RoundingMode.HALF_EVEN);
		transaction.add(new Part(account, money));
	}
	
	public void addBalance(int accountCode) {
		Account account = book.findAccount(accountCode);
		BigDecimal balance = transaction.getBalance();
		transaction.add(new Part(account, balance));
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
		fracDigits = book.getCurrency().getDefaultFractionDigits();
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public void add(String name, String amount) {
		// hitta kontot
		Account account = book.findAccount(name);
		if (account == null)
			throw new IllegalArgumentException(name);
		BigDecimal money = new BigDecimal(amount);
		money = money.setScale(fracDigits, RoundingMode.HALF_EVEN);
		Part part = new Part(account, money);
		transaction.add(part);
	}

	public void addInverse(String name, String amount) {
		// hitta kontot
		Account account = book.findAccount(name);
		if (account == null)
			throw new IllegalArgumentException(name);
		BigDecimal money = new BigDecimal(amount);
		money = money.setScale(fracDigits, RoundingMode.HALF_EVEN);
		money = money.negate();
		Part part = new Part(account, money);
		transaction.add(part);

		
	}

	/**
	 * Performs basic sanitychecks on the transaction
	 * and tries to add it to the book. 
	 * @throws TransactionException 
	 */
	public void addTransactionToBook() throws TransactionException {

		transaction.setDateCreated(new Date());
		// göra några checks här.
		transaction.validateState();
		
		
		book.addTransaction(transaction);
		
	}

}
