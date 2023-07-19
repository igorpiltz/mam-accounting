package corporation.gui;

import java.math.BigDecimal;

import corporation.model.bookkeeping.Part;

public class AccountViewLineItem {
	private Part part;
	private BigDecimal balance;

	public AccountViewLineItem(Part part) {
		this.part = part;
	}

	public Part getPart() {
		return part;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}

}
