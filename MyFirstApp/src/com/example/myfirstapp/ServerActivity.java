package com.example.myfirstapp;


//5556 will be the phone B on the example android page
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class ServerActivity extends Activity {
	private String keywordin, output;
	private Socket connectionSocket;
	private BufferedReader inFromClient;
	private DataOutputStream outToClient;

	public static String SERVER_PHONE_IP = "10.0.2.15";
	public static final int PORT = 92;
	public static final int SERVER_PORT = 12345;
	public boolean connectionEstablished = false; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server);
		SERVER_PHONE_IP = getServerIp(); // address of phone
		
		Thread serverThreadStart = new Thread(new ServerThread());
		serverThreadStart.start();
	

		// TODO Auto-generated catch block

		// try {
		// this.serverSetUp();
		// message = "this is a server";
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// / }

		// Create the text view	
		String message = SERVER_PHONE_IP;

		
		long startVal = System.currentTimeMillis();

		
		while( ( (System.currentTimeMillis()/1000) - startVal/1000) < 10 ){
			int a = 0;
		}
		
		if( connectionEstablished == true) {
			message = keywordin;
		}
		
		TextView textView = new TextView(this);
		textView.setTextSize(40);
		textView.setText(message);

		// Set the text view as the activity layout
		setContentView(textView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.server, menu);
		return true;
	}

	private String getServerIp() {
		// code for finding phone IP address adopted from
		// http://thinkandroid.wordpress.com/2010/03/27/incorporating-socket-programming-into-your-applications/
		String ipString = new String("10.0.2.15");
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				ipString = "FIRST STAGE";
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					ipString = "SECOND STAGE";
					if (!inetAddress.isLoopbackAddress()) {
						ipString = inetAddress.getHostAddress().toString();
						return ipString;
					}
				}
			}
		} catch (SocketException exception) {
			Log.e("ServerActivity", exception.toString());
		}
		return ipString;
	}


	public void closeConnection(ServerSocket socketConnection) {
		try {
			socketConnection.close();
		} catch (IOException error) {
			error.printStackTrace();
		}
	}

	
	public class ServerThread implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			ServerSocket socketConnection = null;
			try {
				// Create a new socket connection!
				socketConnection = new ServerSocket(12345);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			while (true) {
				try {
					Socket connectionSocket = socketConnection.accept();
					connectionEstablished = true;
				    inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
					keywordin = (inFromClient.readLine());
					
				    outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			        outToClient.writeBytes("THIS IS MESSAGE FROM SERVER \n");
				    
				} catch (SocketException e) {
				} catch (IOException e) {
					break;
				}
			}
		}
	}
}
