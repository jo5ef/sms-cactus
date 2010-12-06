package org.ocactus.sms.server;

import org.ocactus.sms.common.PendingSms;
import org.ocactus.sms.common.Sms;

public interface ISmsCactus {

	void archive(Sms[] data);
	Sms[] list(int minId, int count);
	void send(PendingSms sms);
	PendingSms[] sendlist();
}
