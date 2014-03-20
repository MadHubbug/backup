package com.mimic.accesrest;

public class MimicData {

	private String username;
	private String title;
	private Integer commentcounter;
	private Integer likeCounter;
	private Integer sharecount;
	
	public MimicData(String Username, String Title, Integer likecounter, Integer commentcount, Integer shareC){
		
		this.username = Username;
		this.title = Title;
		this.likeCounter = likecounter;
		this.commentcounter = commentcount;
		this.sharecount = shareC;
				
	}
	
	
	public String gettitle(){
		return title;
	}
	
	public void setEmail(String Title){
		this.title = Title;
	}
	
	public String getUsername(){
		return username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public String getlikecounter(){
		String x = likeCounter.toString();
		return x;
	}
	
	public void setlikecounter(Integer likecount){
		this.likeCounter = likecount;
	}
	
	public String getcommentcounter(){
		String x = commentcounter.toString();
		return x;
	}
	
	public void setcommentcounter(Integer commentcount){
		this.commentcounter = commentcount;
	}
	
	public String getsharecount(){
		String x = sharecount.toString();
		return x;
	}
	
	public void setsharecount(Integer sharecount){
		this.sharecount = sharecount;
	}
	
	
	
	
	
}
