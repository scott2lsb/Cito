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
import com.app.tomore.ui.usercenter.MainFansActivity.ViewHolder;
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
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;

public class UserInformationActivity extends Activity {
	private DialogActivity dialog;
	private Activity mContext;
	private String followOrUnfollowModelList;
	private String userThreadString = null;
	private String userInforstring = null;
	private String logindInUserId = null;
	private String thisUserId = null;
	private UserModel userInformation;
	private TextView userSchool;
	private TextView userName;
	private ImageView profileImage;
	private ImageView userGender;
	private Button btnFollowOrUnfollow;
	private Button btnPosts;
	private Button btnFollowing;
	private Button btnFollowed;
	private ArrayList<ThreadModel> threadList;
	private DisplayImageOptions otp;
	private GridView gridView;
	private String memberID; // not Initialized yet!!!
	private String viewerID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_infomation);
		logindInUserId = SpUtils.getUserId(UserInformationActivity.this);
		viewerID = SpUtils.getUserId(UserInformationActivity.this);
		thisUserId = getIntent().getStringExtra("memberId");
		userName = (TextView) findViewById(R.id.tvUserName);
		userSchool = (TextView) findViewById(R.id.tvSchool);
		profileImage = (ImageView) findViewById(R.id.ivProfileImage);
		userGender = (ImageView)findViewById(R.id.ivUserGender);
		btnFollowOrUnfollow = (Button)findViewById(R.id.btnFollow);
		btnPosts = (Button)findViewById(R.id.btnPosts);
		btnFollowing = (Button)findViewById(R.id.btnFollowing);
		btnFollowed = (Button)findViewById(R.id.btnFollowed);
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
		
		btnFollowOrUnfollow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new MyFollowOrUnfollow(UserInformationActivity.this, 1).execute("");
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
//						userGender = userInformation.getGender();
						
						ImageLoader.getInstance().displayImage(userInformation.getProfileImage(), profileImage, otp);
						if(userInformation.getGender().equals("Male")){
							ImageLoader.getInstance().displayImage("@drawable/male_icon.png", userGender, otp);							
						} else if(userInformation.getGender().equals("Female")){
							ImageLoader.getInstance().displayImage("@drawable/fmale_icon.png", userGender, otp);							
						}
						userName.setText(userInformation.getAccountName());
						userSchool.setText(userInformation.getSchool());
						if(userInformation.getStatus().equalsIgnoreCase("0")){
							btnFollowOrUnfollow.setText("+关注");
						} else if(userInformation.getStatus().equalsIgnoreCase("1")){
							btnFollowOrUnfollow.setText("取消关注");
						}
						btnPosts.setText("发帖\n" + userInformation.getTotalThread());
						btnFollowing.setText("关注\n" + userInformation.getFollowingNum());
						btnFollowed.setText("粉丝\n" + userInformation.getFollowedNum());
					}
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}

				new GetUserThreadList(UserInformationActivity.this, 1)
						.execute("");
			}
		}
	}

	private class MyFollowOrUnfollow extends AsyncTask<String, String, String> {
		private int mType;

		private MyFollowOrUnfollow(Context context, int type) {
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
			UserCenterRequest request = new UserCenterRequest(UserInformationActivity.this);
			try {
				String followOrUnfollow = "1";
				if(params[0].equals("0")){
					followOrUnfollow = "1";
				}else if(params[0].equals("1")){
					followOrUnfollow = "0";
				}
				System.out.println("followRequest: " + followOrUnfollow);
				result = request.getFollowOrUnfollowRequest(viewerID, memberID, followOrUnfollow);
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
			if (result == null || result.equals("")) {
				ToastUtils.showToast(mContext, "列表为空");
			} else {				
				if(followOrUnfollowModelList!=null && !followOrUnfollowModelList.equals(""))
				{
					followOrUnfollowModelList = new String();
//					Toast.makeText(getApplicationContext(), "操作失败", 1).show();
				}
				else
					followOrUnfollowModelList = new String();
				try {
						followOrUnfollowModelList = new UserCenterParse().parseFollowOrUnfollowResponse(result);
						System.out.println("followOrUnfollowModelList: " + followOrUnfollowModelList);
						if(followOrUnfollowModelList.toString().equals("0 row(s) exist before command")){
							Toast.makeText(getApplicationContext(), "关注成功", 1).show();
						}else if(followOrUnfollowModelList.toString().equals("1 row(s) exist before command")){
							Toast.makeText(getApplicationContext(), "取消关注成功", 1).show();
						}
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
			}
		}		
	}
}
