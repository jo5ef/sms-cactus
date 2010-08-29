package org.ocactus.sms.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.ocactus.sms.common.JSONUtils;
import org.ocactus.sms.common.PendingSms;
import org.ocactus.sms.common.Sms;
import org.ocactus.sms.prefs.IPreferences;
import org.ocactus.sms.prefs.Preferences;

public class CactusClient implements ICactusClient {

	IPreferences preferences;
	HttpClient httpClient;
	
	public CactusClient(IPreferences preferences) {
		this.preferences = preferences;
		httpClient = new DefaultHttpClient();
	}
	
	private void setupRequest(HttpRequest req) {
		req.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(
			preferences.getUsername(), preferences.getPassword()), Preferences.ENCODING, false));
	}
	
	public void archive(Sms[] messages) throws Exception {
		
		JSONArray data = JSONUtils.getJSONArray(messages);
		
		HttpPost httpPost = new HttpPost(preferences.getArchivingUrl());
		setupRequest(httpPost);
		httpPost.setEntity(new StringEntity(data.toString(), Preferences.ENCODING));
		HttpResponse httpResponse = httpClient.execute(httpPost);
		if(httpResponse.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("server error " + httpResponse.getStatusLine()); 
		}
	}
	
	public PendingSms[] sendlist() throws Exception {
		
		HttpGet req = new HttpGet(preferences.getSendlistUrl());
		setupRequest(req);
		HttpResponse resp = httpClient.execute(req);
		if(resp.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("server error " + resp.getStatusLine());
		}
		
		JSONArray data = new JSONArray();
		HttpEntity responseEntity = resp.getEntity();
		if(responseEntity != null) {
			
			StringBuffer sb = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(responseEntity.getContent()), 8 * 1024);
			String line;
			while((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
			
			data = new JSONArray(sb.toString());
		}
		
		return JSONUtils.getPendingSms(data);
	}
}
