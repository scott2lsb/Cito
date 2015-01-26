package com.app.tomore.beans;

import java.io.Serializable;

import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

public class FollowOrUnfollowModel extends BaseBean implements Serializable, Comparable<FollowOrUnfollowModel> {

	private String result;
	private String exist;
	
	@Override
	public int compareTo(FollowOrUnfollowModel another) {
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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getExist() {
		return exist;
	}

	public void setExist(String exist) {
		this.exist = exist;
	}

}
