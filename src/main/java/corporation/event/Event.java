package corporation.event;

import java.util.Date;

import corporation.model.Company;

public class Event {
	private Company company;
	private Date whenOccurred;
	private Date whenNoticed;
	
	
	public Company getCompany() {
		return company;
	}
	public Date getWhenOccurred() {
		return whenOccurred;
	}
	public Date getWhenNoticed() {
		return whenNoticed;
	}
}
