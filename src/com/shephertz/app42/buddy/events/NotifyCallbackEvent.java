package com.shephertz.app42.buddy.events;

public class NotifyCallbackEvent {

	private String userName;
	private String groupName;
	private int responseCode;
	public NotifyCallbackEvent(String userName, String groupName,
			int responseCode) {
		this.userName = userName;
		this.groupName = groupName;
		this.responseCode = responseCode;
	}
	public String getUserName() {
		return userName;
	}
	public String getGroupName() {
		return groupName;
	}
	public int getResponseCode() {
		return responseCode;
	}
}
