package com.shephertz.app42.buddy.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shephertz.app42.buddy.app.R;
import com.shephertz.app42.buddy.tools.ImageLoader;
import com.shephertz.app42.buddy.tools.UserData;

public class BuddyAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<UserData> userList;
	private static LayoutInflater inflater = null;
	private ImageLoader imageLoader;

	public BuddyAdapter(Context context, ArrayList<UserData> items) {
	this.mContext=context;
		this.userList = items;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader=new ImageLoader(context, 70);
		
	}

	public int getCount() {
		return userList.size();
	}

	public Object getItem(int position) {
		return userList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	private static class ViewHolder {
		protected TextView name;
		protected TextView phNo;
		protected ImageView icon;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
		UserData userInf0=userList.get(position) ;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.contact_item, null);
			holder = new ViewHolder();
			holder.name = (TextView) vi.findViewById(R.id.name);
			holder.phNo = (TextView) vi.findViewById(R.id.message);
			holder.icon = (ImageView) vi.findViewById(R.id.user_pic);
			vi.setTag(holder);
		} else
			holder = (ViewHolder) vi.getTag();
		holder.name.setText(userInf0.getDisplayName());
		holder.phNo.setText(userInf0.getUserName());
		//holder.icon.setImageResource(R.drawable.default_pic);
		imageLoader.DisplayImage(userInf0.getPicUrl(), 	holder.icon);
		
		return vi;
	}
}