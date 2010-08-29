package org.ocactus.sms.server;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ServletBase extends HttpServlet {
	
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
	
	public abstract void defaultAction(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException;
}
