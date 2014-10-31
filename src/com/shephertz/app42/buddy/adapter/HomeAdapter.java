
package com.shephertz.app42.buddy.adapter;

import java.util.HashMap;

import android.content.ContentResolver;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.shephertz.app42.buddy.app.R;
import com.shephertz.app42.buddy.tools.ImageLoader;
import com.shephertz.app42.buddy.tools.UserInfo;

public class HomeAdapter extends BaseAdapter {

	private Context mcontext;
	private String[] keys;
	private HashMap<String, UserInfo> contactMap;
	private static LayoutInflater inflater = null;
	private ImageLoader imageLoader;

	public HomeAdapter(Context context,
			HashMap<String, UserInfo> contactMap) {
		mcontext = context;
		this.keys = contactMap.keySet().toArray(new String[contactMap.size()]);
		this.contactMap = contactMap;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader=new ImageLoader(context, 70);
	}

	public int getCount() {
		return keys.length;
	}

	public Object getItem(int position) {
		return keys[position];
	}

	public long getItemId(int position) {
		return position;
	}

	private static class ViewHolder {
		protected TextView name;
		protected TextView lastMessage;
		protected TextView msgCount;
		protected ImageView icon;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
		UserInfo user = contactMap.get(keys[position]);
		if (convertView == null) {
			vi = inflater.inflate(R.layout.home_item, null);
			holder = new ViewHolder();
			holder.name = (TextView) vi.findViewById(R.id.name);
			holder.lastMessage = (TextView) vi.findViewById(R.id.message);
			holder.msgCount = (TextView) vi.findViewById(R.id.new_messages_count);
			holder.icon = (ImageView) vi.findViewById(R.id.user_pic);
			vi.setTag(holder);

		} else
			holder = (ViewHolder) vi.getTag();
		holder.name.setText(user.getDisplayName());
		holder.lastMessage.setText(user.getLastMessage());
		imageLoader.DisplayImage(user.getPicUrl(), holder.icon);
	     if(user.getCount()>0){
	    	 holder.msgCount.setText(user.getCount()+" new msg");
	     }
	     else{
	    	 holder.msgCount.setText("");
	     }
		
		return vi;
	}



}