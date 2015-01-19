package com.app.tomore.ui.usercenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import com.app.tomore.R;
import com.app.tomore.beans.ThreadImageModel;
import com.app.tomore.beans.ThreadModel;
import com.app.tomore.beans.UserModel;
import com.app.tomore.net.ThreadsParse;
import com.app.tomore.net.ThreadsRequest;
import com.app.tomore.net.UserCenterParse;
import com.app.tomore.net.UserCenterRequest;
import com.app.tomore.ui.threads.DialogActivity;
import com.app.tomore.utils.SpUtils;
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

public class UserInformationActivity extends Activity {
	private DialogActivity dialog;
	private String userThreadString = null;
	private String userInforstring = null;
	private String logindInUserId = null;
	private String thisUserId = null;
	private UserModel userInformation;
	private TextView userSchool;
	private TextView userName;
	private ArrayList<ThreadModel> threadList;
	private DisplayImageOptions otp;
	private GridView gridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_infomation);
		logindInUserId = SpUtils.getUserId(UserInformationActivity.this);
		thisUserId = getIntent().getStringExtra("memberId");
		userName = (TextView) findViewById(R.id.username);
		userSchool = (TextView) findViewById(R.id.userschool);
		otp = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).showImageForEmptyUri(R.drawable.ic_launcher)
				.build();
		RelativeLayout rl = (RelativeLayout) getWindow().getDecorView()
				.findViewById(R.id.bar_title_mythread);
		final Button btnBack = (Button) rl
				.findViewById(R.id.bar_title_blocked_go_back);
		gridView = (GridView) findViewById(R.id.gridView);
		TextView titleTextView = (TextView) rl.findViewById(R.id.btBlocked);
		titleTextView.setText("用户中心");

		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		new GetUserInformaitonById(UserInformationActivity.this, 1).execute("");
	}

	private class GetUserThreadList extends AsyncTask<String, String, String> {
		private int mType;

		private GetUserThreadList(Context context, int type) {
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
					UserInformationActivity.this);
			try {
				Log.d("doInBackground", "start request");
				result = request.getThreadListByMemberID(100, 1,
						Integer.parseInt(logindInUserId));
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
				threadList = new ArrayList<ThreadModel>();

				try {
					ThreadsParse threadParse = new ThreadsParse();
					threadList = threadParse
							.getThreadListByMemberIDParse(result);

					if (threadList.size() > 0) {
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
			return threadList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return threadList.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			final ThreadModel item = (ThreadModel) getItem(position);
			ViewHolder viewHolder = null;
			if (convertView != null) {
				viewHolder = (ViewHolder) convertView.getTag();
			} else {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(UserInformationActivity.this)
						.inflate(R.layout.thread_user_item, null);

				viewHolder.ImageView = (ImageView) convertView
						.findViewById(R.id.ItemImage);

				convertView.setTag(viewHolder);
			}
			ArrayList<ThreadImageModel> threadListImage = item
					.getThreadImageList();
			if (threadListImage.size() > 0) {
				ThreadImageModel image = threadListImage.get(0);

				ImageLoader.getInstance().displayImage(image.getImageUrl(),
						viewHolder.ImageView, otp);
			}
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
			}
		});
	}

	private class GetUserInformaitonById extends
			AsyncTask<String, String, String> {
		// private Context mContext;
		private int mType;

		private GetUserInformaitonById(Context context, int type) {
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
			UserCenterRequest request = new UserCenterRequest(
					UserInformationActivity.this);
			try { // public String getThreadListByMemberID(int limit, int page,
					// int memberID)

				Log.d("doInBackground", "start request");
				result = request.getMemberInfoByMemberID(
						Integer.parseInt(thisUserId),
						Integer.parseInt(logindInUserId));
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
				ArrayList<UserModel> userModelList = new ArrayList<UserModel>();

				try {
					UserCenterParse getrequest = new UserCenterParse();
					userModelList = getrequest
							.getMemberInfoByMemberIDParse(result);
					userInformation = new UserModel();
					if (userModelList.size() > 0 || userModelList != null) {
						userInformation = userModelList.get(0);
						userName.setText(userInformation.getAccountName());
						userSchool.setText(userInformation.getSchool());
					}
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}

				new GetUserThreadList(UserInformationActivity.this, 1)
						.execute("");
			}
		}
	}

}
