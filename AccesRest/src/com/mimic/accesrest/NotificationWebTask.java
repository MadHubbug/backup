package com.mimic.accesrest;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class NotificationWebTask extends AsyncTask<Void, Integer, String>{

	private ProgressDialog progdialog;
	private Context context;
	private Notifications activity;
	private static final String debugtag = "profileBackgroundtask";
	private String s;
	
	public NotificationWebTask(Notifications activity){
		super();
		this.activity = activity; 
		this.context = this.activity.getApplicationContext();
		
	}
	
	@Override
	protected void onPreExecute(){
		super.onPreExecute();
		progdialog = ProgressDialog.show(this.activity, "Search", "Looking for your mimics", true, false);
	}
	
	@Override
	protected String doInBackground(Void... arg0) {
		
		try{
			Log.d(debugtag, "profileBackground");
			String result = Mimicdatahelper.downloadFromServer("http://mimictheapp.herokuapp.com/notifications/?format=json");
			return result;
		}
		catch (Exception e)
		{
			return new String();
		}
		
		
	}
	
	@Override
	protected void onPostExecute(String result){
		
		ArrayList<notificationsdata> notifdata = new ArrayList<notificationsdata>(); 
		
		progdialog.dismiss();
		
		
		if(result.length() == 0){
		
		}
		
		try{
			JSONArray respobj = new JSONArray(result);
			for (int i=0; i<respobj.length(); i++){
			JSONObject returnval = respobj.getJSONObject(i);
			JSONObject user = returnval.getJSONObject("user");
			String profile= user.getString("url");
			String username = user.getString("username");
			String dp = user.getString("profilepictureurl");
			String typeofnotif = returnval.getString("typeofnotif");
			Log.d("typeofnotif", "type: "+ typeofnotif);
			JSONObject post = returnval.getJSONObject("post");
			int posturl= post.getInt("id");
			String posts = post.getString("posturls");
			notifdata.add(new notificationsdata(profile, username, dp, typeofnotif, posturl, posts));
			
			}
			
				
		
			
			} catch (JSONException e){
			
				e.printStackTrace();
			}
		this.activity.setUser(notifdata);
		}
		
		
	}
	


