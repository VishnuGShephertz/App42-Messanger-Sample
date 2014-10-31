package com.shephertz.app42.buddy.util;

import java.util.regex.Pattern;

public class Constants {
	public static final String App42ApiKey = "a6d79b7186a6d3ff6cbb60cd80a6848046ed3ea76e8e6a8f3edd25f855ce6d72";
	public static final String App42ApiSecret ="1b04b8548564f6fa5f37dbd48e8dfbcc0850891fbf04d5cca57c4bdd8d59eefa";

	public static final int BuddyMsg = 0;
	public static final int GroupMsg = 1;
	public static final int AllMsg = 2;

	public static String MsgType = "msgType";
	public static final String GroupName = "group";
	public static final String Owner = "owner";
	public static final String Buddy = "buddy";

	public static final int BuddyList = 0;
	public static final int InvitList = 1;
	public static final int GroupList = 2;
	public static final int AllUsers = 3;
	public static final int AllMsges = 4;

	public static final String APP_URL="";
	public static final String GoogleProjectNo="769491466879";
	
	public static final  String PHONE_PATTERN = "^\\+?[0-9. ()-]{10,25}$";
	public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern
				.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
						+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
						+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
}
