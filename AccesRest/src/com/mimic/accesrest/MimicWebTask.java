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
			String result = Mimicdatahelper.downloadFromServer();
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
			JSONObject respobj = new JSONObject(result);
			JSONArray users = respobj.getJSONArray("objects");
			for (int i=0; i<users.length(); i++){
				JSONObject post= users.getJSONObject(i);
				JSONObject user = post.getJSONObject("Userwhodidit");
				String username = user.getString("Username");
				String title = post.getString("title");
				int share = post.getInt("share");
				int comment = post.getInt("likecounter");
				int likes = post.getInt("commentcounter");
				
				
				
				mimicdata.add(new MimicData(username, title, likes, comment, share));
				
			}
			
			} catch (JSONException e){
			
				e.printStackTrace();
			}
		
		this.activity.setUsers(mimicdata);
		}
		
		
	}
	


