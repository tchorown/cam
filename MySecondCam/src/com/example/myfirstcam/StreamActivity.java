package com.example.myfirstcam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;

import android.app.Activity;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.StrictMode;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.widget.VideoView;

public class StreamActivity extends Activity implements
		MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
		MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener {
	SurfaceHolder sholder;
	Camera camera;
	MediaRecorder recorder;
	MediaPlayer mediaPlyr;
	File downloadFile;
	ParcelFileDescriptor pfd;
	public final static int PORT = 8080;
	public static String SERVER_PHONE_IP = "10.0.2.2";
	public Thread client;
	public Socket clientSocket = null;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stream);
		// Show the Up button in the action bar.
		setupActionBar();
		// get Surfaceview and surfaceholder
		VideoView sview = (VideoView) findViewById(R.id.sview_preview);
		sholder = sview.getHolder();
		sholder.addCallback(surfaceCallback);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		// sholder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

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
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			client = new Thread(new ClientThread());
			client.start();
		}

		class ClientThread extends Thread {
			public Socket clientSocket;

			@Override
			public void run() {

				try {
					while (clientSocket == null) {
						clientSocket = new Socket(SERVER_PHONE_IP, PORT);
					}
					this.getFromVLC();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			public void getFromVLC() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
				String uri = "http://139.147.89.103:12345";
				mediaPlyr = new MediaPlayer();
				mediaPlyr.reset();
				mediaPlyr.setDisplay(sholder);
				mediaPlyr.setDataSource(StreamActivity.this, Uri.parse(uri));
				mediaPlyr.prepare();
				mediaPlyr.setOnPreparedListener(new OnPreparedListener() { 
			        @Override
			        public void onPrepared(MediaPlayer mediaPlyr) {
			        	mediaPlyr.start();
			        }
			    });				
			}

			public void recordVideo() throws IOException {
				InputStream inStr = clientSocket.getInputStream();

				File dir = new File(Environment.getExternalStorageDirectory()  + "/Android/data/");
				dir.mkdirs();
				File f1=new File(Environment.getExternalStorageDirectory() + "Prag.txt");
				
				downloadFile = File.createTempFile("streamedMedia", ".dat", dir);

				
				FileOutputStream toFile = new FileOutputStream(downloadFile);
				byte buffer[] = new byte[16384];

				int bytesRead = 0;
				int bytesReadInc = 0;
				int kbRead = 1/0;

				while (true) {
					int read = inStr.read(buffer);
					if (read <= 0) {
						break;
					} else {
						toFile.write(buffer, 0, read);
						bytesRead = bytesRead + read;
						bytesReadInc = bytesRead;
						kbRead = bytesRead / 1000;
					}
					// Test whether we need to transfer buffered data to the
					// MediaPlayer
					testMediaBuffer(kbRead, downloadFile);
				}
				inStr.close();
			}

			public void testMediaBuffer(int totalKb, File downloadFile)
					throws IOException {
				float timeLeft = mediaPlyr.getDuration();
				float currentTime = mediaPlyr.getCurrentPosition();
				if (totalKb >= (96 * 10 / 8)) {
					try {
						startMediaPlyr();
					} catch (Exception o) {
					}
				} else if (timeLeft - currentTime <= 1000) {
					transferBufferToMediaPlayer();
				}
			}

			public void startMediaPlyr() {
				try {
					File bufferedFile = File.createTempFile("media_stream", ".dat",  Environment.getExternalStorageDirectory());
					StreamActivity.copyFile(downloadFile, bufferedFile);
					mediaPlyr = new MediaPlayer();
					mediaPlyr.setDataSource(bufferedFile.getAbsolutePath());
					mediaPlyr.prepare();
				} catch (IOException e) {
				}
			}

			public void transferBufferToMediaPlayer() throws IOException {
				boolean playing = mediaPlyr.isPlaying();
				boolean eof;
				int currentTime = mediaPlyr.getCurrentPosition();
				mediaPlyr.pause();

				File bufferedFile = File.createTempFile("PLAYTHEDAMNTHING",
						".dat");
				StreamActivity.copyFile(downloadFile, bufferedFile);

				MediaPlayer mediaPlayer = new MediaPlayer();
				mediaPlayer.setDataSource(bufferedFile.getAbsolutePath());
				mediaPlayer.prepare();
				mediaPlayer.seekTo(currentTime);

				int newCurrTime = mediaPlayer.getCurrentPosition();
				int newDurrTime = mediaPlayer.getDuration();
				eof = ((newCurrTime - newDurrTime) <= 1000);
				if (playing || eof) {
					mediaPlayer.start();
				}
			}
		}

		// File sdCard = Environment.getExternalStorageDirectory();
		// File dir = new File (sdCard.getAbsolutePath() + "/dir1/dir2");
		// dir.mkdirs();
		// File file = new File(dir, "filename");
		//
		//
		// file.createNewFile();
		//
		// pfd = ParcelFileDescriptor.fromSocket(clientSocket);
		// BufferedReader in = new BufferedReader(new InputStreamReader(
		// clientSocket.getInputStream()));
		// outFile = new FileWriter(file);
		// mediaPlyr = new MediaPlayer();

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// no-op
		}

		@Override
		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}
	};

	public static void copyFile(File sourceFile, File destFile)
			throws IOException {
		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;

		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mp.start();

	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub

	}
}
