package org.ocactus.sms.common;

import java.util.Date;

public class Sms {

	private int id;
	private String address, body;
	private Date timestamp;
	private boolean incoming;
	
	public Sms(int id, String address, String body, Date timestamp, boolean incoming) {
		super();
		this.id = id;
		this.address = address;
		this.body = body;
		this.timestamp = timestamp;
		this.incoming = incoming;
	}

	public int getId() {
		return id;
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
