package com.example.cat;
 
import java.lang.reflect.Method;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
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
	private static Socket s; 
 
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
	
public static void testIt(){

	try
    {

        s = new Socket("10.23.46.203", 14288);
        
        String q = "qwweetwtguwfgu";

        s.getOutputStream().write(q.getBytes());
        tap_on_mic.setText("OK");

    }
    catch(Exception e)
    {tap_on_mic.setText("OK1");}
	 

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
						    MainActivity.testIt();
						    
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
 
