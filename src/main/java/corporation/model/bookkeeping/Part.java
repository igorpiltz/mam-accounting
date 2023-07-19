package corporation.model.bookkeeping;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Part implements java.io.Serializable {
	private Account account;
	private BigDecimal amount;
	private String description;
	private Long id;
	private Transaction transaction;
		
	
	public Part(Account account, BigDecimal amount) {
		super();
		this.account = account;
		this.amount = amount;
		validateState();
	}
	
	public Part() {}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	@ManyToOne
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
		
	
	public String toString() {
		return account + ": " + amount;
	}
	
	private void validateState() {
		if (account == null)
			throw new IllegalArgumentException("Account cannot be null");
		if (amount == null)
			throw new IllegalArgumentException("Amount cannot be null");
	}

	@ManyToOne
	public Transaction getTransaction() {
		return transaction;
	}
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
	
}
