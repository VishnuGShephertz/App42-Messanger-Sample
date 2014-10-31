package com.shephertz.app42.buddy.adapter;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shephertz.app42.buddy.app.AppContext;
import com.shephertz.app42.buddy.app.R;
import com.shephertz.app42.buddy.tools.MessageInfo;

/**
 * AwesomeAdapter is a Custom class to implement custom row in ListView
 * 
 * @author Vishnu Garg
 * 
 */

public class MessageAdapter extends BaseAdapter {
	private ArrayList<MessageInfo> mMessages;

	private static LayoutInflater inflater = null;
	private Calendar calendra;
	private final int ALIGN_LEFT = RelativeLayout.ALIGN_PARENT_LEFT;
	private final int ALIGN_RIGHT = RelativeLayout.ALIGN_PARENT_RIGHT;
	private final int BUBBBLE_GREEN = R.drawable.speech_bubble_green;
	private final int BUBBBLE_ORANGE = R.drawable.speech_bubble_orange;

	public MessageAdapter(Context context, ArrayList<MessageInfo> messages) {
		super();
		this.mMessages = messages;
	
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		calendra = Calendar.getInstance();
	}

	@Override
	public int getCount() {
		return mMessages.size();
	}

	@Override
	public Object getItem(int position) {
		return mMessages.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.sms_row, null);
			holder = new ViewHolder();

			holder.message = (TextView) convertView
					.findViewById(R.id.message_text);
			holder.user = (TextView) convertView.findViewById(R.id.username);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.messageBox = (LinearLayout) convertView
					.findViewById(R.id.message_bubble);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		String message = mMessages.get(position).getMessage();
		boolean isImage = false;
		if (message.startsWith("http") || message.contains(".jpg")
				|| message.contains(".png")) {
			isImage = true;
		}

		holder.time.setText(mMessages.get(position).getDateTime());
		String owner = mMessages.get(position).getSender();
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		if (owner.equals(AppContext.myUserName)) {
			buildMessageView("",isImage,message,BUBBBLE_ORANGE,holder);
			params.addRule(ALIGN_RIGHT);
		} else {
			
			buildMessageView(owner,isImage,message,BUBBBLE_GREEN,holder);
			params.addRule(ALIGN_LEFT);
		}
		holder.messageBox.setLayoutParams(params);
		return convertView;
	}

	private void buildMessageView(String owner,boolean isImage,String message,int background,ViewHolder holder) {
		if (isImage) {
		//	Utils.loadBackground(holder.messageBox, message);
		} else {
			holder.user.setText(owner);
			holder.message.setText(message);
			holder.messageBox
					.setBackgroundResource(background);

		}
	}

	
	private static class ViewHolder {
		protected TextView message;
		protected TextView user;
		protected TextView time;
		protected LinearLayout messageBox;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}
