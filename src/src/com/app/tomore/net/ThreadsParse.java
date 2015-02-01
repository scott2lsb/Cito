package com.app.tomore.net;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.app.tomore.beans.CommonModel;
import com.app.tomore.beans.EventsModel;
import com.app.tomore.beans.MemberModel;
import com.app.tomore.beans.ThreadCmtModel;
import com.app.tomore.beans.ThreadImageModel;
import com.app.tomore.beans.ThreadLikeModel;
import com.app.tomore.beans.ThreadModel;
import com.app.tomore.beans.EventMemberModel;

public class ThreadsParse {
	public ArrayList<ThreadModel> parseThreadModel(String jsonThreads) 
			throws JsonSyntaxException 
	{
		
//		 "data": [
//		          {
//		              "ThreadID": "733",
//		              "ThreadTitle": "",
//		              "ThreadPostDate": "2015-01-06 19:59:54",
//		              "ThreadUpdateDate": null,
//		              "ThreadContent": "5Ԫ��Omar Deserre �Ե��İ����ͼ",
//		              "ThreadType": "1",
//		              "MemberID": "135",
//		              "LastCommentDate": null,
//		              "ThreadImages": [
//		                  {
//		                      "ImageID": "308",
//		                      "ImageUrl": "http://54.213.167.5/image/thread/20150106-195954-342image.jpg",
//		                      "ImageWidth": "480",
//		                      "ImageHeight": "640"
//		                  }
//		              ],
//		              "LikeList": [],
//		              "Comments": [],
//		              "AccountName": "Xxhottie88xx",
//		              "MemberImage": "http://54.213.167.5/image/member/DefaultMemberImage/default_userProfileF.jpg",
//		              "TimeDiff": "1��ǰ"
//		          }
		
		
		ArrayList<ThreadModel> retList = new ArrayList<ThreadModel>();
		Gson gson = new Gson();
		JsonElement jelement = new JsonParser().parse(jsonThreads);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    JsonArray jarray = jobject.getAsJsonArray("data");
	    for (JsonElement obj : jarray)
	    {
	    	ThreadModel aThread = gson.fromJson(obj, ThreadModel.class);
	    	
	    	JsonObject  jobject2 = obj.getAsJsonObject();
	    	JsonArray threadImageArray = jobject2.getAsJsonArray("ThreadImages");
	    	JsonArray threadLikeArray = jobject2.getAsJsonArray("LikeList");
	    	JsonArray threadCommentArray = jobject2.getAsJsonArray("Comments");
	    	if(threadImageArray != null)
	    	{
	    		ArrayList<ThreadImageModel> imageModelList = new ArrayList<ThreadImageModel>();
	    		for (JsonElement objThreadImage : threadImageArray)
				{
	    			ThreadImageModel imageModel = gson.fromJson(objThreadImage, ThreadImageModel.class);
	    			imageModelList.add(imageModel);
				}
	    		aThread.setThreadImageList(imageModelList);
	    	}
	    	
	    	if(threadLikeArray != null)
	    	{
	    		ArrayList<ThreadLikeModel> likeModelList = new ArrayList<ThreadLikeModel>();
	    		for (JsonElement objLike : threadLikeArray)
	    		{
	    			ThreadLikeModel likeModel = gson.fromJson(objLike, ThreadLikeModel.class);
	    			likeModelList.add(likeModel);
	    		}

	    		aThread.setThreadLikeList(likeModelList);
	    	}
	    	
	    	if(threadCommentArray != null)
	    	{
	    		ArrayList<ThreadCmtModel> listComment = new ArrayList<ThreadCmtModel>();
	    		for (JsonElement objComment : threadCommentArray)
	    		{
	    			ThreadCmtModel commentModel = gson.fromJson(objComment, ThreadCmtModel.class);
	    			listComment.add(commentModel);
	    		}
	    		
	    		aThread.setThreadCmtList(listComment);
	    	}

			retList.add(aThread);
	    }
	    
		return retList;
	}
	
	

	//get recommend list
	// http://54.213.167.5/recommend.php?memberID=2
	public ArrayList<MemberModel> getRecommenddListParse(String result) 
			throws JsonSyntaxException {
		Gson gson = new Gson();
		JsonElement jelement = new JsonParser().parse(result);
		JsonObject jobject = jelement.getAsJsonObject();
		JsonArray jarray = jobject.getAsJsonArray("data");
		ArrayList<MemberModel> lcs = new ArrayList<MemberModel>();
		for (JsonElement obj : jarray) {
			MemberModel cse = gson.fromJson(obj, MemberModel.class);
			lcs.add(cse);
		}
		return lcs;
	}
	
	/*
	 * like or unlike a thread
	 */
	public CommonModel likeOrUnLikeAThreadParse(String result)
			throws JsonSyntaxException 
	{
		return new ToMoreParse().CommonPares(result);
	}
	
	/*
	 * post comment to a thread
	 */
	//http://54.213.167.5/getCommentsByThreadID.php?&limit=20&page=1&threadID=87
	public ArrayList<ThreadCmtModel> getCommentsByThreadIDParse(String result)
			throws JsonSyntaxException {
		Gson gson = new Gson();
		JsonElement jelement = new JsonParser().parse(result);
		JsonObject jobject = jelement.getAsJsonObject();
		JsonArray jarray = jobject.getAsJsonArray("data");
		ArrayList<ThreadCmtModel> lcs = new ArrayList<ThreadCmtModel>();
		for (JsonElement obj : jarray) {
			ThreadCmtModel cse = gson.fromJson(obj, ThreadCmtModel.class);
			lcs.add(cse);
		}
		return lcs;
	}
	/*
	 * get thread details
	 */

	//get threads by member
	//http://54.213.167.5/getThreadListByMemberID.php?memberID=25&limit=20&page=1
	public ArrayList<ThreadModel> getThreadListByMemberIDParse(String result) 
			throws JsonSyntaxException
	{
		return new ThreadsParse().parseThreadModel(result);
	}
	public  ArrayList<ThreadImageModel> parseThreadImageModel(String jsonRestaurantDetail) 
			throws JsonSyntaxException 
	{
		Gson gson = new Gson();
		JsonElement jelement = new JsonParser().parse(jsonRestaurantDetail);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    JsonArray jarray = jobject.getAsJsonArray("ThreadImages");
		ArrayList<ThreadImageModel> MenulistSpecial = new ArrayList<ThreadImageModel>();
		for (JsonElement obj : jarray) {
			ThreadImageModel cse= gson.fromJson(obj, ThreadImageModel.class);
		    MenulistSpecial.add(cse);
		}

		return MenulistSpecial;
	}
	public ArrayList<ThreadImageModel> getThreadimageByMemberIDParse(String result) 
			throws JsonSyntaxException
	{
		return new ThreadsParse().parseThreadImageModel(result);
	}

	
	//post thread .
	//http://54.213.167.5/postThreadComment.php?&memberID=20&parentID=6&threadContent=hihi&threadTitle=2323&threadType=0
	public CommonModel postThreadCommentParse(String result)
			throws JsonSyntaxException
	{
		return new ToMoreParse().CommonPares(result);
	}
	
	/////////////////////////************************EVEVTS*************************/////////////////////////////
	//http://54.213.167.5/APIV2/getEventList.php?limit=5&page=1
	public ArrayList<EventsModel> getEventListParse(String result)
			throws JsonSyntaxException
	{
		Gson gson = new Gson();
		JsonElement jelement = new JsonParser().parse(result);
		JsonObject jobject = jelement.getAsJsonObject();
		JsonArray jarray = jobject.getAsJsonArray("data");
		ArrayList<EventsModel> lcs = new ArrayList<EventsModel>();
		for (JsonElement obj : jarray) {
			EventsModel cse = gson.fromJson(obj, EventsModel.class);
			lcs.add(cse);
		}
		return lcs;
	}

	//http://54.213.167.5/APIV2/joinEventByMemberID.php?memberID=35&eventID=1
	public CommonModel joinEventByMemberIDParse(String result)
			throws JsonSyntaxException
	{
		return new ToMoreParse().CommonPares(result);
	}


	//http://54.213.167.5/APIV2/getMemberInfoByEventID.php?eventID=1
	public ArrayList<EventMemberModel> getMemberInfoByEventIDParse(String result)
		throws JsonSyntaxException
	{
		Gson gson = new Gson();
		JsonElement jelement = new JsonParser().parse(result);
		JsonObject jobject = jelement.getAsJsonObject();
		JsonArray jarray = jobject.getAsJsonArray("data");
		ArrayList<EventMemberModel> lcs = new ArrayList<EventMemberModel>();
		for (JsonElement obj : jarray) {
			EventMemberModel cse = gson.fromJson(obj, EventMemberModel.class);
			lcs.add(cse);
		}
		return lcs;
	}
	//http://54.213.167.5/getThreadInfo.php?threadID=724
	public  ArrayList<ThreadModel> getThreadInfoBythreadIDParse(String result)
	{
		ArrayList<ThreadModel> retList = new ArrayList<ThreadModel>();
		Gson gson = new Gson();
		JsonElement jelement = new JsonParser().parse(result);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    JsonArray jarray = jobject.getAsJsonArray("data");
	    for (JsonElement obj : jarray)
	    {
	    	ThreadModel aThread = gson.fromJson(obj, ThreadModel.class);
	    	
	    	JsonObject  jobject2 = obj.getAsJsonObject();
	    	JsonArray threadImageArray = jobject2.getAsJsonArray("ThreadImages");
	    	JsonArray threadCommentArray = jobject2.getAsJsonArray("Comments");
	    	if(threadImageArray != null)
	    	{
	    		ArrayList<ThreadImageModel> imageModelList = new ArrayList<ThreadImageModel>();
	    		for (JsonElement objThreadImage : threadImageArray)
				{
	    			ThreadImageModel imageModel = gson.fromJson(objThreadImage, ThreadImageModel.class);
	    			imageModelList.add(imageModel);
				}
	    		aThread.setThreadImageList(imageModelList);
	    	}
	    	
	    	
	    	if(threadCommentArray != null)
	    	{
	    		ArrayList<ThreadCmtModel> listComment = new ArrayList<ThreadCmtModel>();
	    		for (JsonElement objComment : threadCommentArray)
	    		{
	    			ThreadCmtModel commentModel = gson.fromJson(objComment, ThreadCmtModel.class);
	    			listComment.add(commentModel);
	    		}
	    		
	    		aThread.setThreadCmtList(listComment);
	    	}

			retList.add(aThread);
	    }
	    
		return retList;
	}
	public  ArrayList<ThreadModel> getThreadByThreadIDParse(String result) 
			throws JsonSyntaxException
	{
		return new ThreadsParse().getThreadInfoBythreadIDParse(result);
	}



	//http://54.213.167.5/APIV2/LikeOrUnlikeForEvent.php?memberID=25&eventID=1&actionerID=34&like=1
	public CommonModel LikeOrUnlikeForEventParse(String result)
	{
		return new ToMoreParse().CommonPares(result);
	}
	
}
