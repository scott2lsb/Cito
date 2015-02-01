package com.app.tomore.net;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Bitmap;

import com.app.tomore.httpclient.BasicHttpClient;
import com.app.tomore.httpclient.HttpResponse;
import com.app.tomore.httpclient.ParameterMap;
import com.app.tomore.utils.AndroidMultiPartEntity;
import com.app.tomore.utils.AndroidMultiPartEntity.ProgressListener;

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
	public  String likeOrUnLikeAThread(String memberID, String threadID, String accountName,int isLike)
			 throws IOException, TimeoutException {
		baseRequest = new BasicHttpClient(url);
		baseRequest.setConnectionTimeout(2000);
		ParameterMap params = baseRequest.newParams()
				.add("memberID", memberID)
				.add("threadID", threadID)
				.add("accountName", accountName)
				.add("like", Integer.toString(isLike));
		;
		HttpResponse httpResponse = baseRequest.get (
				"/APIV2/LikeOrUnlike.php", params);
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
	public String getThreadInfoBythreadID(int threadId)
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
	public String PostThread(Bitmap image,String title, String Content, int memberID, int imagewidth, int imageHeight)
			 throws IOException, TimeoutException {
		String responseString = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://54.213.167.5/postThread.php");
				AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
						new ProgressListener() {
							@Override
							public void transferred(long num) {
							}
						});
				// create temp file for upload
				String fileName = "temp.jpg";

				String path = android.os.Environment.getExternalStorageDirectory()
						.toString();
				File f = new File(path, fileName);
				f.createNewFile();

				// Convert bitmap to byte array
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				image.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] bitmapdata = stream.toByteArray();

				// write the bytes in file
				FileOutputStream fos = new FileOutputStream(f);
				fos.write(bitmapdata);
				fos.flush();
				fos.close();
				//http://54.213.167.5/postThread.php?threadTitle=title&threadContent=content&threadType=1&memberID=25&imageWidth=50&imageHeight=50

				entity.addPart("uploaded1", new FileBody(f));
				entity.addPart("uploaded2", new StringBody(""));
				entity.addPart("threadTitle", new StringBody(""));
				entity.addPart("threadContent", new StringBody(Content));
				entity.addPart("threadType", new StringBody("1"));
				entity.addPart("memberID", new StringBody(Integer.toString(memberID)));
				entity.addPart("imageWidth", new StringBody(Integer.toString(imagewidth)));
				entity.addPart("imageHeight", new StringBody(Integer.toString(imageHeight)));

				httppost.setEntity(entity);

				// Making server call
				org.apache.http.HttpResponse response = httpclient
						.execute(httppost);
				HttpEntity r_entity = response.getEntity();
				// delete temp file after upload
				f.delete();

				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					// Server response
					responseString = EntityUtils.toString(r_entity);
				} else {
					responseString = "Error occurred! Http Status Code: "
							+ statusCode;
				}
			} catch (ClientProtocolException e) {
				responseString = e.toString();
			} catch (IOException e) {
				responseString = e.toString();
			}
		return responseString;
	}
	

}
