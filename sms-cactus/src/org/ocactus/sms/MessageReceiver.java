package org.ocactus.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MessageReceiver extends BroadcastReceiver {

	static String TAG = MessageReceiver.class.getCanonicalName();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Log.i(TAG, "Received a message, starting archiving service.");

		// give it a second to put the sms that just arrived into the inbox
		try {
			Thread.sleep(1000);
		} catch(InterruptedException ex) {
			Log.e(TAG, ex.getMessage(), ex);
		}
		
		context.startService(new Intent(context, ArchivingService.class));
	}
}
