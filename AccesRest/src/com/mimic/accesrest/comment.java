package com.mimic.accesrest;

import java.io.IOException;
import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.fedorvlasov.lazylist.ImageLoader;
import com.mimic.accesrest.R.drawable;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class comment extends SherlockActivity {

	private ArrayList<commentdata> comments;
	private ListView CommentList;
	private LayoutInflater layoutinflater;

	private commentadapter CommentAdapter;
	private ArrayList<MimicData> mimicdatas;
	private ImageButton playpostbutton;
	public MediaPlayer player;
	public ImageLoader imageloader;
	private int postid;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment);
		this.CommentList= (ListView) findViewById(R.id.commentpagelistviews);
		this.layoutinflater = LayoutInflater.from(this);
		this.playpostbutton = (ImageButton) findViewById(R.id.commentpageplays);
		Bundle bundle = getIntent().getExtras();
		postid = bundle.getInt("postid");
		player = new MediaPlayer();
		comment(postid);
		imageloader=new ImageLoader(this.getApplicationContext());
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F86960")));
		SpannableString s = new SpannableString("Comment");
		s.setSpan(new Typefacespan(this, "Roboto-Medium.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		getSupportActionBar().setTitle(s);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.back);
		ImageView view = (ImageView) findViewById(android.R.id.home);
		
		
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
	
	public void setUsers(ArrayList<commentdata> comment) {
		this.comments = comment;
		
		CommentAdapter = new commentadapter(this,this.layoutinflater, this.comments);
		this.CommentList.setAdapter(CommentAdapter);
	
		
	}
	
	  public void comment(int a){
			commentwebtask mimictask = new commentwebtask(comment.this);
			try{
				mimictask.execute(a);
				} catch (Exception e){
					mimictask.cancel(true);
					alert("Nothing to play");
				}
			
		}
	  
	  public void alert(String msg) {
			Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
			
		}
	  
	  public static class MyCommentHolder {
		  public MimicData play;
		  public ImageButton commentplays;
		  public commentdata comments;
		  public String commenturl, posturl;
		public ImageView dp;
		public TextView user;

		  
	  }

	public void setUser(ArrayList<MimicData> playdata) {
		this.mimicdatas = playdata;
		final MimicData x = mimicdatas.get(0);
		String na = x.geturl();
		ImageView dp = (ImageView) findViewById(R.id.commentpagedisplaypic);
		imageloader.DisplayImage(na, dp);
		TextView user = (TextView) findViewById(R.id.commentpageusername);
		user.setText(x.getUsername());
		TextView description = (TextView) findViewById(R.id.commentpagedescription);
		description.setText(x.getsharecount());
		TextView like = (TextView) findViewById(R.id.commentpagelikecount);
		like.setText(x.getlikecounter());
		TextView commentcount = (TextView) findViewById(R.id.commentpagereplycount);
		commentcount.setText(x.getcommentcounter());
		final ImageButton s = (ImageButton) findViewById(R.id.commentpagelike);
		
		final boolean w = x.getLikes();
		if (w==true){
			s.setImageResource(R.drawable.liked);
		}else{
			s.setImageResource(R.drawable.like);
		}
		
		s.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				final liking likes = new liking();
				final dislike dislike = new dislike();
				if (w==true){
					s.setImageResource(R.drawable.like);
					String m = Integer.toString(postid);
					dislike.execute(m);
				}else{
					s.setImageResource(R.drawable.liked);
					String m = Integer.toString(postid);
					likes.execute(m);
				}
				
			}
			
		});
		playpostbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			String url = x.getposturl();	
			
			if (player.isPlaying()){
				playpostbutton.setImageResource(R.drawable.playbutton);
				player.stop();
				startPlaying(url);
			}else{
				playpostbutton.setImageResource(R.drawable.playbutton);
				startPlaying(url);
				
			}
			
			
				
			}
		});
		
	}
	
	public MediaPlayer getmediaplayer(){
		return player;
	};
	private void startPlaying(String url) {
        // TODO Auto-generated method stub
		
        try {
        	
        	
        	
        	player.reset();
            player.setDataSource(url);
            // mPlayer.setDataSource(mFileName);
            player.prepareAsync();
            player.setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					
					player.start();

		
			            	
			            }
			          });
 

        } catch (IOException e) {
            Log.e("preparefailed", "prepare() failed");
            player.stop();
            
        }

    }
	
	@Override
	protected void onDestroy()
	{
		player.release();
		super.onDestroy();
	}
	
}
