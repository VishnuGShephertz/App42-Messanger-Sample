package com.shephertz.app42.buddy.command;

public enum MessageCode {
	TEXT(0), IMAGE(1), AUDIO(2), VIDEO(3);
	private int msgCode;
	public int getMsgCode() {
		return msgCode;
	}
	private MessageCode(int msgCode) {
		this.msgCode = msgCode;
	}
}
