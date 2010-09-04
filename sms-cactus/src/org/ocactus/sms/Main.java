package org.ocactus.sms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity {
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button archiveButton = (Button) findViewById(R.id.archiveButton);
        archiveButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				startService(new Intent(Main.this, ArchivingService.class));
			}
		});
        
        Button sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				startService(new Intent(Main.this, SendService.class));
			}
		});
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 getMenuInflater().inflate(R.menu.main, menu);
         return super.onCreateOptionsMenu(menu);
	}
	
	 @Override
     public boolean onOptionsItemSelected(MenuItem item) {
             switch(item.getItemId()) {
             case R.id.menu_settings:
                     startActivity(new Intent(this, Settings.class));
                     return true;
             }
             return super.onOptionsItemSelected(item);
     }

}