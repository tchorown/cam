package com.example.myfirstcam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PlayActivity extends Activity implements SurfaceHolder.Callback{
	 MediaPlayer mediaPlyr;
	 SurfaceHolder sholder;
	 FileOutputStream fos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			File myfile = new File(getApplicationContext().getFilesDir().getPath().toString() + "/fileName.txt");
			if(!myfile.exists())myfile.createNewFile();
			fos = new FileOutputStream(myfile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		// get Surfaceview and surfaceholder
		SurfaceView sview = (SurfaceView) findViewById(R.id.sview_preview);
		sholder = sview.getHolder();
		sholder.addCallback(this);
		sholder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play, menu);
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		  new Thread(new Runnable() {
			    public void run() {
					try {
						receiveUDP();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
			  }).start();
		mediaPlyr = new MediaPlayer();
		mediaPlyr.reset();
		mediaPlyr.setDisplay(sholder);
		try {
			mediaPlyr.setDataSource(fos.getFD());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			mediaPlyr.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mediaPlyr.setOnPreparedListener(new OnPreparedListener() { 
	        @Override
	        public void onPrepared(MediaPlayer mediaPlyr) {
	        	mediaPlyr.start();
	        }
	    });				
		
		
	}

	protected void receiveUDP() throws IOException {
	    DatagramSocket clientSocket = new DatagramSocket(1234);  
		byte[] receiveData = new byte[1200];
		while(true){
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);  
			clientSocket.receive(receivePacket);  
			fos.write(receivePacket.getData(),0,receivePacket.getLength());
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}

}
