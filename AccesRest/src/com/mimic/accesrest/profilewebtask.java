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

public class profilewebtask extends AsyncTask<String, Integer, String>{

	private ProgressDialog progdialog;
	private Context context;
	private profile activity;
	private static final String debugtag = "profileBackgroundtask";
	private JSONArray respobj;
	private JSONObject post;
	
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
	protected String doInBackground(String... x) {
		String query = x[0];
		try{
			Log.d(debugtag, "profileBackground");
			String result = Mimicdatahelper.downloadFromServer(query);
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
			post = JsonObjectCheck(result);
			JSONArray posts = post.getJSONArray("posts");
				for (int x=0; x<posts.length(); x++){
				JSONObject l = posts.getJSONObject(x);
				int comments = l.getInt("commentscount");
				int likes = l.getInt("likescount");
				String username = null;
				String dpurl = null;
				String url = l.getString("url");
				String posturl = l.getString("posturls");
				String description = "This is so coooooooool. Can you please use my app?! JUST ONCE. GOD";
				Boolean bool = false;
				int postid = l.getInt("id");
				String timestamp = l.getString("time");
				mimicdata.add(new MimicData(username, dpurl, url, postid, likes, comments, posturl, description, bool, timestamp, null));
			}
				Log.d("What is post","post is: "+post);
				String Username = post.getString("username");
				Log.d("What is post","post is: "+Username);
				int followers = post.getInt("followingcount");
				int followings = post.getInt("followerscount");
				int postcount = post.getInt("postcount");
				String fullname = null;
				String profileurl = post.getString("profilepictureurl");
				
				
				
				
				profiledata.add(new profiledata(fullname, Username, followings, followers, profileurl, postcount));
				
			
			
			} catch (JSONException e){
			
				e.printStackTrace();
			}
		
		this.activity.setUser(mimicdata);
		this.activity.setUsers(profiledata);
		}

		
	public JSONObject JsonObjectCheck(String result) throws JSONException{
		try{
			JSONArray x = new JSONArray(result);
			JSONObject w = x.getJSONObject(0);
			
			return w;
		}catch (Exception e){
			JSONObject r = new JSONObject(result);
			return r;
			
		}
		
	}
	
	
	}
	


