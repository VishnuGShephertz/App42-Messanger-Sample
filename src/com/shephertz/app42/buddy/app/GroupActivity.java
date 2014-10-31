package com.shephertz.app42.buddy.app;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shephertz.app42.buddy.adapter.MyCheckListAdapter;
import com.shephertz.app42.buddy.adapter.MyListAdapter;
import com.shephertz.app42.buddy.adapter.SeparateListAdapter;
import com.shephertz.app42.buddy.service.App42ServiceApi;
import com.shephertz.app42.buddy.util.Constants;
import com.shephertz.app42.paas.sdk.android.buddy.Buddy;

public class GroupActivity extends Activity implements OnItemClickListener {
	ListView list;

	private String groupname;
	private String ownername;
	ProgressDialog progressDialog;
	private ArrayList<Buddy> myGroups;;
	private App42ServiceApi app42Service;
	private ArrayList<String> groupFriends;
	private ArrayList<String> nonGroupFriedns;
	private ArrayList<String> selectedFrnds;
	private SeparateListAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_info);
		progressDialog = new ProgressDialog(this);
		buildProfileView();
		Intent intent = getIntent();
		TextView groupName = (TextView) findViewById(R.id.group_name);
		groupname = intent.getStringExtra(Constants.GroupName);
		groupName.setText(groupname);
		ownername = intent.getStringExtra(Constants.Owner);
		list = (ListView) findViewById(R.id.list);

		selectedFrnds = new ArrayList<String>();

		app42Service = App42ServiceApi.instance(this);
		showLoadingPopup("Loading data...");
		if (ownername.equalsIgnoreCase(AppContext.myUserName)) {
//			app42Service.loadFriednsByGroup(AppContext.myUserName, ownername,
//					groupname, true, this);
		} else {
//			app42Service.loadFriednsByGroup(AppContext.myUserName, ownername,
//					groupname, false, this);
		}

	}
	private void buildProfileView(){
		ImageView myPic = (ImageView) findViewById(R.id.user_pic);
		TextView myName= (TextView) findViewById(R.id.username);
		ApplicationState appState=ApplicationState.instance();
		if(appState.getAvatarName()==null){
			myName.setText(AppContext.myUserName);
		}
		else{
			myName.setText(appState.getAvatarName());
		}
		if(appState.getAvatarPic()!=null){
		myPic.setImageBitmap(appState.getAvatarPic());
		}
		else{
			myPic.setImageResource(R.drawable.default_pic);
		}
	}
	public void setSelected(boolean isChecked, int pos) {
		try {
			if (isChecked && !selectedFrnds.contains(nonGroupFriedns.get(pos))) {
				selectedFrnds.add(nonGroupFriedns.get(pos));
			} else if (!isChecked
					&& selectedFrnds.contains(nonGroupFriedns.get(pos))) {
				selectedFrnds.remove(nonGroupFriedns.get(pos));
			}
		} catch (Exception e) {

		}

	}

	private void showLoadingPopup(String message) {
		progressDialog.setMessage(message);
		progressDialog.show();
	}

	public void onAddClicked(View view) {
		if (selectedFrnds.size() > 0) {
			showLoadingPopup("Adding friends..");
//			app42Service.addFriednToGroup(AppContext.myUserName, groupname,
//					selectedFrnds, this);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

	}



	public void makeList() {
		adapter = new SeparateListAdapter(this);
		if (groupFriends.size() > 0) {
			MyListAdapter mylist = new MyListAdapter(this, groupFriends);
			adapter.addSection("Group Friedns", mylist);
		}
		if (nonGroupFriedns.size() > 0) {
			MyCheckListAdapter mylist1 = new MyCheckListAdapter(this,
					nonGroupFriedns);
			adapter.addSection("Friedns", mylist1);
		}
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
	}

//	@Override
//	public void onGetGroupFriends(ArrayList<String> groupFrnds,
//			ArrayList<String> friends) {
//		progressDialog.dismiss();
//		this.groupFriends = groupFrnds;
//
//		if (friends == null) {
//			this.nonGroupFriedns = new ArrayList<String>();
//		} else {
//			this.nonGroupFriedns = friends;
//			if (this.groupFriends != null)
//				nonGroupFriedns.removeAll(groupFrnds);
//			else
//				this.groupFriends = new ArrayList<String>();
//		}
//		makeList();
//	}

//	@Override
//	public void onFriendAddedInGroup() {
//		progressDialog.dismiss();
//		Toast.makeText(this, R.string.buddy_added, Toast.LENGTH_SHORT).show();
//		nonGroupFriedns.removeAll(selectedFrnds);
//		groupFriends.addAll(selectedFrnds);
//		adapter.notifyDataSetChanged();
//	}

	

}