package com.app.tomore.ui.threads;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import com.app.tomore.R;
import com.app.tomore.beans.CommonModel;
import com.app.tomore.beans.EventsModel;
import com.app.tomore.net.ThreadsParse;
import com.app.tomore.net.ThreadsRequest;
import com.app.tomore.ui.threads.DialogActivity;
import com.app.tomore.utils.SpUtils;
import com.app.tomore.utils.ToastUtils;
import com.google.gson.JsonSyntaxException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.widget.ImageView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import com.app.tomore.beans.EventMemberModel;

public class EventDetailsActivity extends Activity {
	private DialogActivity dialog;
	private String logindInUserId = null;
	private EventMemberModel memberModel;
	private ArrayList<EventMemberModel> memberList;
	private DisplayImageOptions otp;
	private GridView gridView;
	private String eventId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_details);
		logindInUserId = SpUtils.getUserId(EventDetailsActivity.this);
		otp = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).showImageForEmptyUri(R.drawable.ic_launcher)
				.build();
		RelativeLayout rl = (RelativeLayout) getWindow().getDecorView()
				.findViewById(R.id.bar_title_league);
		
		final Button btnBack = (Button) rl
				.findViewById(R.id.bar_title_league_go_back);
		
		final  TextView submit = (TextView)findViewById(R.id.submit_button); 
		
		
		//back
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		
		//attend an event
		submit.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View viewIn) {
	        	new AttendAnEvent(EventDetailsActivity.this, 1)
				.execute("");
	        }
	    });
		
		TextView titleTextView = (TextView) rl.findViewById(R.id.btLeague);
		titleTextView.setText("活动详情");

		gridView = (GridView) findViewById(R.id.gridView1);


		EventsModel aEvent = (EventsModel) getIntent().getSerializableExtra(
				"memberList");
		eventId = aEvent.getEventID();
		new GetMemberList(EventDetailsActivity.this, 1)
				.execute("");
	}
	
	private class AttendAnEvent extends AsyncTask<String, String, String> {
		private int mType;

		private AttendAnEvent(Context context, int type) {
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
			String result = null;
			ThreadsRequest request = new ThreadsRequest(
					EventDetailsActivity.this);
			try {
				Log.d("doInBackground", "start request");
				result = request.joinEventByMemberID(Integer.parseInt(eventId), Integer.parseInt(logindInUserId));
				Log.d("doInBackground", "returned");
			} catch (IOException e) {
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
					ThreadsParse aMemberParse = new ThreadsParse();
					CommonModel	aJointResult = aMemberParse.joinEventByMemberIDParse(result);

					if(aJointResult.getResult().equals("succ"))
					{
						ToastUtils.showToast(EventDetailsActivity.this,"感谢参与");
						new GetMemberList(EventDetailsActivity.this, 1)
						.execute(""); //refresh list
					}
					else
					{
						ToastUtils.showToast(EventDetailsActivity.this,"参与失败");
					}
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class LikeOrUnLikeAMember extends AsyncTask<String, String, String> {
		private int mType;
		private boolean mLikeIt;
		private LikeOrUnLikeAMember(Context context, int type, boolean likeIt) {
			// this.mContext = context;
			this.mType = type;
			this.mLikeIt = likeIt;
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
			String result = null;
			ThreadsRequest request = new ThreadsRequest(
					EventDetailsActivity.this);
			try {
				Log.d("doInBackground", "start request");
				result = request.LikeOrUnlikeForEvent(Integer.parseInt(logindInUserId), 
						Integer.parseInt(eventId), 
						Integer.parseInt(memberModel.getMemberID()), mLikeIt?1:0);
				Log.d("doInBackground", "returned");
			} catch (IOException e) {
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
					ThreadsParse aMemberParse = new ThreadsParse();
					CommonModel	aJointResult = aMemberParse.LikeOrUnlikeForEventParse(result);

					if(mLikeIt)
					{
						if(aJointResult.getResult().equals("succ"))
						{
							ToastUtils.showToast(EventDetailsActivity.this,"点赞成功");
							new GetMemberList(EventDetailsActivity.this, 1)
							.execute(""); //refresh list
						}
						else
						{
							ToastUtils.showToast(EventDetailsActivity.this,"点赞失败");
						}
					}
					else
					{
						if(aJointResult.getResult().equals("succ"))
						{
							ToastUtils.showToast(EventDetailsActivity.this,"取消点赞");
							new GetMemberList(EventDetailsActivity.this, 1)
							.execute(""); //refresh list
						}
						else
						{
							ToastUtils.showToast(EventDetailsActivity.this,"取消失败");
						}
					}
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class GetMemberList extends AsyncTask<String, String, String> {
		private int mType;

		private GetMemberList(Context context, int type) {
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
			String result = null;
			ThreadsRequest request = new ThreadsRequest(
					EventDetailsActivity.this);
			try {
				Log.d("doInBackground", "start request");
				result = request.getMemberInfoByEventID(Integer.parseInt(eventId));
				Log.d("doInBackground", "returned");
			} catch (IOException e) {
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
				memberList = new ArrayList<EventMemberModel>();

				try {
					ThreadsParse aMemberParse = new ThreadsParse();
					memberList = aMemberParse.getMemberInfoByEventIDParse(result);

					if (memberList.size() > 0) {
						BindDataToGridView();
					}
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	
	class ViewHolder {
		private ImageView ImageView;
	}

	private class UserInformationAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return memberList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return memberList.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			final EventMemberModel item = (EventMemberModel) getItem(position);
			ViewHolder viewHolder = null;
			if (convertView != null) {
				viewHolder = (ViewHolder) convertView.getTag();
			} else {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(EventDetailsActivity.this)
						.inflate(R.layout.event_member_item, null);

				viewHolder.ImageView = (ImageView) convertView
						.findViewById(R.id.ItemImage);

				convertView.setTag(viewHolder);
			}
			ImageLoader.getInstance().displayImage(item.getImage(),
					viewHolder.ImageView, otp);
			// viewHolder.textView.setText(item.getName());
			return convertView;
		}
	}

	private void BindDataToGridView() {
		gridView.setAdapter(new UserInformationAdapter());
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// To thread reply activity
				memberModel = memberList.get(position);
				if(memberModel.getLike().equals("0"))
				{
					//like this person
					new LikeOrUnLikeAMember(EventDetailsActivity.this, 1, true)
					.execute("");
				}
				else
				{
					//unlike this person
					new LikeOrUnLikeAMember(EventDetailsActivity.this, 1, false)
					.execute("");
				}
			}
		});
	}

}
