package com.shephertz.app42.buddy.app;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.shephertz.app42.buddy.adapter.MessageAdapter;
import com.shephertz.app42.buddy.command.EventCode;
import com.shephertz.app42.buddy.command.MessageCode;
import com.shephertz.app42.buddy.events.NotifyCallbackEvent;
import com.shephertz.app42.buddy.listener.BaseListener;
import com.shephertz.app42.buddy.listener.PushEventListener;
import com.shephertz.app42.buddy.service.App42ServiceApi;
import com.shephertz.app42.buddy.tools.DatabaseService;
import com.shephertz.app42.buddy.tools.MessageInfo;
import com.shephertz.app42.buddy.util.Utils;
import com.shephertz.app42.paas.sdk.android.App42Exception;

/**
 * MessageActivity is a main Activity to show a ListView containing Message
 * items
 * 
 * @author Vishnu Garg
 * 
 */
public class MessageActivity extends Activity implements PushEventListener {
	/** Called when the activity is first created. */

	private MessageAdapter adapter;
	private App42ServiceApi app42Service;
	private String userName;
	private int eventCode;
	private String tableName;
	private String picUrl;
	private ProgressDialog progressDialog;
	private ListView list;
	private EditText text;
	private ArrayList<MessageInfo> messages;
	private DatabaseService dbService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_list);

		progressDialog = new ProgressDialog(this);
		text = (EditText) this.findViewById(R.id.text);

		parseIntentData();
		this.app42Service = App42ServiceApi.instance(this);
		dbService = DatabaseService.instance(this);
		list = (ListView) findViewById(R.id.list);
		messages = new ArrayList<MessageInfo>();
		// adapter = new MessageAdapter(this, messages, AppContext.myUserName);
		list.setAdapter(adapter);
		((TextView) findViewById(R.id.page_header)).setText("Messages");
			messages = dbService.getAllMessages(tableName);
		
	}

	// private void loadMessages() {
	// if (msgFrom == Constants.AllMsg) {
	// showProgressDialog();
	// app42Service.getAllMessages(AppContext.myUserName, this);
	// } else if (msgFrom == Constants.BuddyMsg) {
	// showProgressDialog();
	// app42Service.getAllMessagesFromBuddy(AppContext.myUserName,
	// buddyName, this);
	// } else if (msgFrom == Constants.GroupMsg) {
	// showProgressDialog();
	// app42Service.getAllMessagesFromGroup(AppContext.myUserName,
	// ownerName, grpName, this);
	// }
	// }

	private void parseIntentData() {
		Intent intent = getIntent();
		eventCode = intent.getIntExtra(BaseListener.Code, -1);
		userName = intent.getStringExtra(BaseListener.Username);
		tableName=Utils.getTableName(eventCode, userName);
		picUrl = intent.getStringExtra(BaseListener.PicUrl);
	}

	private void showProgressDialog() {
		progressDialog.setMessage("Loading messages.......");
		progressDialog.show();
	}

	void onError() {
		progressDialog.dismiss();
	}

	public void onStop() {
		super.onStop();

	}


	public void sendMessage(View view) {
		try {
			String newMessage = text.getText().toString().trim();
			JSONObject json;
			if (newMessage.length() > 0) {
				text.setText("");
				

					json = AppContext.getEvent(
							eventCode, "",
							AppContext.myUserName, newMessage,
							MessageCode.TEXT.getMsgCode(), Utils.getDateTime(),
							"", "");
					app42Service.sendMessageToUser(userName, json,
							EventCode.INVITATION.getEventCode(), this);

				} else if (eventCode == EventCode.GROUP_MESSAGE.getEventCode()) {
					json = AppContext.getEvent(
							eventCode, "",
							AppContext.myUserName, newMessage,
							MessageCode.TEXT.getMsgCode(), Utils.getDateTime(),
							"", userName);
					app42Service.sendMessageToGroup(userName, json,
							eventCode, this);
				}
				addNewMessage(newMessage);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	void addNewMessage(String msg) {

		MessageInfo msgInfo=new MessageInfo(AppContext.myUserName, msg, MessageCode.TEXT.getMsgCode(),
				Utils.getDateTime());
		 messages.add(msgInfo);
		adapter.notifyDataSetChanged();
		list.setSelection(messages.size() - 1);
	}

	@Override
	public void onPushSent(NotifyCallbackEvent notifyEvent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPushFailed(App42Exception ex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGroupCreated(String groupName, String ownerName) {
		// TODO Auto-generated method stub

	}

}