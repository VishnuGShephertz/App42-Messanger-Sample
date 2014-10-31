package com.shephertz.app42.buddy.app;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;

import com.shephertz.app42.buddy.tools.ContactInfo;
import com.shephertz.app42.buddy.tools.UserInfo;

public class ApplicationState {
	private static ApplicationState mInstance = null;
	private boolean isAuthenticated;
	private String userName;
	private String avatarName;
	private String avatarPicUrl;
	private Bitmap avatarPic;
	private HashMap<String, ContactInfo> contactInfoMap;
	private ArrayList<String> allUsers;
	private HashMap<String, UserInfo> myBuddiesMap;
	private String countryCode="91";

	
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public HashMap<String, UserInfo> getMyBuddiesMap() {
		return myBuddiesMap;
	}

	public HashMap<String, ContactInfo> getContactInfo() {
		return contactInfoMap;
	}

	public void setContactInfo(HashMap<String, ContactInfo> contactInfoMap) {
		this.contactInfoMap = contactInfoMap;
	}
	
	public String getDisplayName(String userId){
		if(contactInfoMap.get(userId)!=null){
			return contactInfoMap.get(userId).getName();
		}
		return userId;
	}

	public boolean isMyBuddy(String buddyName){
		return myBuddiesMap.containsKey(buddyName) ;
	}
	public void updateAllUsers(ArrayList<String> newUsers) {
		allUsers.addAll(newUsers);
	}

	public ArrayList<String> getApp42UserInfo() {
		return allUsers;
	}

	public void setApp42UserInfo(ArrayList<String> allUsers) {
		this.allUsers = allUsers;
	}

	private ApplicationState() {
		contactInfoMap = new HashMap<String, ContactInfo>();
		allUsers = new ArrayList<String>();
		myBuddiesMap=new HashMap<String, UserInfo>();
	}

	public void addInBuddyMap(String userKey,UserInfo userInfo){
		myBuddiesMap.put(userKey, userInfo);
	}
	
	public void increaseCount(String key,String time,String message){
		UserInfo user=myBuddiesMap.get(key);
		user.setCount(myBuddiesMap.get(key).getCount()+1);
		user.setDateTime(time);
		user.setLastMessage(message);
	}
	public Bitmap getAvatarPic() {
		return avatarPic;
	}

	public void setAvatarPic(Bitmap avatarPic) {
		this.avatarPic = avatarPic;
	}

	private String avatarUserName;

	public String getAvatarUserName() {
		return avatarUserName;
	}

	public void setAvatarUserName(String avatarUserName) {
		this.avatarUserName = avatarUserName;
	}

	public String getAvatarName() {
		return avatarName;
	}

	public void setAvatarName(String avatarName) {
		this.avatarName = avatarName;
	}

	public String getAvatarPicUrl() {
		return avatarPicUrl;
	}

	public void setAvatarPicUrl(String avatarPicUrl) {
		this.avatarPicUrl = avatarPicUrl;
	}

	public static ApplicationState getmInstance() {
		return mInstance;
	}

	public static void setmInstance(ApplicationState mInstance) {
		ApplicationState.mInstance = mInstance;
	}

	public boolean isAuthenticated() {
		return isAuthenticated;
	}

	public void setAuthenticated(boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	private String password;
	private String mailId;

	/*
	 * instance of class
	 */
	public static ApplicationState instance() {
		if (mInstance == null) {
			mInstance = new ApplicationState();
		}
		return mInstance;
	}

}
