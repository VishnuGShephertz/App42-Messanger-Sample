package com.shephertz.app42.buddy.service;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.google.android.gcm.GCMRegistrar;
import com.shephertz.app42.buddy.events.NotifyCallbackEvent;
import com.shephertz.app42.buddy.listener.AvatarEventListener;
import com.shephertz.app42.buddy.listener.BaseListener;
import com.shephertz.app42.buddy.listener.PushEventListener;
import com.shephertz.app42.buddy.listener.UserEventListener;
import com.shephertz.app42.buddy.util.Constants;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.App42Log;
import com.shephertz.app42.paas.sdk.android.App42Response;
import com.shephertz.app42.paas.sdk.android.ServiceAPI;
import com.shephertz.app42.paas.sdk.android.avatar.Avatar;
import com.shephertz.app42.paas.sdk.android.avatar.AvatarService;
import com.shephertz.app42.paas.sdk.android.buddy.Buddy;
import com.shephertz.app42.paas.sdk.android.buddy.BuddyService;
import com.shephertz.app42.paas.sdk.android.push.PushNotificationService;
import com.shephertz.app42.paas.sdk.android.user.User;
import com.shephertz.app42.paas.sdk.android.user.UserService;
public class App42ServiceApi implements BaseListener{
	private UserService userService;
	private static App42ServiceApi mInstance = null;
	private BuddyService buddyService;
	private AvatarService avatarService;
	private  PushNotificationService pushService;

	private App42ServiceApi(Context context) {
		ServiceAPI sp = new ServiceAPI(Constants.App42ApiKey,
				Constants.App42ApiSecret);
		App42API.initialize(context, Constants.App42ApiKey, Constants.App42ApiSecret);
		this.userService = App42API.buildUserService();
		this.buddyService = new BuddyService(Constants.App42ApiKey,
				Constants.App42ApiSecret, "");
		this.avatarService = App42API.buildAvatarService();
		this.pushService=App42API.buildPushNotificationService();

	}

	/*
	 * instance of class
	 */
	public static App42ServiceApi instance(Context context) {

		if (mInstance == null) {
			mInstance = new App42ServiceApi(context);
		}

		return mInstance;
	}
	public  void registerWithApp42(String projectNo) {
		App42Log.debug(" ..... Registeration Check ....");
		App42PushService.setSenderId(projectNo);
			final String deviceRegId = GCMRegistrar.getRegistrationId(App42API.appContext);
			if (deviceRegId.equals("")) {
				// Automatically registers application on startup.
				GCMRegistrar.register(App42API.appContext, projectNo);	
			} else if(!GCMRegistrar.isRegisteredOnServer(App42API.appContext)) {
					App42Log.debug(" Registering on Server ....");
					pushService.storeDeviceToken(App42API.getLoggedInUser(), deviceRegId, new App42CallBack() {	
							@Override
							public void onSuccess(Object paramObject) {
								// TODO Auto-generated method stub
								App42Log.debug(" ..... Registeration Success ....");
								GCMRegistrar.setRegisteredOnServer(App42API.appContext, true);
							}
							
							@Override
							public void onException(Exception paramException) {
								App42Log.debug(" ..... Registeration Failed ....");
								App42Log.debug("storeDeviceToken :  Exception : on start up " +paramException);
								
							}
						});
					

				}
			}
	/*
	 * This function validate user's authentication with APP42
	 */
	public void createAvatar(final String userName, final String avatarName,
			final String imagePath, final String description,
			final AvatarEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					final Avatar avatar = avatarService.createAvatar(
							avatarName, userName, imagePath, description);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onAvatarCreated(avatar);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								System.out.println(ex.toString());
								callBack.onAvatarCreationFailed(ex);
							}
						}
					});
				}
			}
		}.start();
	}
	/*
	 * This function validate user's authentication with APP42
	 */
	public void authenticateUser(final String name, final String pswd,
			final UserEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					final App42Response response = userService.authenticate(
							name, pswd);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onUserAuthenticated(response);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								System.out.println(ex.toString());
								callBack.onUserAuthenticated(null);
							}
						}
					});
				}
			}
		}.start();
	}

	/*
	 * This function allows to create user using APP42 service
	 */
	public void createUser(final String name, final String pswd,
			final String email, final UserEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					final User user = userService.createUser(name, pswd, email);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onUserCreated(user);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								System.out.println(ex.toString());
								callBack.onUserCreated(null);
							}
						}
					});

				}
			}
		}.start();
	}

	public void sendMessageToUser(final String username,
			final JSONObject jsonMsg,final int eventCode,
			final PushEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					pushService.sendPushMessageToUser(username, jsonMsg.toString());
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onPushSent(new  NotifyCallbackEvent(username, "",eventCode));
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								callBack.onPushFailed(ex);
							}
						}
					});
				}
			}
		}.start();
	}
	public void sendMessageToGroup(final String groupName,
			final JSONObject jsonMsg,final int eventCode,
			final PushEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					pushService.sendPushMessageToChannel(groupName, jsonMsg.toString());
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onPushSent(new  NotifyCallbackEvent(groupName, "",eventCode));
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								callBack.onPushFailed(ex);
							}
						}
					});
				}
			}
		}.start();
	}
	
	public void createGroup(final String owner,
			final String groupName,
			final PushEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					pushService.createChannelForApp(groupName, "My Group");
					pushService.subscribeToChannel(groupName,owner);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onGroupCreated(groupName, owner);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								callBack.onPushFailed(ex);
							}
						}
					});
				}
			}
		}.start();
	}
		

	/*
	 * This function validate user's authentication with APP42
	 */
	public void loaduserList(final UserEventListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					ArrayList<User> userList = userService.getAllUsers();
					final ArrayList<String> users = getUserList(userList);
					
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onGetAllUsers(users);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								System.out.println(ex.toString());
								callBack.onError(ex);
							}
						}
					});
				}
			}
		}.start();
	}
	private ArrayList<String> getUserList(ArrayList<User> userList) {
		ArrayList<String> users = new ArrayList<String>();
		for (int i = 0; i < userList.size(); i++) {
			users.add(userList.get(i).getUserName().toString());
			
		}
		return users;
	}

	

	private ArrayList<String> getRequestList(ArrayList<Buddy> friendRequest) {
		// TODO Auto-generated method stub
		ArrayList<String> users = new ArrayList<String>();
		int size = friendRequest.size();
		for (int i = 0; i < size; i++) {
			users.add(friendRequest.get(i).getBuddyName());
		}
		return users;
	}


	
	

}
