package com.shephertz.app42.buddy.app;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shephertz.app42.buddy.adapter.MyGroupAdapter;
import com.shephertz.app42.buddy.command.EventCode;
import com.shephertz.app42.buddy.command.MessageCode;

import com.shephertz.app42.buddy.events.MessageEvent;
import com.shephertz.app42.buddy.events.NotifyCallbackEvent;
import com.shephertz.app42.buddy.listener.PushEventListener;
import com.shephertz.app42.buddy.service.App42ServiceApi;
import com.shephertz.app42.buddy.tools.DatabaseService;
import com.shephertz.app42.buddy.tools.GroupInfo;
import com.shephertz.app42.buddy.util.Utils;
import com.shephertz.app42.paas.sdk.android.App42Exception;
public class GroupList extends Activity implements PushEventListener{
	ListView list;
	ProgressDialog progressDialog;
	private App42ServiceApi app42Service;
	private MyGroupAdapter adapter;
	private ArrayList<GroupInfo> groupsInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_list);
		progressDialog = new ProgressDialog(this);
		list = (ListView) findViewById(R.id.list);
		buildProfileView();
		app42Service = App42ServiceApi.instance(this);
		DatabaseService dbService=DatabaseService.instance(this);
		groupsInfo=dbService.getGroupList();
		MyGroupAdapter grpad=new MyGroupAdapter(this,groupsInfo);
		list.setAdapter(grpad);
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

	public void createNewGroup(View view) {
		sendGroupPopup();
	}

	public void onError(Exception ex) {
		progressDialog.dismiss();
	}

	/**
	 * Create and return an example alert dialog with an edit text box.
	 */
	private void sendGroupPopup() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Create Group");

		final EditText input = new EditText(this);
		input.setHint("Group name");
		builder.setView(input);
		builder.setPositiveButton("create",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						String groupname = input.getText().toString();
						showLoadingPopup("Creating group....");
						createGroup(groupname);

					}
				});

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		builder.show();
	}

	private void createGroup(String groupName) {
		app42Service.createGroup("+91"+AppContext.myUserName, groupName, this);
	}

	public void onEditGroupClicked(int pos) {
		Intent intent = new Intent(this, GroupActivity.class);
		intent.putExtra("group_Info", groupsInfo.get(pos));
		startActivity(intent);
	}

	@Override
	public void onPushSent(NotifyCallbackEvent notifyEvent) {
     progressDialog.dismiss();
	}

	@Override
	public void onPushFailed(App42Exception ex) {
		progressDialog.dismiss();
	}

	@Override
	public void onGroupCreated(String groupName, String ownerName) {
		progressDialog.dismiss();
		// TODO Auto-generated method stub
		AppContext.showResponse(new MessageEvent(EventCode.GROUP_CREATION.getEventCode(), groupName,
				ownerName, "", MessageCode.TEXT.getMsgCode(), Utils.getDateTime(), "", groupName), this);

	}

}