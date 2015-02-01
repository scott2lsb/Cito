package com.app.tomore.net;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import android.content.Context;

import com.app.tomore.httpclient.BasicHttpClient;
import com.app.tomore.httpclient.HttpResponse;
import com.app.tomore.httpclient.ParameterMap;

public class ThreadsRequest {

	private final String url = "http://54.213.167.5/";
	protected Context mContext;
	private BasicHttpClient baseRequest;

	public ThreadsRequest(Context context) {
		mContext = context;
	}

	/*
	 * get thread list with filter
	 * http://54.213.167.5/APIV2/getThreadList.php?page
	 * =1&limit=2&viewerID=2&filter=0
	 */

	public String getThreadList(int pageNum, int number, int viewerId,
			int filterId) throws IOException, TimeoutException {
		baseRequest = new BasicHttpClient(url);
		baseRequest.setConnectionTimeout(2000);
		ParameterMap params = baseRequest.newParams()
				.add("page", Integer.toString(pageNum))
				.add("limit", Integer.toString(number))
				.add("viewerID", Integer.toString(viewerId))
				.add("filter", Integer.toString(filterId));
		;
		HttpResponse httpResponse = baseRequest.get(
				"/APIV2/getThreadList.php", params);
		return httpResponse.getBodyAsString();
	}

	/*
	 * get thread fall list http://54.213.167.5/get20RandomThread.php
	 */
	public String getThreadFallList() throws IOException, TimeoutException {
		baseRequest = new BasicHttpClient(url);
		baseRequest.setConnectionTimeout(2000);
		HttpResponse httpResponse = baseRequest.get("/get20RandomThread.php",
				null);
		return httpResponse.getBodyAsString();
	}

	//get recommend list
	// http://54.213.167.5/recommend.php?memberID=2
	public String getRecommenddList(int memberID)  throws IOException, TimeoutException {
		baseRequest = new BasicHttpClient(url);
		baseRequest.setConnectionTimeout(2000);
		ParameterMap params = baseRequest.newParams().add("memberID",
				Integer.toString(memberID));
		HttpResponse httpResponse = baseRequest.post("/recommend.php", params);
		return httpResponse.getBodyAsString();
	}
	
	/*
	 * like or unlike a thread
	 */
	public String likeOrUnLikeAThread(String memberID, String threadID, String accountName,int isLike)
			 throws IOException, TimeoutException {
		baseRequest = new BasicHttpClient(url);
		baseRequest.setConnectionTimeout(2000);
		ParameterMap params = baseRequest.newParams()
				.add("memberID", memberID)
				.add("threadID", threadID)
				.add("accountName", accountName)
				.add("like", Integer.toString(isLike));
		;
		HttpResponse httpResponse = baseRequest.post(
				"/APIV2/likeOrUnlike.php", params);
		return httpResponse.getBodyAsString();
	}

	/*
	 * post comment to a thread
	 */
	//http://54.213.167.5/getCommentsByThreadID.php?&limit=20&page=1&threadID=87
	public String getCommentsByThreadID(int limit, int page, int threadID)
			 throws IOException, TimeoutException {
		baseRequest = new BasicHttpClient(url);
		baseRequest.setConnectionTimeout(2000);
		ParameterMap params = baseRequest.newParams()
				.add("limit", Integer.toString(limit))
				.add("page", Integer.toString(page))
				.add("threadID", Integer.toString(threadID));
		HttpResponse httpResponse = baseRequest.post(
				"/getCommentsByThreadID.php", params);
		return httpResponse.getBodyAsString();
	}
	/*
	 * get thread details
	 */

	//get threads by member
	//http://54.213.167.5/getThreadListByMemberID.php?memberID=25&limit=20&page=1
	public String getThreadListByMemberID(int limit, int page, int memberID)
			 throws IOException, TimeoutException {
		baseRequest = new BasicHttpClient(url);
		baseRequest.setConnectionTimeout(2000);
		ParameterMap params = baseRequest.newParams()
				.add("limit", Integer.toString(limit))
				.add("page", Integer.toString(page))
				.add("memberID", Integer.toString(memberID));
		HttpResponse httpResponse = baseRequest.post(
				"/getThreadListByMemberID.php", params);
		return httpResponse.getBodyAsString();
	}
	//http://54.213.167.5/getThreadInfo.php?threadID=724
	public String getThreadInfoBythreadrID(int threadId)
			 throws IOException, TimeoutException {
		baseRequest = new BasicHttpClient(url);
		baseRequest.setConnectionTimeout(2000);
		ParameterMap params = baseRequest.newParams()

				.add("threadID", Integer.toString(threadId));
		HttpResponse httpResponse = baseRequest.post(
				"/getThreadInfo.php", params);
		return httpResponse.getBodyAsString();
	}
	
	//post thread comment
	//http://54.213.167.5/postThreadComment.php?&memberID=20&parentID=6&threadContent=hihi&threadTitle=2323&threadType=0
	public String postThreadComment(int memberID, int threadId, String threadContent)
			 throws IOException, TimeoutException {
		baseRequest = new BasicHttpClient(url);
		baseRequest.setConnectionTimeout(2000);
		ParameterMap params = baseRequest.newParams()
				.add("memberID", Integer.toString(memberID))
				.add("parentID", Integer.toString(threadId))
				.add("threadType", "1")
				.add("threadTitle", "")
				.add("threadContent", threadContent);
		HttpResponse httpResponse = baseRequest.post(
				"/postThreadComment.php", params);
		return httpResponse.getBodyAsString();
	}
	
	/////////////////////////************************EVEVTS*************************/////////////////////////////
	//http://54.213.167.5/APIV2/getEventList.php?limit=5&page=1
	public String getEventList(int limit, int page)
			 throws IOException, TimeoutException {
		baseRequest = new BasicHttpClient(url);
		baseRequest.setConnectionTimeout(2000);
		ParameterMap params = baseRequest.newParams()
				.add("limit", Integer.toString(limit))
				.add("page", Integer.toString(page));
		HttpResponse httpResponse = baseRequest.post(
				"/APIV2/getEventList.php", params);
		return httpResponse.getBodyAsString();
	}

	//http://54.213.167.5/APIV2/joinEventByMemberID.php?memberID=35&eventID=1
	public String joinEventByMemberID(int eventID, int memberID)
			 throws IOException, TimeoutException {
		baseRequest = new BasicHttpClient(url);
		baseRequest.setConnectionTimeout(2000);
		ParameterMap params = baseRequest.newParams()
				.add("eventID", Integer.toString(eventID))
				.add("memberID", Integer.toString(memberID));
		HttpResponse httpResponse = baseRequest.post(
				"/APIV2/joinEventByMemberID.php", params);
		return httpResponse.getBodyAsString();
	}


	//http://54.213.167.5/APIV2/getMemberInfoByEventID.php?eventID=1
	public String getMemberInfoByEventID(int eventID)
			 throws IOException, TimeoutException {
		baseRequest = new BasicHttpClient(url);
		baseRequest.setConnectionTimeout(2000);
		ParameterMap params = baseRequest.newParams()
				.add("eventID", Integer.toString(eventID));
		HttpResponse httpResponse = baseRequest.post(
				"/APIV2/getMemberInfoByEventID.php", params);
		return httpResponse.getBodyAsString();
	}

	//http://54.213.167.5/APIV2/LikeOrUnlikeForEvent.php?memberID=25&eventID=1&actionerID=34&like=1
	public String LikeOrUnlikeForEvent(int actionerID, int eventID, int memberID, int isLike)
			 throws IOException, TimeoutException {
		baseRequest = new BasicHttpClient(url);
		baseRequest.setConnectionTimeout(2000);
		ParameterMap params = baseRequest.newParams()
				.add("actionerID", Integer.toString(actionerID))
				.add("eventID", Integer.toString(eventID))
				.add("memberID", Integer.toString(memberID))
				.add("isLike", Integer.toString(isLike));
		HttpResponse httpResponse = baseRequest.post(
				"/APIV2/LikeOrUnlikeForEvent.php", params);
		return httpResponse.getBodyAsString();
	}
	//http://54.213.167.5/postThread.php?threadTitle=title&threadContent=content&threadType=1&memberID=25&imageWidth=50&imageHeight=50
	public String PostThread(String title, String Content, int memberID, int imagewidth, int imageHeight)
			 throws IOException, TimeoutException {
		baseRequest = new BasicHttpClient(url);
		baseRequest.setConnectionTimeout(2000);
		ParameterMap params = baseRequest.newParams()
				.add("title", title)
				.add("threadContent", Content)
				.add("threadType", "1")
				.add("memberID", Integer.toString(memberID))
				.add("imageWidth", Integer.toString(imagewidth))
				.add("imageHeight", Integer.toString(imageHeight));
		HttpResponse httpResponse = baseRequest.post(
				"/postThread.php?", params);
		return httpResponse.getBodyAsString();
	}
	

}
