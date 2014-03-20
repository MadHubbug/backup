package com.mimic.accesrest;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

import com.facebook.UiLifecycleHelper;
import com.mimic.accesrest.posting.apacheHttpClientPost;
import com.stream.aws.Response;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.preference.PreferenceManager;

public class signup extends SherlockFragmentActivity  implements OnClickListener{

private final String logtag ="Signuptag";
//private Session.StatusCallback statusCallback = new SessionStatusCallback();

private UiLifecycleHelper uiHelper;		
private boolean isResumed = false;
public static AmazonClientManager clientManager = null;
public int fbid = 0;
private static final String LOG_TAG = "signup";
public ImageView dp;
public SharedPreferences prefs;
public EditText name, email, username, password;
public SharedPreferences.Editor editor;
public String firstname, lastname, namebun, usernamebun, passwordbun, emailbun;
private static byte[] buff = new byte[1024];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F86960")));
		getSupportActionBar().setTitle("Sign Up");
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		editor = prefs.edit();
		name = (EditText) findViewById(R.id.editText1);
		email = (EditText) findViewById(R.id.editText2);
		username = (EditText) findViewById(R.id.editText3);
		password = (EditText) findViewById(R.id.editText4);
		dp = (ImageView) findViewById(R.id.dppic);
		Bundle bundle = getIntent().getExtras();
		namebun = bundle.getString("name");
		name.setText(namebun);
		username.setText(bundle.getString("username"));
		fbid = bundle.getInt("fbid");
		lastname = bundle.getString("last_name");
		firstname = bundle.getString("first_name");
		getuserPic x = new getuserPic();
		x.execute();
		
		
		
		
		
		clientManager = new AmazonClientManager(getSharedPreferences(
				"com.mimic.accessrest", Context.MODE_PRIVATE));
		if (signup.clientManager.hasCredentials()) {
			Log.d(LOG_TAG, "has credentials");
		}else {
			Log.d(LOG_TAG, "no credentials");
		}
		
		Button signup = (Button) findViewById(R.id.Signup);
		signup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				emailbun = email.getText().toString();
				usernamebun = username.getText().toString();
				passwordbun = password.getText().toString();
				
				new ValidateCredentialsTask().execute();
				
				editor.putString("fullname", name.getText().toString()); 
				editor.putString("username", username.getText().toString());
				editor.putString("email", email.getText().toString());
				editor.putInt("fbid", fbid);
				editor.putString("lastname", lastname);
				editor.putString("firstname", firstname);
				editor.putString("dpurl", "http://graph.facebook.com/"+fbid+"/picture?type=large");
				editor.commit();
				
			}
		});
	
		
//		uiHelper = new UiLifecycleHelper(this, statusCallback);
//	    uiHelper.onCreate(savedInstanceState); 
//		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
//
//		 Session session = Session.getActiveSession();
//	        if (session == null) {
//	            if (savedInstanceState != null) {
//	                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
//	            }
//	            if (session == null) {
//	                session = new Session(this);
//	            }
//	            Session.setActiveSession(session);
//	            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
//	                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
//	            }
//	        }
//        updateView();
	}
	private class ValidateCredentialsTask extends
	AsyncTask<Void, Void, Response> {

		@Override
		protected Response doInBackground(Void... arg0) {


			return signup.clientManager.validateCredentials();
		}
		@Override
		protected void onPostExecute(Response response) {
			Createbucket bucket = new Createbucket();
			bucket.execute(response);
			Log.d(LOG_TAG, "onpostexecute");


		}



	}

	

//	 @Override
//	    public void onStart() {
//	        super.onStart();
//	        Session.getActiveSession().addCallback(statusCallback);
//	    }
//
//	    @Override
//	    public void onStop() {
//	        super.onStop();
//	        Session.getActiveSession().removeCallback(statusCallback);
//	    }
//	    
//	    
//	    @Override
//	    protected void onSaveInstanceState(Bundle outState) {
//	        super.onSaveInstanceState(outState);
//	        Session session = Session.getActiveSession();
//	        Session.saveSession(session, outState);
//	    }
//	    public void updateView() {
//	        Session session = Session.getActiveSession();
//	        if (session.isOpened()) {
//	        	 Intent i = new Intent(signup.this,MainActivity.class);
//	        	 i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);   
//	        	 startActivity(i);
//	        	 finish();
//	            
//	        	        }
//	    }
//
//	   
//	    public class SessionStatusCallback implements Session.StatusCallback {
//	        @Override
//	        public void call(Session session, SessionState state, Exception exception) {
//	            updateView();
//	        }
//	    }
	
	public class getuserPic extends
	AsyncTask<Void, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(Void... userid) {
			String imageURL;
		    Bitmap bitmap = null;
		    Log.d(LOG_TAG, "Loading Picture");
		    imageURL = "http://graph.facebook.com/"+fbid+"/picture?type=large";
		    try {
		        bitmap = BitmapFactory.decodeStream((InputStream)new URL(imageURL).getContent());
		    } catch (Exception e) {
		        Log.d("TAG", "Loading Picture FAILED");
		        e.printStackTrace();
		    }
			return bitmap;

		}
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

			BitmapShader shader = new BitmapShader (bitmap,  TileMode.CLAMP, TileMode.CLAMP);
			Paint paint = new Paint();
			        paint.setShader(shader);

			Canvas c = new Canvas(circleBitmap);
			c.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, bitmap.getWidth()/2, paint);

			dp.setImageBitmap(circleBitmap);


		}

	}

	
	
	@Override
	public void onClick(View v) {

		
	}
	

	


	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

	}
	
	public class Createbucket extends
	AsyncTask<Response, Void, String> {


		private String data = null;
		@Override
		protected String doInBackground(Response... responses) {

			Response response = responses[0];
			if (response != null && response.requestWasSuccessful()) {
				
				TransferManager manager = new TransferManager(clientManager.s3());
				AmazonS3Client s3Client = clientManager.s3();
				Log.d(LOG_TAG, "clientmanager");
				try{
					CreateBucketRequest request = new CreateBucketRequest(String.valueOf(fbid)+"mimicbucket");
			        Bucket bucket = s3Client.createBucket(request);
					
					Log.d(LOG_TAG,bucket.getName());
					

				
				}
				catch (Exception exception){
					Log.d(logtag, exception.getMessage());
				}


			}
			return null;


		}
		@Override
		protected void onPostExecute(String result){
			try{
				apacheHttpClientPost post = new apacheHttpClientPost();
				Log.d(LOG_TAG, "Thisis");
				post.execute("http://192.168.1.131:8006/api/v1/create_user/?format=json");
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
			String retval = null;
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

				nameValuePairs.add(new BasicNameValuePair("\"username\"", "\""+usernamebun+"\""));
				nameValuePairs.add(new BasicNameValuePair("\"first_name\"", "\""+firstname+"\""));
				nameValuePairs.add(new BasicNameValuePair("\"last_name\"", "\""+lastname+"\"")); 
				nameValuePairs.add(new BasicNameValuePair("\"email\"", "\""+emailbun+"\""));
				nameValuePairs.add(new BasicNameValuePair("\"password\"", "\""+passwordbun+"\""));
				nameValuePairs.add(new BasicNameValuePair("\"fbid\"", "\""+fbid+"\""));
				
				
				DefaultHttpClient httpClient = new DefaultHttpClient();
				Log.d(LOG_TAG, "httpclient");        
				HttpPost postRequest = new HttpPost(params[0]);
				StringEntity entity = new StringEntity(getQueryJSON(nameValuePairs));
				Log.d(LOG_TAG,getQueryJSON(nameValuePairs));
				postRequest.setHeader("Content-type","application/json");
				postRequest.setEntity(entity);
				Log.d(LOG_TAG, "noresponse");  
		
				Log.d(LOG_TAG, "noresponse");  
				httpClient.execute(postRequest);
				
				

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


		
		

	