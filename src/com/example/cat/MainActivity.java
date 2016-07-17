package com.example.cat;
 
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Locale;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

 
public class MainActivity extends Activity {
	private HashMap<String, String> dictionary = new HashMap<String, String>();
    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    static TextView tap_on_mic; 
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dictionary.put("Включи свет", "setLightsOn");
        dictionary.put("Выключи свет", "setLightsOf");
               
        setContentView(R.layout.activity_main);
        tap_on_mic = (TextView)findViewById(R.id.tap_on_mic);
    
        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
 
        // hide the action bar
        getActionBar().hide();
 
        btnSpeak.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
 
    }
    
public static String getHash(String str) throws NoSuchAlgorithmException,
	UnsupportedEncodingException {

MessageDigest m = MessageDigest.getInstance("MD5");
m.reset();

m.update(str.getBytes("utf-8"));

String s2 = new BigInteger(1, m.digest()).toString(16);
StringBuilder sb = new StringBuilder(32);

for (int i = 0, count = 32 - s2.length(); i < count; i++) {
	sb.append("0");
}

return sb.append(s2).toString();
}
	
public static void testIt() throws IOException, NoSuchAlgorithmException, KeyManagementException {


    /* URL url = new URL("https://google.com");

     HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
     con.setRequestMethod( "POST" );
     con.setDoOutput(true);
     con.setDoInput(true);
     
     SSLContext sslContext = SSLContext.getInstance("TLS");

     TrustManager[] trustManagers = new TrustManager[] {
         new X509TrustManager() {

             public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                 return null;
             }

             public void checkClientTrusted(X509Certificate[] certs, String authType) {  }

             public void checkServerTrusted(X509Certificate[] certs, String authType) {  }

         }
     };
     tap_on_mic.setText("0");
     HostnameVerifier hostnameVerifier = new HostnameVerifier() {

         public boolean verify(String s, SSLSession sslSession) {
             return s.equals(sslSession.getPeerHost());
         }
     };
     con.setHostnameVerifier(hostnameVerifier);
     
     tap_on_mic.setText("1");
     
     sslContext.init(null, trustManagers, null);
     tap_on_mic.setText("21");
     con.setSSLSocketFactory(sslContext.getSocketFactory());
     tap_on_mic.setText("22");
     tap_on_mic.setText("23");
     OutputStream output = con.getOutputStream();  
     
     tap_on_mic.setText("2");
     String s = "data="+MainActivity.getHash("{\"method\":\"UPDATE\",\"type\":\"TEMPERATURE\",\"id\":234,\"temperature\":23.9}")+"{\"method\":\"UPDATE\",\"type\":\"TEMPERATURE\",\"id\":234,\"temperature\":23.9}";

     output.write(s.getBytes());
     output.flush();
     output.close();
    
     int responseCode = con.getResponseCode();
     
     InputStream inputStream;
     if (responseCode == HttpURLConnection.HTTP_OK) {
         inputStream = con.getInputStream();
     } else {
         inputStream = con.getErrorStream();
     }
     tap_on_mic.setText("3");
     // Process the response
     BufferedReader reader;
     String line = "";
     reader = new BufferedReader( new InputStreamReader( inputStream ) );
     while( ( line = reader.readLine() ) != null )
     {
    	 MainActivity.tap_on_mic.setText(line);
     }
     tap_on_mic.setText("4");
     inputStream.close(); */
	
	
	// Load CAs from an InputStream
	// (could be from a resource or ByteArrayInputStream or ...)
	//CertificateFactory cf = CertificateFactory.getInstance("X.509");
	// From https://www.washington.edu/itconnect/security/ca/load-der.crt
	//InputStream caInput = new BufferedInputStream(new FileInputStream("load-der.crt"));
	//Certificate ca;
	//try {
	    //ca = cf.generateCertificate(caInput);
	   // System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
	//} finally {
	//    caInput.close();
	//}

	// Create a KeyStore containing our trusted CAs
	String keyStoreType = KeyStore.getDefaultType();
	KeyStore keyStore = KeyStore.getInstance(keyStoreType);
	keyStore.load(null, null);
	keyStore.setCertificateEntry("ca", ca);

	// Create a TrustManager that trusts the CAs in our KeyStore
	String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
	TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
	tmf.init(keyStore);

	// Create an SSLContext that uses our TrustManager
	SSLContext context = SSLContext.getInstance("TLS");
	context.init(null, tmf.getTrustManagers(), null);

	// Tell the URLConnection to use a SocketFactory from our SSLContext
	URL url = new URL("https://certs.cac.washington.edu/CAtest/");
	HttpsURLConnection urlConnection =
	    (HttpsURLConnection)url.openConnection();
	urlConnection.setSSLSocketFactory(context.getSocketFactory());
	InputStream in = urlConnection.getInputStream();
	//copyInputStreamToOutputStream(in, System.out);
 }
 
    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }
 
    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       
    	super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
         case REQ_CODE_SPEECH_INPUT: {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                txtSpeechInput.setText(result.get(0));

                	if(dictionary.containsKey(result.get(0))){
                	 tap_on_mic.setText("команда распознана " + result.get(0));
						try {
						    MainActivity.testIt();
						    tap_on_mic.setText("OK");
						} catch (KeyManagementException e) {
							tap_on_mic.setText("1");
							e.printStackTrace();
						} catch (NoSuchAlgorithmException e) {
							tap_on_mic.setText("2");
							e.printStackTrace();
						} catch (IOException e) {
							
							e.printStackTrace();
						}
                	 
            		 	}
                	else{
                		tap_on_mic.setText("команда не распознана");
                	}
               
            }
            break;
         }
 
        }
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
 
