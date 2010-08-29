package org.ocactus.sms.prefs;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;

public class Preferences implements IPreferences {
	
	public static final String ENCODING = "UTF-8";
	
	private static final String PREFERENCES = "org.ocactus.sms";
	private static final Map<String, String> DEFAULTS = new HashMap<String, String>();
	
	private static final String USER = "user";
	private static final String PASSWORD = "password";
	private static final String ARCHIVEURL = "archiveurl";
	private static final String SENDLISTURL = "sendlisturl";
	private static final String ARCHIVING_CUTOFFID = "smacutoff";
	
	static {
		DEFAULTS.put(USER, "android");
		DEFAULTS.put(PASSWORD, "aNdr01d");
		DEFAULTS.put(ARCHIVEURL, "http://172.16.0.16:8080/sms-cactus/msg/archive");
		DEFAULTS.put(SENDLISTURL, "http://172.16.0.16:8080/sms-cactus/msg/sendlist");
		DEFAULTS.put(ARCHIVING_CUTOFFID, "0");
	}
	
	private SharedPreferences sharedPreferences;
	private Context context;
	
	public Preferences(Context context) {
		this.context = context;
		sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
	}
		
	public void set(String key, String value) {
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public String get(String key) {
		return sharedPreferences.getString(key, DEFAULTS.get(key));
	}
	
	public String getMyPhoneNumber() {
		TelephonyManager phone = (TelephonyManager) context.getSystemService("phone");
		return phone.getLine1Number();
	}
	
	public int getArchivingCutoffId() {
		return Integer.parseInt(get(ARCHIVING_CUTOFFID));
	}
	
	public void setArchivingCutoffId(int id) {
		set(ARCHIVING_CUTOFFID, id + "");
	}
	
	public String getArchivingUrl() {
		return get(ARCHIVEURL);
	}
	
	public String getSendlistUrl() {
		return get(SENDLISTURL);
	}

	public String getUsername() {
		return get(USER);
	}	
	
	public String getPassword() {
		return get(PASSWORD);
	}
}