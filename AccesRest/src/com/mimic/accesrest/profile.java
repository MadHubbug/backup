package com.mimic.accesrest;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

public class profile extends SherlockActivity {

	private ArrayList<MimicData> mimic;
	private ArrayList<profiledata> profiledatas;
	private ListView MimicList;
	private LayoutInflater layoutinflater;
	private TextView fullnameprof;
	private TextView Usernameprof;
	private TextView followers;
	private TextView followings;
	
	private static final String logtaskact = "MainUI";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		this.layoutinflater = LayoutInflater.from(this);
		this.fullnameprof = (TextView) findViewById(R.id.FullNameprof);
		this.Usernameprof= (TextView) findViewById(R.id.UserNameprof);
		this.followers = (TextView) findViewById(R.id.followertext);
		this.followings = (TextView) findViewById(R.id.followingtext);
		this.MimicList = (ListView) findViewById(R.id.proflist);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F86960")));
		getSupportActionBar().setTitle("Profile");
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		
		mimic();
	}


	
	public void mimic(){
		profilewebtask proftask = new profilewebtask(profile.this);
		try{
			proftask.execute();
			} catch (Exception e){
				proftask.cancel(true);
				alert("No Mimics");
			}
		
	}
	
	public void alert(String msg) {
		Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
		
	}


	public void setUser(ArrayList<MimicData> mimic) {
		this.mimic = mimic;
		this.MimicList.setAdapter(new profileadapter(this,this.layoutinflater, this.mimic));
	}

	public void setUsers(ArrayList<profiledata> profiledata) {
		this.profiledatas = profiledata;
		profiledata x = profiledatas.get(0);
		fullnameprof.setText(x.getfullname());
		Usernameprof.setText(x.getUsername());
		followers.setText(x.getfollowers());
		followings.setText(x.getfollowing());
		
		
	}
	  public static class MyViewHolder {
	        public TextView  title, sharenum, commentnum, likesnum;
	        public MimicData mimic;
	    }
}
