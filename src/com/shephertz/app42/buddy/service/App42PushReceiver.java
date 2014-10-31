package com.shephertz.app42.buddy.service;

import android.content.Context;

import com.google.android.gcm.GCMBroadcastReceiver;

public class App42PushReceiver extends GCMBroadcastReceiver{
	@Override
	protected String getGCMIntentServiceClassName(Context context) { 
		return "com.shephertz.app42.buddy.service.App42PushService"; 
	} 
}
