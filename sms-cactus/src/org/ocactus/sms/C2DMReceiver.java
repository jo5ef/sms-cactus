package org.ocactus.sms;

import java.io.IOException;

import org.ocactus.sms.client.CactusClient;
import org.ocactus.sms.prefs.Preferences;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.c2dm.C2DMBaseReceiver;

public class C2DMReceiver extends C2DMBaseReceiver {

	private static final String TAG = C2DMReceiver.class.getCanonicalName();
	
	public C2DMReceiver() {
		super("joe.blooming@gmail.com");
	}
	
	@Override
	public void onRegistrered(Context context, String registrationId)
			throws IOException {
		
		try {
			CactusClient cc = new CactusClient(new Preferences(context));
			cc.registerC2DM(registrationId);
			Log.i(TAG, "Registration successful. Registration Id: " + registrationId);
		} catch(Exception ex) {
			Log.e(TAG, ex.getMessage(), ex);
		}
	}
	
	@Override
	public void onError(Context context, String errorId) {
		Log.e(TAG, "Registration error! " + errorId);
	}
	
	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "C2DM Message received, starting Sync");
		context.startService(new Intent(context, ArchivingService.class));
		context.startService(new Intent(context, SendService.class));
	}
}
