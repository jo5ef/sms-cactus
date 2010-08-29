package org.ocactus.sms;

import org.ocactus.sms.client.CactusClient;
import org.ocactus.sms.client.ICactusClient;
import org.ocactus.sms.common.PendingSms;
import org.ocactus.sms.prefs.Preferences;
import org.ocactus.sms.send.Telephony.Sms;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

public class SendService extends Service {

	static String TAG = SendService.class.getCanonicalName();
	
	ICactusClient client;
	SendThread thread;
	
	public SendService() {
		this(null);
	}
	
	public SendService(ICactusClient client) {
		this.client = client;
	}
	
	private void ensureSetup() {
		if(client == null) {
			client = new CactusClient(new Preferences(this));
		}
		if(thread == null) {
			thread = new SendThread();
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		Log.i(TAG, "SendService started.");
		
		ensureSetup();
		thread.start();
	}
	
	@Override
	public void onDestroy() {
		Log.i(TAG, "SendService stopped.");
	}
	
	class SendThread extends Thread {
		
		@Override
		public void run() {
			try {
				
				SmsManager smsManager = SmsManager.getDefault();
				PendingSms[] sendlist = client.sendlist();
				
				for(PendingSms sms : sendlist) {
					smsManager.sendTextMessage(sms.getAddress(), null, sms.getBody(), null, null);
					Sms.Sent.addMessage(getContentResolver(),
						sms.getAddress(), sms.getBody(), null, System.currentTimeMillis());
				}
				
				Log.i(TAG, "successfully sent " + sendlist.length + " messages.");
				
			} catch(Exception ex) {
				Log.e(TAG, "Error sending messages.", ex);
			} finally {
				stopSelf();
			}
		}
	}
}
