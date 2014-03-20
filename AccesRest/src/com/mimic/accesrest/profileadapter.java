package com.mimic.accesrest;

import java.util.ArrayList;

import com.mimic.accesrest.MainActivity.MyViewHolder;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class profileadapter extends BaseAdapter implements OnClickListener{

	private static final String debugtag = "MimicAdapter";
	private Activity activity;
	private LayoutInflater layoutinflater;
	private ArrayList<MimicData> mimicdata;
	
	public profileadapter(Activity a, LayoutInflater l, ArrayList <MimicData> m){
		
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
	public View getView(int pos, View ConvertView, ViewGroup parent) {
		
		MyViewHolder holder;
		if (ConvertView == null){
			ConvertView = layoutinflater.inflate(R.layout.profrows, parent, false);
			holder = new MyViewHolder();
			holder.title = (TextView) ConvertView.findViewById(R.id.title);
			holder.likesnum= (TextView) ConvertView.findViewById(R.id.likesnum);
			holder.commentnum= (TextView) ConvertView.findViewById(R.id.commentnum);
			holder.sharenum= (TextView) ConvertView.findViewById(R.id.sharenum);
			
			
			ConvertView.setTag(holder);
			
		}
		else{
			holder = (MyViewHolder)ConvertView.getTag();
		}
		
		ConvertView.setOnClickListener(this);
		
		MimicData mimic = mimicdata.get(pos);
		holder.mimic = mimic;
		holder.title.setText(mimic.gettitle());
		holder.likesnum.setText(mimic.getlikecounter());
		holder.sharenum.setText(mimic.getsharecount());
		holder.commentnum.setText(mimic.getcommentcounter());
		
		return ConvertView;
		
		
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
