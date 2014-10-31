package com.shephertz.app42.buddy.listener;

import java.util.HashMap;

import com.shephertz.app42.buddy.tools.ContactInfo;

public interface ContactListener {

	public void onContactFetched(HashMap<String, ContactInfo> contactMap);
	public void onErrorInFetching();
}
