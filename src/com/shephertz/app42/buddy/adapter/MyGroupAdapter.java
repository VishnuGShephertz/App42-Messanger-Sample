package com.shephertz.app42.buddy.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shephertz.app42.buddy.app.GroupList;
import com.shephertz.app42.buddy.app.R;
import com.shephertz.app42.buddy.tools.GroupInfo;
import com.shephertz.app42.buddy.tools.ImageLoader;

public class MyGroupAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<GroupInfo> groups;
	private static LayoutInflater inflater = null;
	private ImageLoader imageLoader;

	public MyGroupAdapter(Context context, ArrayList<GroupInfo> items) {
	this.mContext=context;
		this.groups = items;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader=new ImageLoader(context, 70);
		
	}

	public int getCount() {
		return groups.size();
	}

	public Object getItem(int position) {
		return groups.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	private static class ViewHolder {
		protected TextView text1;
		protected Button btnEdit;
		protected ImageView grpPic;
		
		// public ImageView image;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.grp_list_item, null);
			holder = new ViewHolder();
			holder.text1 = (TextView) vi.findViewById(R.id.group_name);
			holder.grpPic=(ImageView)vi.findViewById(R.id.grp_img);
			holder.btnEdit=(Button)vi.findViewById(R.id.btn_edit);
			vi.setTag(holder);

		} else

		holder = (ViewHolder) vi.getTag();
		holder.text1.setText("  " + groups.get(position).getDisplayName());
		imageLoader.DisplayImage( groups.get(position).getPicUrl(), holder.grpPic);
     holder.btnEdit.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View arg0) {
 				// TODO Auto-generated method stub
 				((GroupList)mContext).onEditGroupClicked(position);
 			}
 		});
		return vi;
	}
}