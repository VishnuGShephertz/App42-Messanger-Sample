package com.shephertz.app42.buddy.events;

import org.json.JSONException;
import org.json.JSONObject;

import com.shephertz.app42.buddy.command.MessageCode;
import com.shephertz.app42.buddy.listener.BaseListener;
import com.shephertz.app42.buddy.tools.UserData;

public class MessageEvent implements BaseListener {
	private int eventCode;
	private String displayName;
	private String userName;
	private String groupName;
	public String getGroupName() {
		return groupName;
	}
	private String message;
	private int msgType;
	private String time;
	private String picUrl;
	public String getPicUrl() {
		return picUrl;
	}
	public String getTime() {
		return time;
	}
	

	public int getEventCode() {
		return eventCode;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMessage() {
		return message;
	}
	public int getMsgType() {
		return msgType;
	}
	public static MessageEvent getEvent(JSONObject notifyData)
			throws JSONException {
		return new MessageEvent(notifyData.getInt(Code),
				notifyData.getString(DisplayName),
				notifyData.getString(Username),
				notifyData.getString(Message), 
				notifyData.getInt(MsgType),
				notifyData.getString(MsgTime),
				notifyData.getString(PicUrl),
				notifyData.getString(GroupName));
	}
	
	public static MessageEvent getBuddyEvent(UserData userdata){
		return new MessageEvent(userdata.getEvent(),
				userdata.getDisplayName(),
				userdata.getUserName(),
			     "", 
				MessageCode.TEXT.getMsgCode(),
				userdata.getDateTime(),
				userdata.getPicUrl(),
				"");
	}
	public static MessageEvent getGroupEvent(UserData userdata){
		return new MessageEvent(userdata.getEvent(),
				userdata.getDisplayName(),
				userdata.getUserName(),
			     "", 
				MessageCode.TEXT.getMsgCode(),
				userdata.getDateTime(),
				userdata.getPicUrl(),
				userdata.getUserName());
	}
	public MessageEvent(int eventCode, String displayName, String userName,
			String message, int msgType, String time,String picUrl,String groupName) {
		this.eventCode = eventCode;
		this.displayName = displayName;
		this.userName = userName;
		this.message = message;
		this.time = time;
		this.picUrl=picUrl;
		this.groupName=groupName;
	}
}
