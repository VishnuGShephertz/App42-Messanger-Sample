package com.shephertz.app42.buddy.adapter;

import java.io.InputStream;
import java.util.HashMap;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shephertz.app42.buddy.app.GroupList;
import com.shephertz.app42.buddy.app.R;
import com.shephertz.app42.buddy.app.UserList;
import com.shephertz.app42.buddy.tools.ContactInfo;

public class ContactAdapter extends BaseAdapter {

	private Context mcontext;
	private String[] keys;
	private HashMap<String, ContactInfo> contactMap;
	private static LayoutInflater inflater = null;
	private ContentResolver cr;

	public ContactAdapter(Context context,
			HashMap<String, ContactInfo> contactMap) {
		mcontext = context;
		this.keys = contactMap.keySet().toArray(new String[contactMap.size()]);
		this.contactMap = contactMap;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		cr = mcontext.getContentResolver();
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
		protected TextView phNo;
		protected ImageView icon;
		protected ImageView iconAction;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
		final ContactInfo contact = contactMap.get(keys[position]);
		if (convertView == null) {
			vi = inflater.inflate(R.layout.contact_item, null);
			holder = new ViewHolder();
			holder.name = (TextView) vi.findViewById(R.id.name);
			holder.phNo = (TextView) vi.findViewById(R.id.message);
			holder.icon = (ImageView) vi.findViewById(R.id.user_pic);
			holder.iconAction = (ImageView) vi.findViewById(R.id.add_share);
			vi.setTag(holder);
		} else
			holder = (ViewHolder) vi.getTag();
		if(contact.isInstalled()){
			holder.iconAction.setImageResource(android.R.drawable.ic_input_add);
		}
		else{
			holder.iconAction.setImageResource(android.R.drawable.ic_menu_share);
		}
		holder.name.setText(contact.getName());
		holder.phNo.setText(keys[position]);
		holder.icon.setImageResource(R.drawable.default_pic);
		
		  holder.iconAction.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					((UserList)mcontext).performAction(contact,keys[position]);
				}
			});
		return vi;
	}

	public Bitmap getContactPhoto(long id) {
		Uri photoUri = ContentUris.withAppendedId(
				ContactsContract.Contacts.CONTENT_URI, id);
		if (photoUri != null) {
			InputStream input = ContactsContract.Contacts
					.openContactPhotoInputStream(cr, photoUri);
			if (input != null) {
				return BitmapFactory.decodeStream(input);
			}
		} else {
			// Log.d(getClass().getSimpleName(), "No photo Uri");
		}
		return null;
	}

}