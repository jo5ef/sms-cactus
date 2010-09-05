package org.ocactus.sms.server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.ocactus.sms.server.c2dm.IKeyValueStore;

public class Database implements IKeyValueStore {

	private Connection db;
	
	public Database(Connection db) {
		this.db = db;
	}
	
	public String get(String key) {
		try {
			
			String value = null;
			
			PreparedStatement stmt = db.prepareStatement("SELECT value FROM c2dm WHERE `key` = ?;");
			stmt.setString(1, key);
			
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				value = rs.getString(1);
			}
			rs.close();
			
			return value;
			
		} catch(SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public void put(String key, String value) {
		try {
			
			PreparedStatement stmt = db.prepareStatement(
				"INSERT INTO c2dm VALUES (?,?) ON DUPLICATE KEY UPDATE value = ?;");
			
			int paramIdx = 1;
			stmt.setString(paramIdx++, key);
			stmt.setString(paramIdx++, value);
			stmt.setString(paramIdx++, value);
			
			stmt.execute();
			db.commit();
			
		} catch(SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
}
