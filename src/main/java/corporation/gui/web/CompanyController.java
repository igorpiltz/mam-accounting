package corporation.gui.web;

import java.io.IOException;
import java.util.Currency;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import corporation.dao.Dao;
import corporation.model.Company;
import corporation.model.CompanyType;
import corporation.model.StateIdNumber;
import corporation.model.bookkeeping.Book;
import corporation.model.bookkeeping.Transaction;

public class CompanyController extends HttpServlet {
	
	static Logger log = LoggerFactory.getLogger(CompanyController.class);
	
	private static String INSERT_OR_EDIT = "/company.jsp";
    private static String LIST_USER = "/listCompany.jsp";
    private Dao dao;

	
	public void init() {
		dao = (Dao)getServletContext().getAttribute("data");
	}
	
	public void destroy() {
		
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward="";
		String action = request.getParameter("action");
		log.info("CompanyController.GET, action=" + action);
		
		if (action.equalsIgnoreCase("delete")) {
			int userId = Integer.parseInt(request.getParameter("companyId"));
			dao.deleteCompany(userId);
			forward = LIST_USER;
			request.setAttribute("companies", dao.listCompanies());    
		} else if (action.equalsIgnoreCase("edit")) {
			forward = INSERT_OR_EDIT;
			int userId = Integer.parseInt(request.getParameter("companyId"));
			Company user = dao.getCompany(userId);
			request.setAttribute("comp", user);
		} else if (action.equalsIgnoreCase("listAll")) {
			forward = LIST_USER;
			request.setAttribute("companies", dao.listCompanies());
		} else if (action.equalsIgnoreCase("bookkeeping")) {
			int userId = Integer.parseInt(request.getParameter("companyId"));
			Long bookId = dao.getCompany(userId).getBook().getId();
			if (bookId == null)
				throw new AssertionError();
			forward = "/AccountController.do?action=listAll&bookId=" + bookId;
		} else {
			forward = INSERT_OR_EDIT;
		}

		RequestDispatcher view = request.getRequestDispatcher(forward);
		view.forward(request, response);
	}
	
	public void doPost( HttpServletRequest request,
						HttpServletResponse response)
					throws IOException, ServletException {

		String id = request.getParameter("id");
        if(id == null || id.isEmpty())
        {
        	Company comp = new Company(
        			request.getParameter("name"), 
        			null, 
        			CompanyType.valueOf(request.getParameter("companyType")), 
        			Currency.getInstance("SEK"));
        	
            comp.setStateIdNumber(request.getParameter("stateIdNumber"));
                        
            dao.addCompany(comp);
        } else {
        	Company comp = dao.getCompany(Long.parseLong(id));
        	comp.setName(request.getParameter("name"));
            comp.setStateIdNumber(request.getParameter("stateIdNumber"));
            
            if (request.getParameter("companyType") != null)
            	comp.setCompanyType(CompanyType.valueOf(request.getParameter("companyType")));
        	
            dao.updateCompany(comp);
        }
        
        RequestDispatcher view = request.getRequestDispatcher(LIST_USER);
        request.setAttribute("companies", dao.listCompanies());
        view.forward(request, response);	}

}
