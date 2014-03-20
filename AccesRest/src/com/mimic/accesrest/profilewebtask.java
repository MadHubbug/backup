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

public class profilewebtask extends AsyncTask<Void, Integer, String>{

	private ProgressDialog progdialog;
	private Context context;
	private profile activity;
	private static final String debugtag = "profileBackgroundtask";
	
	public profilewebtask (profile activity){
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
			String result = profiledatahelper.downloadFromServer();
			return result;
		}
		catch (Exception e)
		{
			return new String();
		}
		
		
	}
	
	@Override
	protected void onPostExecute(String result){
		
		ArrayList<profiledata> profiledata= new ArrayList<profiledata>(); 
		ArrayList<MimicData> mimicdata = new ArrayList<MimicData>();
		progdialog.dismiss();
		
		
		if(result.length() == 0){
			this.activity.alert("Nada denada");
		}
		
		try{
			JSONObject respobj = new JSONObject(result);
			JSONArray posts = respobj.getJSONArray("Posts");
			for (int i=0; i<posts.length(); i++){
				JSONObject post= posts.getJSONObject(i);
				String title = post.getString("title");
				int share = post.getInt("share");
				int comment = post.getInt("likecounter");
				int likes = post.getInt("commentcounter");
				String username = respobj.getString("Username");
				mimicdata.add(new MimicData(username, title, likes, comment, share));
			}
				String Username = respobj.getString("Username");
				int followers = respobj.getInt("followers");
				int followings = respobj.getInt("following");
				String fullname = respobj.getString("fullname");
				
				
				
				profiledata.add(new profiledata(fullname, Username, followings, followers));
				
			
			
			} catch (JSONException e){
			
				e.printStackTrace();
			}
		
		this.activity.setUser(mimicdata);
		this.activity.setUsers(profiledata);
		}
		
		
	}
	


