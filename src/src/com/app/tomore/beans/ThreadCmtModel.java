package com.app.tomore.beans;

import java.io.Serializable;

public class ThreadCmtModel implements Serializable {

	public String getCommentPostDate() {
		return CommentPostDate;
	}
	public void setCommentPostDate(String commentPostDate) {
		CommentPostDate = commentPostDate;
	}
	public String getCommentContent() {
		return CommentContent;
	}
	public void setCommentContent(String commentContent) {
		CommentContent = commentContent;
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
	private String CommentPostDate;
	private String CommentContent;
	private String MemberID;
	private String AccountName;
	private String MemberImage;
	public String getThreadID() {
		return ThreadID;
	}
	public void setThreadID(String threadID) {
		ThreadID = threadID;
	}
	private String ThreadID;
}
