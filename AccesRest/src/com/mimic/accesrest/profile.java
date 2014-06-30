package com.mimic.accesrest;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.Session;
import com.fedorvlasov.lazylist.ImageLoader;

public class profile extends SherlockActivity implements OnClickListener{

	private ArrayList<MimicData> mimic;
	private ArrayList<profiledata> profiledatas;
	private ListView MimicList;
	private LayoutInflater layoutinflater;
	private TextView fullnameprof;
	private TextView Usernameprof;
	private TextView followers;
	private TextView followings;
	public ImageLoader imageloader;
	private ImageView display;
	private TextView mimicscount;
	private static final String logtaskact = "MainUI";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		this.layoutinflater = LayoutInflater.from(this);
		Bundle bundle = getIntent().getExtras();
		String x = bundle.getString("profileurl");
		Boolean y = bundle.getBoolean("prof");
		ImageButton profile = (ImageButton) findViewById(R.id.profilebuttonprofpage);
		profile.setOnClickListener(this);
		if (y==false){
			Log.d("y", "y is null"+ y);
			getSupportActionBar().setDisplayShowHomeEnabled(false);
		}else{
			
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setIcon(R.drawable.back);
			profile.setImageResource(R.drawable.profilebuttonunclicked);
			
		}
		this.followers = (TextView) findViewById(R.id.followertext);
		this.followings = (TextView) findViewById(R.id.followingtext);
		this.MimicList = (ListView) findViewById(R.id.proflist);
		ImageButton homebutton = (ImageButton) findViewById(R.id.homebuttonprofpage);
		homebutton.setOnClickListener(this);
		ImageButton postbutton = (ImageButton) findViewById(R.id.postbuttonprofpage);
		postbutton.setOnClickListener(this);
		ImageButton notification = (ImageButton) findViewById(R.id.notificationbuttonprofpage);
		notification.setOnClickListener(this);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F86960")));
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		display = (ImageView) findViewById(R.id.profilepic);
		mimicscount = (TextView) findViewById(R.id.postcounts);
		
		
		mimic(x);
		
	}
	
	@Override
	public void onClick (View v){
	
	if (v.getId() == R.id.postbutton)
	{
		startActivity(new Intent(profile.this, post.class));
		
	}else if(v.getId() == R.id.homebuttonprofpage){
		startActivity(new Intent(profile.this, MainActivity.class));
	}
	else if(v.getId() == R.id.notificationbutton){
		startActivity(new Intent(profile.this, Notifications.class));
	} else if (v.getId()== R.id.profilebuttonprofpage){
		Intent x = new Intent(profile.this, profile.class);
		x.putExtra("profileurl", "http://mimictheapp.herokuapp.com/profiles/");
		startActivity(x);
	}
}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            Log.d("did you", "click?");
        	this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	public void mimic(String x){
		profilewebtask proftask = new profilewebtask(profile.this);
		String query = x+"?format=json";
		
		try{
			proftask.execute(query);
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
		imageloader=new ImageLoader(this);
		imageloader.DisplayImage(x.getprofileurl(), display);
		SpannableString s = new SpannableString(x.getUsername().toUpperCase());
		s.setSpan(new Typefacespan(this, "Roboto-Medium.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		getSupportActionBar().setTitle(s);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		mimicscount.setText(x.getpostcount());
		followers.setText(x.getfollowers());
		followings.setText(x.getfollowing());
		
		
	}
	  public static class MyViewHolder {
	        public TextView  title, sharenum, commentnum, likesnum, timestamp;
	        public MimicData mimic;
	        public String posturl;
	        public ImageButton plays;
	    }
}
