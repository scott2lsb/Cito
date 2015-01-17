package com.app.tomore.beans;

import java.io.Serializable;

public class ThreadLikeModel implements Serializable{
	public String getAccountName() {
		return AccountName;
	}
	public void setAccountName(String accountName) {
		AccountName = accountName;
	}
	public String getImage() {
		return Image;
	}
	public void setImage(String image) {
		Image = image;
	}
	public String getMemberID() {
		return MemberID;
	}
	public void setMemberID(String memberID) {
		MemberID = memberID;
	}
	private String Image;
	private String MemberID;
	private String AccountName;

}
