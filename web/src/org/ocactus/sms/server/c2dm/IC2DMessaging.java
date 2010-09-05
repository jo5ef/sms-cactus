package org.ocactus.sms.server.c2dm;

public interface IC2DMessaging {
	
	void login(String email, String password);
	void registerDevice(String registrationId);
	boolean isRegisteredAndLoggedIn();
	void send();
}
