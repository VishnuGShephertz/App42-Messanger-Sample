package com.shephertz.app42.buddy.app;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

import com.shephertz.app42.buddy.command.EventCode;
import com.shephertz.app42.buddy.events.MessageEvent;
import com.shephertz.app42.buddy.listener.BaseListener;
import com.shephertz.app42.buddy.tools.DataBaseHelper;

public class AppContext implements BaseListener{
	public static String myUserName;
	public static final String AppName="App42Messanger";

	public static JSONObject getEvent(int eventCode, String displayName, String userName,
			String message, int msgType, String time,String picUrl,String groupName) throws JSONException
			 {
		JSONObject notifyData=new JSONObject();
		      notifyData.put(Code,eventCode);
				notifyData.put(DisplayName,displayName);
				notifyData.put(Username,userName);
				notifyData.put(Message,message);
				notifyData.put(MsgType,msgType);
				notifyData.put(MsgTime,time);
				notifyData.put(PicUrl,picUrl);
				notifyData.put(GroupName,groupName);
				return notifyData;
	}
	
	
	public static void showResponse(MessageEvent notifyEvent,Context context){
		
		if(notifyEvent.getEventCode()==EventCode.INVITATION.getEventCode()){
			showMessages(R.string.invite_request+" "+notifyEvent.getUserName(),context);
			DataBaseHelper.instance(context).createUserTable(notifyEvent);
		}
		else if(notifyEvent.getEventCode()==EventCode.GROUP_CREATION.getEventCode()){
			showMessages(R.string.group_created+" "+notifyEvent.getGroupName(),context);
			DataBaseHelper.instance(context).createGroupTable(notifyEvent);
		
		}
	}
	
	public static void showMessages(String message,Context context){
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
}
