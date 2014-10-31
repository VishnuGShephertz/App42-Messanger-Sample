package com.shephertz.app42.buddy.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import com.shephertz.app42.buddy.command.EventCode;
import com.shephertz.app42.buddy.listener.AvatarEventListener;

public class Utils {

	/*
	 * This function allows user to load image from Url in a background Thraed
	 * 
	 * @param image ImageView on which image is loaded
	 * 
	 * @param url of image
	 */
	public static void loadImageFromUrl(final String url,
			final AvatarEventListener callback) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				final Bitmap bitmap = loadBitmap(url);
				callerThreadHandler.post(new Runnable() {
					@Override
					public void run() {
						if (callback != null)
							callback.onLoadImage(bitmap);
					}
				});
			}
		}.start();
	}

	/*
	 * This function allows user to load bitmap from url
	 * 
	 * @param url of image
	 * 
	 * @return Bitmap
	 */
	public static Bitmap loadBitmap(String url) {
		Bitmap bitmap = null;
		try {
			InputStream in = new java.net.URL(url).openStream();
			bitmap = BitmapFactory.decodeStream(in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public static String urlSpaceEncode(String sUrl) {
		StringBuffer urlOK = new StringBuffer();
		for (int i = 0; i < sUrl.length(); i++) {
			char ch = sUrl.charAt(i);
			switch (ch) {
			case ' ':
				urlOK.append("%20");
				break;
			default:
				urlOK.append(ch);
				break;
			}
		}
		return urlOK.toString();
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		// final int buffer_size = ;
		try {
			byte[] bytes = new byte[1024 * 8];
			for (;;) {
				int count = is.read(bytes, 0, 1024 * 8);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public static String getFormattedNo(String countryCode,String pnNo) {
		if(pnNo.contains("-"))
			pnNo=	pnNo.replaceAll("-", "");
		
		if (pnNo.startsWith(countryCode)) {
			return pnNo;
		} else if (pnNo.startsWith("0")) {
			return countryCode + pnNo.substring(1);
		} else {
			return countryCode + pnNo;
		}
	}

	public boolean isEmailValid(String email) {
		return Constants.EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}

	public boolean isPhoneNoValid(String phoneNo) {
		return isValid(phoneNo, Constants.PHONE_PATTERN);
	}

	private boolean isValid(String text, String pattern) {
		return Pattern.matches(pattern, text);
	}
	public static String getDateTime() {
		SimpleDateFormat dd = new SimpleDateFormat("yyyy-mm-dd hh-mm-ss");
		return dd.format(new Date());
	}
	public static String getUserTableName(String userName){
		return "user"+"_"+userName;
	}
	public static String getGrouptableName(String groupName){
		return "group"+"_"+groupName;
	}
	public static String getTableName(int typeCode,String name){
	if(typeCode==EventCode.BUDDY_MESSAGE.getEventCode()){
		return  getUserTableName(name);
	}
	else if(typeCode==EventCode.GROUP_MESSAGE.getEventCode()){
		return  getGrouptableName(name);
	}
	return getUserTableName(name);
	}
}
