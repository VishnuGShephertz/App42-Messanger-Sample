package com.shephertz.app42.buddy.app;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.shephertz.app42.buddy.listener.UserEventListener;
import com.shephertz.app42.buddy.service.App42ServiceApi;
import com.shephertz.app42.buddy.util.Constants;
import com.shephertz.app42.buddy.util.Utils;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.App42Response;
import com.shephertz.app42.paas.sdk.android.user.User;

public class LoginActivity extends Activity implements UserEventListener {

	private App42ServiceApi app42Service;
	private EditText userName;
	private EditText emailid;
	private SharedPreferences mPrefs;
	private ProgressDialog progressDialog;
	private ApplicationState appState;
	private String regName = "9252064192";
	private String myUSerName;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		progressDialog = new ProgressDialog(this);
		userName = (EditText) this.findViewById(R.id.uname);
		emailid = (EditText) this.findViewById(R.id.email);

		app42Service = App42ServiceApi.instance(this);
		appState = ApplicationState.instance();

		userName.setText(regName);

		emailid.setText("vikki" + "@gmail.com");
		//upDateCountryCode();
		appState.setCountryCode("91");
	}

	private void upDateCountryCode() {
		
		TelephonyManager manager = (TelephonyManager) this
				.getSystemService(this.TELEPHONY_SERVICE);
		String CountryID = manager.getSimCountryIso().toUpperCase();
		String[] rl = this.getResources().getStringArray(R.array.country_codes);
		for (int i = 0; i < rl.length; i++) {
			String[] g = rl[i].split(",");
			if (g[1].trim().equals(CountryID.trim())) {
				appState.setCountryCode("+" + g[0]);
				break;
			}
		}
	}

	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	public void onStart() {
		super.onStart();
		// userName.setText(appState.getUserName());
		// password.setText(appState.getPassword());
		// emailid.setText(appState.getMailId());
		// if(appState.isAuthenticated()){
		// gotoHomeActivity(appState.getUserName());
		// }
	}

	public void onSigninClicked(View view) {
		progressDialog = ProgressDialog.show(this, "", "signing in..");
		progressDialog.setCancelable(true);
		myUSerName=Utils.getFormattedNo(appState.getCountryCode(),
				userName.getText().toString());
		app42Service.authenticateUser(myUSerName,
				getDeviceId(), this);
	}

	public void onRegisterClicked(View view) {
		progressDialog = ProgressDialog.show(this, "", "registering..");
		progressDialog.setCancelable(true);
		myUSerName=Utils.getFormattedNo(appState.getCountryCode(),
				userName.getText().toString());
		app42Service.createUser(myUSerName, getDeviceId(), emailid
				.getText().toString(), this);
	}

	private void saveDetails() {
		appState.setUserName(myUSerName);
		appState.setPassword(getDeviceId());
		appState.setMailId(emailid.getText().toString());
		appState.setAuthenticated(true);
	}

	private String getDeviceId() {
		// TelephonyManager tm = (TelephonyManager)
		// getSystemService(TELEPHONY_SERVICE);
		return "123456789";// tm.getDeviceId();
	}

	private void gotoHomeActivity() {
		this.finish();
		AppContext.myUserName = myUSerName;
		Intent mainIntent = new Intent(this, BuddyEventList.class);
		this.startActivity(mainIntent);
	}

	@Override
	public void onUserCreated(final User user) {
		progressDialog.dismiss();
		if (user != null) {
			saveDetails();
			App42API.setLoggedInUser(Utils.getFormattedNo(appState.getCountryCode(),
					userName.getText().toString()));
			App42ServiceApi.instance(this).registerWithApp42(
					Constants.GoogleProjectNo);
			gotoHomeActivity();
			
		} else {
			Toast.makeText(this, R.string.user_creation_failed,
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onUserAuthenticated(final App42Response response) {
		progressDialog.dismiss();
		if (response != null) {
			saveDetails();
			App42API.setLoggedInUser(Utils.getFormattedNo(appState.getCountryCode(),
					userName.getText().toString()));
			App42ServiceApi.instance(this).registerWithApp42(
					Constants.GoogleProjectNo);
			gotoHomeActivity();
		} else {
			Toast.makeText(this, R.string.user_auth_failed, Toast.LENGTH_SHORT)
					.show();
		}

	}

	@Override
	public void onError(App42Exception ex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetAllUsers(ArrayList<String> users) {
		// TODO Auto-generated method stub
		
	}

}
