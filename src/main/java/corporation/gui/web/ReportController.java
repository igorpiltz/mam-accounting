package corporation.gui.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;

import javassist.bytecode.Descriptor.Iterator;

import javax.persistence.EntityManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;

import org.apache.log4j.Logger;
import java.util.Collection;

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
import corporation.model.bookkeeping.convenience.BookDataFile;
import corporation.model.bookkeeping.convenience.HtmlScriptParser;
import corporation.model.bookkeeping.convenience.ImportTransactionHandler;
import corporation.model.bookkeeping.convenience.ScriptTransactionFactory;

/**
 * Har hand om uppladdade filer.
 * 
 * @author Wiggo Plitz
 *
 */

public class ReportController extends HttpServlet {
	
	static Logger log = Logger.getLogger(UploadedFileController.class);
	
	private static String INSERT_OR_EDIT = "/editReport.jsp";
    private static String LIST_OBJ = "/uploadedFiles.jsp";
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
		log.info("ReportController.GET(" + action + "): ");

		if (action.equalsIgnoreCase("delete")) {
			long reportId = Long.parseLong(request.getParameter("reportId"));
			long bookId = Long.parseLong(request.getParameter("bookId"));
			
			EntityManager em = dao.getEm();
			
			em.getTransaction().begin();
			Book book = em.find(Book.class, bookId);
			// book.deleteReport(reportId);
			em.getTransaction().commit();
			em.close();
						
			forward = "/ReportController.do?action=listAll&bookId=" + bookId;
			    
		} else if (action.equalsIgnoreCase("edit")) {
			forward = INSERT_OR_EDIT;
			long accountId = Long.parseLong(request.getParameter("accountId"));
			Account account = dao.getAccount(accountId);
			request.setAttribute("account", account);    
			request.setAttribute("book", account.getBook());
		} else if (action.equalsIgnoreCase("listAll")) {
			forward = LIST_OBJ;
			long bookId = Long.parseLong(request.getParameter("bookId"));
			List<BookDataFile> files = dao.getFiles(bookId);
			request.setAttribute("files", files);
			request.setAttribute("book", dao.getBook(bookId));
			
		} else if (action.equalsIgnoreCase("view")) {
			forward = viewHandler(request);
			
		} else if (action.equalsIgnoreCase("insert")) {
			forward = INSERT_OR_EDIT;
			int bookId = Integer.parseInt(request.getParameter("bookId"));
			request.setAttribute("book", dao.getBook(bookId));
		} else if (action.equalsIgnoreCase("setCharset")) {
			long fileId = Long.parseLong(request.getParameter("fileId"));
			String charset = request.getParameter("charset");
			
			EntityManager em = dao.getEm();
			try {
				em.getTransaction().begin();
				BookDataFile file = em.find(BookDataFile.class, fileId);
				file.setEncoding(charset);
				em.getTransaction().commit();
			} finally {
				if (em != null)
					em.close();
			}
			
			forward = "/UploadedFileController.do?action=view&fileId=" + fileId;
		} else if (action.equalsIgnoreCase("nada")) {
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
				em.close();
			}
			forward = "/AccountController.do?action=view&accountId=" + parent.getId();
		} else if (action.equalsIgnoreCase("chooseAccount")) {
			long accountId = Long.parseLong(request.getParameter("account"));
			List<Transaction> transactions = (List<Transaction>)request.getSession().getAttribute("transactions");
			Account account = dao.getAccount(accountId);
			request.getSession().setAttribute("account", account);
			AccountView accountView = new AccountView(account, dao.getParts(account));
			
			if (transactions == null)
				throw new NullPointerException("no transactions are set");
			// set temporary ids on transactions
			for (int index = 0; index < transactions.size(); index++) 
				transactions.get(index).setId((long)index);
			
			request.setAttribute("accountView", accountView);
			request.setAttribute("transactions", transactions);
			request.setAttribute("account", account);
			
			forward = "/listImportTransactions.jsp";
		} else if (action.equalsIgnoreCase("listImportTransactions")) {	 
			Account account = (Account)request.getSession().getAttribute("account");
			
			AccountView accountView = new AccountView(account, dao.getParts(account));
			List<Transaction> transactions = (List<Transaction>)request.getSession().getAttribute("transactions");
			
			request.setAttribute("accountView", accountView);
			request.setAttribute("transactions", transactions);
			request.setAttribute("account", account);
			forward = "/listImportTransactions.jsp";
		} else if (action.equalsIgnoreCase("importTransaction")) {
			long transactionId = Long.parseLong(request.getParameter("transactionId"));
			List<Transaction> transactions = (List<Transaction>)request.getSession().getAttribute("transactions");
			
			Account account = (Account)request.getSession().getAttribute("account");
			Book book = dao.getBook(account.getBook().getId()); 
			
			Transaction transaction = null;
			for (int index = 0; index < transactions.size(); index++) {
				if (transactions.get(index).getId() == transactionId)
					transaction = transactions.get(index);
			}
									
			request.setAttribute("transaction", transaction);
			request.getSession().setAttribute("transaction", transaction);
			request.setAttribute("importTransactionHandlers", book.getImportTransactionHandlers());
			
			forward = "/importTransaction.jsp";
		} else if (action.equalsIgnoreCase("runImportHandler")) {
			Enumeration<String> e = request.getParameterNames();
			
			Long handlerId = null;
			for (;e.hasMoreElements();) {
				// handler_(handler_id)
				String elem = e.nextElement();
				if (elem.contains("handler")) {
					handlerId = Long.parseLong(elem.split("_")[1]);
				}
			}
			
			ImportTransactionHandler handler = dao.getImportTransactionHandler(handlerId);
			Account account = (Account)request.getSession().getAttribute("account");
			Transaction transaction = (Transaction)request.getSession().getAttribute("transaction");
			
			ScriptTransactionFactory factory = null;
			EntityManager em = dao.getEm();
			try {
				em.getTransaction().begin();
				factory = new ScriptTransactionFactory(account.getBook());
				factory.setBook(em.merge(factory.getBook()));
				factory.importTransactionData(transaction);
				factory.eval(handler.getScript());
				em.getTransaction().commit();
			} catch(ScriptException ex) {
				throw new ServletException(ex);
			} finally {
				if (em != null)
					em.close();
			}
						
			
			transaction = factory.getTransaction();
									
			request.setAttribute("transaction", transaction);
			request.setAttribute("book", factory.getBook());
			account.setBook(factory.getBook());
			request.setAttribute("dateCreated", format.format(new Date()));
			request.setAttribute("number", new Long(dao.getMaxNumber(factory.getBook().getId())));
			
			forward = "/transaction.jsp";
		} else if (action.equalsIgnoreCase("newHandler")) {
			long bookId = Long.parseLong(request.getParameter("bookId"));
			long transactionId = Long.parseLong(request.getParameter("transactionId"));
			Book book = dao.getBook(bookId);
						
			List<Transaction> transactions = (List<Transaction>)request.getSession().getAttribute("transactions");
						
			Transaction transaction = null;
			for (int index = 0; index < transactions.size(); index++) {
				if (transactions.get(index).getId() == transactionId)
					transaction = transactions.get(index);
			}
			
			request.getSession().setAttribute("transaction", transaction);
			
			forward = "/addImportTransactionHandler.jsp";
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

		String forward = null;
		String action = request.getParameter("action");
		String secAction = "";
		if (action.equalsIgnoreCase("script")) {
			if (!(	request.getParameter("testScript") == null ||
					request.getParameter("testScript").isEmpty()))
				secAction = "testScript";
			if (!(	request.getParameter("runScript") == null ||
					request.getParameter("runScript").isEmpty()))
				secAction = "runScript";
		}
				
			
		if (secAction.equalsIgnoreCase("testScript")) {
			long fileId = Long.parseLong(request.getParameter("fileId"));
			String script = request.getParameter("script");
			Book book = dao.getBookByFileId(fileId);
			BookDataFile file = dao.getFile(fileId);
			String result = null;
			
			EntityManager em = dao.getEm();
			try {
				em.getTransaction().begin();
				file = em.merge(file);
				file.setScript(script);
				em.getTransaction().commit();
			} finally {
				if (em != null)
					em.close();
			}
			
			List<Element> segments = null;
			String[] text = null;
			HtmlScriptParser parser = null;
			List<Transaction> transactions = null;
			try {
				parser = new HtmlScriptParser(file.decode());
			
				parser.eval(script);
			
				segments = (List<Element>)parser.get("segments");
				text = (String[])parser.get("text");
				transactions = (List<Transaction>)parser.get("transactions");
			} catch (Exception e) {
				result = e.getMessage();
			}
			
			// ingen exception
			if (result == null) {
				ScriptEngine engine = parser.getEngine();
				StringBuffer buf = new StringBuffer();
				
				// printa variabler i EngineScope
				for (java.util.Iterator<Entry<String, Object>> it = parser.getVars(); it.hasNext();) {
					Entry<String, Object> entry = it.next();
					if (Collection.class.isInstance(entry.getValue())) {
						buf.append(entry.getKey().toString() + "(" + ((Collection)entry.getValue()).size() + ")" + "\n");
					} else buf.append(entry.getKey().toString() + "\n");
							// segments.get(index).getDebugInfo() + "\n";
				}
				
				// Printa Segments
				if (segments != null) {
					buf.append("\n\nSEGEMENTS\n");
					buf.append("Segments: " + segments.size() + "\n");
					for (int index = 0; index < segments.size(); index++) {
						Element element = segments.get(index);
						buf.append("Segment(" + index + "):\n");
						buf.append(element.toString() + "\n\n");
					}
				}
				// Printa text
				if (text != null) {
					buf.append("\n\nTEXT\n");
					buf.append("Text: " + text.length + "\n");
					for (int index = 0; index < text.length; index++) {
						
						buf.append("Text(" + index + "):\n");
						buf.append(text[index] + "\n\n");
					}
				}
			
				
				// Printa transactions
				if (transactions != null) {
					buf.append("\n\nTRANSACTIONS\n");
					buf.append("Transactions: " + transactions.size() + "\n");
					for (int index = 0; index < transactions.size(); index++) {
						Transaction trans = transactions.get(index);
						buf.append("Transaction(" + index + "):\n");
						buf.append(trans.toString() + "\n\n");
					}
				}
				
				
				result = buf.toString();
			}
			
			request.setAttribute("result", result);
			
			RequestDispatcher view = request.getRequestDispatcher("/UploadedFileController.do?action=view&fileId=" + fileId);
			view.forward(request, response);
			
		} else if (secAction.equalsIgnoreCase("runScript")) {
			long fileId = Long.parseLong(request.getParameter("fileId"));
			String script = request.getParameter("script");
			Book book = dao.getBookByFileId(fileId);
			BookDataFile file = dao.getFile(fileId);
			String result = null;
			
			EntityManager em = dao.getEm();
			try {
				em.getTransaction().begin();
				file = em.merge(file);
				file.setScript(script);
				em.getTransaction().commit();
			} finally {
				if (em != null)
					em.close();
			}
			
			List<Element> segments = null;
			String[] text = null;
			HtmlScriptParser parser = null;
			List<Transaction> transactions = null;
			try {
				parser = new HtmlScriptParser(file.decode());
			
				parser.eval(script);
			
				segments = (List<Element>)parser.get("segments");
				text = (String[])parser.get("text");
				transactions = (List<Transaction>)parser.get("transactions");
			} catch (Exception e) {
				result = e.getMessage();
			}
			
			// ingen exception
			if (result == null) {
				ScriptEngine engine = parser.getEngine();
				StringBuffer buf = new StringBuffer();
				
				// printa variabler i EngineScope
				for (java.util.Iterator<Entry<String, Object>> it = parser.getVars(); it.hasNext();) {
					Entry<String, Object> entry = it.next();
					if (Collection.class.isInstance(entry.getValue())) {
						buf.append(entry.getKey().toString() + "(" + ((Collection)entry.getValue()).size() + ")" + "\n");
					} else buf.append(entry.getKey().toString() + "\n");
							// segments.get(index).getDebugInfo() + "\n";
				}
				
				// Printa Segments
				if (segments != null) {
					buf.append("\n\nSEGEMENTS\n");
					buf.append("Segments: " + segments.size() + "\n");
					for (int index = 0; index < segments.size(); index++) {
						Element element = segments.get(index);
						buf.append("Segment(" + index + "):\n");
						buf.append(element.toString() + "\n\n");
					}
				}
				// Printa text
				if (text != null) {
					buf.append("\n\nTEXT\n");
					buf.append("Text: " + text.length + "\n");
					for (int index = 0; index < text.length; index++) {
						
						buf.append("Text(" + index + "):\n");
						buf.append(text[index] + "\n\n");
					}
				}
			
				
				// Printa transactions
				if (transactions != null) {
					buf.append("\n\nTRANSACTIONS\n");
					buf.append("Transactions: " + transactions.size() + "\n");
					for (int index = 0; index < transactions.size(); index++) {
						Transaction trans = transactions.get(index);
						buf.append("Transaction(" + index + "):\n");
						buf.append(trans.toString() + "\n\n");
					}
				}
				
				
				result = buf.toString();
			}
			
			
			
			
			List<Account> accounts = dao.getAccountsByClass(book, AccountClass.ASSET);
			request.setAttribute("accounts", accounts);
			request.setAttribute("book", book);
			request.getSession().setAttribute("transactions", transactions);
			
			RequestDispatcher view = request.getRequestDispatcher("/chooseAccount.jsp");
			view.forward(request, response);
			
		} else if (action.equalsIgnoreCase("view")) {  
			forward = viewHandler(request);
			RequestDispatcher view = request.getRequestDispatcher(forward);
			view.forward(request, response);
		} else if (action.equalsIgnoreCase("importTransactionHandler")) {
			String testScript = request.getParameter("testScript");
			if (!((testScript == null) || testScript.isEmpty())) {
				// vi ska testa skriptet bara. 
				log.info("Test Script");
				String title = request.getParameter("title");
				String description = request.getParameter("description");
				String script = request.getParameter("script");
				
				Account account = (Account)request.getSession().getAttribute("account");
				ScriptTransactionFactory factory = null;
				try {
					factory = new ScriptTransactionFactory(account.getBook());
				} catch (ScriptException e) {
					throw new ServletException(e);
				}

				Transaction transaction = (Transaction)request.getSession().getAttribute("transaction");
				factory.importTransactionData(transaction);
				String errMsg = null;
				EntityManager em = dao.getEm();
				try {
					em.getTransaction().begin();
					factory.setBook(em.merge(factory.getBook()));
					factory.eval(script);
					em.getTransaction().commit();
				} catch (ScriptException e) {
					errMsg = e.getMessage();
				} finally {
					if (em != null)
						em.close();
				}
								
				
				request.setAttribute("handler", new ImportTransactionHandler(title, description, script));
				if (errMsg != null)
					request.setAttribute("result", errMsg);
				else request.setAttribute("result", factory.getTransaction());
				
				forward = "/addImportTransactionHandler.jsp";
			} else {
				String addHandler = request.getParameter("addHandler");
				if (!((addHandler == null) || addHandler.isEmpty())) {
					// Vi ska lägga till en handler.
					log.info("Add Script");
					String title = request.getParameter("title");
					String description = request.getParameter("description");
					String script = request.getParameter("script");
					
					
					EntityManager em = dao.getEm();
					try {
						em.getTransaction().begin();
						Account account = (Account)request.getSession().getAttribute("account");
						Book book = em.merge(account.getBook());
						book.getImportTransactionHandlers().add(new ImportTransactionHandler(title, description, script));
						em.getTransaction().commit();
						account.setBook(book);
					} finally {
						if (em != null)
							em.close();
					}
					

					Transaction transaction = (Transaction)request.getSession().getAttribute("transaction");
					response.sendRedirect("UploadedFileController.do?action=importTransaction&transactionId=" + transaction.getId());
					return;
				}
			}
				
			
			 
			RequestDispatcher view = request.getRequestDispatcher(forward);
			view.forward(request, response);
		} else {
			forward = "index.jsp";
			response.sendRedirect(forward);
		}
		
		
		
	}
	
	public String viewHandler(HttpServletRequest request) throws IOException {
		String forward = "/fileParseView.jsp";
		long fileId = Long.parseLong(request.getParameter("fileId"));
		// Here we check for our guess of charset. Ah, we set i manually for now. 
		BookDataFile file = dao.getFile(fileId);
					
		String rawText = file.decode();
		/*
		 * .replace(">", "&lt;").replace("<", "&gt;").replace("&", "&amp;").replace("'", "&#039;").replace("\"", "&#034;")
		 *  >  - &lt;
<  - &gt;
&  - &amp;
'  - &#039;
'' - &#034;
		 */
		
		
		// alla charsets
		SortedMap charsets = Charset.availableCharsets();
		
		
		request.setAttribute("file", file);
		request.setAttribute("charsets", charsets);
		request.setAttribute("rawText", rawText);
		return forward;
	}
	
}
