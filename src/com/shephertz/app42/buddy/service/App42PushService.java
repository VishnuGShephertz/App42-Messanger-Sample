/**
 * 
 */
package com.shephertz.app42.buddy.service;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.shephertz.app42.buddy.app.BuddyEventList;
import com.shephertz.app42.buddy.app.R;
import com.shephertz.app42.buddy.tools.DatabaseService;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.App42Log;

/**
 * @author Ajay Tiwari
 * 
 */
public class App42PushService extends GCMBaseIntentService {
	public static String PROJECT_NUMBER = "";
	static final String LARGE_IMAGE_URL = "<YOUR IMAGE URL>";
	static int msgCount = 0;

	/**
	 * Intent used to display a message in the screen.
	 */
	static final String DISPLAY_MESSAGE_ACTION = "com.example.app42sample.DISPLAY_MESSAGE";

	/**
	 * Intent's extra that contains the message to be displayed.
	 */
	static final String EXTRA_MESSAGE = "message";

	public App42PushService() {
		super(PROJECT_NUMBER);
	}

	@Override
	protected void onError(Context arg0, String msg) {
		Log.i(TAG, "onError " + msg);

	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "Received message "
				+ intent.getExtras().getString("message"));
		String message = intent.getExtras().getString("message");
	
		displayMessage(context, message);
		generateNotification(context, message);
		try {
			DatabaseService.instance(context).performOperation(new JSONObject(message));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static void setSenderId(String senderId) {
		PROJECT_NUMBER = senderId;
	}

	@Override
	protected void onRegistered(Context arg0, String regId) {
		Log.i(TAG, "Device registered: regId = " + regId);
		registerWithApp42(regId);

	}

	private void registerWithApp42(String regId) {
		App42Log.debug(" Registering on Server ....");
		App42API.buildPushNotificationService().storeDeviceToken(
				App42API.getLoggedInUser(), regId, new App42CallBack() {
					@Override
					public void onSuccess(Object paramObject) {
						// TODO Auto-generated method stub
						App42Log.debug(" ..... Registeration Success ....");
						GCMRegistrar.setRegisteredOnServer(App42API.appContext,
								true);
					}

					@Override
					public void onException(Exception paramException) {
						App42Log.debug(" ..... Registeration Failed ....");
						App42Log.debug("storeDeviceToken :  Exception : on start up "
								+ paramException);

					}
				});

	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {

		Log.i(TAG, "Device unRegistered: regId = " + arg1);

	}

	/**
	 * Notifies UI to display a message.
	 * <p>
	 * This method is defined in the common helper because it's used both by the
	 * UI and the background service.
	 * 
	 * @param context
	 *            application's context.
	 * @param message
	 *            message to be displayed.
	 */
	public void displayMessage(Context context, String message) {
		Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
		intent.putExtra(EXTRA_MESSAGE, message);
		context.sendBroadcast(intent);
	}

	public static void resetMsgCount() {
		msgCount = 0;
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	public void generateNotification(Context context, String message) {
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
	
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		String title = context.getString(R.string.app_name);
		Intent notificationIntent;
		//try {
		//	notificationIntent = new Intent(context,
		//			BuddYE);
		//	notificationIntent.putExtra("message_delivered", true);
		//	notificationIntent.putExtra(EXTRA_MESSAGE, message);
		//} catch (Exception e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
			

	//	}
		// set intent so it does not start a new activity
		notificationIntent = new Intent(context, BuddyEventList.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		Notification notification = new NotificationCompat.Builder(context)
				.setContentTitle(title).setContentText(message)
				.setContentIntent(intent).setSmallIcon(icon).setWhen(when)
				.setNumber(++msgCount)
				.setLargeIcon(getBitmapFromURL(LARGE_IMAGE_URL))
				.setLights(Color.YELLOW, 1, 2).setAutoCancel(true).build();

		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notificationManager.notify(0, notification);
	}

	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
