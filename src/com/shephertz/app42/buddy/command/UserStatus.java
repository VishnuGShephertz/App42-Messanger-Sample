package com.shephertz.app42.buddy.command;

public enum UserStatus {
	PENDING(0), JOINED(1), 
	LEFT(2), REJECT(3);
	 
	private int statusCode;
 
	private UserStatus(int s) {
		statusCode = s;
	}
 
	public int getStatusCode() {
		return statusCode;
	}
}
