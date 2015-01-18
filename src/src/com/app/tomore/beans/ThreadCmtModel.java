package com.app.tomore.beans;

import java.io.Serializable;

public class ThreadCmtModel implements Serializable {

	public String getCommentPostDate() {
		if(CommentPostDate != null)
		{
			return CommentPostDate;
		}
		else
		{
			return ThreadPostDate;
		}
	}
	public void setCommentPostDate(String commentPostDate) {
		CommentPostDate = commentPostDate;
	}
	public String getCommentContent() {

		if(CommentContent != null)
		{
			return CommentContent;
		}
		else
		{
			return ThreadContent;
		}
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
	public String getThreadContent() {
		return ThreadContent;
	}
	public void setThreadContent(String threadContent) {
		ThreadContent = threadContent;
	}
	public String getThreadPostDate() {
		return ThreadPostDate;
	}
	public void setThreadPostDate(String threadPostDate) {
		ThreadPostDate = threadPostDate;
	}
	public String getTimeDiff() {
		return TimeDiff;
	}
	public void setTimeDiff(String timeDiff) {
		TimeDiff = timeDiff;
	}
	private String ThreadContent;
	private String ThreadPostDate;
	private String TimeDiff;
    	
	public String getThreadID() {
		return ThreadID;
	}
	public void setThreadID(String threadID) {
		ThreadID = threadID;
	}
	private String ThreadID;
}
