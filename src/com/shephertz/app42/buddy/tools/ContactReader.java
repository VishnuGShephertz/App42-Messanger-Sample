package com.shephertz.app42.buddy.tools;

import java.util.LinkedHashMap;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;

import com.shephertz.app42.buddy.listener.ContactListener;
import com.shephertz.app42.buddy.util.Utils;
import com.shephertz.app42.paas.sdk.android.App42Exception;

public class ContactReader {
	private static ContactReader mInstance = null;
	private Context mContext;
	private String countryCode = "+91";

	private ContactReader(Context context) {
		this.mContext = context;
	}

	public static ContactReader instance(Context context) {
		if (mInstance == null) {
			mInstance = new ContactReader(context);
		}
		return mInstance;
	}

	public void loadContacts(final String countryCode,
			final ContactListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					final LinkedHashMap<String, ContactInfo> contactMap = GetMyPhoneContacts(countryCode);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onContactFetched(contactMap);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								System.out.println(ex.toString());
								callBack.onErrorInFetching();
							}
						}

					});
				}
			}
		}.start();
	}

	public LinkedHashMap<String, ContactInfo> GetMyPhoneContacts(
			String countryCode) {
		long i = 1;
		LinkedHashMap<String, ContactInfo> contactMap = new LinkedHashMap<String, ContactInfo>();
		String[] mProjection = new String[] { Phone.DISPLAY_NAME, Phone.NUMBER };
		String strName = null;

		String strPhoneNumber = null;
		Cursor curPhoneContacts = mContext.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				mProjection, null, null, Phone.DISPLAY_NAME + " ASC");
		curPhoneContacts.moveToFirst();
		do {
			strName = curPhoneContacts.getString(curPhoneContacts
					.getColumnIndex(Contacts.DISPLAY_NAME));
			strPhoneNumber = curPhoneContacts
					.getString(curPhoneContacts
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			// contactMap.put(strPhoneNumber,
			// ContactInfo.buildContact(strName, i++));
			contactMap.put(Utils.getFormattedNo(countryCode, strPhoneNumber),
					ContactInfo.buildContact(strName, null, false));
			System.out.println(strName);
		} while (curPhoneContacts.moveToNext());
		curPhoneContacts.close();
		return contactMap;
	}

}
