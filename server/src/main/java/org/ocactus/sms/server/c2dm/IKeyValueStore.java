package org.ocactus.sms.server.c2dm;

public interface IKeyValueStore {

	void put(String key, String value);
	String get(String key);
}
