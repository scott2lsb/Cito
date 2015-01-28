package com.app.tomore.net;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tomore.beans.ArticleCatogoryModel;
import com.app.tomore.beans.ArticleCommentModel;
import com.app.tomore.beans.ArticleModel;
import com.app.tomore.beans.BLRestaurantModel;
import com.app.tomore.beans.CategoryModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class MagParse {

	public ArrayList<ArticleModel> parseArticleResponse(String result) throws JsonSyntaxException 
	{
		Gson gson = new Gson();
		JsonElement jelement = new JsonParser().parse(result);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    JsonArray jarray = jobject.getAsJsonArray("data");
		ArrayList<ArticleModel> articlelist = new ArrayList<ArticleModel>();
		for (JsonElement obj : jarray) {
			ArticleModel cse = gson.fromJson(obj, ArticleModel.class);
			articlelist.add(cse);
		}
		return articlelist;
	}
	
	public ArrayList<ArticleCommentModel> parseArticleComment(String result) throws JsonSyntaxException 
	{
		Gson gson = new Gson();
		JsonElement jelement = new JsonParser().parse(result);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    JsonArray jarray = jobject.getAsJsonArray("data");
		ArrayList<ArticleCommentModel> articleCommentlist = new ArrayList<ArticleCommentModel>();
		for (JsonElement obj : jarray) {
			ArticleCommentModel cse = gson.fromJson(obj, ArticleCommentModel.class);
			articleCommentlist.add(cse);
		}
		return articleCommentlist;
	}
	
	public ArrayList<ArticleCatogoryModel> parseCtegoryResponse(String jsonCategory)  throws JsonSyntaxException 
	{
		Gson gson = new Gson();
		JsonElement jelement = new JsonParser().parse(jsonCategory);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    JsonArray jarray = jobject.getAsJsonArray("data");
		ArrayList<ArticleCatogoryModel> lcs = new ArrayList<ArticleCatogoryModel>();
		for (JsonElement obj : jarray) {
			ArticleCatogoryModel cse = gson.fromJson(obj, ArticleCatogoryModel.class);
			lcs.add(cse);
		}
		return lcs;
	}
	
	public String parsePre(String pre) throws JsonSyntaxException
	{
		JsonElement jelement = new JsonParser().parse(pre);
		JsonObject  jobject2 = jelement.getAsJsonObject();
		if(jobject2.get("pre").isJsonNull())
		{
			return null;
		}
		else
			return jobject2.get("pre").getAsString();
	}
	
	public String parseNext(String next) throws JsonSyntaxException
	{
		JsonElement jelement = new JsonParser().parse(next);
		JsonObject  jobject3 = jelement.getAsJsonObject();
		if(jobject3.get("next").isJsonNull())
		{
			return null;
		}
		else
			return jobject3.get("next").getAsString();
	}
}
