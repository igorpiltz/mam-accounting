package corporation.model.invoice;

import java.util.ArrayList;
import java.util.List;

public class Invoicing {
	private List<Invoice> invoices = new ArrayList<Invoice>();
	private List<InvoiceStatus> invoiceStatus = new ArrayList<InvoiceStatus>();

	
	public InvoiceStatus getInvoiceStatus(Integer number) {
		for (int index = 0; index < invoiceStatus.size(); index++) {
			if (invoiceStatus.get(index).getInvoiceNumber().equals(number))
				return invoiceStatus.get(index);
		}
		return null;
	}
}
