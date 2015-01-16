package com.app.tomore.beans;

import java.io.Serializable;
import java.util.ArrayList;

import com.app.tomore.beans.ThreadLikeModel;
import com.app.tomore.beans.ThreadCmtModel;

public class ThreadUpdateModel  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<ThreadCmtModel> commentList;
	private ArrayList<ThreadUpdateLikeModel> likeList;
	
	public ArrayList<ThreadCmtModel> getCommentList() {
		return commentList;
	}
	public void setCommentList(ArrayList<ThreadCmtModel> commentList) {
		this.commentList = commentList;
	}
	public ArrayList<ThreadUpdateLikeModel> getLikeList() {
		return likeList;
	}
	public void setLikeList(ArrayList<ThreadUpdateLikeModel> likeList) {
		this.likeList = likeList;
	}

}
