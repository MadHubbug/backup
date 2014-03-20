package com.mimic.accesrest;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import com.actionbarsherlock.app.SherlockActivity;
import com.mimic.accesrest.ExtAudioRecorder;
import com.stream.aws.Response;



public class post extends SherlockActivity {
	private static final String LOG_TAG = "AudioRecordTest";
	private static String mFileName = null;
	
	
	private ExtAudioRecorder mRecorder = null;
	
	
	private MediaPlayer mPlayer = null;
	TextView tv;
	private static final String Logs = "postui";
	
	private void onRecord (boolean start){
		if (start){
			startRecording();
		} else
			stopRecording();
	}
	
	private void onPlay(boolean start){
		if(start){
			startPlaying();
		} else {
			stopPlaying();
		}
	}
	
	private void startPlaying(){
		mPlayer = new MediaPlayer();
		try{
			mPlayer.setDataSource(mFileName);
			mPlayer.prepare();
			mPlayer.start();
		} catch (IOException e){
			Log.e(LOG_TAG, "prepare() failed");
		}
	}
	
	private void stopPlaying(){
		mPlayer.release();
		mPlayer = null;
	}
	
	private void startRecording(){
		// Start recording
		//mRecorder = ExtAudioRecorder.getInstanse(true);	  // Compressed recording (AMR)
		mRecorder = ExtAudioRecorder.getInstanse(false); // Uncompressed recording (WAV)
		mRecorder.setOutputFile(mFileName);
		
		
		mRecorder.prepare();
		mRecorder.start();
	}
	
	private void stopRecording(){
		
		mRecorder.stop();
		mRecorder.release();

	}
	
	public post(){
		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
		mFileName += "/audiorecordtest.wav";
	}
	

	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recording);
		tv = (TextView) findViewById(R.id.countdown);
		Button next = (Button) findViewById(R.id.next);
		
		next.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				 Intent i = new Intent(post.this,posting.class);
				 i.putExtra("audiofile", mFileName);
				 Log.e(Logs, "putextraintent");
				 startActivity(i);
				 finish();
				 Log.e(Logs, "startactivity");
	          	
			}
			
		});
		final MyCounter timer = new MyCounter(20000, 1000);
		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		getWindow().setGravity(Gravity.BOTTOM);
		final ImageButton record = (ImageButton) findViewById(R.id.view1);
		record.setOnClickListener(new OnClickListener(){
			boolean mStartRecording = true;
			
			@Override
			public void onClick(View v) {
				onRecord(mStartRecording);
				if (mStartRecording){
					timer.start();
					record.setImageResource(R.drawable.bordsh);
				} else{
					timer.cancel();
					record.setImageResource(R.drawable.shape);
				}
				mStartRecording = !mStartRecording;
			}
				
			
			
		});
		
		final ImageButton play =(ImageButton) findViewById(R.id.playbutt);
		play.setOnClickListener(new OnClickListener(){
			boolean mStartPlaying = true;
			
			@Override
			public void onClick(View v) {
				onPlay(mStartPlaying);
				if (mStartPlaying){
					play.setImageResource(R.drawable.ic_action_stop);
				} else{
					play.setImageResource(R.drawable.ic_action_play);
				}
				mStartPlaying = !mStartPlaying;
			}
				
			
			
		});
	}	
		
		public class MyCounter extends CountDownTimer{

		    public MyCounter(long millisInFuture, long countDownInterval) {
		        super(millisInFuture, countDownInterval);
		    }

		    

		    @Override
		    public void onTick(long millisUntilFinished  ) {
		        tv.setText(("0:" +  millisUntilFinished/1000));
		        
		    }



			@Override
			public void onFinish() {
				
				Log.d(LOG_TAG, "Timer Completed.");
			}
		
	}
		
	
	
}
	
