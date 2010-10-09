package org.ocactus.sms.server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONTokener;
import org.ocactus.sms.common.JSONUtils;
import org.ocactus.sms.common.PendingSms;
import org.ocactus.sms.common.Sms;
import org.ocactus.sms.common.Utils;

public class MsgServlet extends ServletBase {
	
	private static final Charset ENCODING = Charset.forName("UTF-8");
	
	private static void setupResponse(HttpServletResponse resp) {
		resp.setCharacterEncoding(ENCODING.displayName());
		resp.setContentType("application/json");
	}

	public void latest(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		
		Connection db = getDbConnection();
		ISmsCactus server = new SmsCactus(db);
		
		try {
			setupResponse(resp);	
			Sms[] latest = server.latest(25);
			
			JSONArray data = JSONUtils.getJSONArray(latest);
			data.write(resp.getWriter());
			
			db.close();
			
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public void send(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		
		String address = req.getParameter("address");
		String text = req.getParameter("text");
		
		if(Utils.isNullOrEmpty(address) || Utils.isNullOrEmpty(text)) {
			resp.setStatus(400);
			resp.getWriter().write("missing text or address");
		} else {
			
			Connection db = getDbConnection();
			ISmsCactus server = new SmsCactus(db);
			
			try {
				server.send(new PendingSms(address, text));
				db.close();
				
				resp.sendRedirect("../c2dm/send");
				
			} catch(Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	}
	
	public void sendlist(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		
		Connection db = getDbConnection();
		ISmsCactus server = new SmsCactus(db);
		
		try {
			setupResponse(resp);
			
			PendingSms[] sendlist = server.sendlist();
			JSONArray data = JSONUtils.getJSONArray(sendlist);
			data.write(resp.getWriter());
			
			db.close();
			
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public void archive(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		
		Connection db = getDbConnection();
		ISmsCactus server = new SmsCactus(db);
		
		try {
			JSONArray data = new JSONArray(new JSONTokener(
				new InputStreamReader(req.getInputStream(), ENCODING)));
			
			server.archive(JSONUtils.getSms(data));
			
			db.close();
			
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
