package com.shephertz.app42.buddy.tools;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.database.Cursor;
import com.shephertz.app42.buddy.app.ApplicationState;
import com.shephertz.app42.buddy.command.EventCode;
import com.shephertz.app42.buddy.events.MessageEvent;

public class DatabaseService implements DbColumns{

	private static DatabaseService mInstance=null;
	private DataBaseHelper dbHelper;
	private ApplicationState appState;
	private DatabaseService(Context context){
		this.dbHelper=new DataBaseHelper(context);
		appState=ApplicationState.instance();
	}
	
	public static DatabaseService instance(Context  context){
		if(mInstance==null)
			mInstance=new DatabaseService(context);
		return mInstance;
	}
	
	public void performOperation(JSONObject jsonMsg){
		try {
			MessageEvent event=MessageEvent.getEvent(jsonMsg);
			if(event.getEventCode()==EventCode.INVITATION.getEventCode()){
				dbHelper.insertUserInformation(event);
			}
			else if(event.getEventCode()==EventCode.GROUP_INVITATION.getEventCode()){
				dbHelper.insertGroupInformation(event);
			}
			else if(event.getEventCode()==EventCode.ACCEPTANCE.getEventCode()){

				dbHelper.updateUserStatus(event);
			}
			else if(event.getEventCode()==EventCode.GROUP_JOIN.getEventCode()){
				dbHelper.updateGroupStatus(event);
			}
			else if(event.getEventCode()==EventCode.GROUP_LEFT.getEventCode()){
				dbHelper.updateGroupStatus(event);
			}
			else if(event.getEventCode()==EventCode.REJECTION.getEventCode()){
				dbHelper.updateUserStatus(event);
			}
			else if(event.getEventCode()==EventCode.BUDDY_MESSAGE.getEventCode()){
				dbHelper.insertBuddyMessage(event);
			}
	        else if(event.getEventCode()==EventCode.GROUP_MESSAGE.getEventCode()){
	        	dbHelper.insertGroupMessage(event);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void performClickOperation(MessageEvent event){
		if(event.getEventCode()==EventCode.ACCEPTANCE.getEventCode()){
			dbHelper.createUserTable(event);
		}
		else if(event.getEventCode()==EventCode.GROUP_JOIN.getEventCode()){
			dbHelper.createGroupTable(event);
		}
		else if(event.getEventCode()==EventCode.GROUP_LEFT.getEventCode()){
			dbHelper.updateGroupStatus(event);
		}
		else if(event.getEventCode()==EventCode.REJECTION.getEventCode()){
			dbHelper.updateUserStatus(event);
		}
	}
	
	public ArrayList<GroupInfo> getGroupList(){
		Cursor dbCursor=dbHelper.getGroupInfo();
		ArrayList<GroupInfo> group=new ArrayList<GroupInfo>();
		if (dbCursor.getCount() > 0){
			int noOfScorer = 0;
			dbCursor.moveToFirst();
			    while ((!dbCursor.isAfterLast())&&noOfScorer<dbCursor.getCount()) 
			    {
			        noOfScorer++;
			        group.add(new GroupInfo(dbCursor.getString(0), dbCursor.getString(3), dbCursor.getString(1), dbCursor.getString(2)));
			        dbCursor.moveToNext();
			    }
			}
		  return group;
	}
	
	public ArrayList<UserData> getAllDetails(){
		Cursor dbCursor=dbHelper.getAllInfo();
		ArrayList<UserData> infoList=new ArrayList<UserData>();
		if (dbCursor.getCount() > 0){
			int noOfScorer = 0;
			dbCursor.moveToFirst();
			    while ((!dbCursor.isAfterLast())&&noOfScorer<dbCursor.getCount()) 
			    {
			    	infoList.add(new UserData(dbCursor.getString(0),
			    			appState.getDisplayName(dbCursor.getString(1)), dbCursor.getString(2),
			    			dbCursor.getInt(3), dbCursor.getString(4)));
			        noOfScorer++;
			        dbCursor.moveToNext();
			    }
			}
		return infoList;
	}
	

public ArrayList<MessageInfo> getAllMessages(String tableName){
	Cursor dbCursor=dbHelper.getMessages(tableName);
	ArrayList<MessageInfo> messageList=new ArrayList<MessageInfo>();
	if (dbCursor.getCount() > 0){
		int noOfScorer = 0;
		dbCursor.moveToFirst();
		    while ((!dbCursor.isAfterLast())&&noOfScorer<dbCursor.getCount()) 
		    {
		    	messageList.add(new MessageInfo(dbCursor.getString(0), dbCursor.getString(1), dbCursor.getInt(2),
		    			dbCursor.getString(3)));
		        noOfScorer++;
		        dbCursor.moveToNext();
		    }
		}
	return messageList;

	}

public int getUserStatus(String userName){
	Cursor dbCursor=dbHelper.getStatus(userName);
	if (dbCursor.getCount() > 0){
		dbCursor.moveToFirst();
		return dbCursor.getInt(0);   
		}
	return -1;
}
	
}
