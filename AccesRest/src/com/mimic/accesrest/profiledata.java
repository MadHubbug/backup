package com.mimic.accesrest;

public class profiledata{
	
	private String fullname;
	private String username;
	private Integer followers;
	private Integer following;
	
	public profiledata(String full, String Username, Integer Followers, 
			Integer Following){
		
		this.fullname = full;
		this.username = Username;
		this.followers = Followers;
		this.following = Following;
				
	}
	

	public String getfullname(){
		return fullname;
	}
	
	public void setfullname(String full){
		this.fullname= full;
	}
	
	

	
	public String getUsername(){
		return username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	
	
	public String getfollowing(){
		String x = following.toString();
		return x;
	}
	
	public void setfollowing(Integer following){
		this.following= following;
	}
	
	public String getfollowers(){
		String x = followers.toString();
		return x;
	}
	
	public void setfollowers(Integer follower){
		this.followers = follower;
	}
	
	
	
}
