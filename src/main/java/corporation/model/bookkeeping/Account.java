package corporation.model.bookkeeping;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class Account implements java.io.Serializable {
	
	private Long id; 
	private AccountClass accountClass;
	private String name;
	private List<Account> children = new ArrayList<Account>();
	private Integer accountCode;
	private Book book;
	private Account parent;
	private boolean readOnly = false;
	
	public Account() {}
	
	public Account(
			AccountClass accountClass, 
			String name,
			Integer accountCode) {
		super();
		this.accountClass = accountClass;
		this.name = name;
		this.accountCode = accountCode;
		
		validateState();
	}



	private void validateState() {
		if (accountClass == null)
			throw new IllegalArgumentException();
		if (name == null)
			throw new IllegalArgumentException();		
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public AccountClass getAccountClass() {
		return accountClass;
	}



	public void setAccountClass(AccountClass accountClass) {
		this.accountClass = accountClass;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}


	@OneToMany(mappedBy="parent", fetch=FetchType.EAGER)
	public List<Account> getChildren() {
		return children;
	}

	public void setChildren(List<Account> children) {
		this.children = children;
	}

	public void addChild(Account account) {
		if (!account.getAccountClass().equals(getAccountClass()))
			throw new IllegalArgumentException("Not same AccountClass");
		if (getBook().findAccount(account.getName()) == null)
			throw new IllegalArgumentException("subaccount must be on book already");
		children.add(account);
		account.setParent(this);
	}
	
	public void removeChild(Account account) {
		book.findAccount(account.getName()).setParent(null);
		if (false == children.remove(account))
			throw new IllegalArgumentException("not a child");
	}
	


	public Integer getAccountCode() {
		return accountCode;
	}



	public void setAccountCode(Integer accountCode) {
		this.accountCode = accountCode;
	}
	
	public String toString() {
		return getName() + " (" + getAccountCode() + " " + getAccountClass().name().substring(0, 1) + ")";
	}


	@ManyToOne
	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;		
	}
	
	@ManyToOne
	public Account getParent() { return parent; }
	public void setParent(Account parent) { this.parent = parent; }

	public boolean getReadOnly() {
		return readOnly ;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	@Override
	public boolean equals(Object o) {
		Account account = (Account)o;
		if (account.getName().equals(getName()))
			return true;
		else return false;
	}

}
