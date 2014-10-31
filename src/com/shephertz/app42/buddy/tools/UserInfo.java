package com.shephertz.app42.buddy.tools;

import com.shephertz.app42.buddy.events.MessageEvent;

public class UserInfo {

	public UserInfo(MessageEvent event) {
		this.userName = event.getUserName();
		this.displayName = event.getDisplayName();
		this.picUrl = event.getPicUrl();
		this.dateTime = event.getTime();
		this.count = 0;
		this.lastMessage=event.getMessage();
	}
private String userName;
private String displayName;
private String picUrl;
private String dateTime;
private int count;
private String lastMessage;


public String getLastMessage() {
	return lastMessage;
}
public void setLastMessage(String lastMessage) {
	this.lastMessage = lastMessage;
}
public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
public String getDisplayName() {
	return displayName;
}
public void setDisplayName(String displayName) {
	this.displayName = displayName;
}
public String getPicUrl() {
	return picUrl;
}
public void setPicUrl(String picUrl) {
	this.picUrl = picUrl;
}
public String getDateTime() {
	return dateTime;
}
public void setDateTime(String dateTime) {
	this.dateTime = dateTime;
}
public int getCount() {
	return count;
}
public void setCount(int count) {
	this.count = count;
}

public static UserInfo getUserInfo(MessageEvent msgEvent){
	return new UserInfo(msgEvent);
}
	
}
