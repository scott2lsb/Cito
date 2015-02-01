package com.app.tomore.ui.usercenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import com.app.tomore.R;
import com.app.tomore.beans.ThreadImageModel;
import com.app.tomore.beans.ThreadModel;
import com.app.tomore.beans.UserModel;
import com.app.tomore.net.ThreadsParse;
import com.app.tomore.net.ThreadsRequest;
import com.app.tomore.net.UserCenterParse;
import com.app.tomore.net.UserCenterRequest;
import com.app.tomore.ui.mag.MagCommentActivity;
import com.app.tomore.ui.mag.MagDetailActivity;
import com.app.tomore.ui.threads.DialogActivity;
import com.app.tomore.ui.threads.MainDuoliaoActivity;
import com.app.tomore.ui.threads.ThreadReplyActivity;
import com.app.tomore.utils.AndroidShare;
import com.app.tomore.utils.SpUtils;
import com.app.tomore.utils.ToastUtils;
import com.google.gson.JsonSyntaxException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.widget.ImageView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

public class UserInformationActivity extends Activity {
	private DialogActivity dialog;
	private Activity mContext;
	private String followOrUnfollowModelList;
	private String blockList;
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
	private Button commentButton;
	private ArrayList<ThreadModel> threadList;
	private DisplayImageOptions otpPorfileImage;
	private DisplayImageOptions otp;
	private GridView gridView;
	private String memberID; 
	private String viewerID;
	private String followed;
	private AlertDialog alertDialog;
	UserModel usermodel;
	
	private String[] allOptionsMenuTexts = {"加入黑名单","举报该用户"};  
	   private int[] allOptionsMenuOrders = {2,6};  
	   private int[] allOptionsMenuIds = {Menu.FIRST+2,Menu.FIRST+6};  
	   private int[] allOptionsMenuIcons = {   
	        android.R.drawable.ic_menu_edit,  
	        android.R.drawable.ic_menu_send,  
	        }; 
	   
	   

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_infomation);
		usermodel =SpUtils.getUserInformation(UserInformationActivity.this);
		logindInUserId=usermodel.getMemberID();
		//logindInUserId = SpUtils.getUserId(UserInformationActivity.this);
		viewerID =usermodel.getMemberID();
		//viewerID = SpUtils.getUserId(UserInformationActivity.this);
		memberID = getIntent().getStringExtra("memberID");
		//followed= usermodel.getFollowedNum();
//		followed = getIntent().getStringExtra("followed");
		thisUserId = getIntent().getStringExtra("memberID");
		userName = (TextView) findViewById(R.id.tvUserName);
		userSchool = (TextView) findViewById(R.id.tvSchool);
		profileImage = (ImageView) findViewById(R.id.ivProfileImage);
		userGender = (ImageView)findViewById(R.id.ivUserGender);
		btnFollowOrUnfollow = (Button)findViewById(R.id.btnFollow);
		btnPosts = (Button)findViewById(R.id.btnPosts);
		btnFollowing = (Button)findViewById(R.id.btnFollowing);
		btnFollowed = (Button)findViewById(R.id.btnFollowed);
		commentButton = (Button)findViewById(R.id.bar_title_bt_share);
		commentButton.setOnClickListener(new buttonComment());
		otpPorfileImage = new DisplayImageOptions.Builder().displayer(new RoundedBitmapDisplayer(100)).cacheInMemory(true)
				.cacheOnDisk(true).showImageForEmptyUri(R.drawable.ic_launcher)
				.build();
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
		
//		if(followed.equals("0")){
//			btnFollowOrUnfollow.setText("+关注");
//		}else if(followed.equalsIgnoreCase("1")){
//			btnFollowOrUnfollow.setText("取消关注");
//		}
		btnFollowOrUnfollow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new MyFollowOrUnfollow(UserInformationActivity.this, 1).execute(followed, memberID);
			}
		});
		new GetUserInformaitonById(UserInformationActivity.this, 1).execute("");
		
		btnPosts.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						MainFansActivity.class); // should start activity: user posts
				intent.putExtra("memberID", memberID);
				startActivity(intent);				
			}
		});
		
		btnFollowing.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						MainFollowingActivity.class); 
				intent.putExtra("memberID", memberID);
				startActivity(intent);				
			}
		});
		
		btnFollowed.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						MainFansActivity.class); 
				intent.putExtra("memberID", memberID);
				startActivity(intent);				
			}
		});
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
						Integer.parseInt(memberID));
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
						.inflate(R.layout.gridview_item, null);

				viewHolder.ImageView = (ImageView) convertView
						.findViewById(R.id.picture);
				viewHolder.ImageView.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(UserInformationActivity.this,
								ThreadReplyActivity.class);
						intent.putExtra("threadModel", item);
						startActivity(intent);
					}
				});

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
						ImageLoader.getInstance().displayImage(userInformation.getImage(), profileImage, otpPorfileImage);
						if(userInformation.getGender().equals("Male")){
							userGender.setImageResource(R.drawable.male_icon);					
						} else if(userInformation.getGender().equals("Female")){
							userGender.setImageResource(R.drawable.female_icon);						
						}
						followed = userInformation.getFollowed();
						userName.setText(userInformation.getAccountName());
						userSchool.setText(userInformation.getSchool());
						if(followed.equalsIgnoreCase("0")){
							btnFollowOrUnfollow.setText("+关注");
						} else if(followed.equalsIgnoreCase("1")){
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
			String followOrUnfollowMemberID = params[1];
			try {
				String followOrUnfollow = "1";
				if(params[0].equals("0")){
					followOrUnfollow = "1";
				}else if(params[0].equals("1")){
					followOrUnfollow = "0";
				}
				result = request.getFollowOrUnfollowRequest(viewerID, followOrUnfollowMemberID, followOrUnfollow);
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
						if(followed.equals("0")){
							Toast.makeText(getApplicationContext(), "关注成功", 1).show();
							followed = "1";
						}else if(followed.equals("1")){
							Toast.makeText(getApplicationContext(), "取消关注成功", 1).show();
							followed = "0";
						}
						new GetUserInformaitonById(UserInformationActivity.this, 1).execute("");
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
			}
		}		
	}
	
	private class buttonComment implements OnClickListener  
    {  
        public void onClick(View v)  
        {  
        	showDialog8();
        }  
    } 
	
	public void showDialog8(){  
	    final Context context = this;  
	       
	    LayoutInflater layoutInflater = getLayoutInflater();  
	    View menuView = layoutInflater.inflate(R.layout.group_list, null);  
	    
	    GridView gridView = (GridView)menuView.findViewById(R.id.gridview);  
	    SimpleAdapter menuSimpleAdapter = createSimpleAdapter(allOptionsMenuTexts,allOptionsMenuIcons);  
	    gridView.setAdapter(menuSimpleAdapter);  
	    
	    AlertDialog.Builder builder ;
	    builder = new AlertDialog.Builder(context);  
        builder.setView(menuView);  
        //alertDialog = builder.create();  
        alertDialog=builder.show();  
	    gridView.setOnItemClickListener(new OnItemClickListener(){  
	        @Override  
	        public void onItemClick(AdapterView<?> parent, View view,  
	                int position, long id) {  
	        	if(position==0)
	        	{
	        		new MyBlock(UserInformationActivity.this, 1).execute(followed, memberID);
//	        		Intent intent=new Intent(UserInformationActivity.this,ThreadReplyActivity.class);   
//	    			intent.putExtra("articleid", articleItem.getArticleID());
//	                startActivity(intent);  
//	                alertDialog.dismiss();
	                //finish();
	        	}
	        	else if(position==1)
	        	{
	        		//here should be report function!!!
	        		AndroidShare as = new AndroidShare(
	        				UserInformationActivity.this,
	        				"你正在使用多伦多最潮的APP，快来看看吧",
	        				"www.tomoreapp.com");
	        		as.show();
	        		alertDialog.dismiss();
//	        		mController.getConfig().removePlatform(SHARE_MEDIA.RENREN,
//	    					SHARE_MEDIA.DOUBAN);
//	    			//Ĭ�Ϸ��?ʽ
//	    			mController.openShare(MagDetailActivity.this, null);
	        				
	        	}

	        }  
	    });  
	   // new AlertDialog.Builder(context).setView(menuView).show();  
	}
	
	public SimpleAdapter createSimpleAdapter(String[] menuNames,int[] menuImages){  
	    List<Map<String,?>> data = new ArrayList<Map<String,?>>();  
	    String[] fromsAdapter = {"item_text","item_image"};  
	    int[] tosAdapter = {R.id.item_text,R.id.item_image};  
	    for(int i=0;i<menuNames.length;i++){  
	        Map<String,Object> map = new HashMap<String,Object>();  
	        map.put(fromsAdapter[0], menuNames[i]);  
	        map.put(fromsAdapter[1], menuImages[i]);  
	        data.add(map);  
	    }  
	      
	    SimpleAdapter SimpleAdapter = new SimpleAdapter(this, data, R.layout.group_item_view, fromsAdapter, tosAdapter);  
	    return SimpleAdapter;  
	}  
	
	private class MyBlock extends AsyncTask<String, String, String> {
		private int mType;
		
		private MyBlock(Context context, int type) {
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
			String blockMemberID = params[1];
			try {
				String block = "1";
				result = request.getBlockOrUnblockRequest(viewerID, blockMemberID, block);
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
				if(blockList!=null && !blockList.equals(""))
				{
					blockList = new String();
//					Toast.makeText(getApplicationContext(), "操作失败", 1).show();
				}
				else
					blockList = new String();
				try {
					blockList = new UserCenterParse().parseBlockOrUnblockwResponse(result);
							Toast.makeText(getApplicationContext(), "加入黑名单", 1).show();
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
			}
		}		
	}
}
