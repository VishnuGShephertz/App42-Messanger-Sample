package com.shephertz.app42.buddy.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shephertz.app42.buddy.app.R;

public class MyListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> items;
	private static LayoutInflater inflater = null;

	public MyListAdapter(Context context,ArrayList<String>items) {
		mContext = context;
		this.items = items;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return items.size();
	}

	public Object getItem(int position) {
		return items.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {
		public TextView text1;

		// public ImageView image;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
		if (convertView == null) {
			vi = inflater.inflate(
					R.layout.item, null);
			holder = new ViewHolder();
			holder.text1 = (TextView) vi.findViewById(R.id.name);
			vi.setTag(holder);

		} else

			holder = (ViewHolder) vi.getTag();
		holder.text1.setText("  "+items.get(position).trim());

		return vi;
	}
}