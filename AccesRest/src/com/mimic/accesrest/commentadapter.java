package com.mimic.accesrest;

import java.io.IOException;
import java.util.ArrayList;

import com.mimic.accesrest.comment.MyCommentHolder;
import com.fedorvlasov.lazylist.ImageLoader;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class commentadapter extends BaseAdapter{
		private Activity activity;
		private LayoutInflater layoutinflater;
		private ArrayList<commentdata> commentdata;
		private commentdata comments;
		int position=-1;
		private static int not_playing = -1;
		private final boolean[] mHighlightedPositions = new boolean[100];
		int initialposition = -1; 
		private int mPlayingPosition = not_playing;
		private comment commentx;
		private MediaPlayer player;
		private ImageLoader imageloader;
		
		
	 public commentadapter(Activity a, LayoutInflater l, ArrayList <commentdata> m){
			
			this.activity = a;
			this.layoutinflater = l;
			this.commentdata = m;
			imageloader=new ImageLoader(activity.getApplicationContext());
        	
			
		}
	 @Override
		public int getCount() {
			return this.commentdata.size();
		}
		
		@Override
		public boolean areAllItemsEnabled(){
			return true;
		}
		
		@Override
		public Object getItem(int arg0) {
			return null;
		}
		
		@Override
		public long getItemId(int pos) {
			return pos;
				}
		@Override
		public View getView(final int pos, View ConvertView, ViewGroup parent) {
			final MyCommentHolder holder;
			
			if (ConvertView == null){
				ConvertView = layoutinflater.inflate(R.layout.commentrows, parent, false);
				holder = new MyCommentHolder();		
				player = new MediaPlayer();
				holder.dp = (ImageView)ConvertView.findViewById(R.id.commentdisplaypic);
				holder.user = (TextView) ConvertView.findViewById(R.id.commentusername);
				holder.commentplays = (ImageButton) ConvertView.findViewById(R.id.commentplay);
				holder.commentplays.setFocusable(false);
				holder.commentplays.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						int position = (Integer)v.getTag();
					    Log.d("clicked", "Button row pos click: " + position);
					    String url = holder.commenturl;
					    
					    
					    // Toggle background resource
					    RelativeLayout layout = (RelativeLayout)v.getParent();
					    ImageButton button = (ImageButton)layout.getChildAt(2);

					 
		
					    ListView lv = (ListView) layout.getParent();
					    for (int i=0; i < lv.getChildCount(); i++){
					    	RelativeLayout row = (RelativeLayout) lv.getChildAt(i);
					    	ImageButton btns = (ImageButton) row.getChildAt(2);
					    	btns.setImageResource(R.drawable.ic_action_play);
					    	
					    }
					    
					    if(initialposition!=-1)
					    {
					    	if(mHighlightedPositions[position]) {
					    	button.setImageResource(R.drawable.playbutton);
					        mHighlightedPositions[position] = false;
					        stopPlayback();
					        
					    }	else {
					    	button.setImageResource(R.drawable.stopbutton);
					        mHighlightedPositions[position] = true;
					        mHighlightedPositions[initialposition]=false;
					        if (player.isPlaying()){
					        	player.stop();
					        	mPlayingPosition = position;
					        						        	
					        	startPlaying(url);
					  	      
					    	} else{
					    		mPlayingPosition = position;
					    		
					    	  		startPlaying(url);
					    		
					    	}
					   }
					    }
					    else {
					    	button.setImageResource(R.drawable.stopbutton);
					    	mHighlightedPositions[position] = true;
					    	if (player.isPlaying()){
					    		player.stop();
					    		mPlayingPosition = position;
					    		
					    		startPlaying(url);
					    		
					    	}else
					    	{
					    		mPlayingPosition = position;
					    		
					    		startPlaying(url);
					    	
					    	}
					    
					    }
					    initialposition = position;
				}
				});
				
				
				
			}else{
				holder = (MyCommentHolder)ConvertView.getTag();
			}
			comments = commentdata.get(pos);
			holder.commenturl = "awesome";
			holder.comments = comments;
			Log.d("comment", "comments: " + comments.getcommenturl());
			holder.commenturl = comments.getcommenturl();
			holder.user.setText(comments.getusercommenturl());
			String na = comments.getprofilepictureurl();
			imageloader.DisplayImage(na, holder.dp);
			holder.commentplays.setTag(pos);
			ConvertView.setTag(holder);
			commentx = (comment) this.activity;
        	player = commentx.getmediaplayer();
			
	            
	        //holder.commentplays.setTag(pos);   
			
	        return ConvertView;
		}
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
                stopPlayback();
            }

        }
		
		private void stopPlayback()
	    {
	        mPlayingPosition = not_playing;;
	       
	        player.stop();
	    }

}
