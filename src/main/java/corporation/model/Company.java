package corporation.model;

import java.io.Serializable;
import java.util.Currency;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import corporation.model.bookkeeping.Book;
import corporation.model.invoice.Invoicing;

@Entity
public class Company implements Serializable {
	private Long id;
	private String name;
	private String stateIdNumber;
	private CompanyType companyType;
	private Book book;
	private Invoicing invoicing;
	private Currency currency;
	
	public Company() {}
	
	public Company(
			String name,
			String stateIdNumber,
			CompanyType companyType,
			Currency currency) {
		this.name = name;
		this.stateIdNumber = stateIdNumber;
		this.companyType = companyType;
		this.currency = currency;
		setStateIdNumber(stateIdNumber);
		
		
		if (name == null)
			throw new IllegalArgumentException();
		if (companyType == null)
			throw new IllegalArgumentException();
		if (currency == null)
			throw new IllegalArgumentException();
		
					
			
		setBook(new Book(currency, this));
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	
	public String getStateIdNumber() { return stateIdNumber;	}
	public void setStateIdNumber(String stateIdNumber) { 
		if (stateIdNumber != null) 
			new StateIdNumber(stateIdNumber); // if no exceptions are thrown the number is wellformed
		this.stateIdNumber = stateIdNumber; 
	}
	
	public CompanyType getCompanyType() { return companyType; }
	public void setCompanyType(CompanyType companyType) { this.companyType = companyType; }
	
	@OneToOne(cascade = {CascadeType.ALL})
	public Book getBook() {	return book; }
	public void setBook(Book book) { 
		this.book = book;
		if (book != null)
			book.setCompany(this);
	}
	
	@Transient
	public Invoicing getInvoicing() { return invoicing; }
	public void setInvoicing(Invoicing invoicing) { this.invoicing = invoicing; }
	
	
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	
}
