package com.app.tomore.net;

import java.util.ArrayList;

import com.app.tomore.beans.BLMenuSpecial;
import com.app.tomore.beans.BlockedModel;
import com.app.tomore.beans.CommonModel;
import com.app.tomore.beans.FansModel;
import com.app.tomore.beans.FollowingModel;
import com.app.tomore.beans.MemberModel;
import com.app.tomore.beans.ThreadCmtModel;
import com.app.tomore.beans.ThreadUpdateLikeModel;
import com.app.tomore.beans.ThreadUpdateModel;
import com.app.tomore.beans.ThreadModel;
import com.app.tomore.beans.UpdateFollowedModel;
import com.app.tomore.beans.UserModel;
import com.app.tomore.httpclient.BasicHttpClient;
import com.app.tomore.httpclient.HttpResponse;
import com.app.tomore.httpclient.ParameterMap;
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

		if (loninResult.equals("\"succ\"") || loninResult.equals("\"Unactivated account\"")) {
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

	// get all my threads list
	// http://54.213.167.5/getThreadListByMemberID.php?memberID=25&limit=20&page=1
	public ArrayList<ThreadModel> parseThreadModel(String jsonThreads)
			throws JsonSyntaxException {
		return new ThreadsParse().parseThreadModel(jsonThreads);
	}

	// http://54.213.167.5/APIV2/getUpdates.php?memberID=25
	public ArrayList<ThreadUpdateModel> praserMyUpdateModel(
			String jsonUpdateString) throws JsonSyntaxException {
		ArrayList<ThreadUpdateModel> retList = new ArrayList<ThreadUpdateModel>();
		Gson gson = new Gson();
		JsonElement jelement = new JsonParser().parse(jsonUpdateString);
		JsonObject jobject = jelement.getAsJsonObject();
		JsonArray jarray = jobject.getAsJsonArray("data");
		for (JsonElement obj : jarray) {
			ThreadUpdateModel aThread = gson.fromJson(obj,
					ThreadUpdateModel.class);
			JsonObject jobject2 = obj.getAsJsonObject();
			JsonArray threadLikeArray = jobject2.getAsJsonArray("LikeList");
			JsonArray threadCommentArray = jobject2.getAsJsonArray("Comments");

			if (threadLikeArray != null) {
				ArrayList<ThreadUpdateLikeModel> likeModelList = new ArrayList<ThreadUpdateLikeModel>();
				for (JsonElement objLike : threadLikeArray) {
					ThreadUpdateLikeModel likeModel = gson.fromJson(objLike,
							ThreadUpdateLikeModel.class);
					likeModelList.add(likeModel);
				}

				aThread.setLikeList(likeModelList);
			}

			if (threadCommentArray != null) {
				ArrayList<ThreadCmtModel> listComment = new ArrayList<ThreadCmtModel>();
				for (JsonElement objComment : threadCommentArray) {
					ThreadCmtModel commentModel = gson.fromJson(objComment,
							ThreadCmtModel.class);
					listComment.add(commentModel);
				}

				aThread.setCommentList(listComment);
			}

			retList.add(aThread);
		}

		return retList;
	}

	// search
	// http://54.213.167.5/APIV2/searchByAccountName.php?accountName=neo&viewerID=25
	public ArrayList<MemberModel> searchByAccountNameParse(String result) {
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

	// /http://54.213.167.5/getMemberInfoByMemberID.php?memberID=6&viewerID=3
	public ArrayList<UserModel> getMemberInfoByMemberIDParse(String result)
			throws JsonSyntaxException {
		Gson gson = new Gson();
		JsonElement jelement = new JsonParser().parse(result);
		JsonObject jobject = jelement.getAsJsonObject();
		JsonArray jarray = jobject.getAsJsonArray("data");
		ArrayList<UserModel> lcs = new ArrayList<UserModel>();
		for (JsonElement obj : jarray) {
			UserModel cse = gson.fromJson(obj, UserModel.class);
			lcs.add(cse);
		}
		return lcs;
	}

	// forget password
	// http://54.213.167.5/APIV2/recoverPasswordRequest.php?&email=alexliubo%40gmail.com
	public CommonModel recoverPasswordParse(String result) {
		return new ToMoreParse().CommonPares(result);
	}
	
	public String parseFollowOrUnfollowResponse(String result) {
		Gson gson = new Gson();
		JsonElement jelement = new JsonParser().parse(result);
		JsonObject jobject = jelement.getAsJsonObject();
		String followOrUnfollowResult = jobject.get("result").toString();
		String followOrUnfollowExist = null;
		if (followOrUnfollowResult.equals("\"succ\"")) {
			followOrUnfollowExist = jobject.get("exist").toString();
		}
		return followOrUnfollowExist;
	}
	
	public String parseBlockOrUnblockwResponse(String result) {
		Gson gson = new Gson();
		JsonElement jelement = new JsonParser().parse(result);
		JsonObject jobject = jelement.getAsJsonObject();
		String blockOrUnblockResult = jobject.get("result").toString();
		String blockOrUnblockExist = null;
		if (blockOrUnblockResult.equals("\"succ\"")) {
			blockOrUnblockExist = jobject.get("exist").toString();
		}
		return blockOrUnblockExist;
	}
	
	// find password
	public boolean parseFindPasswordResponse(String result) {
		JsonElement jelement = new JsonParser().parse(result);
		JsonObject jobject = jelement.getAsJsonObject();
		String resultSucc = jobject.get("result").toString();

		if (resultSucc.equals("\"succ\"")) {
			return true;
		} else
			return false;
	}

}
