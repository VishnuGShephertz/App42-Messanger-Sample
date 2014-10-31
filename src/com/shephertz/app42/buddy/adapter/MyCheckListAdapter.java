package com.shephertz.app42.buddy.adapter;


import java.util.ArrayList;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.shephertz.app42.buddy.app.GroupActivity;
import com.shephertz.app42.buddy.app.R;

public class MyCheckListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> items;
	private static LayoutInflater inflater = null;

	public MyCheckListAdapter(Context context,ArrayList<String>items) {
		mContext = context;
		this.items = items;
		inflater = (LayoutInflater) mContext
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
		private CheckBox check;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
		if (convertView == null) {
			vi = inflater.inflate(
					R.layout.item_check, null);
			holder = new ViewHolder();
			holder.text1 = (TextView) vi.findViewById(R.id.name);
			holder.text1.setTextColor(Color.BLACK);
			holder.check=(CheckBox)vi.findViewById(R.id.checkbox);
			vi.setTag(holder);

		} else
			holder = (ViewHolder) vi.getTag();
		    holder.text1.setText("  "+items.get(position).trim());
		    holder.check.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		        final boolean isChecked = ((CheckBox)arg0).isChecked();
		        ((GroupActivity)mContext).setSelected(isChecked, position);
		    }
		});

		return vi;
	}
}