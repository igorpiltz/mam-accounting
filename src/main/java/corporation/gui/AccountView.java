package corporation.gui;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import corporation.model.bookkeeping.Account;
import corporation.model.bookkeeping.Part;

public class AccountView {
	
	private List<AccountViewLineItem> items = new ArrayList<AccountViewLineItem>();

	public AccountView(Account account, List<Part> parts) {
		Collections.sort(parts, new Comparator<Part>() {

			@Override
			public int compare(Part part0, Part part1) {
				int dateComparison = part0.getTransaction().getDateNoticed().compareTo(part1.getTransaction().getDateNoticed());
				if (dateComparison != 0)
					return dateComparison;
				else return (int)(part0.getTransaction().getNumber() - part1.getTransaction().getNumber());
			}
		});
		for (Iterator<Part> it = parts.iterator(); it.hasNext();) {
			Part part = it.next();
			if (!part.getAccount().equals(account))
				throw new AssertionError();
			AccountViewLineItem item = new AccountViewLineItem(part);
			items.add(item);
		}
		
		compute();
	}
	
	public void compute() {
		
		BigDecimal total = BigDecimal.ZERO;
		for (int index = 0; index < items.size(); index++) {
			AccountViewLineItem item = items.get(index);
			total = item.getPart().getAmount().add(total);
			item.setBalance(total);
		}
		
	}
	
	public List<AccountViewLineItem> getLineItems() {
	    return (List<AccountViewLineItem>)((ArrayList<AccountViewLineItem>)items).clone();
	}

	public List<AccountViewLineItem> getLineItemsReverse() {
	    List<AccountViewLineItem> reverseList = (List<AccountViewLineItem>)((ArrayList<AccountViewLineItem>)items).clone();
	    for (int index = 0; index < reverseList.size()/2; index++) {
		AccountViewLineItem intermediate = reverseList.get(index);
		reverseList.set(index, reverseList.get(reverseList.size() - index -1));
		reverseList.set(reverseList.size() - index - 1, intermediate);
	    }
	    return reverseList;
	}
	
	public List<AccountViewLineItem> getLineItemsLatest(int number) {
		if (number <= items.size())
			return items.subList(items.size()-number, items.size());
		else return items;
	}
}
