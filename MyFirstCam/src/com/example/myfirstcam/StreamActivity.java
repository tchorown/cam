package com.example.myfirstcam;

import java.io.IOException;


import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class StreamActivity extends Activity {
	SurfaceHolder sholder;
	Camera camera;
	MediaRecorder recorder;

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
				camera.setPreviewDisplay(holder);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.recordVideo();
		}

		public void recordVideo() {
			int start = 0;
			String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
			mFileName += "/youraudiofile.3gp";
			
			try {
				camera.setPreviewDisplay(sholder);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			camera.unlock();
			
			MediaRecorder recorder = new MediaRecorder();
			
			recorder.setCamera(camera);
			
			
			recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
			recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
			recorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			recorder.setVideoSize(320, 240);
			
			if(sholder != null){
				recorder.setPreviewDisplay(sholder.getSurface());
			}
			
			recorder.setOutputFile(mFileName);
			//this is where the camera will set the data to write to file.
			try {
				recorder.prepare();
				start = 1;
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block4
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		//	camera.unlock();
			if (start == 1){
				recorder.start();   // Recording is now started, can do whatever with it
			}

//			
//			recorder = new MediaRecorder();
//			recorder.reset();
//			recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//			recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//			recorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
//			recorder.setOutputFile(writeFile);
//			
//			try {
//				recorder.prepare();
//			} catch (IllegalStateException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			recorder.start();
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {

			camera.startPreview();
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// no-op
		}
	};
}


