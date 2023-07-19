package corporation.gui.web;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import corporation.model.bookkeeping.Account;
import corporation.model.bookkeeping.Book;
import corporation.model.bookkeeping.Transaction;
import corporation.model.bookkeeping.TransactionException;
import corporation.model.bookkeeping.convenience.TransactionFactory;

/**
 * Har hand om Book och Transactions. 
 * 
 * @author Wiggo Plitz
 *
 */

public class TransactionController extends HttpServlet {
	
	static Logger log = LoggerFactory.getLogger(TransactionController.class);
	
	private static String INSERT_OR_EDIT = "/transaction.jsp";
    private static String LIST_OBJ = "/book.jsp";
    private Dao dao;
    
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	
	public void init() {
		dao = (Dao)getServletContext().getAttribute("data");
	}
	
	public void destroy() {
		
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward="";
		String action = request.getParameter("action");

		if (action.equalsIgnoreCase("delete")) {
			long transactionId = Long.parseLong(request.getParameter("transactionId"));
			Account account = dao.getAccount(transactionId);
			Long bookId = account.getBook().getId();
			dao.deleteAccount(transactionId);
			forward = LIST_OBJ;
			request.setAttribute("book", dao.getBook(bookId));    
		} else if (action.equalsIgnoreCase("view")) {
			forward = "transactionView.jsp";
			long transactionId = Long.parseLong(request.getParameter("transactionId"));
			Transaction trans = dao.getTransaction(transactionId);
			request.setAttribute("transaction", trans);    
		} else if (action.equalsIgnoreCase("listAll")) {
			// listAll
			long bookId = Long.parseLong(request.getParameter("bookId"));
			Book book = dao.getBook(bookId);
			request.setAttribute("book", book);
			List<Transaction> transactions = dao.getTransactions(bookId);
			request.setAttribute("transactions", transactions);
			forward = "transactionList.jsp";
		} else if (action.equalsIgnoreCase("bookkeeping")) {
			forward = "/accountTransList.jsp";
			int transactionId = Integer.parseInt(request.getParameter("transactionId"));
			Account account = dao.getAccount(transactionId);
			request.setAttribute("account", account);
		} else if (action.equalsIgnoreCase("insert")) {
			forward = INSERT_OR_EDIT;
			int bookId = Integer.parseInt(request.getParameter("bookId"));
			Book book = dao.getBook(bookId);
			
			// hämta accounts
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
			
			
			request.setAttribute("accounts", accounts);
			request.setAttribute("book", dao.getBook(bookId));
			request.setAttribute("dateCreated", format.format(new Date()));
			request.setAttribute("number", dao.getMaxNumber(bookId));
			request.getSession().removeAttribute("transaction");
			
		}  else if (action.equalsIgnoreCase("correct")) {
			forward = INSERT_OR_EDIT;
			int bookId = Integer.parseInt(request.getParameter("bookId"));
			int transactionId = Integer.parseInt(request.getParameter("transactionId"));
			request.setAttribute("book", dao.getBook(bookId));
			request.setAttribute("dateCreated", format.format(new Date()));
			request.setAttribute("number", new Long(dao.getNoTransactions(bookId)));
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

		Book book = dao.getBook(Long.parseLong(request.getParameter("bookId")));
		EntityManager em = dao.getEm();
		
		try {
			em.getTransaction().begin();
			book = em.merge(book);
			
			TransactionFactory factory = book.getTransactionFactory();
	        
	        Transaction trans = factory.getTransaction();
	               
	        trans.setNumber(Long.parseLong(request.getParameter("number")));
	        try {
				trans.setDateOccurred(format.parse(request.getParameter("dateOccurred")));
			} catch (ParseException e) {
				throw new IOException(e);
			}
	        try {
				trans.setDateNoticed(format.parse(request.getParameter("dateNoticed")));
			} catch (ParseException e) {
				throw new IOException(e);
			}
	        trans.setDescription(request.getParameter("description"));
	        trans.setOtherParty(request.getParameter("otherParty"));
	        trans.setBaseDocument(request.getParameter("baseDocument"));
	        
	        for (int index = 0; index < 15; index++) {
	        	String account = request.getParameter("account" + index);
	        	String credit = request.getParameter("credit" + index);
	        	String debit = request.getParameter("debit" + index);
	        	
	        	// rensa upp
	        	credit = sanitizeNumber(credit);
	        	debit = sanitizeNumber(debit);
	        	
	        	if ((credit != null && !credit.isEmpty()) &&
	        		(debit != null && !debit.isEmpty()))
	        		throw new IOException("Both credit and debit cannot be non-null");
	        	if (credit != null && !credit.isEmpty())
	        		factory.add(account, credit);
	        	if (debit != null && !debit.isEmpty())
	        		factory.addInverse(account, debit);
	        }
	        
	        
	        
	        try {
				factory.addTransactionToBook();
			} catch (TransactionException e) {
				throw new ServletException(e);
			}
	        
	        em.flush();
	        em.getTransaction().commit();
		} finally {
			if (em != null)
				em.close();
		}
        
        
        String importTransaction = request.getParameter("importTransaction");
        
        if (!((importTransaction == null) || importTransaction.isEmpty()))
        	response.sendRedirect("UploadedFileController.do?action=listImportTransactions");
        else response.sendRedirect("AccountController.do?action=listAll&bookId=" + book.getId());
	}
	
	private static String sanitizeNumber(String number) {
		if (number == null)
			return null;
		
		// replace comma with dot
		String result = number.replace(",", ".");
		// remove whitespace
		result = result.replace(" ", "");
		
		return result; 
	}

}
