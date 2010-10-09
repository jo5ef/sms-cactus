package org.ocactus.sms.common;

public class PendingSms {

	private String address, body;
	
	public PendingSms(String address, String body) {
		this.address = address;
		this.body = body;
	}

	public String getAddress() {
		return address;
	}

	public String getBody() {
		return body;
	}
}
