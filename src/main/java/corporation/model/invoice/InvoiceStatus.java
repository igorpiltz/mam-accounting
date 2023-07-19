package corporation.model.invoice;

import java.util.Date;

public class InvoiceStatus {
	
	private Integer invoiceNumber = null;
	private Date dateSent = null;
	private Date datePaid = null;
	private Date dateRutSent = null;
	private Date dateRutPaid = null;
	
	public Integer getInvoiceNumber() {
		return invoiceNumber;
	}

	public Date getDatePaid() {
		return datePaid;
	}
	
}
