package com.mimic.accesrest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

public class unfollow extends AsyncTask<String,String,Void> {
	@Override
	protected Void doInBackground(String... params) {
		
		String post = params[0];

		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			Log.d("appache", "httpclient");        
			HttpDelete postRequest = new HttpDelete("http://mimictheapp.herokuapp.com/follows/?follower=1&following="+post);
			postRequest.addHeader("Authorization", "Basic " + Base64.encodeToString(("madfresco"+":"+"genesis09").getBytes(), Base64.NO_WRAP));
			postRequest.setHeader("Content-type","application/json");
			httpClient.execute(postRequest);
			Log.d("apache", "noresponse");  

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
