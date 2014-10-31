package com.shephertz.app42.buddy.listener;

import android.graphics.Bitmap;

import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.avatar.Avatar;

public interface AvatarEventListener {

	public void onAvatarCreated(Avatar avatar);
	public void onAvatarCreationFailed(App42Exception ex);
	public void onLoadImage(Bitmap bitmap);
}
