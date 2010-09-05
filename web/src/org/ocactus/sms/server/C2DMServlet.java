package org.ocactus.sms.server;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ocactus.sms.common.Utils;
import org.ocactus.sms.server.c2dm.C2DMessaging;
import org.ocactus.sms.server.c2dm.IC2DMessaging;
import org.ocactus.sms.server.db.Database;

public class C2DMServlet extends ServletBase {

	private IC2DMessaging c2dm;
	
	public C2DMServlet() {
		this(null);
	}
	
	public C2DMServlet(IC2DMessaging c2dm) {
		this.c2dm = c2dm;
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		if(c2dm == null) {
			c2dm = new C2DMessaging(new Database(dbConnection));
		}
	}
	
	public void login(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		
		if(Utils.isNullOrEmpty(email) || Utils.isNullOrEmpty(password)) {
			resp.setStatus(400);
			resp.getWriter().write("missing email or password");
		} else {
			c2dm.login(email, password);
		}
	}
	
	public void register(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {

		String registrationId = req.getParameter("id");
		
		if(registrationId != null) {
			c2dm.registerDevice(registrationId);
			resp.sendRedirect(req.getContextPath());			
		} else {
			resp.setStatus(400);
			resp.getWriter().write("id missing");
		}
	}
	
	public void send(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		
		if(!c2dm.isRegisteredAndLoggedIn()) {
			resp.setStatus(401);
			resp.getWriter().write("not logged in or registered");
		} else {
			c2dm.send();
			resp.sendRedirect(req.getContextPath());
		}
	}
}
