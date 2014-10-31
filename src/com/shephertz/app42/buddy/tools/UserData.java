package com.shephertz.app42.buddy.tools;

public class UserData {
	private String userName;
	private String displayName;
	private String picUrl;
	private int event;
	public void setEvent(int event) {
		this.event = event;
	}

	private String dateTime;

	public String getUserName() {
		return userName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getPicUrl() {
		return picUrl;
	}
public String getDateTime() {
		return dateTime;
	}
	
	public int getEvent() {
		return event;
	}

	public  UserData(String userName, String displayName, String picUrl,
			 int event,String dateTime) {
		this.userName = userName;
		this.displayName = displayName;
		this.picUrl = picUrl;
		this.dateTime = dateTime;
		this.event = event;
	}
	
	
}
