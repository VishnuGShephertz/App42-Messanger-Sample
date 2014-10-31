package com.shephertz.app42.buddy.listener;

import java.util.ArrayList;

import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.App42Response;
import com.shephertz.app42.paas.sdk.android.user.User;

public interface UserEventListener {
	public void onUserCreated(User response);
	public void onError(App42Exception ex);
	public void onUserAuthenticated(App42Response response);
	public void onGetAllUsers(ArrayList<String> users);
}
