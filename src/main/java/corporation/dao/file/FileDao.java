package corporation.dao.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.serial.ObjectStreamUtil;

import corporation.dao.Dao;
import corporation.model.Company;
import corporation.model.bookkeeping.Account;
import corporation.model.bookkeeping.Book;

public abstract class FileDao implements Dao {
	static Logger log = LoggerFactory.getLogger(FileDao.class);

	private String file;
	private List<Company> companyList = null;
			
	public FileDao(String file) throws FileNotFoundException, ClassNotFoundException, IOException {
		this.file = file;
	}

	@Override
	public void addCompany(Company company) {
		long id = 0;
		for (int index = 0; index < companyList.size(); index++) {
			if (companyList.get(index).getId().longValue() > id)
				id = companyList.get(index).getId().longValue();
		}
		id++;
		company.setId(id);
		company.getBook().setId(id);
		companyList.add(company);
		log.info("add");
	}

	@Override
	public void deleteCompany(long id) {
		for (int index = 0; index < companyList.size(); index++) {
			if (companyList.get(index).getId() == id) {
				companyList.remove(index);
				return;
			}
		}
		throw new IllegalArgumentException();
	}

	@Override
	public Company updateCompany(Company company) {
		for (int index = 0; index < companyList.size(); index++) {
			if (companyList.get(index).getId().longValue() == company.getId().longValue()) {
				companyList.remove(index);
				companyList.add(index, company);
				return company;
			}
		}
		throw new IllegalArgumentException();
	}

	@Override
	public List<Company> listCompanies() {
		return new ArrayList<Company>(companyList);
	}

	@Override
	public Company getCompany(long id) {
		for (int index = 0; index < companyList.size(); index++) {
			if (companyList.get(index).getId() == id) {
				return companyList.get(index);
			}
		}
		return null;
	}

	@Override
	public void init() throws Exception {
		try {
			companyList = (List<Company>) ObjectStreamUtil.readObject(file);
		} catch (FileNotFoundException e) {
			log.error("File", e);
		} catch (IOException e) {
			log.error("IO", e);
		}
		if (companyList == null)
			companyList = new ArrayList<Company>();
		
		log.info("CompanyFileDao.init" + file);
	}

	@Override
	public void destroy() throws Exception {
		ObjectStreamUtil.writeObject(file, companyList);
	}

	public static Account searchById(long id, List<Account> list) {
		// TODO Auto-generated method stub
		for (int index = 0; index < list.size(); index++) {
			if (list.get(index).getId().longValue() == id)
				return list.get(index);
		}
		return null;
	}

	public Account getAccount(long accountId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteAccount(long accountId) {
		// TODO Auto-generated method stub
		
	}

	public Book getBook(long bookId) {
		for (int index = 0; index < companyList.size(); index++) {
			if (companyList.get(index).getBook().getId().longValue() == bookId)
				return companyList.get(index).getBook();
		}
		return null;
	}

	@Override
	public Book updateBook(Book book) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Account updateAccount(Account account) {
		// TODO Auto-generated method stub
		return null;
	}

}
