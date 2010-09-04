package org.ocactus.sms.prefs;

import java.net.URI;
import java.net.URISyntaxException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

public class Preferences implements IPreferences {
	
	private static final String TAG = Preferences.class.getCanonicalName();
	
	public static final String ENCODING = "UTF-8";
	
	private static final String SERVERUSR = "serverUsr";
	private static final String SERVERPWD = "serverPwd";
	private static final String SERVERURL = "serverUrl";
	private static final String ARCHIVING_CUTOFFID = "smacutoff";
	
	private SharedPreferences sharedPrefs;
	
	public Preferences(Context context) {
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public void setArchivingCutoffId(int id) {
		Editor editor = sharedPrefs.edit();
		editor.putInt(ARCHIVING_CUTOFFID, id);
		editor.commit();
	}
	
	public int getArchivingCutoffId() {
		return sharedPrefs.getInt(ARCHIVING_CUTOFFID, 0);
	}
	
	public URI getServerUrl() {
		try {
			return new URI(sharedPrefs.getString(SERVERURL, "http://localhost/sms"));
		} catch(URISyntaxException ex) {
			Log.e(TAG, "Invalid server url!", ex);
			return null;
		}
	}
	
	public String getServerUser() {
		return sharedPrefs.getString(SERVERUSR, "");
	}
	
	public String getServerPassword() {
		return sharedPrefs.getString(SERVERPWD, "");
	}
}