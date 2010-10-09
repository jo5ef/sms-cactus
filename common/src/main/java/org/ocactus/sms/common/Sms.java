package org.ocactus.sms.common;

import java.util.Date;

public class Sms {

	private int id, phoneId;
	private String address, body;
	private Date timestamp;
	private boolean incoming;
	
	public Sms(int id, int phoneId, String address, String body, Date timestamp, boolean incoming) {
		super();
		this.id = id;
		this.phoneId = phoneId;
		this.address = address;
		this.body = body;
		this.timestamp = timestamp;
		this.incoming = incoming;
	}

	public int getId() {
		return id;
	}
	
	public int getPhoneId() {
		return phoneId;
	}
	
	public String getAddress() {
		return address;
	}
	
	public String getBody() {
		return body;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public boolean isIncoming() {
		return incoming;
	}
}
