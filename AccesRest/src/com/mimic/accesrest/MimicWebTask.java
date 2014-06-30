package com.mimic.accesrest;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class MimicWebTask extends AsyncTask<Void, Integer, String>{

	private ProgressDialog progdialog;
	private Context context;
	private MainActivity activity;
	private static final String debugtag = "Backgroundtask";
	
	public MimicWebTask(MainActivity activity){
		super();
		this.activity = activity; 
		this.context = this.activity.getApplicationContext();
		
	}
	
	@Override
	protected void onPreExecute(){
		super.onPreExecute();
		progdialog = ProgressDialog.show(this.activity, "Search", "Looking for mimics", true, false);
	}
	
	@Override
	protected String doInBackground(Void... arg0) {
		
		try{
			Log.d(debugtag, "Background");
			String result = Mimicdatahelper.downloadFromServer("http://mimictheapp.herokuapp.com/posts/?format=json");
			return result;
		}
		catch (Exception e)
		{
			return new String();
		}
		
		
	}
	
	@Override
	protected void onPostExecute(String result){
		
		ArrayList<MimicData> mimicdata= new ArrayList<MimicData>(); 
		progdialog.dismiss();
		
		
		if(result.length() == 0){	
			this.activity.alert("Nada denada");
		}
		
		try{
			JSONArray array= new JSONArray(result);
			for (int i=0; i<array.length(); i++){
				JSONObject post= array.getJSONObject(i);
				JSONObject user = post.getJSONObject("user");
				String username = user.getString("username");
				String dpurl = user.getString("profilepictureurl");
				String profileurl = user.getString("url");
				String url= post.getString("url");
				String posturl = post.getString("posturls");
				String description = post.getString("description");
				if (description == "null"){
					description = " ";
				}
				int comments = post.getInt("commentscount");
				int likes= post.getInt("likescount");
				int postid = post.getInt("id");
				Boolean likesbool = post.getBoolean("favourites");
				String timestamp = post.getString("time");
				
				
				mimicdata.add(new MimicData(username, dpurl, url, postid, likes, comments, posturl, description, likesbool, timestamp, profileurl));
				
			}
			
			} catch (JSONException e){
			
				e.printStackTrace();
			}
		
		this.activity.setUsers(mimicdata);
		}
		
		
	}
	


