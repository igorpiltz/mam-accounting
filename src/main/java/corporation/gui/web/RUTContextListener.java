package corporation.gui.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import corporation.dao.Dao;
import corporation.dao.database.JpaDao;
import corporation.dao.file.FileDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RUTContextListener implements ServletContextListener {
	
	static Logger log = LoggerFactory.getLogger(RUTContextListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		try {
			((Dao)context.getAttribute("data")).destroy();
			log.info("dao.destroy");
		} catch (Exception e) {
			log.error("dao.destroy", e);
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		try {			
			Dao dao = new JpaDao("rut-prod");
			dao.init();
			context.setAttribute("data", dao);
			log.info("dao.init");
		} catch (Exception e) {
			log.error("dao.init", e);
		}
	}


}
