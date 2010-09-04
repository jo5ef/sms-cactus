package org.ocactus.sms.prefs;

import java.net.URI;

public interface IPreferences {

	URI getServerUrl();
	String getServerUser();
	String getServerPassword();
	int getArchivingCutoffId();
	void setArchivingCutoffId(int id);
}
