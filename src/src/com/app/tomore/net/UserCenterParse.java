package com.app.tomore.net;

import java.util.ArrayList;

import com.app.tomore.beans.BlockedModel;
import com.app.tomore.beans.FansModel;
import com.app.tomore.beans.FollowOrUnfollowModel;
import com.app.tomore.beans.FollowingModel;
import com.app.tomore.beans.ThreadCmtModel;
import com.app.tomore.beans.ThreadUpdateLikeModel;
import com.app.tomore.beans.ThreadUpdateModel;
import com.app.tomore.beans.ThreadLikeModel;
import com.app.tomore.beans.ThreadModel;
import com.app.tomore.beans.UserModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class UserCenterParse {
	public UserModel parseLoginResponse(String result) {
		Gson gson = new Gson();
		JsonElement jelement = new JsonParser().parse(result);
		JsonObject jobject = jelement.getAsJsonObject();
		String loninResult = jobject.get("result").toString();
		JsonArray jarray = jobject.getAsJsonArray("data");

		if (loninResult.equals("\"succ\"")) {
			for (JsonElement obj : jarray) {
				UserModel cse = gson.fromJson(obj, UserModel.class);
				if (cse != null) {
					return cse;
				} else
					return null;
			}
		}
		return null;
	}

	public String parseRegisterResponse(String result) {
		Gson gson = new Gson();
		JsonElement jelement = new JsonParser().parse(result);
		JsonObject jobject = jelement.getAsJsonObject();
		String registerResult = jobject.get("result").toString();
		return registerResult;
	}

	public ArrayList<FansModel> parseFansResponse(String result)
			throws JsonSyntaxException {
		Gson gson = new Gson();
		JsonElement jelement = new JsonParser().parse(result);
		JsonObject jobject = jelement.getAsJsonObject();
		JsonArray jarray = jobject.getAsJsonArray("data");
		ArrayList<FansModel> lcs = new ArrayList<FansModel>();
		for (JsonElement obj : jarray) {
			FansModel cse = gson.fromJson(obj, FansModel.class);
			lcs.add(cse);
		}
		return lcs;
	}

	public ArrayList<FollowingModel> parseFollowingResponse(String result)
			throws JsonSyntaxException {
		Gson gson = new Gson();
		JsonElement jelement = new JsonParser().parse(result);
		JsonObject jobject = jelement.getAsJsonObject();
		JsonArray jarray = jobject.getAsJsonArray("data");
		ArrayList<FollowingModel> lcs = new ArrayList<FollowingModel>();
		for (JsonElement obj : jarray) {
			FollowingModel cse = gson.fromJson(obj, FollowingModel.class);
			lcs.add(cse);
		}
		return lcs;
	}

	public ArrayList<BlockedModel> parseBlockedResponse(String result)
			throws JsonSyntaxException {
		Gson gson = new Gson();
		JsonElement jelement = new JsonParser().parse(result);
		JsonObject jobject = jelement.getAsJsonObject();
		JsonArray jarray = jobject.getAsJsonArray("data");
		ArrayList<BlockedModel> lcs = new ArrayList<BlockedModel>();
		for (JsonElement obj : jarray) {
			BlockedModel cse = gson.fromJson(obj, BlockedModel.class);
			lcs.add(cse);
		}
		return lcs;
	}

	// delete a thread by member id parse
	// return true - deleted.
	public boolean parseDeleteThreadResponse(String result) {
		JsonElement jelement = new JsonParser().parse(result);
		JsonObject jobject = jelement.getAsJsonObject();
		String resultSucc = jobject.get("result").toString();

		if (resultSucc.equals("\"succ\"")) {
			return true;
		} else
			return false;
	}

	//get all my threads list
	// http://54.213.167.5/getThreadListByMemberID.php?memberID=25&limit=20&page=1
	public ArrayList<ThreadModel> parseThreadModel(String jsonThreads)
			throws JsonSyntaxException {
		return new ThreadsParse().parseThreadModel(jsonThreads);
	}
	
	//http://54.213.167.5/APIV2/getUpdates.php?memberID=25
	public ArrayList<ThreadUpdateModel> praserMyUpdateModel(String jsonUpdateString)
				throws JsonSyntaxException {
		ArrayList<ThreadUpdateModel> retList = new ArrayList<ThreadUpdateModel>();
		Gson gson = new Gson();
		JsonElement jelement = new JsonParser().parse(jsonUpdateString);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    JsonArray jarray = jobject.getAsJsonArray("data");
	    for (JsonElement obj : jarray)
	    {
	    	ThreadUpdateModel aThread = gson.fromJson(obj, ThreadUpdateModel.class);
	    	JsonObject  jobject2 = obj.getAsJsonObject();
	    	JsonArray threadLikeArray = jobject2.getAsJsonArray("LikeList");
	    	JsonArray threadCommentArray = jobject2.getAsJsonArray("Comments");
	    	
	    	
	    	if(threadLikeArray != null)
	    	{
	    		ArrayList<ThreadUpdateLikeModel> likeModelList = new ArrayList<ThreadUpdateLikeModel>();
	    		for (JsonElement objLike : threadLikeArray)
	    		{
	    			ThreadUpdateLikeModel likeModel = gson.fromJson(objLike, ThreadUpdateLikeModel.class);
	    			likeModelList.add(likeModel);
	    		}

	    		aThread.setLikeList(likeModelList);
	    	}
	    	
	    	if(threadCommentArray != null)
	    	{
	    		ArrayList<ThreadCmtModel> listComment = new ArrayList<ThreadCmtModel>();
	    		for (JsonElement objComment : threadCommentArray)
	    		{
	    			ThreadCmtModel commentModel = gson.fromJson(objComment, ThreadCmtModel.class);
	    			listComment.add(commentModel);
	    		}
	    		
	    		aThread.setCommentList(listComment);
	    	}

			retList.add(aThread);
	    }
	    
		return retList;
	}
	
	public String parseFollowOrUnfollowResponse(String result)
			throws JsonSyntaxException {
		Gson gson = new Gson();
		JsonElement jelement = new JsonParser().parse(result);
		JsonObject jobject = jelement.getAsJsonObject();
		JsonObject jarray = jobject.getAsJsonObject();
		String followOrUnfollowResult = jobject.get("exist").toString();
//		ArrayList<FollowOrUnfollowModel> lcs = new ArrayList<FollowOrUnfollowModel>();
//		for (JsonElement obj : jarray) {
//			FollowOrUnfollowModel cse = gson.fromJson(obj, FollowOrUnfollowModel.class);
//			lcs.add(cse);
//		}
		return followOrUnfollowResult;
	}
}
