package com.mimic.accesrest;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


public class commentwebtask extends AsyncTask<Integer, Integer, String>{

		private ProgressDialog progdialog;
		private Context context;
		private comment activity;
		private static final String debugtag = "Backgroundtask";
		
		public commentwebtask(comment comment){
			super();
			this.activity = comment; 
			this.context = this.activity.getApplicationContext();
			
		}
		
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			progdialog = ProgressDialog.show(this.activity, "Search", "Looking for mimics", true, false);
		}
		
		@Override
		protected String doInBackground(Integer... posts) {
			int post = posts[0];
			
			try{
				Log.d(debugtag, "Background");
				String result = commentdatahelper.downloadFromServer(post);
				return result;
			}
			catch (Exception e)
			{
				return new String();
			}
			
			
		}
		
		@Override
		protected void onPostExecute(String result){
			
			ArrayList<MimicData> playdata = new ArrayList<MimicData>(); 
			ArrayList<commentdata> commentdata = new ArrayList<commentdata>();
			progdialog.dismiss();
			
			
			if(result.length() == 0){	
				this.activity.alert("Nada denada");
			}
			
			try{
				JSONObject respobj = new JSONObject(result);
				JSONObject user = respobj.getJSONObject("user");
				String username = user.getString("username");
				String dpurl = user.getString("profilepictureurl");
				String url= respobj.getString("url");
				String posturl = respobj.getString("posturls");
				String description = "This is crazy, I'm freaking loving this app. It's super coool. Woah.";
				int commentsx = respobj.getInt("commentscount");
				int likes= respobj.getInt("likescount");
				int postid = respobj.getInt("id");
				Boolean likesbool = respobj.getBoolean("favourites");
				String timestamp = respobj.getString("time");
				playdata.add(new MimicData(username, dpurl, url, postid, likes, commentsx, posturl, description, likesbool, timestamp, null));
				JSONArray comments = respobj.getJSONArray("comments");
					for (int i=0; i<comments.length(); i++){
						JSONObject comment = comments.getJSONObject(i);
						String commenturl = comment.getString("commenturl");
						JSONObject usercomment = comment.getJSONObject("user");
						String commentuser = usercomment.getString("username");
						String profilepictureurl = usercomment.getString("profilepictureurl");
						Log.d("whats in commentdata", "commentdata:"+commenturl);
						commentdata.add(new commentdata(commenturl, commentuser, profilepictureurl));
				
				}
				
				} catch (JSONException e){
				
					e.printStackTrace();
				}
			
			this.activity.setUsers(commentdata);
			this.activity.setUser(playdata);
			}
			
			
		}
		


