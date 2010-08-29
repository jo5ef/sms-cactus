package org.ocactus.sms.prefs;

public interface IPreferences {

	String getArchivingUrl();
	String getSendlistUrl();
	String getUsername();
	String getPassword();
	int getArchivingCutoffId();
	void setArchivingCutoffId(int id);
}
