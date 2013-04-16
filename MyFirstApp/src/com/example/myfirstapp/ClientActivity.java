package com.example.myfirstapp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ClientActivity extends Activity {
	//For the interface section
	//Change this editText and button to the UI shit section
	
	String sendStr = null;
	String serverOut;
	String server; //server is user input - will be default of "10.0.2.2"
	    
	private EditText serverIP;
	private Button connect;
	
	private String serverIPAddr = "NOT AN IP ADDRESS";
	private boolean connectionEstablished  = false;
	private Handler handler = new Handler();
	private boolean connectonEstablished;
	private int activity_server;
		
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client);
		
	    serverIP = (EditText) findViewById(activity_server);
	    
	}


    public void onClick(View v) throws IOException {
    	server = "10.0.2.2";
		Socket clientSocket = null;
		try {
			clientSocket = new Socket(server, 6789);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


        DataOutputStream outToServer = null;
		try {
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        BufferedReader inFromServer = null;
		try {
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
                
        if (!connectonEstablished) {
            serverIPAddr = serverIP.getText().toString();
            while(true){
            	sendStr = ("THIS IS A STRING");
                outToServer.writeBytes(sendStr + '\n');
                serverOut = inFromServer.readLine();
            }
        }
    }
            
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.client, menu);
		return true;
	}

}