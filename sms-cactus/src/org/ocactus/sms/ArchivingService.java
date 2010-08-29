package org.ocactus.sms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ocactus.sms.client.CactusClient;
import org.ocactus.sms.client.ICactusClient;
import org.ocactus.sms.common.Sms;
import org.ocactus.sms.prefs.IPreferences;
import org.ocactus.sms.prefs.Preferences;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class ArchivingService extends Service {
	
	static String TAG = ArchivingService.class.getCanonicalName();

	ICactusClient client;
	IPreferences preferences;
	ArchivingThread thread;
	
	public ArchivingService() {
		this(null, null);
	}
	
	public ArchivingService(ICactusClient client, IPreferences preferences) {
		this.client = client;
		this.preferences = preferences;
	}
	
	private void ensureSetup() {
		if(preferences == null) {
			preferences = new Preferences(this);
		}
		
		if(client == null) {
			client = new CactusClient(preferences);
		}
		
		if(thread == null) {
			thread = new ArchivingThread();
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		Log.i(TAG, "ArchivingService started!");
		
		ensureSetup();
		thread.start();
	}
	
	@Override
	public void onDestroy() {
		Log.i(TAG, "ArchivingService stopped!");
	}
	
	class ArchivingThread extends Thread {
		
		@Override
		public void run() {
			try {
				
				List<Sms> data = new ArrayList<Sms>();
				int cutoffId = preferences.getArchivingCutoffId(); 
				
				int newCutoffId = Math.max(
					addUnarchived(data, "content://sms/inbox", cutoffId, true),
					addUnarchived(data, "content://sms/sent", cutoffId, false)
				);
				
				client.archive(data.toArray(new Sms[] {}));
				preferences.setArchivingCutoffId(newCutoffId);
				
				Log.i(TAG, "Successfully archived " + data.size() + " messages.");
				
			} catch(Exception ex) {
				Log.e(TAG, "error archiving messages", ex);
			} finally {
				stopSelf();
			}
		}
		
		private int addUnarchived(List<Sms> data, String contentUri, int cutoffId, boolean incoming) {
			
			ContentResolver contentResolver = getContentResolver();
			Cursor cursor = contentResolver.query(Uri.parse(contentUri),
				new String[] { "_id", "address", "body", "date" },
				"_id > ?", new String[] { cutoffId + "" }, "_id");
			
			while(cursor.moveToNext()) {
				data.add(fromCursor(cursor, incoming));
				cutoffId = cursor.getInt(0);
			}
			
			cursor.close();
			
			return cutoffId;
		}
		
		private Sms fromCursor(Cursor cursor, boolean incoming) {
			int paramIdx = 0;
			return new Sms(
				cursor.getInt(paramIdx++),
				cursor.getString(paramIdx++),
				cursor.getString(paramIdx++),
				new Date(cursor.getLong(paramIdx++)),
				incoming);
		}
	}
}
