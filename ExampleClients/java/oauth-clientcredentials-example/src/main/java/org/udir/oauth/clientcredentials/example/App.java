package org.udir.oauth.clientcredentials.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.security.cert.X509Certificate;
import java.security.GeneralSecurityException;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;	
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.DatatypeConverter;

public class App 
{
	private final static String AUTH_SERVER_BASE = "https://localhost:44301"; 
	private final static String RESOURCE_SERVER_BASE = "https://localhost:44338";
	private final static String SCOPE = "SCOPE"; //PUT IN YOUR SCOPE HERE
	private final static String CLIENT_ID = "CLIENTID"; //PUT IN YOUR CLIENT ID HERE
	private final static String CLIENT_SECRET = "SECRET"; // PUT IN YOUR SECRET HERE
	
    public static void main( String[] args ) throws Exception {
		BypassSslTrust_NOT_FOR_PRODUCTION();

		// *** Fetch access token from authorization server using client credentials flow ***

		HttpsURLConnection con = CreateConnection(AUTH_SERVER_BASE + "/connect/token");
		String credentials = CLIENT_ID + ":" + CLIENT_SECRET;
		String credentialsBase64 = DatatypeConverter.printBase64Binary(credentials.getBytes());
		con.setRequestProperty("Authorization", "Basic " + credentialsBase64);
		con.setRequestMethod("POST");
		WriteBody(con, "grant_type=client_credentials&scope=" + SCOPE);

		ExitOnWrongResponse(con, "Could not get access token.");

        JsonObject authorization = CreateJsonReader(con).readObject();
        System.out.println("Got token of type "
        	+ authorization.getString("token_type")
        	+ ", which expires in "
        	+ authorization.getInt("expires_in"));

        // *** Call resource server using access token retrieved from authorization server ***

        HttpsURLConnection resourceCon = CreateConnection(RESOURCE_SERVER_BASE + "/api/ping");
        resourceCon.setRequestMethod("GET");
        resourceCon.setRequestProperty("Authorization", "Bearer " + authorization.getString("access_token"));
		resourceCon.setRequestProperty("Accept", "application/json");
		
		ExitOnWrongResponse(resourceCon, "Could not access resource");       
	        	
		String response = GetResponse(resourceCon);

        System.out.println("All OK. Got response: " + response);
    }

    private static void ExitOnWrongResponse(HttpsURLConnection conn, String message)
    	throws IOException {
    	int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
        	System.err.println(message + " Response code: " + responseCode);
        	System.exit(1);
        }
    }

   	private static HttpsURLConnection CreateConnection(String uri)
		throws MalformedURLException, IOException {
		URL tokenRequestUri = new URL(uri);
		HttpsURLConnection con = (HttpsURLConnection) tokenRequestUri.openConnection();
		con.setDoInput(true);
		return con;	
	}

	private static HttpsURLConnection WriteBody(HttpsURLConnection conn, String body)
		throws IOException {
		conn.setDoOutput(true);
		OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(body);
        writer.flush();
        writer.close();
        os.close();
        return conn;
	}

	private static JsonReader CreateJsonReader(HttpsURLConnection conn)
		throws UnsupportedEncodingException, IOException {
		return Json.createReader(
			new BufferedReader(
				new InputStreamReader(conn.getInputStream(), "UTF-8")));
	}

	private static String GetResponse(HttpsURLConnection conn)
		throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		StringBuilder sb = new StringBuilder();
		String line = "";
		while((line = br.readLine()) != null){
			sb.append(line);
		}

		return sb.toString();
	}

	private static void BypassSslTrust_NOT_FOR_PRODUCTION() throws GeneralSecurityException {
		TrustManager[] trustAllCerts = new TrustManager[] { 
    		new X509TrustManager() {     
        		public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
            		return new X509Certificate[0];
        		} 
        		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {} 
        		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
    		} 
		}; 

		SSLContext sc = SSLContext.getInstance("SSL"); 
	    sc.init(null, trustAllCerts, new java.security.SecureRandom()); 
    	HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}
}