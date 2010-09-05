package org.ocactus.sms.server;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ServletBase extends HttpServlet {
	
	protected Connection dbConnection;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		
		try {
			
			ServletContext ctx = config.getServletContext();
			Class.forName(ctx.getInitParameter("dbDriver")).newInstance();
			dbConnection = DriverManager.getConnection(
				ctx.getInitParameter("dbConnection"),
				ctx.getInitParameter("dbUser"),
				ctx.getInitParameter("dbPassword"));
			
			dbConnection.setAutoCommit(false);
			
		} catch(Exception ex) {
			throw new ServletException("error connecting to database", ex);
		}
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		if(req.getPathInfo() == null) {
			defaultAction(req, resp);
			return;
		}
		
		String[] path = req.getPathInfo().substring(1).split("/");
		
		if(path.length < 1) {
			defaultAction(req, resp);
			return;
		}
		
		try {
			for(Method m : this.getClass().getDeclaredMethods()) {
				if(m.getName().equals(path[0])) {
					m.invoke(this, req, resp);
					return;
				}
			}
			
			defaultAction(req, resp);
			
		} catch(Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}
	
	public void defaultAction(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setStatus(404);
	}
	
	@Override
	public void destroy() {
		try {
			dbConnection.close();
		} catch(Exception ex) {
			log("Error closing database.", ex);
		}
	}
}
