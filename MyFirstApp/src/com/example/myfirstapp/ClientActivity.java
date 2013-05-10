package com.example.myfirstapp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.example.myfirstapp.ServerActivity.ServerThread;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ClientActivity extends Activity {
	// For the interface section
	// Change this editText and button to the UI section

	String sendStr = null;
	String serverOut;
	String server; // server is user input - will be default of "10.0.2.2"

	private EditText serverIP;
	private Button connect;
	private String serverIPAddr = "NOT AN IP ADDRESS";
	private String userMessage;
	private String serverReturn;
	private boolean connectionEstablished = false;
	private Handler handler = new Handler();
	private boolean connectonEstablished;
	private int activity_server;

	public BufferedReader inFromServer;
	public DataOutputStream outToServer;
	public ClientThread client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client);
		server = "10.0.2.2";
		Thread cThread = new Thread(new ClientThread());
		cThread.start();
		//Intent intent = getIntent();
		// TODO Auto-generated catch block
		//serverIP = (EditText) findViewById(activity_server);
	//	client = new Thread(new ClientThread());
//		client = new ClientThread();
//		client.run();
//		String message = serverOut; // intent.getStringExtra(DisplayMessageActivity.EXTRA_MESSAGE);
//
//		TextView editText = (TextView) findViewById(R.id.clientChat);
//		editText.append(message);
		
	}


	public void forwardMessage(View view) throws IOException {
		EditText userArea = (EditText) findViewById(R.id.client_chat_area);
		userMessage = "Client: " + userArea.getText().toString() + "\n";

		client.sendMessage(outToServer);
		
		if (client!=null){
			if (serverOut != null){
				TextView editText = (TextView) findViewById(R.id.clientChat);
				serverOut = serverOut + "\n";
				editText.append(serverOut);
			}
		}

		TextView editText = (TextView) findViewById(R.id.clientChat);
		editText.append(userMessage);
		userArea.setText("");
	}

	public class ClientThread implements Runnable {
		public void run() {
			try {
				Socket clientSocket = new Socket("10.0.2.2", 8080);
				connectonEstablished = true;
				//while(connectionEstablished){
				outToServer = new DataOutputStream(
						clientSocket.getOutputStream());

				outToServer.writeBytes("Sent message "+"\n");
				//inFromServer = new BufferedReader(new InputStreamReader(
				//clientSocket.getInputStream()));
				//this.sendMessage(outToServer);
				//serverOut = "Server:" + inFromServer.readLine() + "\n";
				//}
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}



		public void sendMessage(DataOutputStream outToServer)
				throws IOException {
			EditText userArea = (EditText) findViewById(R.id.client_chat_area);
			userMessage = "Client: " + userArea.getText().toString() + "\n";
			outToServer.writeBytes(userMessage);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.client, menu);
		return true;
	}

}