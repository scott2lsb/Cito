package com.app.tomore.beans;

import java.io.Serializable;
import java.util.ArrayList;


public class ThreadUpdateModel  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<ThreadCmtModel> commentList;
	private ArrayList<ThreadUpdateLikeModel> likeList;
	private ArrayList<UpdateFollowedModel> listModel;
	private int newCommentNum;
	
	public ArrayList<UpdateFollowedModel> getListModel() {
		return listModel;
	}
	public void setListModel(ArrayList<UpdateFollowedModel> listModel) {
		this.listModel = listModel;
	}
	
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
	public int getNewCommentNum() {
		return newCommentNum;
	}
	public void setNewCommentNum(int newCommentNum) {
		this.newCommentNum = newCommentNum;
	}


}
