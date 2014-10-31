package com.shephertz.app42.buddy.command;

public enum EventCode {
	BUDDY_MESSAGE(0), GROUP_MESSAGE(1), INVITATION(2), ACCEPTANCE(3),
	REJECTION(4), GROUP_INVITATION(5), GROUP_JOIN(6), GROUP_LEFT(7),
	GROUP_PIC_CHANGE(8), GROUP_NAME_CHANGE(9), GROUP_CREATION(10);
	private int eventCode;
	public int getEventCode() {
		return eventCode;
	}
	private EventCode(int eventCode) {
		this.eventCode = eventCode;
	}
}
