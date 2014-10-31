package com.shephertz.app42.buddy.tools;

import android.os.Parcel;
import android.os.Parcelable;

public class GroupInfo implements Parcelable{

	private String groupname;
	private String owner;
	private String displayName;
	private String picUrl;
	public GroupInfo(String groupname, String owner, String displayName,
			String picUrl) {
		this.groupname = groupname;
		this.owner = owner;
		this.displayName = displayName;
		this.picUrl = picUrl;
	}
	public String getGroupname() {
		return groupname;
	}
	public String getOwner() {
		return owner;
	}
	public String getDisplayName() {
		return displayName;
	}
	public String getPicUrl() {
		return picUrl;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		// TODO Auto-generated method stub

		dest.writeString(groupname);
		dest.writeString(owner);
		dest.writeString(displayName);
		dest.writeString(picUrl);

	}
	
	 public GroupInfo(Parcel source) {
	       
		 groupname = source.readString();
		 owner = source.readString();
	        displayName = source.readString();
	        picUrl = source.readString();
	    }
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public GroupInfo createFromParcel(Parcel in) {
			return new GroupInfo(in);
		}

		public GroupInfo[] newArray(int size) {
			return new GroupInfo[size];
		}
	};
}
