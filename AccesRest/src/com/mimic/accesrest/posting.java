package com.mimic.accesrest;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.stream.aws.Response;



public class posting extends SherlockActivity implements OnClickListener{
	private static final String LOG_TAG = "secondpost";
	private MediaPlayer mPlayer = null;
	private static String mFileName = null;
	public static AmazonClientManager clientManager = null;
	public static String bucketname = "mimic";
	private static URL newurl = null;
	public static String gg = null;
	private static EditText et = null;
	private static String Title = null;
	
    private void onPlay(boolean start){
		if(start){
			startPlaying();
		} else {
			stopPlaying();
		}
	}
	
    private void mpPrepare(){
    	mPlayer = new MediaPlayer();
    	try{
    		mPlayer.setDataSource(mFileName);
    		mPlayer.prepare();
    	} catch (IOException e){
    		Log.e(LOG_TAG, "prepare() failed");
    	}
    }
    
	private void startPlaying(){
			
			try{
				Log.d(LOG_TAG, mFileName);
				mPlayer.setDataSource(mFileName);
				mPlayer.prepare();
				mPlayer.start();
				  } catch(IOException e){
					  Log.e(LOG_TAG, "prepare() failed");
					  
				}
				
		
	}
	

	private void stopPlaying(){
		mPlayer.release();
		mPlayer = null;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(LOG_TAG, "gettingintent");
		setContentView(R.layout.post);
		Bundle bundle = getIntent().getExtras();
		Log.d(LOG_TAG, "getextras");
		mFileName = bundle.getString("audiofile");
		Log.d(LOG_TAG, mFileName);
		
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F86960")));
		getSupportActionBar().setTitle("Post");
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		final ImageButton play =(ImageButton) findViewById(R.id.play);
		et = (EditText) findViewById(R.id.captet);
		
		clientManager = new AmazonClientManager(getSharedPreferences(
				"com.mimic.accessrest", Context.MODE_PRIVATE));
		if (posting.clientManager.hasCredentials()) {
			Log.d(LOG_TAG, "has credentials");
		}else {
			Log.d(LOG_TAG, "no credentials");
		}
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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 MenuInflater inflater = getSupportMenuInflater();
		    inflater.inflate(R.menu.cont, menu);
				return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	   // Handle item selection
	   switch (item.getItemId()) {
	      case R.id.contin:
	    	  Title = et.getText().toString();
	    	  new ValidateCredentialsTask().execute();
	    	  Intent i = new Intent(posting.this,MainActivity.class);
	      		startActivity(i);
	      		finish();
	      		default:
	         return super.onOptionsItemSelected(item);
	   }
	}
	
	@Override
	public void onClick(View v) {
		
		
	}
	
	private class ValidateCredentialsTask extends
	AsyncTask<Void, Void, Response> {

		@Override
		protected Response doInBackground(Void... arg0) {


			return posting.clientManager.validateCredentials();
		}
		@Override
		protected void onPostExecute(Response response) {
			Createbucket bucket = new Createbucket();
			bucket.execute(response);
			Log.d(LOG_TAG, "onpostexecute");


		}



	}
	
	public class Createbucket extends
	AsyncTask<Response, Void, String> {


		private String data = null;
		@Override
		protected String doInBackground(Response... responses) {

			Response response = responses[0];
			if (response != null && response.requestWasSuccessful()) {
				Log.d(LOG_TAG, "Validated");
				TransferManager manager = new TransferManager(clientManager.s3());
				Log.d(LOG_TAG, "creating file");
				File filex = new File(mFileName);
				AmazonS3Client s3Client = clientManager.s3();
				try{
					final ObjectMetadata metx = new ObjectMetadata();
					metx.addUserMetadata("Content-Type", "audio/x-wav");
					PutObjectRequest por = new PutObjectRequest("tesst", "post2.wav", filex);
					por.withMetadata(metx);
					por.withCannedAcl(CannedAccessControlList.PublicRead);
					Upload upload = manager.upload(por);

					if (upload.isDone()){
						Log.d(LOG_TAG, "done");

					} 

					Log.d(LOG_TAG, upload.getDescription());
					newurl = s3Client.getUrl("tesst", "post.wav");
				
					Log.d(LOG_TAG, "creating object");
				
					Log.d(LOG_TAG, data);

					Log.d(LOG_TAG, "posting data");
				}
				catch (Exception exception){
					Log.d(LOG_TAG, "catching error");
				}


			}
			return null;


		}
		@Override
		protected void onPostExecute(String result){

			
			try{
				apacheHttpClientPost post = new apacheHttpClientPost();
				Log.d(LOG_TAG, "Thisis");
				post.execute("http://192.168.1.131:8000/api/v1/Post/");
				Log.d(LOG_TAG, "Executing gg");


			}catch (Exception exception){

			}

		}



	}
	private String getQueryJSON(List<NameValuePair> params) throws UnsupportedEncodingException

	{

		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (NameValuePair pair : params)

		{

			if (first){

				first = false;

				result.append("{");

			}else

				result.append(",");


			result.append(pair.getName());

			result.append(":");

			result.append(pair.getValue());


		}

		result.append("}");

		return result.toString();

	}
	public class apacheHttpClientPost extends AsyncTask<String,String,Void> {
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

				nameValuePairs.add(new BasicNameValuePair("\"Createdthispost\"", "\"/api/v1/User/1/\""));
				nameValuePairs.add(new BasicNameValuePair("\"Userwhodidit\"", "\"/api/v1/User/1/\""));
				nameValuePairs.add(new BasicNameValuePair("\"postlink\"", "\""+newurl.toString()+"\"")); 
				nameValuePairs.add(new BasicNameValuePair("\"title\"", "\""+Title+"\""));
				
				DefaultHttpClient httpClient = new DefaultHttpClient();
				Log.d(LOG_TAG, "httpclient");        
				HttpPost postRequest = new HttpPost(params[0]);
				StringEntity entity = new StringEntity(getQueryJSON(nameValuePairs));
				Log.d(LOG_TAG,getQueryJSON(nameValuePairs));
				postRequest.setHeader("Content-type","application/json");
				postRequest.setEntity(entity);
				Log.d(LOG_TAG, "noresponse");  
				httpClient.execute(postRequest);
				Log.d(LOG_TAG, "noresponse");  

				System.out.println("Output from Server .... \n");

				httpClient.getConnectionManager().shutdown();

			} catch (MalformedURLException e) {

				e.printStackTrace();

			} catch (IOException e) {

				e.printStackTrace();

			}
			return null;

		}

	}
	
	
	
	
	
	
	
	
}