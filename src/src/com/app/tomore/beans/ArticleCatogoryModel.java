package com.app.tomore.beans;

import java.io.Serializable;

import org.json.JSONObject;
import android.content.ContentValues;
import android.database.Cursor;

public class ArticleCatogoryModel implements Serializable{ 
	
	public String getCategoryID() {
		return CategoryID;
	}

	public void setCategoryID(String categoryID) {
		CategoryID = categoryID;
	}

	public String getCategoryName() {
		return CategoryName;
	}

	public void setCategoryName(String categoryName) {
		CategoryName = categoryName;
	}

	public String getCategoryImage() {
		return CategoryImage;
	}

	public void setCategoryImage(String categoryImage) {
		CategoryImage = categoryImage;
	}

	
	private String CategoryID;
	private String  CategoryName;
	private String  CategoryImage;
	
}
	