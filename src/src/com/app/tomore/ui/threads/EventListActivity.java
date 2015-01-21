package com.app.tomore.ui.threads;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import com.app.tomore.ui.threads.DialogActivity;
import com.app.tomore.net.ThreadsParse;
import com.app.tomore.net.ThreadsRequest;
import com.google.gson.JsonSyntaxException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.app.tomore.R;
import com.app.tomore.utils.AppUtil;
import com.app.tomore.utils.ToastUtils;
import com.app.tomore.utils.PullToRefreshListView;
import com.app.tomore.utils.PullToRefreshBase;
import com.app.tomore.utils.PullToRefreshBase.OnLastItemVisibleListener;
import com.app.tomore.utils.PullToRefreshBase.OnRefreshListener;
import com.app.tomore.beans.EventsModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class EventListActivity extends Activity {
	private DialogActivity dialog;
	private ArrayList<EventsModel> eventList;
	private EventsModel eventModel;
	private DisplayImageOptions otp;
	private PullToRefreshListView mListView;
	private Activity mContext;
	private TextView noneData;
	private View no_net_lay;
	EventListAdapter aEventAdapter;
	private boolean onRefresh = false;
	private boolean headerRefresh = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_list);
		mContext = this;
		getWindow().getDecorView().setBackgroundColor(Color.WHITE);
		otp = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).showImageForEmptyUri(R.drawable.ic_launcher)
				.build();
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(this));

			mListView = (PullToRefreshListView) findViewById(R.id.mag_listviews);
			mListView.setOnRefreshListener(onRefreshListener);
			mListView.setOnLastItemVisibleListener(onLastItemVisibleListener);
			mListView.setOnItemClickListener(itemClickListener);
			noneData = (TextView)findViewById(R.id.noneData);
			no_net_lay = findViewById(R.id.no_net_lay);
			Button reloadData = (Button)findViewById(R.id.reloadData);
			reloadData.setOnClickListener(reloadClickListener);
			
			RelativeLayout rl = (RelativeLayout) getWindow().getDecorView()
					.findViewById(R.id.bar_title_mythread);
			
//			final Button btnBack = (Button) rl
//					.findViewById(R.id.bar_title_blocked_go_back);
//			
//			btnBack.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View view) {
//					finish();
//				}
//			});
			new GetData(EventListActivity.this, 1).execute("");
	}

	private void BindDataToListView() {
		if (onRefresh) {
			onRefresh = false;
		}
		if (aEventAdapter == null) {
			aEventAdapter = new EventListAdapter();
			mListView.setAdapter(aEventAdapter);
		} else {
			aEventAdapter.notifyDataSetChanged();
		}
		if(eventList!=null && eventList.size()>0){
			showDataUi();
		}else{
			showNoDataUi();
		}
	}
	
	void showDataUi(){
		mListView.setVisibility(View.VISIBLE);
		noneData.setVisibility(View.GONE);
		no_net_lay.setVisibility(View.GONE);
	}

	void showNoDataUi(){
		mListView.setVisibility(View.GONE);
		noneData.setVisibility(View.VISIBLE);
		no_net_lay.setVisibility(View.GONE); 
	}

	protected void showNoNetUi() {
		no_net_lay.setVisibility(View.VISIBLE);
		noneData.setVisibility(View.GONE);
		mListView.setVisibility(View.GONE);
	}
	private class GetData extends AsyncTask<String, String, String> {
		private int mType;
		private GetData(Context context, int type) {
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
			ThreadsRequest request = new ThreadsRequest(EventListActivity.this);
			try {
				
				Log.d("doInBackground", "start request");			
					result = request.getEventList(100, 1);
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
			mListView.onRefreshComplete();
			Log.d("onPostExecute", "postExec state");
			if (result == null || result.equals("")) {
				ToastUtils.showToast(mContext, "列表为空");
			} else {
				if(eventList!=null && eventList.size()>0)
				{
					if(headerRefresh)
						eventList = new ArrayList<EventsModel>();
				}
				else
				{
					eventList = new ArrayList<EventsModel>();
				}
				try {
					eventList = new ThreadsParse().getEventListParse(result);

					BindDataToListView();
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) { 
			if(!AppUtil.networkAvailable(mContext)){
				ToastUtils.showToast(mContext, "列表为空");
				return;
			}
			if (eventList == null) {
				return;
			}
			Object obj = (Object) eventList.get(position-1);
			if (obj instanceof String) {
				return;
			}

			Intent intent = new Intent(EventListActivity.this,
					EventDetailsActivity.class);
			intent.putExtra("memberList", (Serializable) obj);
			startActivity(intent);
		}
	};
	

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			mListView.onRefreshComplete();
		}
	};

	public OnRefreshListener<ListView> onRefreshListener = new OnRefreshListener<ListView>() {
		@Override
		public void onRefresh(PullToRefreshBase<ListView> refreshView) {
			if(AppUtil.networkAvailable(mContext) ){
				onRefresh = true;
				headerRefresh = true;
				//Toast.makeText(getApplicationContext(), "~", Toast.LENGTH_SHORT).show();
				//new GetData(MainMagActivity.this, 1).execute("");

			}else{
				ToastUtils.showToast(mContext, "到头了");
				mListView.onRefreshComplete();
			}
		}
	};

	private OnLastItemVisibleListener  onLastItemVisibleListener = new OnLastItemVisibleListener() {
		@Override
		public void onLastItemVisible() {
			if(AppUtil.networkAvailable(mContext)){

				headerRefresh = false;
				//Toast.makeText(getApplicationContext(), "到头了，休息一下~", Toast.LENGTH_SHORT).show();

			//	new GetData(MainMagActivity.this, 1).execute("");

			}else{
				ToastUtils.showToast(mContext, "到头了");
			}
		}

		
	};

	OnClickListener reloadClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			//onRefresh = true;
			new GetData(EventListActivity.this, 1).execute("");
		}
	};
	
	class ViewHolder {
		TextView textViewTitle;
		TextView textViewComment;
		TextView TimeDiff;
		ImageView imageView;
	}
	private class EventListAdapter extends BaseAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = new ViewHolder();
			eventModel = (EventsModel) getItem(position);
			final String speakerName = eventModel.getEventDes();
			final String content = eventModel.getEventName();
			final String time =  eventModel.getStartTime();
			final String imageUrl = eventModel.getEventImage();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.comment_list_item, null);      
			viewHolder.textViewTitle = (TextView) convertView
					.findViewById(R.id.speakerName);
			viewHolder.textViewTitle.setText(speakerName);
			
			viewHolder.textViewComment = (TextView) convertView
					.findViewById(R.id.content);
			viewHolder.textViewComment.setText(content);
			
			viewHolder.TimeDiff = (TextView) convertView
					.findViewById(R.id.time);
			viewHolder.TimeDiff.setText(time);
			
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.memberImage);
			
			ImageLoader.getInstance().displayImage(imageUrl,
					viewHolder.imageView, otp);


			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return eventList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
		 return eventList.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}	
}