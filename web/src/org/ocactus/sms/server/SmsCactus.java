package org.ocactus.sms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.ocactus.sms.common.PendingSms;
import org.ocactus.sms.common.Sms;

public class SmsCactus implements ISmsCactus {

	private Connection db;
	
	public SmsCactus(Connection db) {
		this.db = db;
	}
	
	public Sms[] latest(int count) {
		List<Sms> data = new ArrayList<Sms>();
		
		try {
			PreparedStatement stmt = db.prepareStatement(
				"SELECT id, address, body, timestamp, incoming " +
				"FROM messages ORDER BY timestamp DESC LIMIT ?");
			
			stmt.setInt(1, count);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				data.add(new Sms(
					rs.getInt("id"),
					rs.getString("address"),
					rs.getString("body"),
					rs.getTimestamp("timestamp"),
					rs.getBoolean("incoming")));
			}
			
			rs.close();
			
		} catch(SQLException ex) {
			throw new RuntimeException(ex);
		}
		
		return data.toArray(new Sms[] { });
	}
	
	public void send(PendingSms sms) {
		
		try {
			PreparedStatement stmt = db.prepareStatement(
				"INSERT INTO sendlist (address, body) VALUES (?, ?);");
			stmt.setString(1, sms.getAddress());
			stmt.setString(2, sms.getBody());
			stmt.execute();
			db.commit();
		} catch(SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public PendingSms[] sendlist() {
		List<PendingSms> data = new ArrayList<PendingSms>();
		
		try {
			Statement stmt = db.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT id, address, body FROM sendlist");
			
			StringBuilder sb = new StringBuilder();
			sb.append("DELETE FROM sendlist WHERE id IN (-1");
			
			while(rs.next()) {
				data.add(new PendingSms(
					rs.getString("address"),
					rs.getString("body")));
				sb.append(",");
				sb.append(rs.getInt("id"));
			}
			
			rs.close();
			
			sb.append(");");
			db.createStatement().execute(sb.toString());
			db.commit();
			
		} catch(SQLException ex) {
			try { db.rollback(); } catch(SQLException e) { }
			throw new RuntimeException(ex);
		}
		
		return data.toArray(new PendingSms[] { });
	}
	
	public void archive(Sms[] data) {
		
		try {
			//TODO: batch insert
			for(Sms s : data) {
				archive(s);
			}
			
			db.commit();
			
		} catch(SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private void archive(Sms sms) throws SQLException {
		
		PreparedStatement stmt = db.prepareStatement(
			"INSERT IGNORE INTO messages (id, address, body, timestamp, incoming)" +
			"VALUES (?, ?, ?, ?, ?);");
			//"ON DUPLICATE KEY UPDATE address = ?, body = ?, timestamp = ?;");
		
		int paramIdx = 1;
		
		stmt.setInt(paramIdx++, sms.getId());
		stmt.setString(paramIdx++, sms.getAddress());
		stmt.setString(paramIdx++, sms.getBody());
		stmt.setTimestamp(paramIdx++, new Timestamp(sms.getTimestamp().getTime()));
		stmt.setBoolean(paramIdx++, sms.isIncoming());
		
		stmt.execute();
	}
}
