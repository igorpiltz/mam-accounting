package corporation.gui.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import corporation.dao.Dao;
import corporation.model.bookkeeping.Book;
import corporation.model.bookkeeping.convenience.BookDataFile;

 
@MultipartConfig 
public class FileUploadServlet extends HttpServlet {

	static Logger log = LoggerFactory.getLogger(FileUploadServlet.class);
	private Dao dao;

	public void init() {
		dao = (Dao)getServletContext().getAttribute("data");
	}
	

	 @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long bookId = Long.parseLong(request.getParameter("bookId"));
		Part uploadedFile = request.getPart("file"); // Retrieves <input type="file" name="uploadedFile">
        InputStream content = uploadedFile.getInputStream();
                
        Book book = dao.getBook(bookId);
        BookDataFile file = new BookDataFile();
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        do {
        	int in = content.read();
        	if (in == -1)
        		break;
        	bos.write(in);
        } while (true);
        
        
        file.setFileData(bos.toByteArray());
        
        EntityManager em = dao.getEm();
        try {
        	em.getTransaction().begin();
        	Book managed = em.merge(book);
        	managed.addBookDataFile(file);
        	em.getTransaction().commit();
        } finally {
        	if (em != null)
        		em.close();
        }
		
        response.sendRedirect("UploadedFileController.do?action=listAll&bookId=" + bookId);

    }
}
