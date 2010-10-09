package org.ocactus.sms.common;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {

	public static JSONArray getJSONArray(Sms[] sms) throws JSONException {
		JSONArray data = new JSONArray();
		for(Sms s : sms) {
			data.put(getJSONObject(s));
		}
		return data;
	}
	
	public static JSONObject getJSONObject(Sms sms) throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("phoneId", sms.getPhoneId());
		obj.put("address", sms.getAddress());
		obj.put("body", sms.getBody());
		obj.put("timestamp", sms.getTimestamp().getTime());
		obj.put("incoming", sms.isIncoming());
		return obj;
	}
	
	public static Sms[] getSms(JSONArray data) throws JSONException {
		Sms[] messages = new Sms[data.length()];
		
		for(int i = 0; i < messages.length; i++) {
			messages[i] = getSms(data.getJSONObject(i));
		}
		
		return messages;
	}
	
	public static Sms getSms(JSONObject obj) throws JSONException {
		String[] required = new String[] { "phoneId", "address", "body", "timestamp", "incoming" };
		for(String r : required) {
			if(!obj.has(r)) {
				throw new IllegalArgumentException("JSONObject is missing " + r);
			}
		}
		
		return new Sms(
			-1,
			obj.getInt("phoneId"),
			obj.getString("address"),
			obj.getString("body"),
			new Date(obj.getLong("timestamp")),
			obj.getBoolean("incoming"));
	}
	
	public static PendingSms[] getPendingSms(JSONArray data) throws JSONException {
		PendingSms[] messages = new PendingSms[data.length()];
		for(int i = 0; i < messages.length; i++) {
			messages[i] = getPendingSms(data.getJSONObject(i));
		}
		return messages;
	}
	
	public static PendingSms getPendingSms(JSONObject obj) throws JSONException {
		String[] required = new String[] { "address", "body" };
		for(String r : required) {
			if(!obj.has(r)) {
				throw new IllegalArgumentException("JSONObject is missing " + r);
			}
		}
		
		return new PendingSms(
			obj.getString("address"),
			obj.getString("body"));
	}
	
	public static JSONArray getJSONArray(PendingSms[] messages) throws JSONException {
		JSONArray data = new JSONArray();
		for(PendingSms msg : messages) {
			data.put(getJSONObject(msg));
		}
		return data;
	}
	
	public static JSONObject getJSONObject(PendingSms sms) throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("address", sms.getAddress());
		obj.put("body", sms.getBody());
		return obj;
	}
}