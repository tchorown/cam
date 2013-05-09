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
	 boolean freceived;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		SurfaceView sview = (SurfaceView) findViewById(R.id.sview_preview);
		sholder = sview.getHolder();
		sholder.addCallback(this);
		sholder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.play, menu);
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		mediaPlyr = new MediaPlayer();
		mediaPlyr.reset();
		mediaPlyr.setDisplay(sholder);
		try {
			System.out.println("Debug-setting up rtsp");
			mediaPlyr.setDataSource("rtsp://v6.cache1.c.youtube.com/CjYLENy73wIaLQmbdQAJyAH5WBMYDSANFEIJbXYtZ29vZ2xlSARSBXdhdGNoYN-xj96xp425UQw=/0/0/0/video.3gp");
		} catch (Exception e) {
			e.printStackTrace();
		}
		mediaPlyr.prepareAsync();

		mediaPlyr.setOnPreparedListener(new OnPreparedListener() { 
	        @Override
	        public void onPrepared(MediaPlayer mediaPlyr) {
				System.out.println("Preparing");
	        	mediaPlyr.start();
	        }
	    });				
		
		
	}

//	protected void receiveUDP() throws IOException {
//	    DatagramSocket clientSocket = new DatagramSocket(1234);  
//		byte[] receiveData = new byte[1200];
//		while(true){
//			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);  
//			clientSocket.receive(receivePacket);
//			freceived=true;
//			fos.write(receivePacket.getData(),0,receivePacket.getLength());
//		}
//	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {		
	}

}
