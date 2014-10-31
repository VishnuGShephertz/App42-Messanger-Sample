package com.shephertz.app42.buddy.tools;

public class ContactInfo {
	private String name;
	private boolean isInstalled;
	private String picUrl;

	public boolean isInstalled() {
		return isInstalled;
	}

	public void setInstalled(boolean isInstalled) {
		this.isInstalled = isInstalled;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	
	public ContactInfo(String name, boolean isInstalled, String picUrl) {
		this.name = name;
		this.isInstalled = isInstalled;
		this.picUrl = picUrl;
	}

	public static ContactInfo buildContact(String name,String picUrl,boolean isInstalled){
		return new ContactInfo(name, isInstalled,picUrl);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
