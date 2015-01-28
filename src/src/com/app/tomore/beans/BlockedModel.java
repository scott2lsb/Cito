package com.app.tomore.beans;

import java.io.Serializable;

import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

public class BlockedModel  implements Serializable {

	private String MemberID;
	private String AccountName;
	private String MemberImage;
	
	
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
}
