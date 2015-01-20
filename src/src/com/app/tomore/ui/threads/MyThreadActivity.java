package com.app.tomore.ui.threads;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import com.app.tomore.MyCameraActivity;
import com.app.tomore.R;
import com.app.tomore.R.layout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.app.tomore.beans.BLRestaurantModel;
import com.app.tomore.beans.ThreadImageModel;
import com.app.tomore.beans.ThreadModel;












import com.app.tomore.net.ThreadsParse;
import com.app.tomore.net.ThreadsRequest;
import com.app.tomore.net.YellowPageParse;
import com.app.tomore.net.YellowPageRequest;
import com.app.tomore.ui.yellowpage.RestaurantBLActivity;
import com.app.tomore.ui.yellowpage.RestaurantDetailActivity;
import com.app.tomore.ui.yellowpage.RestaurantBLActivity.RestaurantAdapter;
import com.google.gson.JsonSyntaxException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MyThreadActivity extends Activity {
	private DialogActivity dialog;
	private DisplayImageOptions otp;
	ListView listview;
	private Activity mContext;
	private ArrayList<ThreadModel> threadmodel;
	private ArrayList<ThreadImageModel> threadImageList;
	ThreadModel ThreadItem;
	ThreadImageModel ThreadImageitem;
	private ListView listView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_thread);
		getWindow().getDecorView().setBackgroundColor(Color.WHITE);
		Intent intent = getIntent();
		new GetData(MyThreadActivity.this, 1).execute("");
		otp = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).showImageForEmptyUri(R.drawable.ic_launcher)
				.build();
		listView  = (ListView) findViewById(R.id.mythreadactivity_listview);
		mContext = this;
		
	}
	
	private class GetData extends AsyncTask<String, String, String>{
		
		private int mType;
		private GetData(Context context, int type) {
			// this.mContext = context;
			this.mType = type;
			dialog = new DialogActivity(context, type);
		}
		@Override
		protected void onPreExecute() {
			if (mType == 1) {
				if (null != dialog && !dialog.isShowing()) {
					dialog.show();
				}
			}
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result = null;
			String result1 =null;
			ThreadsRequest request = new ThreadsRequest(MyThreadActivity.this);
			try {
				int page =1;
				int limit=1000;
				int memberID=25;
				Log.d("doInBackground", "start request");
				result = request.getThreadListByMemberID(limit, page, memberID);
				Log.d("doInBackground", "returned");
			}catch (IOException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			if (null != dialog) {
				dialog.dismiss();
			}
			Log.d("onPostExecute", "postExec state");
			if (result == null || result.equals("")) {
				// show empty alert
			} else {
				
				try {
					threadmodel =new ThreadsParse().getThreadListByMemberIDParse(result);
					//threadImageList=new ThreadsParse().getThreadimageByMemberIDParse(result);
					BindDataToListView();
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
				if(threadmodel !=null){
					Intent intent =new Intent(MyThreadActivity.this,MyCameraActivity.class);
					intent.putExtra("threadslist",(Serializable) threadmodel);
					// startActivity(intent);
				}
				else{
					// show empty alert
				}
			}
		}

	}
	private void BindDataToListView() {
		
		//ListView listView = (ListView) findViewById(R.id.bianlirestaurant_listview);
		MyThreadAdapter newsListAdapter = new MyThreadAdapter();
		listView.setAdapter(newsListAdapter);
	
	}
	
	class ViewHolder {
		private TextView ThreadContent;
	    private ImageView ThreadImage;
	    private TextView TimeDiff;
	}
	
	public class MyThreadAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return threadmodel.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return threadmodel.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ThreadItem =(ThreadModel) getItem(position);
			ViewHolder viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.thread_myactivity, null);
			viewHolder.ThreadImage=(ImageView) convertView.findViewById(R.id.Mythreadimage);
			ImageLoader.getInstance().displayImage(ThreadItem.getThreadImageList().get(0).getImageUrl(),
					viewHolder.ThreadImage,otp);
			viewHolder.ThreadContent = (TextView) convertView.findViewById(R.id.Mythreadcontent);
			viewHolder.ThreadContent.setText(ThreadItem.getThreadContent());
			viewHolder.TimeDiff=(TextView) convertView.findViewById(R.id.MythreadTimeDiff);
			viewHolder.TimeDiff.setText(ThreadItem.getTimeDiff());
			return convertView;
		}

	
		}
}
