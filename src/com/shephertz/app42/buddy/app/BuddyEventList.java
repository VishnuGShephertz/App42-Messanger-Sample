package com.shephertz.app42.buddy.app;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shephertz.app42.buddy.adapter.BuddyAdapter;
import com.shephertz.app42.buddy.adapter.HomeAdapter;
import com.shephertz.app42.buddy.command.EventCode;
import com.shephertz.app42.buddy.command.MessageCode;
import com.shephertz.app42.buddy.events.MessageEvent;
import com.shephertz.app42.buddy.events.NotifyCallbackEvent;
import com.shephertz.app42.buddy.listener.AvatarEventListener;
import com.shephertz.app42.buddy.listener.BaseListener;
import com.shephertz.app42.buddy.listener.PushEventListener;
import com.shephertz.app42.buddy.service.App42ServiceApi;
import com.shephertz.app42.buddy.tools.DatabaseService;
import com.shephertz.app42.buddy.tools.UserData;
import com.shephertz.app42.buddy.util.Utils;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.avatar.Avatar;

public class BuddyEventList extends Activity implements OnItemClickListener,
		AvatarEventListener,PushEventListener {
	private final int RESULT_LOAD_IMAGE = 1;
	private String avatarName;
	private App42ServiceApi app42Service;
	private ApplicationState appState;
	private ImageView myPic;
	private TextView myName;
	private ArrayList<UserData> userInfo;
	private DatabaseService dbService;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buddy_home);
		myPic = (ImageView) findViewById(R.id.user_pic);
		myName = (TextView) findViewById(R.id.username);
		ListView list = (ListView) findViewById(R.id.list);
		
		list.setOnItemClickListener(this);
		app42Service = App42ServiceApi.instance(this);
		appState = ApplicationState.instance();
		buildProfileView();
		dbService=DatabaseService.instance(this);
		userInfo=DatabaseService.instance(this).getAllDetails();
		list.setAdapter(new BuddyAdapter(this, userInfo));
		if(appState.getAvatarName()!=null)
		browsePhotoDialog();
	
		
	}

	private void buildProfileView() {
		if (appState.getAvatarName() == null) {
			myName.setText(AppContext.myUserName);
		} else {
			myName.setText(appState.getAvatarName());
		}
		if (appState.getAvatarPic() != null) {
			myPic.setBackgroundResource(0);
			myPic.setImageBitmap(appState.getAvatarPic());
		} else {
			myPic.setImageResource(R.drawable.default_pic);
		}
	}


	public void onLogoutClicked(View view) {
		ApplicationState.instance().setAuthenticated(false);
		this.finish();
		Intent mainIntent = new Intent(this, LoginActivity.class);
		this.startActivity(mainIntent);
	}

	public void onEditClicked(View view) {
		browsePhotoDialog();
	}

	/*
	 * used to create alert dialog when logout option is selected
	 * 
	 * @param name of friend whom you want to sahre image
	 */
	public void browsePhotoDialog() {
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		alertbox.setTitle("Create Your Avatar");
		final EditText input = new EditText(this);
		input.setHint("Avatar name");
		alertbox.setView(input);
		alertbox.setPositiveButton("Browse Pic",
				new DialogInterface.OnClickListener() {
					// do something when the button is clicked
					public void onClick(DialogInterface arg0, int arg1) {
						browsePhoto(input.getText().toString());
					}
				});
		alertbox.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {

					}
				});
		alertbox.show();
	}

	/*
	 * Call when user clicks on browse photo
	 */
	private void browsePhoto(String avtarName) {
		this.avatarName = avtarName;
		if (this.avatarName != null && !this.avatarName.equals("")) {
			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(i, RESULT_LOAD_IMAGE);
		} else {
			Toast.makeText(this, R.string.error_avatar, Toast.LENGTH_SHORT)
					.show();
		}
	}

	/*
	 * Callback when user select image from gallery for upload and call
	 * and he has to send autherize callback
	 * 
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			createAvater(picturePath);
		}

	}

	public void showInvitations(View view) {
//		Intent intent = new Intent(this, InvitationList.class);
//		startActivity(intent);
	}

	public void showUserList(View view) {
		Intent intent = new Intent(this, UserList.class);
		startActivity(intent);
	}

	public void showGroupList(View view) {
		Intent intent = new Intent(this, GroupList.class);
		startActivity(intent);
	}

	public void showRejections(View view) {

	}

	private void createAvater(String imgPath) {
		app42Service.createAvatar(AppContext.myUserName, avatarName, imgPath,
				"My Avatar", this);
	}



	@Override
	public void onAvatarCreated(Avatar avatar) {
		appState.setAvatarUserName(avatar.getUserName());
		appState.setAvatarName(avatar.getName());
		appState.setAvatarPicUrl(avatar.getTinyURL());
		Utils.loadImageFromUrl(avatar.getTinyURL(), this);
		buildProfileView();
	}

	@Override
	public void onAvatarCreationFailed(App42Exception ex) {
	}

	@Override
	public void onLoadImage(Bitmap bitmap) {
		if (bitmap != null) {
			appState.setAvatarPic(bitmap);
			myPic.setImageBitmap(bitmap);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		UserData user=userInfo.get(arg2);
		int eventCode=user.getEvent();
		if(eventCode==EventCode.INVITATION.getEventCode()){
			acceptRequest(user);
		}
		else {
			if(eventCode==EventCode.ACCEPTANCE.getEventCode()){
				eventCode=EventCode.BUDDY_MESSAGE.getEventCode();
			}
			else if(eventCode==EventCode.GROUP_JOIN.getEventCode()){
				eventCode=EventCode.GROUP_MESSAGE.getEventCode();
			}
			Intent intent=new Intent(this,MessageActivity.class);
			intent.putExtra(BaseListener.Code, eventCode);
			intent.putExtra(BaseListener.Username,user.getUserName());
			intent.putExtra(BaseListener.PicUrl,user.getPicUrl());
			startActivity(intent);
		}
	}

	/*
	 * used to create alert dialog when logout option is selected
	 * 
	 * @param name of friend whom you want to sahre image
	 */
	public void acceptRequest(final UserData user) {
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		alertbox.setTitle("Add as Friend "+user.getDisplayName());
		alertbox.setPositiveButton("Add ",
				new DialogInterface.OnClickListener() {
					// do something when the button is clicked
					public void onClick(DialogInterface arg0, int arg1) {
						accept(user);
					}
				});
		alertbox.setNegativeButton("Reject",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						reject(user);
					}
				});
		alertbox.show();
	}
	private void accept(UserData userData){
		try {
			userData.setEvent(EventCode.ACCEPTANCE.getEventCode());
			dbService.performClickOperation(MessageEvent.getBuddyEvent(userData));
			JSONObject json=AppContext.getEvent(EventCode.ACCEPTANCE.getEventCode(),
					"", AppContext.myUserName, "", MessageCode.TEXT.getMsgCode(), Utils.getDateTime(), appState.getAvatarPicUrl(), 
					"");
			app42Service.sendMessageToUser(userData.getUserName(), json,EventCode.ACCEPTANCE.getEventCode(), this);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		app42Service.sendMessageToUser(buddyId, json,EventCode.INVITATION.getEventCode(), this);
	}
	
	private void reject(UserData userData){
		try {
			userData.setEvent(EventCode.REJECTION.getEventCode());
			dbService.performClickOperation(MessageEvent.getBuddyEvent(userData));
			JSONObject json=AppContext.getEvent(EventCode.REJECTION.getEventCode(),
					"", AppContext.myUserName, "", MessageCode.TEXT.getMsgCode(), Utils.getDateTime(), appState.getAvatarPicUrl(), 
					"");
			app42Service.sendMessageToUser(userData.getUserName(), json,EventCode.REJECTION.getEventCode(), this);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		app42Service.sendMessageToUser(buddyId, json,EventCode.INVITATION.getEventCode(), this);
	}
//	private void accept(UserData userData){
//		JSONObject json=AppContext.getEvent(eventCode,
//				"", AppContext.myUserName, "", MessageCode.TEXT.getMsgCode(), Utils.getDateTime(), appState.getAvatarPicUrl(), 
//				"");
//		app42Service.sendMessageToUser(buddyId, json,EventCode.INVITATION.getEventCode(), this);
//	}
//}
//	private void reject(int eventCode){
//		JSONObject json=AppContext.getEvent(eventCode,
//				"", AppContext.myUserName, "", MessageCode.TEXT.getMsgCode(), Utils.getDateTime(), appState.getAvatarPicUrl(), 
//				"");
//		app42Service.sendMessageToUser(userData.g, json,EventCode.INVITATION.getEventCode(), this);
//	}

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