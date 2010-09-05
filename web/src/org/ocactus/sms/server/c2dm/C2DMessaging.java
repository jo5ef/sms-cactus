package org.ocactus.sms.server.c2dm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class C2DMessaging implements IC2DMessaging {

	private static final String AUTH_TOKEN = "Auth";
	private static final String REGISTRATION_ID = "registration_id";

	private HttpClient httpClient;
	private IKeyValueStore store;

	public C2DMessaging(IKeyValueStore store) {
		this(store, new DefaultHttpClient());
	}
	
	public C2DMessaging(IKeyValueStore store, HttpClient httpClient) {
		this.store = store;
		this.httpClient = httpClient;
	}

	public void login(String email, String password) {
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("accountType", "HOSTED_OR_GOOGLE"));
			params.add(new BasicNameValuePair("Email", email));
			params.add(new BasicNameValuePair("Passwd", password));
			params.add(new BasicNameValuePair("service", "ac2dm"));
			params.add(new BasicNameValuePair("source", "smscactus"));

			HttpPost login = new HttpPost(
				"https://www.google.com/accounts/ClientLogin");
			login.setEntity(new UrlEncodedFormEntity(params));

			HttpResponse response = httpClient.execute(login);

			Map<String, String> data = readKeyValueEntity(response.getEntity());
			
			switch(response.getStatusLine().getStatusCode()) {
			case 200:	// successful login
				for(String k : data.keySet()) {
					store.put(k, data.get(k));
				}
				break;
			case 403:	// login failed
			default:
				throw new Exception("unexpected response " + mapToString(data));
			}

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public void registerDevice(String registrationId) {
		store.put(REGISTRATION_ID, registrationId);
	}
	
	public boolean isRegisteredAndLoggedIn() {
		String r = store.get(REGISTRATION_ID);
		String a = store.get(AUTH_TOKEN);
		return r != null && r.length() > 0 && a != null && a.length() > 0;
	}
	
	public void send() {
		
		if(!isRegisteredAndLoggedIn()) {
			throw new IllegalStateException("C2DM must be both logged in and registered to send a message!");
		}
		
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(REGISTRATION_ID, store.get(REGISTRATION_ID)));
			params.add(new BasicNameValuePair("collapse_key", "smscactus"));
			
			HttpPost send = new HttpPost("https://android.apis.google.com/c2dm/send");
			send.addHeader("Authorization", "GoogleLogin auth=" + store.get(AUTH_TOKEN));
			send.setEntity(new UrlEncodedFormEntity(params));
			
			HttpResponse response = httpClient.execute(send);
			
			Map<String, String> data = readKeyValueEntity(response.getEntity());
			
			switch(response.getStatusLine().getStatusCode()) {
			case 200:
				if(data.containsKey("Error")) {
					String error = data.get("Error");
					if(error.equals("InvalidRegistration") || error.equals("NotRegistered")) {
						store.put(REGISTRATION_ID, "");
					} else {
						throw new Exception("unexpected error: " + error);
					}
				} else if(data.containsKey("id")) {
					System.out.println("Message sent! id=" + data.get("id"));
				}
				break;
			case 401:	// clientlogin auth token is invalid
				store.put(AUTH_TOKEN, "");
				break;
			case 503:	// service temporarily unavailable
			default:
				throw new Exception("unexpected response " + mapToString(data));
			}
			
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private static Map<String, String> readKeyValueEntity(HttpEntity entity) throws IOException {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()), 8 * 1024);
		String line;
		Map<String, String> data = new Hashtable<String, String>();
		
		while ((line = reader.readLine()) != null) {
			String[] parts = line.split("=", 2);
			if(parts.length == 2) {
				data.put(parts[0], parts[1]);
			} else {
				data.put(line, "");
			}
		}
		
		reader.close();
		
		return data;
	}
	
	private static String mapToString(Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		for(String k : map.keySet()) {
			sb.append(k);
			sb.append(':');
			sb.append(map.get(k));
			sb.append(',');
		}
		return sb.toString();
	}
}
