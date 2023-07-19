package corporation.model.invoice;

import java.math.BigDecimal;

public class InvoiceEntry {
	private String text;
	private double quantity;
	private BigDecimal unitPrice;
	private BigDecimal total;
	private double VAT;
	
	public BigDecimal getTotal() {
		return total;
	}

}
