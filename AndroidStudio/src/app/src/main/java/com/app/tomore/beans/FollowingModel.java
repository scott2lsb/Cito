package com.app.tomore.beans;

import java.io.Serializable;

import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

public class FollowingModel extends BaseBean implements Serializable, Comparable<FollowingModel>{

	private String MemberID;
	private String AccountName;
	private String MemberImage;
	private String followed;
	
	@Override
	public int compareTo(FollowingModel another) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object parseJSON(JSONObject jsonObj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object cursorToBean(Cursor cursor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentValues beanToValues() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getMemberID() {
		return MemberID;
	}

	public void setMemberID(String memberID) {
		MemberID = memberID;
	}

	public String getAccountName() {
		return AccountName;
	}

	public void setAccountName(String accountName) {
		AccountName = accountName;
	}

	public String getMemberImage() {
		return MemberImage;
	}

	public void setMemberImage(String memberImage) {
		MemberImage = memberImage;
	}

	public String getFollowed() {
		return followed;
	}

	public void setFollowed(String followed) {
		this.followed = followed;
	}

}
