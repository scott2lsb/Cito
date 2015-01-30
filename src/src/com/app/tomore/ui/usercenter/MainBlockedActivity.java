package com.app.tomore.ui.usercenter;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import com.app.tomore.R;
import com.app.tomore.R.drawable;
import com.app.tomore.R.id;
import com.app.tomore.R.layout;
import com.app.tomore.beans.BlockedModel;
import com.app.tomore.beans.FansModel;
import com.app.tomore.net.UserCenterParse;
import com.app.tomore.net.UserCenterRequest;
import com.app.tomore.ui.threads.DialogActivity;
import com.app.tomore.ui.usercenter.MainFansActivity.FollowingAdapter;
import com.app.tomore.ui.usercenter.MainFansActivity.ViewHolder;
import com.app.tomore.ui.yellowpage.GeneralBLDetailActivity;
import com.app.tomore.utils.AppUtil;
import com.app.tomore.utils.PullToRefreshBase;
import com.app.tomore.utils.PullToRefreshListView;
import com.app.tomore.utils.SpUtils;
import com.app.tomore.utils.ToastUtils;
import com.app.tomore.utils.PullToRefreshBase.OnLastItemVisibleListener;
import com.app.tomore.utils.PullToRefreshBase.OnRefreshListener;
import com.google.gson.JsonSyntaxException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainBlockedActivity extends Activity {

	private DialogActivity dialog;
	private Activity mContext;
	private ArrayList<BlockedModel> blockedList;
	private String blockOrUnblockItem;
	private FansModel fansItem;
	private DisplayImageOptions otp;
	BlockedAdapter blockedListAdapter;
	private PullToRefreshListView mListView;
	private boolean onRefresh = false;
	private boolean headerRefresh = false; // false -> footer
	private int pageNumber;
	private int limit;
	private TextView noneData;
	private View no_net_lay;
	private LayoutInflater inflater; 
	private View layout;
	private Bitmap bitmap;
	private Button btnUbBlock;
	private String memberID;
	private String viewerID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_blocked);
		getWindow().getDecorView().setBackgroundColor(Color.WHITE);
		viewerID = SpUtils.getUserId(MainBlockedActivity.this);
		memberID = getIntent().getStringExtra("memberID");
		if(memberID == null){
			memberID = viewerID;
		}	
		mContext = this;
		limit = 20;
		pageNumber = 1;
		mListView = (PullToRefreshListView) findViewById(R.id.list);
		mListView.setOnRefreshListener(onRefreshListener);
		mListView.setOnLastItemVisibleListener(onLastItemVisibleListener);
		mListView.setOnItemClickListener(itemClickListener);
		noneData = (TextView) findViewById(R.id.noneData);
		no_net_lay = findViewById(R.id.no_net_lay);
		inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = findViewById(R.id.MainBlockedLayout);
		otp = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).showImageForEmptyUri(R.drawable.ic_launcher)
				.build();
		TextView header_Text = (TextView) layout.findViewById(R.id.btBlocked);
//		header_Text.setText(name);
		final Button btnBack = (Button) layout.findViewById(R.id.bar_title_blocked_go_back);

		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		new MyBlocked(MainBlockedActivity.this, 1).execute("");
	}
	
	private class MyBlocked extends AsyncTask<String, String, String> {
		private int mType;

		private MyBlocked(Context context, int type) {
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
			UserCenterRequest request = new UserCenterRequest(MainBlockedActivity.this);
			//memberID, viewerID, limit, page
			String sLimite = Integer.toString(limit);
			String sPageNumber = Integer.toString(pageNumber);
			try {
				result = request.getBlockedRequest(viewerID, sLimite, sPageNumber);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
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
				
				if(blockedList!=null && blockedList.size()!=0)
				{
					if(headerRefresh)
						blockedList = new ArrayList<BlockedModel>();
				}
				else
					blockedList = new ArrayList<BlockedModel>();
				try {
					if(headerRefresh)
						blockedList = new UserCenterParse().parseBlockedResponse(result);
					else
					{
						blockedList.addAll(new UserCenterParse().parseBlockedResponse(result));
					}
					BindDataToListView();
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
			}
		}		
	}
	
	private void BindDataToListView(){
		if (onRefresh) {
			onRefresh = false;
		}
		if (blockedListAdapter == null) {
			blockedListAdapter = new BlockedAdapter();
			mListView.setAdapter(blockedListAdapter);
			blockedListAdapter.notifyDataSetChanged();
		} else {
			blockedListAdapter.notifyDataSetChanged();
		}
		if (blockedList != null && blockedList.size() > 0) {
			showDataUi();
		} else {
			showNoDataUi();
		}
	}
		
	void showDataUi() {
		mListView.setVisibility(View.VISIBLE);
		noneData.setVisibility(View.GONE);
		no_net_lay.setVisibility(View.GONE);
	}

	void showNoDataUi() {
		mListView.setVisibility(View.GONE);
		noneData.setVisibility(View.VISIBLE);
		no_net_lay.setVisibility(View.GONE);
	}
	
	public class BlockedAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return blockedList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return blockedList.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final BlockedModel blockedText = (BlockedModel) getItem(position);
			ViewHolder viewHolder = null;
			if (convertView != null) {
				viewHolder = (ViewHolder) convertView.getTag();
			} else {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.main_blocked_text, null);
				viewHolder.MemberImage = (ImageView) convertView.findViewById(R.id.MemberImage);
				viewHolder.AccountName = (TextView) convertView.findViewById(R.id.AccountName);
				btnUbBlock = (Button) convertView.findViewById(R.id.UnBlock);
				convertView.setTag(viewHolder);
			}
			
			ImageLoader.getInstance().displayImage(blockedText.getMemberImage(), viewHolder.MemberImage, otp);
			viewHolder.AccountName.setText(blockedText.getAccountName());
			viewHolder.MemberImage.setTag(blockedText.getMemberID());
			viewHolder.MemberImage.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					String viewMemberID = (String)v.getTag();
					Intent intent = new Intent(getApplicationContext(),
							UserInformationActivity.class);
					intent.putExtra("memberID", viewMemberID);
					intent.putExtra("followed", "0");
					startActivity(intent);					
				}
			});
			btnUbBlock.setText("移除黑名单");
			btnUbBlock.setTag(blockedText.getMemberID());
			btnUbBlock.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String blockOrUnblockMemberID = (String)v.getTag();
					new blockOrUnblock(MainBlockedActivity.this, 1).execute(blockOrUnblockMemberID);
					
				}
			});
			return convertView;
		}
	}
	
	class ViewHolder {
		ImageView MemberImage;
	    TextView AccountName;
	    Button UnBlock;
	}
	
	private class blockOrUnblock extends AsyncTask<String, String, String> {
		private int mType;

		private blockOrUnblock(Context context, int type) {
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
			String blockOrUnblockMemberID = params[0];
			UserCenterRequest request = new UserCenterRequest(MainBlockedActivity.this);
			try {
				result = request.getBlockOrUnblockRequest(viewerID, blockOrUnblockMemberID, "0");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return result;	
		}
		
		@Override
		protected void onPostExecute(String result) {
			if (null != dialog) {
				dialog.dismiss();
			}
			if (result == null || result.equals("")) {
				ToastUtils.showToast(mContext, "列表为空");
			} else {				
				if(blockOrUnblockItem!=null && !blockOrUnblockItem.equals(""))
				{
					blockOrUnblockItem = new String();
				}
				else
					blockOrUnblockItem = new String();
				try {
					blockOrUnblockItem = new UserCenterParse().parseBlockOrUnblockwResponse(result);
//						if(blockOrUnblockItem.toString().equals("0 row(s) exist before command")){
//							Toast.makeText(getApplicationContext(), "加入黑名单", 1).show();
//						}else if(blockOrUnblockItem.toString().equals("1 row(s) exist before command")){
							Toast.makeText(getApplicationContext(), "移除黑名单成功", 1).show();
//						}
						mListView.setOnRefreshListener(onRefreshListener);
						new MyBlocked(MainBlockedActivity.this, 1).execute("");
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
			}
		}	
	}
	
	public void onUnBlockClick(View view){
		Toast.makeText(getApplicationContext(), "unBlock", 1).show();
	}
	
	public OnRefreshListener<ListView> onRefreshListener = new OnRefreshListener<ListView>() {
		@Override
		public void onRefresh(PullToRefreshBase<ListView> refreshView) {
			if (AppUtil.networkAvailable(mContext)) {
				headerRefresh = true;
				onRefresh = true;
				pageNumber = 1;
				new MyBlocked(MainBlockedActivity.this, 1).execute("");
			} else {
				ToastUtils.showToast(mContext, "到头了");
				mListView.onRefreshComplete();
			}
		}
	};
	
	private OnLastItemVisibleListener onLastItemVisibleListener = new OnLastItemVisibleListener() {
		@Override
		public void onLastItemVisible() {
			if (AppUtil.networkAvailable(mContext)) {
				headerRefresh = false;
				pageNumber ++;
				new MyBlocked(MainBlockedActivity.this, 1).execute("");
			} else {
				ToastUtils.showToast(mContext, "到头了");
			}
		}
	};
	
	private OnItemClickListener itemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (!AppUtil.networkAvailable(mContext)) {
				ToastUtils.showToast(mContext, "列表为空");
				return;
			}
			if (blockedList == null) {
				return;
			}
			Object obj = (Object) blockedList.get(position - 1);
			if (obj instanceof String) {
				return;
			}
			Open_Activity(position-1);
		}
	};
	
	private void Open_Activity(int position) {
		Intent intent;
		intent = new Intent(MainBlockedActivity.this,
				GeneralBLDetailActivity.class); // Should open fans detail activity
		intent.putExtra("BlockedData", (Serializable)blockedList.get(position));
		startActivityForResult(intent, 100);
	}
}
