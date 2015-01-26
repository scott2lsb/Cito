package com.app.tomore.beans;

import java.io.Serializable;

public class ThreadUpdateLikeModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String AccountName;
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
	public String getPostTime() {
		return PostTime;
	}
	public void setPostTime(String postTime) {
		PostTime = postTime;
	}
	public String getThreadID() {
		return ThreadID;
	}
	public void setThreadID(String threadID) {
		ThreadID = threadID;
	}
	private String Image;
	private String MemberID;
	private String PostTime;
	private String ThreadID;
}
