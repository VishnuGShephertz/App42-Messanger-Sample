package com.shephertz.app42.buddy.app;

import java.util.Date;

public class MyBuddy implements Comparable<MyBuddy> {

	public String message;
	public MyBuddy(String owner,String message,Date sendDate){
		this.sendDate=sendDate;
		this.message=message;
		this.owner=owner;
	}
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	private String owner;

	private Date sendDate;

	@Override
	public int compareTo(MyBuddy arg0) {
		if (getSendDate() == null || arg0.getSendDate() == null)
			return 0;
		return getSendDate().compareTo(arg0.getSendDate());
	}
}
