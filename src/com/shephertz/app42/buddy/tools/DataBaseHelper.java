package com.shephertz.app42.buddy.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.shephertz.app42.buddy.command.EventCode;
import com.shephertz.app42.buddy.events.MessageEvent;
import com.shephertz.app42.buddy.util.Utils;

public class DataBaseHelper extends SQLiteOpenHelper implements DbColumns {
	private static final String TAG = DataBaseHelper.class.getSimpleName();
	private static final String DATABASE_NAME = "App42Messanger.db";
	private static final int DATABASE_VERSION = 1;
	
	private static final String TABLE_INFO = "userInfo";
	//private static final String TABLE_INVITATION = "invitation";
	//private static final String TABLE_REJECTION = "rejection";
	
	

	private static final String QueryCreateInfo = "CREATE TABLE "
			+ TABLE_INFO + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ USERNAME + " VARCHAR(50), " + DISPLAY_NAME + " VARCHAR(50), "
			+ OWNER_NAME + " VARCHAR(50), " + TYPE + " INTEGER, " + DATE_TIME
			+ " VARCHAR(25), " + TABLE_NAME + " VARCHAR(50), " + PIC_URL
			+ " TEXT );";

	private static final String TABLE_MESSAGE_DROP = "DROP TABLE IF EXISTS "
			+ TABLE_INFO;

	private static DataBaseHelper mInstance;
	private SQLiteDatabase dbInstance;

	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		dbInstance = getWritableDatabase();
	}

	public static DataBaseHelper instance(Context context) {
		if (mInstance == null) {
			mInstance = new DataBaseHelper(context);
		}
		return mInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(QueryCreateInfo);
	//	db.execSQL(QueryCreateInvitation);
	//	db.execSQL(QueryCreateRejection);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrade der DB von V: " + oldVersion + " zu V:"
				+ newVersion + "; Alle Daten werden gelöscht!");
		db.execSQL(TABLE_MESSAGE_DROP);
		onCreate(db);
	}

//	/*
//	 * Called when Notify Rejection event
//	 */
//	public void rejectRequest(String name, String displayName, int type) {
//		try {
//			ContentValues values = new ContentValues();
//			values.put(USERNAME, name);
//			values.put(DISPLAY_NAME, displayName);
//			values.put(TYPE, type);
//			values.put(DATE_TIME, Utils.getDateTime());
//			dbInstance.insert(TABLE_REJECTION, null, values);
//		} catch (SQLiteException e) {
//			Log.e(TAG, "insert()", e);
//		}
//	}
//
//	/*
//	 * Clear table when all rejections are seen by user
//	 */
//	public void clearRejectionTable() {
//		dbInstance.delete(TABLE_REJECTION, null, null);
//	}

//	/*
//	 * Called when any Invitation Came
//	 */
//	public void insertInvitation(String name, String displayName, int type) {
//
//		try {
//			ContentValues values = new ContentValues();
//			values.put(USERNAME, name);
//			values.put(DISPLAY_NAME, displayName);
//			values.put(TYPE, type);
//			values.put(DATE_TIME, Utils.getDateTime());
//			dbInstance.insert(TABLE_INVITATION, null, values);
//		} catch (SQLiteException e) {
//			Log.e(TAG, "insert()", e);
//		}
//	}

	/*
	 * Called in Case of acceptance or InVitation event
	 * 
	 */
	public void createUserTable(MessageEvent event) {
		try {
			
			String query = "CREATE TABLE IF NOT EXISTS " + Utils.getUserTableName(event.getUserName()) + " ("+ USERNAME + " VARCHAR(50), "
					+ TYPE + " INTEGER, " + DATE_TIME + " VARCHAR(25), "
				+ MESSAGE + " TEXT);";
			dbInstance.execSQL(query);
			if(event.getEventCode()==EventCode.INVITATION.getEventCode())
			insertUserInformation(event);
			else if(event.getEventCode()==EventCode.ACCEPTANCE.getEventCode()){
				updateUserStatus(event);
			}
		} catch (SQLiteException e) {
			Log.e(TAG, "insert()", e);
		}
	}
	public void updateUserStatus(MessageEvent event){
		   ContentValues updateValues = new ContentValues();
			updateValues.put(TYPE, event.getEventCode());
			updateValues.put(DATE_TIME, event.getTime());
			dbInstance.update(TABLE_INFO, updateValues, USERNAME
					+ "=?", new String[] { 
					event.getUserName() });
		}
	/*
	 * Calls when any Notification comes except Message
	 */
	public void insertUserInformation(MessageEvent event){
		
		if(isUserExist(event.getUserName())){
		dbInstance.insert(
				TABLE_INFO,
				null,
				getQueryString(event.getUserName(), event.getDisplayName(), event.getUserName(),
						event.getEventCode(), event.getUserName(), event.getPicUrl()));
		}
	}
	
	
	private boolean isUserExist(String userName){
		Cursor cursor=getStatus(userName);
		if(cursor.getCount()>0){
			return false;
		}
		else
		{
			return true;
		}
		
		
	}
	
	/*
	 * Calls when any Notification comes except Message
	 */
	public void insertGroupInformation(MessageEvent event){
		
		dbInstance.insert(
				TABLE_INFO,
				null,
				getQueryString(event.getUserName(), event.getDisplayName(), event.getGroupName(),
						event.getEventCode(), event.getGroupName(), event.getPicUrl()));
	}
	/*
	 * Called at Group Creation
	 * Called if Group Join in case also update info in Info Table
	 * 
	 */
	public void createGroupTable(MessageEvent event) {
		try {
			String query = "CREATE TABLE IF NOT EXISTS " + Utils.getGrouptableName(event.getGroupName()) + " ("
					+ TYPE + " INTEGER, " + DATE_TIME + " VARCHAR(25), "
					+ USERNAME + " VARCHAR(50), " + MESSAGE + " TEXT);";
			dbInstance.execSQL(query);
			if(event.getEventCode()==EventCode.GROUP_CREATION.getEventCode()){
				insertGroupInformation(event);
			}
			else if(event.getEventCode()==EventCode.GROUP_JOIN.getEventCode()){
				updateGroupStatus(event);
			}
		} catch (SQLiteException e) {
			Log.e(TAG, "insert()", e);
		}
	}
   public void updateGroupStatus(MessageEvent event){
	   ContentValues updateValues = new ContentValues();
		updateValues.put(TYPE, event.getEventCode());
		updateValues.put(DATE_TIME, event.getTime());
		dbInstance.update(TABLE_INFO, updateValues, USERNAME
				+ "=?", new String[] { 
				event.getGroupName() });
	}

	public ContentValues getQueryString(String owner, String displayName,
			String name, int type, String tableName, String picUrl) {
		ContentValues values = new ContentValues();
		values.put(USERNAME, name);
		values.put(OWNER_NAME, owner);
		values.put(DISPLAY_NAME, displayName);
		values.put(PIC_URL, picUrl);
		values.put(TYPE, type);
		values.put(DATE_TIME, Utils.getDateTime());
		values.put(TABLE_NAME, type);
		return values;
	}


	public void insertGroupMessage(MessageEvent messageEvent) {
		try {
			ContentValues values = new ContentValues();
			values.put(USERNAME, messageEvent.getUserName());
			values.put(MESSAGE, messageEvent.getMessage());
			values.put(TYPE, messageEvent.getMsgType());
			values.put(DATE_TIME, messageEvent.getTime());
			dbInstance.insert(Utils.getGrouptableName(messageEvent.getGroupName()),
					null, values);
			messageEvent.setUserName(messageEvent.getGroupName());
			updateInfoTable(messageEvent);
		} catch (SQLiteException e) {
			Log.e(TAG, "insert()", e);
		}
	}

	public void insertBuddyMessage(MessageEvent messageEvent) {	
		try {
			ContentValues values = new ContentValues();
			values.put(USERNAME, messageEvent.getUserName());
			values.put(MESSAGE, messageEvent.getMessage());
			values.put(TYPE, messageEvent.getMsgType());
			values.put(DATE_TIME, messageEvent.getTime());
			dbInstance.insert(Utils.getUserTableName(messageEvent.getUserName()),
					null, values);
			updateInfoTable(messageEvent);
		} catch (SQLiteException e) {
			Log.e(TAG, "insert()", e);
		}
	}

	private void updateInfoTable(MessageEvent messageEvent)
			throws SQLiteException {
		ContentValues updateValues = new ContentValues();
		updateValues.put(DATE_TIME, messageEvent.getTime());
		updateValues.put(DISPLAY_NAME, messageEvent.getDisplayName());
		dbInstance.update(TABLE_INFO, updateValues, USERNAME
				+ "=?", new String[] {
				messageEvent.getUserName() });
	}

//	/*
//	 * Called when Group Joined or rejection Called when Invitation accepted or
//	 * Rejected to delete particular record
//	 */
//	public void deleteFromInvitation(String name, int tableType) {
//		dbInstance.delete(TABLE_INVITATION, USERNAME + "=? AND " + TYPE + "=?",
//				new String[] { name, Integer.toString(tableType) });
//	}

	
	public void updateGroupDisplayName(MessageEvent event) {
		ContentValues updateValues = new ContentValues();
		updateValues.put(DISPLAY_NAME, event.getDisplayName());
		updateValues.put(DATE_TIME, event.getTime());
		dbInstance.update(TABLE_INFO, updateValues, TYPE + "=? AND " + USERNAME
				+ "=?", new String[] { Integer.toString(GROUP_TYPE),
				event.getUserName() });
	}
	
	public void updateGroupPic(MessageEvent event) {
		ContentValues updateValues = new ContentValues();
		updateValues.put(PIC_URL, event.getPicUrl());
		updateValues.put(DATE_TIME, event.getTime());
		dbInstance.update(TABLE_INFO, updateValues, TYPE + "=? AND " + USERNAME
				+ "=?", new String[] { Integer.toString(GROUP_TYPE),
				event.getUserName() });
	}

	public Cursor getGroupInfo() {
		String SELECT_QUERY = "SELECT  " + USERNAME + "," + DISPLAY_NAME + "," + PIC_URL + ","
				+ OWNER_NAME + " FROM " + TABLE_INFO + " WHERE " + TYPE + "="
				+ EventCode.GROUP_CREATION.getEventCode() + " ORDER BY " + _ID + " ASC";
		return dbInstance.rawQuery(SELECT_QUERY, null);
	}

	public Cursor getAllInfo(){
		String SELECT_QUERY = "SELECT  " + USERNAME + "," + DISPLAY_NAME + "," + PIC_URL + ","
				+ TYPE + ","+ DATE_TIME + " FROM " + TABLE_INFO +  " ORDER BY " + _ID + " ASC";
		return dbInstance.rawQuery(SELECT_QUERY, null);
	}
	
	public Cursor getStatus(String userName){
		String SELECT_QUERY = "SELECT  " + TYPE +" FROM " + TABLE_INFO + " WHERE " + USERNAME + "="
				+ userName;
		return dbInstance.rawQuery(SELECT_QUERY, null);
	}
	public Cursor getMessages(String tablename){
		String SELECT_QUERY = "SELECT  " + USERNAME + "," + MESSAGE + "," + TYPE + ","
				+ DATE_TIME +" FROM " + tablename ;
		return dbInstance.rawQuery(SELECT_QUERY, null);
	}
	

}
