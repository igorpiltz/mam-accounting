package corporation.model.invoice;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import corporation.model.Company;
import corporation.model.Customer;

public class Invoice {
	private Integer invoiceNumber;
	private Date invoiceDate;
	private Date dueDate;
	private Customer customer;
	private List<InvoiceEntry> entries;
	private Company company;
	
	private InvoiceStatus invoiceStatus;
	
	public Customer getCustomer() {
		return customer;
	}

	public InvoiceStatus getInvoiceStatus() {
		return invoiceStatus;
	}

	
	public BigDecimal getSum() {
		BigDecimal sum = BigDecimal.ZERO;
		
		for (int index = 0; index < entries.size(); index++) {
			InvoiceEntry entry = entries.get(index);
			BigDecimal add = entry.getTotal();
			sum = sum.add(add);
		}
		
		return sum;
	}
	
	
	
}
