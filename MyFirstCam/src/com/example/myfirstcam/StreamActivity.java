package com.example.myfirstcam;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.StrictMode;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Stream to the destination server. Uses mediacontroller and camera to record the video
 */

public class StreamActivity extends Activity {
	SurfaceHolder sholder;
	Camera camera;
	MediaRecorder recorder;
	LocalSocket mReceiver,mSender;
	InputStream ris;
	ParcelFileDescriptor pfd;
	int start;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stream);
		// Show the Up button in the action bar.
		setupActionBar();
		// get Surfaceview and surfaceholder
		SurfaceView sview = (SurfaceView) findViewById(R.id.sview_preview);
		sholder = sview.getHolder();
		sholder.addCallback(surfaceCallback);
		sholder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		//sholder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	// public void sendVideoStream(){
	/* need socket programmin */
	// }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
		public void surfaceCreated(SurfaceHolder holder) {
			camera = Camera.open(0);
			try {
				recordVideo();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		}
		//Open the mediarecorder and record the video
		public void recordVideo() {
			new Thread(new Runnable() {
				public void run() {
					try {
						createConnection();
						System.out.println("Connection created");

						} catch (Exception e) {
							e.printStackTrace();
						}
				    }
				  }).start();
			try {
				camera.setPreviewDisplay(sholder);
			} catch (IOException e) {
				e.printStackTrace();
			}		
			camera.unlock();
			MediaRecorder recorder = new MediaRecorder();
			
			recorder.setCamera(camera);
			recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
			recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
			recorder.setVideoSize(320, 240);
			
			if(sholder != null){
				recorder.setPreviewDisplay(sholder.getSurface());
			}
			while(start!=1){}
			recorder.setOutputFile(pfd.getFileDescriptor());
			//this is where the camera will set the data to write to file.
			try {
				recorder.prepare();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			recorder.start();   // Recording is now started, can do whatever with it

		}
		private void createConnection() throws Exception{
			ServerSocket welcomeSocket=new ServerSocket(6789);
			Socket connectionSocket = welcomeSocket.accept();
			pfd = ParcelFileDescriptor.fromSocket(connectionSocket);
			start =1;
		}
/**
 * Uses UDP to send the video packets to the destination
 */
//		private void sendUDP() throws Exception {
//			DatagramSocket serverSocket = new DatagramSocket(12345);
//			InetAddress IPaddress = InetAddress.getByName("10.0.2.2");
//			int port = 12345;
//			int PACKETSIZE = 1200;
//			byte[] buffer = new byte[PACKETSIZE];
//			while(true){
//				int actualread=0;
//				int totalread =0;
//				while(totalread < PACKETSIZE){
//					try {
//						actualread= ris.read(buffer,totalread,PACKETSIZE-totalread);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					totalread+=actualread;
//				}
//				DatagramPacket sendPacket = new DatagramPacket(buffer,buffer.length,IPaddress,port);
//				serverSocket.send(sendPacket);
//				
//			}
//			H263Packetizer mPacketizer=new H263Packetizer();
//			InetAddress mDestination = InetAddress.getByName("10.0.2.2");
//			
//			mPacketizer.setDestination(mDestination, 6789, 6790);
//			mPacketizer.setInputStream(mReceiver.getInputStream());
//			mPacketizer.start();
//			
			
//		}

//		private void createSockets() {
//			try {
//				LocalServerSocket lss = new LocalServerSocket("net.majorkernelpanic.librtp-"+11111);
//				mReceiver = new LocalSocket();
//				mReceiver.connect( new LocalSocketAddress("net.majorkernelpanic.librtp-" + 11111 ) );
//				mReceiver.setReceiveBufferSize(500000);
//				mSender = lss.accept();
//				mSender.setSendBufferSize(500000);
//				ris=mReceiver.getInputStream();
//
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {

			camera.startPreview();
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// no-op
		}
	};
}


