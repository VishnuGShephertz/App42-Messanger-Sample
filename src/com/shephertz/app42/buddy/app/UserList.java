package com.shephertz.app42.buddy.app;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.shephertz.app42.buddy.adapter.ContactAdapter;
import com.shephertz.app42.buddy.command.EventCode;
import com.shephertz.app42.buddy.command.MessageCode;
import com.shephertz.app42.buddy.events.MessageEvent;
import com.shephertz.app42.buddy.events.NotifyCallbackEvent;
import com.shephertz.app42.buddy.listener.ContactListener;
import com.shephertz.app42.buddy.listener.PushEventListener;
import com.shephertz.app42.buddy.listener.UserEventListener;
import com.shephertz.app42.buddy.service.App42ServiceApi;
import com.shephertz.app42.buddy.tools.ContactInfo;
import com.shephertz.app42.buddy.tools.ContactReader;
import com.shephertz.app42.buddy.tools.DatabaseService;
import com.shephertz.app42.buddy.tools.DbColumns;
import com.shephertz.app42.buddy.util.Utils;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.App42Response;
import com.shephertz.app42.paas.sdk.android.user.User;

public class UserList extends Activity implements ContactListener,
		UserEventListener, PushEventListener {
	private ListView list;
	private ProgressDialog progressDialog;
	private ArrayList<String> users;
	private App42ServiceApi app42Service;
	private ApplicationState appState;
	private HashMap<String, ContactInfo> contactMap;
	private ContactInfo currentContact;
	private DatabaseService dbService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_list);
		progressDialog = new ProgressDialog(this);
		list = (ListView) findViewById(R.id.list);
		((Button) findViewById(R.id.create)).setVisibility(View.GONE);
		app42Service = App42ServiceApi.instance(this);
		appState = ApplicationState.instance();
		dbService = DatabaseService.instance(this);
		contactMap = new HashMap<String, ContactInfo>();
		showLoadingPopup("Loading data....");
		app42Service.loaduserList(this);
		buildProfileView();
	}

	private void buildProfileView() {
		ImageView myPic = (ImageView) findViewById(R.id.user_pic);
		TextView myName = (TextView) findViewById(R.id.username);
		ApplicationState appState = ApplicationState.instance();
		if (appState.getAvatarName() == null) {
			myName.setText(AppContext.myUserName);
		} else {
			myName.setText(appState.getAvatarName());
		}
		if (appState.getAvatarPic() != null) {
			myPic.setImageBitmap(appState.getAvatarPic());
		} else {
			myPic.setImageResource(R.drawable.default_pic);
		}
	}

	private void showLoadingPopup(String message) {
		progressDialog.setMessage(message);
		progressDialog.show();
	}

	@Override
	public void onGetAllUsers(ArrayList<String> users) {
		users.remove(AppContext.myUserName);
		appState.setApp42UserInfo(users);
		ContactReader.instance(this).loadContacts(appState.getCountryCode(),
				this);
		this.users = users;
	}

	@Override
	public void onContactFetched(HashMap<String, ContactInfo> fetchedMap) {
		// TODO Auto-generated method stub
		progressDialog.dismiss();
		String[] keys = fetchedMap.keySet().toArray(
				new String[contactMap.size()]);

		HashMap<String, ContactInfo> shareMap = new HashMap<String, ContactInfo>();
		for (int i = 0; i < keys.length; i++) {
			String key = keys[i];
			// if(!appState.isMyBuddy(key)){
			if (users.contains(key)) {
				fetchedMap.get(key).setInstalled(true);
				contactMap.put(key, fetchedMap.get(key));
			}
			// else{
			// shareMap.put(key, fetchedMap.get(key));
			// }
			// }
		}
		// contactMap.putAll(shareMap);
		appState.setContactInfo(contactMap);
		list.setAdapter(new ContactAdapter(this, contactMap));
	}

	@Override
	public void onErrorInFetching() {

		progressDialog.dismiss();

	}

	@Override
	public void onUserCreated(User response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserAuthenticated(App42Response response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(App42Exception ex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPushFailed(App42Exception ex) {
		// TODO Auto-generated method stub
		Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onPushSent(NotifyCallbackEvent notifyEvent) {

		AppContext.showResponse(new MessageEvent(notifyEvent.getResponseCode(),
				currentContact.getName(), notifyEvent.getUserName(), "",
				MessageCode.TEXT.getMsgCode(), Utils.getDateTime(),
				currentContact.getPicUrl(), ""), this);

	}

	public void performAction(ContactInfo contact, String buddyId) {
		// TODO Auto-generated method stub
         this.currentContact=contact;
		if (contact.isInstalled()) {
			int currentStatus = dbService.getUserStatus(buddyId);

		} else {
			Toast.makeText(this, "Share Your APP", Toast.LENGTH_SHORT).show();
		}
	}

	private void checkBeforeInvitation(String buddyId, int eventStatus) {
		if (eventStatus == -1) {
			JSONObject json;
			try {
				json = AppContext.getEvent(EventCode.INVITATION.getEventCode(),
						"", AppContext.myUserName, "",
						MessageCode.TEXT.getMsgCode(), Utils.getDateTime(),
						appState.getAvatarPicUrl(), "");
				app42Service.sendMessageToUser(buddyId, json,
						EventCode.INVITATION.getEventCode(), this);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (eventStatus == EventCode.INVITATION.getEventCode()) {
			Toast.makeText(this, currentContact.getName()+" already request has bennt sent", Toast.LENGTH_SHORT).show();
		} else if (eventStatus == EventCode.ACCEPTANCE.getEventCode()) {
			Toast.makeText(this,  currentContact.getName()+" already friend with this", Toast.LENGTH_SHORT).show();
		} 
		else if (eventStatus == EventCode.REJECTION.getEventCode()) {
			Toast.makeText(this,  currentContact.getName()+"  request has bennt rejected", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onGroupCreated(String groupName, String ownerName) {
		// TODO Auto-generated method stub

	}

}