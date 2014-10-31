package com.shephertz.app42.buddy.listener;


import com.shephertz.app42.buddy.events.NotifyCallbackEvent;
import com.shephertz.app42.paas.sdk.android.App42Exception;

public interface PushEventListener{

	public void onPushSent(NotifyCallbackEvent notifyEvent);
	public void onPushFailed(App42Exception ex);
	public void onGroupCreated(String groupName,String ownerName);
}
