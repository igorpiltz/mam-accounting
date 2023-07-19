package corporation.model.bookkeeping;

import java.math.BigDecimal;
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

@Entity
public class Transaction implements java.io.Serializable {
	
	private Long id;

	private Long number;
	
	private Date dateCreated;
	private Date dateOccurred;
	private Date dateNoticed;
	private String description;
	private String otherParty;
	private String baseDocument;
	
	private List<Part> parts = new ArrayList<Part>();
	
	private Transaction correctedTransaction;
	private Transaction erronousTransaction;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {	return id; }
	public void setId(Long id) {	this.id = id; }
	
	/**
	 * @return the dateCreated
	 */
	public Date getDateCreated() {
		return dateCreated;
	}



	/**
	 * @param dateCreated the dateCreated to set
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}



	/**
	 * @return the dateOccurred
	 */
	public Date getDateOccurred() {
		return dateOccurred;
	}



	/**
	 * @param dateOccurred the dateOccurred to set
	 */
	public void setDateOccurred(Date dateOccurred) {
		this.dateOccurred = dateOccurred;
	}



	/**
	 * @return the dateNoticed
	 */
	public Date getDateNoticed() {
		return dateNoticed;
	}



	/**
	 * @param dateNoticed the dateNoticed to set
	 */
	public void setDateNoticed(Date dateNoticed) {
		this.dateNoticed = dateNoticed;
	}



	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}



	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}



	/**
	 * @return the otherParty
	 */
	public String getOtherParty() {
		return otherParty;
	}



	/**
	 * @param otherParty the otherParty to set
	 */
	public void setOtherParty(String otherParty) {
		this.otherParty = otherParty;
	}



	/**
	 * @return the baseDocument
	 */
	public String getBaseDocument() {
		return baseDocument;
	}



	/**
	 * @param baseDocument the baseDocument to set
	 */
	public void setBaseDocument(String baseDocument) {
		this.baseDocument = baseDocument;
	}



	/**
	 * @return the correctedTransaction
	 */
	@OneToOne
	public Transaction getCorrectedTransaction() {
		return correctedTransaction;
	}


	/**
	 * Sets both sides of the association for correctedTransaction.
	 * This is the function you are supposed to actually use to set correctedTransaction.
	 * 
	 * @param correctedTransaction
	 */
	public void addCorrectedTransaction(Transaction correctedTransaction) {
		setCorrectedTransaction(correctedTransaction);
		correctedTransaction.setErronousTransaction(this);
	}

	/**
	 * @param correctedTransaction the correctedTransaction to set
	 */
	public void setCorrectedTransaction(Transaction correctedTransaction) {
		this.correctedTransaction = correctedTransaction;
	}

	@OneToOne(mappedBy="correctedTransaction")
	public Transaction getErronousTransaction() {
		return erronousTransaction;
	}
	
	public void setErronousTransaction(Transaction erronousTransaction) {
		this.erronousTransaction = erronousTransaction;
	}
	
	
	
	
	
	public Transaction() {}
	
	public Transaction(Date dateCreated) {
		super();
		
		this.dateCreated = dateCreated;
				
		
		if (dateCreated == null)
			throw new IllegalArgumentException("dateCreated null");
	}

	public Transaction(
			Date dateCreated, 
			Date dateOccurred,
			Date dateNoticed, 
			String description, 
			String otherParty,
			String baseDocument) {
		super();

		this.dateCreated = dateCreated;
		this.dateOccurred = dateOccurred;
		this.dateNoticed = dateNoticed;
		this.description = description;
		this.otherParty = otherParty;
		this.baseDocument = baseDocument;
				
		if (dateCreated == null)
			throw new IllegalArgumentException("dateCreated null");
	}


	@OneToMany(cascade = {CascadeType.ALL}, fetch=FetchType.EAGER, mappedBy="transaction")
	public List<Part> getParts() {
		return parts;
	}

	public void setParts(List<Part> parts) {
		this.parts = parts;
	}
	
	public void add(Part part) {
		parts.add(part);
		part.setTransaction(this);
	}

	public String toString() {
		StringBuffer buf = new StringBuffer("Transaction");
		buf.append("\nNoticed: " + dateNoticed);
		buf.append("\nOccurred: " + dateOccurred);
		buf.append("\nCreated: " + dateCreated);
		buf.append("\nDescription: " + description);
		buf.append("\nOther Party: " + otherParty);
		buf.append(", based on: " + baseDocument);
				
		
		buf.append("\nParts:\n");
		for (int index = 0; index < parts.size(); index++) {
			buf.append(parts.get(index) + "\n");
		}
		return buf.toString();
	}

	@Transient
	public BigDecimal getBalance() {
		BigDecimal sum = BigDecimal.ZERO;
		
		for (Iterator<Part> it = parts.iterator(); it.hasNext();) {
			Part part = it.next();
			sum = sum.add(part.getAmount());
		}
		
		return sum.negate();
	}
	
	@Transient
	public Part getPart(int accountCode) {
		for (Iterator<Part> it = parts.iterator(); it.hasNext();) {
			Part part = it.next();
			if (part.getAccount().getAccountCode().intValue() == accountCode)
				return part;
		}
		return null;
	}
	public void setNumber(Long number) {
		this.number = number;
	}
	
	public Long getNumber() {
		return number;
	}
	
	public void validateState() {
		if (dateCreated == null)
			throw new IllegalStateException("dateCreated = null");
		if (dateNoticed == null)
			throw new IllegalStateException("dateNoticed = null");
		
		if (getBalance().compareTo(BigDecimal.ZERO) != 0)
			throw new IllegalStateException("Nonzero balance: " + getBalance());
	}
	
}
