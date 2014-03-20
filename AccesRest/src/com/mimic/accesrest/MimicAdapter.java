package com.mimic.accesrest;

import java.util.ArrayList;

import com.mimic.accesrest.MainActivity.MyViewHolder;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MimicAdapter extends BaseAdapter {

	private static final String debugtag = "MimicAdapter";
	private Activity activity;
	private LayoutInflater layoutinflater;
	private ArrayList<MimicData> mimicdata;
	private MimicData mimic;
	public MimicAdapter(Activity a, LayoutInflater l, ArrayList <MimicData> m){
		
		this.activity = a;
		this.layoutinflater = l;
		this.mimicdata = m;
		
	}
		
	@Override
	public int getCount() {
		return this.mimicdata.size();
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
		
		final MyViewHolder holder;
		if (ConvertView == null){
			ConvertView = layoutinflater.inflate(R.layout.rows, parent, false);
			holder = new MyViewHolder();
			holder.user = (TextView) ConvertView.findViewById(R.id.Username);
			holder.title = (TextView) ConvertView.findViewById(R.id.title);
			holder.likesnum= (TextView) ConvertView.findViewById(R.id.likesnum);
			holder.commentnum= (TextView) ConvertView.findViewById(R.id.commentnum);
			holder.sharenum= (TextView) ConvertView.findViewById(R.id.sharenum);
			holder.like = (ImageButton) ConvertView.findViewById(R.id.likebutton);
			holder.like.setFocusable(false);

	        holder.like.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	                int posi = pos;
	            	mimic = mimicdata.get(pos);
	            	holder.likesnum.setText("1");
	                
	            }
	        });
			ConvertView.setTag(holder);
			ConvertView.setOnClickListener(new OnClickListener() {
				public void onClick(View v){
					Log.d("hi", "hi from "+ pos);
				}
			});
		}
		else{
			holder = (MyViewHolder)ConvertView.getTag();
		}
		
		
		
		mimic = mimicdata.get(pos);
		holder.mimic = mimic;
		holder.user.setText(mimic.getUsername());
		holder.title.setText(mimic.gettitle());
		holder.likesnum.setText(mimic.getlikecounter());
		holder.sharenum.setText(mimic.getsharecount());
		holder.commentnum.setText(mimic.getcommentcounter());
		
		return ConvertView;
		
		
	}

	
	
	
}
