package com.shephertz.app42.buddy.tools;

public class MessageInfo {

	private String sender;
	private String dateTime;
	private int msgCode;
	private String message;
	public MessageInfo(String sender,String message, int msgCode,String dateTime
			) {
		this.sender = sender;
		this.dateTime = dateTime;
		this.msgCode = msgCode;
		this.message = message;
	}
	public String getSender() {
		return sender;
	}
	public String getDateTime() {
		return dateTime;
	}
	public int getMsgCode() {
		return msgCode;
	}
	public String getMessage() {
		return message;
	}
	
}
