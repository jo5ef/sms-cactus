package org.ocactus.sms.client;

import org.ocactus.sms.common.PendingSms;
import org.ocactus.sms.common.Sms;

public interface ICactusClient {

	void archive(Sms[] messages) throws Exception;
	PendingSms[] sendlist() throws Exception;
	
	/* c2dm */
	void registerC2DM(String id) throws Exception;
}
