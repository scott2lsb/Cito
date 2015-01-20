package com.app.tomore.ui.usercenter;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import com.app.tomore.MyCameraActivity;
import com.app.tomore.R;
import com.app.tomore.R.layout;
import com.app.tomore.beans.ThreadModel;
import com.app.tomore.beans.ThreadUpdateModel;
import com.app.tomore.net.ThreadsParse;
import com.app.tomore.net.ThreadsRequest;
import com.app.tomore.net.UserCenterParse;
import com.app.tomore.net.UserCenterRequest;
import com.app.tomore.ui.threads.DialogActivity;
import com.app.tomore.ui.threads.MyThreadActivity;
import com.app.tomore.ui.threads.MyThreadActivity.MyThreadAdapter;
import com.google.gson.JsonSyntaxException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

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

public class MyReplyListActivity extends Activity {
	private DialogActivity dialog;
	private DisplayImageOptions otp;
	ListView listview;
	private Activity mContext;
	private ArrayList<ThreadUpdateModel> threadupdatemodel;
	ThreadUpdateModel threadupdateitem;
	private ListView listView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_reply_list);
		getWindow().getDecorView().setBackgroundColor(Color.WHITE);

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
			UserCenterRequest request = new UserCenterRequest(MyReplyListActivity.this);
			try {

				String memberID="25";
				Log.d("doInBackground", "start request");
				result = request.getUserUpdate(memberID);
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
					threadupdatemodel =new UserCenterParse().praserMyUpdateModel(result);
					//threadImageList=new ThreadsParse().getThreadimageByMemberIDParse(result);
					BindDataToListView();
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
				if(threadupdatemodel !=null){
					Intent intent =new Intent(MyReplyListActivity.this,MyCameraActivity.class);
					intent.putExtra("threadupdatelist",(Serializable) threadupdatemodel);
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
	//MyThreadAdapter newsListAdapter = new MyThreadAdapter();
	//listView.setAdapter(newsListAdapter);

    }
  	class ViewHolder {
		private TextView membername;
	    private ImageView memberimage;
	    private TextView comment;
	}
  	public class MyThreadAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return threadupdatemodel.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return threadupdatemodel.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			threadupdateitem =(ThreadUpdateModel) getItem(position);
			ViewHolder viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.myreplay_activity_usercenter, null);
			viewHolder.memberimage=(ImageView) convertView.findViewById(R.id.Myreplyimage);
			//ImageLoader.getInstance().displayImage(threadupdateitem.,
				//	viewHolder.memberimage,otp);
			viewHolder.membername = (TextView) convertView.findViewById(R.id.Myreplymember);
			//viewHolder.membername.setText(ThreadItem.getThreadContent());
			//viewHolder.TimeDiff=(TextView) convertView.findViewById(R.id.MythreadTimeDiff);
			//viewHolder.TimeDiff.setText(ThreadItem.getTimeDiff());
			return convertView;
		}

	
		}
}
