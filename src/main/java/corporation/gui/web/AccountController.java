package corporation.gui.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import corporation.dao.Dao;
import corporation.dao.file.FileDao;
import corporation.gui.AccountView;
import corporation.model.bookkeeping.Account;
import corporation.model.bookkeeping.Book;
import corporation.model.bookkeeping.Part;
import corporation.model.bookkeeping.Transaction;
import corporation.model.Company;
import corporation.model.CompanyType;
import corporation.model.StateIdNumber;
import corporation.model.bookkeeping.AccountClass;

/**
 * Har hand om Book och Accounts. 
 * 
 * @author Wiggo Plitz
 *
 */

public class AccountController extends HttpServlet {
	
	static Logger log = LoggerFactory.getLogger(AccountController.class);
	
	private static String INSERT_OR_EDIT = "/account.jsp";
    private static String LIST_OBJ = "/book.jsp";
    private Dao dao;

	
	public void init() {
		dao = (Dao)getServletContext().getAttribute("data");
	}
	
	public void destroy() {
		
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward="";
		String action = request.getParameter("action");
		log.info("AccountController.GET, action: " + action);

		if (action.equalsIgnoreCase("delete")) {
			long accountId = Long.parseLong(request.getParameter("accountId"));
			Account account = dao.getAccount(accountId);
			Long bookId = account.getBook().getId();
			// Vi väntar på en GUI för det här så vi inte råkar klicka fel och tar bort ett konto. 
			dao.deleteAccount(accountId);
			forward = "/AccountController.do?action=listAll&bookId=" + bookId;
			request.setAttribute("book", dao.getBook(bookId));    
		} else if (action.equalsIgnoreCase("edit")) {
			forward = INSERT_OR_EDIT;
			long accountId = Long.parseLong(request.getParameter("accountId"));
			Account account = dao.getAccount(accountId);
			request.setAttribute("accountEdit", account);    
			request.setAttribute("book", account.getBook());
		} else if (action.equalsIgnoreCase("listAll")) {
			forward = LIST_OBJ;
			long bookId = Long.parseLong(request.getParameter("bookId"));
			log.info("bookId: " + bookId);
						
			Book book = dao.getBook(bookId);
			EntityManager em = dao.getEm();
			List<Account> accounts = null;
			try {
				em.getTransaction().begin();
				book = em.merge(book);
				book.getAccounts().size();
				accounts = new ArrayList<Account>(book.getAccounts());
				Collections.sort(accounts,
					new Comparator<Account>() {
						@Override
						public int compare(Account arg0, Account arg1) {
							int classDiff = arg0.getAccountClass().ordinal() - arg1.getAccountClass().ordinal();
							if (classDiff != 0)
								return classDiff;
							return arg0.getName().compareTo(arg1.getName());
						}
					});
				em.getTransaction().commit();
			} finally {
				if (em != null)
					em.close();
			}
			
			request.setAttribute("book", book);
			request.setAttribute("accounts", accounts);
			
		} else if (action.equalsIgnoreCase("view")) {
			forward = "/accountView.jsp";
			int accountId = Integer.parseInt(request.getParameter("accountId"));
			Account account = dao.getAccount(accountId);
			List<Part> parts = dao.getParts(account);
			AccountView accountView = new AccountView(account, parts);
			List<Account> children = dao.getAccountsByClass(account);
			
			request.setAttribute("account", account);
			request.setAttribute("accountView", accountView);
			request.setAttribute("children", children);
			
		} else if (action.equalsIgnoreCase("insert")) {
			forward = INSERT_OR_EDIT;
			int bookId = Integer.parseInt(request.getParameter("bookId"));
			request.setAttribute("book", dao.getBook(bookId));
		} else if (action.equalsIgnoreCase("orphan")) {
			int accountId = Integer.parseInt(request.getParameter("accountId"));
			Account account = dao.getAccount(accountId);
			Account parent = dao.getAccount(account.getParent().getId());
			
			EntityManager em = dao.getEm();
			em.getTransaction().begin();
			try {
				parent = em.merge(parent);
				parent.removeChild(account);
				em.getTransaction().commit();
			} finally {
				if (em != null)
					em.close();
			}
			forward = "/AccountController.do?action=view&accountId=" + parent.getId();
		} else if (action.equalsIgnoreCase("addChild")) {
			int childId = Integer.parseInt(request.getParameter("newChild"));
			int parentId = Integer.parseInt(request.getParameter("parent"));
			
			Account child = dao.getAccount(childId);
			Account parent = dao.getAccount(parentId);
			
			EntityManager em = dao.getEm();
			em.getTransaction().begin();
			try {
				parent = em.merge(parent);
				child = em.merge(child);
				parent.addChild(child);
				em.getTransaction().commit();
			} finally {
				if (em != null)
					em.close();
			}
			forward = "/AccountController.do?action=view&accountId=" + parent.getId();
		} else {
			// Fel, index.jsp
			forward = "/index.jsp";
		}

		RequestDispatcher view = request.getRequestDispatcher(forward);
		view.forward(request, response);
	}
	
	public void doPost( HttpServletRequest request,
						HttpServletResponse response)
					throws IOException, ServletException {

		String id = request.getParameter("id");
		Book book = dao.getBook(Long.parseLong(request.getParameter("bookId")));
		if (book == null)
			throw new NullPointerException("BookId: " + request.getParameter("bookId"));
		
        if(id == null || id.isEmpty())
        {
            Account account = new Account(
            		AccountClass.valueOf(request.getParameter("accountClass")),
            		request.getParameter("name"),
            		Integer.parseInt(request.getParameter("accountCode")));
            if (request.getParameter("readOnly") != null)
            	account.setReadOnly(true);
            else account.setReadOnly(false);
            
            EntityManager em = dao.getEm();
            try {
            	em.getTransaction().begin();
            	book = em.merge(book);
            	book.addAccount(account);
            	em.getTransaction().commit();
            } finally {
            	if (em != null)
            		em.close();
            }
            
        } else {
        	Account account = dao.getAccount(Long.parseLong(id));
            	
        	account.setName(request.getParameter("name"));
            account.setAccountCode(Integer.parseInt(request.getParameter("accountCode")));
            if (request.getParameter("readOnly") != null)
            	account.setReadOnly(true);
            else account.setReadOnly(false);
            
            dao.updateAccount(account);
            
        }
        
        response.sendRedirect("AccountController.do?action=listAll&bookId=" + book.getId());
	}
	

}
