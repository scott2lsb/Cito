package com.app.tomore.ui.threads;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import com.app.tomore.R;
import com.app.tomore.beans.CommonModel;
import com.app.tomore.beans.ThreadCmtModel;
import com.app.tomore.beans.ThreadModel;
import com.app.tomore.fragment.BackToMainActivity;
import com.app.tomore.net.ThreadsParse;
import com.app.tomore.net.ThreadsRequest;
import com.app.tomore.net.UserCenterRequest;
import com.app.tomore.ui.member.MainMemActivity;
import com.app.tomore.ui.usercenter.AboutusActivity;
import com.app.tomore.ui.usercenter.LoginActivity;
import com.app.tomore.ui.usercenter.MainBlockedActivity;
import com.app.tomore.ui.usercenter.MainFansActivity;
import com.app.tomore.ui.usercenter.MainFollowingActivity;
import com.app.tomore.ui.usercenter.MyReplyListActivity;
import com.app.tomore.ui.usercenter.UserInformationActivity;
import com.app.tomore.utils.AppUtil;
import com.app.tomore.utils.ExpandedListView;
import com.app.tomore.utils.PullToRefreshBase;
import com.app.tomore.utils.PullToRefreshListView;
import com.app.tomore.utils.SpUtils;
import com.app.tomore.utils.ToastUtils;
import com.app.tomore.utils.PullToRefreshBase.OnLastItemVisibleListener;
import com.app.tomore.utils.PullToRefreshBase.OnRefreshListener;
import com.google.gson.JsonSyntaxException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainDuoliaoActivity extends Activity implements OnClickListener {
	private TextView bt1;
	private TextView bt2;
	private TextView bt3;
	private TextView bt4, bt5, bt6, bt7,bt8;
	private Context context;
	private ImageButton menubtn;
	private ImageButton rightBtn;
	private ImageView headView;
	private SlidingMenu menu;
	private Activity mContext;
	private DialogActivity dialog;
	private boolean headerRefresh;
	private ArrayList<ThreadModel> threadList;
	//private ArrayList<ThreadCmtModel> commentList;
	private int pageNumber = 1;
	private int limit = 20;
	private PullToRefreshListView mListView;
	DuoliaoAdapter duoliaoAdapter;
	private boolean onRefresh = false;
	private DisplayImageOptions otp;
	protected ImageLoader imageLoader;
	private Bitmap head;
	private static String path="/sdcard/myHead/";//sd路径

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_duoliao_activity);
		mContext = this;
		getWindow().getDecorView().setBackgroundColor(Color.WHITE);

		context = this;
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		View view = LayoutInflater.from(this).inflate(
				R.layout.main_left_fragment, null);
		menu.setMenu(view);
		bt1 = (TextView) view.findViewById(R.id.my_backtomain_bt);
		bt2 = (TextView) view.findViewById(R.id.my_tiezi_bt);
		bt3 = (TextView) view.findViewById(R.id.my_guanzhu_bt);
		bt4 = (TextView) view.findViewById(R.id.my_fensi_bt);
		bt5 = (TextView) view.findViewById(R.id.my_blacklist_bt);
		bt6 = (TextView) view.findViewById(R.id.my_aboutus_bt);
		bt7 = (TextView) view.findViewById(R.id.my_logout_bt);
		bt8= (TextView) view.findViewById(R.id.my_reply_bt);
		menubtn = (ImageButton) findViewById(R.id.ivTitleBtnLeft);
		rightBtn = (ImageButton) findViewById(R.id.ivTitleBtnRigh);
		headView = (ImageView) findViewById(R.id.head_view);
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
		bt4.setOnClickListener(this);
		bt5.setOnClickListener(this);
		bt6.setOnClickListener(this);
		bt7.setOnClickListener(this);

		bt8.setOnClickListener(this);

		headView.setOnClickListener(this);

		menubtn.setOnClickListener(this);
		mListView = (PullToRefreshListView) findViewById(R.id.threadlist);
		new GetData(MainDuoliaoActivity.this, 1).execute("");
		otp = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).showImageForEmptyUri(R.drawable.ic_launcher)
				.build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		
		
		rightBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//goto even list first
				Intent intent = new Intent(MainDuoliaoActivity.this,
		    			EventListActivity.class);
				startActivity(intent);
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		BackToMainActivity newContent = null;
		int id = v.getId();
		if (id == R.id.my_backtomain_bt) {
			newContent = new BackToMainActivity();
		}else if(id == R.id.my_reply_bt){
			onMyreply(v);
		} 
		else if (id == R.id.my_tiezi_bt) {
			onMyThreadlist(v);
		} else if (id == R.id.my_guanzhu_bt) {
			onMyFollowingClick(v);
//			Toast.makeText(context, "����3", 1).show();
		} else if (id == R.id.ivTitleBtnLeft) {
			menu.toggle();
		} else if (id == R.id.my_fensi_bt) {
			onMyFansClick(v);
//			Toast.makeText(context, "����1", 1).show();
		} else if (id == R.id.my_blacklist_bt) {
			onMyBlockedClick(v);
//			Toast.makeText(context, "����1", 1).show();
		} else if (id == R.id.my_aboutus_bt) {
			onAboutUSClick(v);
//			Toast.makeText(context, "����1", 1).show();
		}else if (id == R.id.my_logout_bt) {
			onLogoutClick(v);
		}
		else if(id == R.id.head_view){
			OnAvatarClick(v);
		}

	}


	private void OnAvatarClick(View view) {
		// TODO Auto-generated method stub
		Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");//从Sd中找头像，转换成Bitmap
		if(bt!=null){
			@SuppressWarnings("deprecation")
			Drawable drawable = new BitmapDrawable(bt);//转换成drawable
			headView.setImageDrawable(drawable);
		}else{
			/**
			 *	如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
			 * 
			 */
		}
		String Cancel = getString(R.string.Cancel);
		String TakePhoto = getString(R.string.TakePhoto);
		String FromAlbum = getString(R.string.FromAlbum);
    	CharSequence [] options = {TakePhoto,FromAlbum,Cancel};
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.ChangeAvatar));
		builder.setItems(options, new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface Optiondialog, int which) {
		        if (which == 0){
					Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
									"head.jpg")));
					startActivityForResult(intent1, 2);//采用ForResult打
		        }
		        else if(which == 1){
					Intent intent2 = new Intent(Intent.ACTION_PICK, null);
					intent2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
					startActivityForResult(intent2, 1);
		        }
		    }
		});
		builder.show();
    	
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				cropPhoto(data.getData());//裁剪图片
			}

			break;
		case 2:
			if (resultCode == RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/head.jpg");
				cropPhoto(Uri.fromFile(temp));//裁剪图片
			}

			break;
		case 3:
			if (data != null) {
				Bundle extras = data.getExtras();
				head = extras.getParcelable("data");
				if(head!=null){
					/**
					 * 上传服务器代码
					 */
					setPicToView(head);//保存在SD卡中
					headView.setImageBitmap(head);//用ImageView显示出来
				}
			}
			break;
		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	};
	/**
	 * 调用系统的裁剪
	 * @param uri
	 */
	public void cropPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		 // aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}
	private void setPicToView(Bitmap mBitmap) {
		new UpdateAvatar(MainDuoliaoActivity.this, 1,mBitmap).execute("");
		 String sdStatus = Environment.getExternalStorageState();  
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { 
               return;  
           }  
		FileOutputStream b = null;
		File file = new File(path);
		file.mkdirs();// 创建文件夹
		String fileName =path + "head.jpg";//图片名字
		try {
			b = new FileOutputStream(fileName);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				//关闭流
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
	private void onAboutUSClick(View v) {
		Intent intent = new Intent(this, AboutusActivity.class);
		startActivity(intent);
	}
	public void onMyreply(View view){
		Intent intent= new Intent(this,MyReplyListActivity.class);
		startActivity(intent);
	}
	public void onMyThreadlist(View view) {

		Intent intent = new Intent(this, MyThreadActivity.class);
		startActivity(intent);
	}

	public void onLogoutClick(View view) {

		SpUtils.clearUserInfo(mContext);//退出登录
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}

	public void onMyFansClick(View view) {
		Intent intent = new Intent(this, MainFansActivity.class);
		startActivity(intent);
	}
	
	public void onMyFollowingClick(View view) {
		Intent intent = new Intent(this, MainFollowingActivity.class);
		startActivity(intent);
	}
	
	public void onMyBlockedClick(View view) {
		Intent intent = new Intent(this, MainBlockedActivity.class);
		startActivity(intent);
	}
	
	private class UpdateAvatar extends AsyncTask<String, String, String> {
		// private Context mContext;
		private int mType;
		private Bitmap bitmap;
		private UpdateAvatar(Context context, int type,Bitmap newbitmap) {
			// this.mContext = context;
			this.mType = type;
			bitmap = newbitmap;
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
					MainDuoliaoActivity.this);
				Log.d("doInBackground", "start request");
				//result = request.updateUserProfile(bitmap,"1"); //for
																//test
				Log.d("doInBackground", "returned");


			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (null != dialog) {
				dialog.dismiss();
			}			Log.d("onPostExecute", "postExec state");
			if (result == null || result.equals("")) {
				// show empty alert
			} 
		}
	}


	private class GetData extends AsyncTask<String, String, String> {
		// private Context mContext;
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
			String result = null;
			ThreadsRequest request = new ThreadsRequest(
					MainDuoliaoActivity.this);
			try {
				Log.d("doInBackground", "start request");
				result = request.getThreadList(pageNumber, limit, 25, 0);// for
																			// test
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
				// show empty alert
			} else {

				if (threadList != null && threadList.size() != 0) {
					if (headerRefresh)
						threadList = new ArrayList<ThreadModel>();
				} else
					threadList = new ArrayList<ThreadModel>();
				try {
					if (headerRefresh)
						threadList = new ThreadsParse()
								.parseThreadModel(result);
					else {
						threadList.addAll(new ThreadsParse()
								.parseThreadModel(result));
					}
					BindDataToListView();
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	

	private void BindDataToListView() {
		if (onRefresh) {
			onRefresh = false;
		}
		if (duoliaoAdapter == null) {
			duoliaoAdapter = new DuoliaoAdapter();
			mListView.setAdapter(duoliaoAdapter);
		} else {
			duoliaoAdapter.notifyDataSetChanged();
		}
	}

	private class DuoliaoAdapter extends BaseAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ThreadModel threadItem = (ThreadModel) getItem(position);
			ViewHolder viewHolder = null;
			if (convertView != null) {
				viewHolder = (ViewHolder) convertView.getTag();
			} else {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.duoliao_listview_item, null);
				viewHolder.account_name = (TextView) convertView
						.findViewById(R.id.account_name);
				viewHolder.content = (TextView) convertView
						.findViewById(R.id.content);
				viewHolder.time = (TextView) convertView
						.findViewById(R.id.time);
				viewHolder.comment_num = (TextView) convertView
						.findViewById(R.id.comment_num);
				viewHolder.like_num = (TextView) convertView
						.findViewById(R.id.like_num);
				viewHolder.avatar = (ImageView) convertView
						.findViewById(R.id.avatar);
				viewHolder.content_img = (ImageView) convertView
						.findViewById(R.id.content_img);
//				viewHolder.liker_img1 = (ImageView) convertView
//						.findViewById(R.id.liker_img1);
//				viewHolder.liker_img2 = (ImageView) convertView
//						.findViewById(R.id.liker_img2);
//				viewHolder.liker_img3 = (ImageView) convertView
//						.findViewById(R.id.liker_img3);
				viewHolder.comment_listview = (ExpandedListView) convertView
						.findViewById(R.id.comment_listview);
				
				
				viewHolder.comment_img1 = (ImageView) convertView
						.findViewById(R.id.comment_img);
				viewHolder.comment_img2 = (ImageView) convertView
						.findViewById(R.id.comment_img2);
				viewHolder.like_img1 = (ImageView) convertView
						.findViewById(R.id.like_img);
				viewHolder.like_img2 = (ImageView) convertView
						.findViewById(R.id.like_img2);
			
				convertView.setTag(viewHolder);
			}
			viewHolder.content_img.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View v) {
			        //
			    	//
			    	Intent intent = new Intent(MainDuoliaoActivity.this,
							ThreadReplyActivity.class);
					intent.putExtra("threadModel", threadItem);
					startActivity(intent);
			    }
			});

			viewHolder.avatar.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View v) {

			    	Intent intent = new Intent(MainDuoliaoActivity.this,
			    			UserInformationActivity.class);
					intent.putExtra("memberId", threadItem.getMemberID());
					startActivity(intent);
			    }
			});
			
			viewHolder.comment_img1.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View v) {
			    	Intent intent = new Intent(MainDuoliaoActivity.this,
							ThreadReplyActivity.class);
					intent.putExtra("threadModel", threadItem);
					startActivity(intent);
			    }
			});

			viewHolder.comment_img2.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View v) {
			    	Intent intent = new Intent(MainDuoliaoActivity.this,
							ThreadReplyActivity.class);
					intent.putExtra("threadModel", threadItem);
					startActivity(intent);
			    }
			});
			
			viewHolder.like_img1.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View v) {
			    	new LikeOrUnLike(MainDuoliaoActivity.this, 1,threadItem).execute("");
						//new GetData(MainDuoliaoActivity.this, 1).execute("");
			    }
			});
			
			imageLoader.displayImage(threadItem.getMemberImage(),
					viewHolder.avatar, otp);

			//get screen width
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int screenWidth = size.x;
			// change size of content image
			LayoutParams params = (LayoutParams) viewHolder.content_img
					.getLayoutParams();
			params.width = screenWidth;
			params.height = (screenWidth/Integer.parseInt(threadItem.getThreadImageList().get(0).getImageWidth()))
					*Integer.parseInt(threadItem.getThreadImageList().get(0).getImageHeight());
			//params.height = (int) Math.round(Integer.parseInt(threadItem
			//		.getThreadImageList().get(0).getImageHeight()) * 2.5);
			viewHolder.content_img.setLayoutParams(params);
			//

			imageLoader.displayImage(threadItem.getThreadImageList().get(0)
					.getImageUrl(), viewHolder.content_img, otp);
			viewHolder.account_name.setText(threadItem.getAccountName());
			viewHolder.comment_num.setText(String.valueOf(threadItem
					.getThreadCmtList().size()));
			viewHolder.content.setText(threadItem.getThreadContent());
			viewHolder.like_num.setText(String.valueOf(threadItem
					.getThreadLikeList().size()));
			viewHolder.time.setText(threadItem.getTimeDiff());
			
			if(!threadItem.getThreadCmtList().isEmpty())
			{
				ArrayList<ThreadCmtModel> commentList = new ArrayList<ThreadCmtModel>();
				commentList = threadItem.getThreadCmtList();
				
	
				DuoliaoCommentAdapter duoliaoCommentAdapter = new DuoliaoCommentAdapter(commentList);
				viewHolder.comment_listview.setAdapter(duoliaoCommentAdapter);
				duoliaoCommentAdapter.notifyDataSetChanged();
				viewHolder.comment_img2.setVisibility(View.VISIBLE);
			}
			else
			{
				viewHolder.comment_img2.setVisibility(View.GONE);
			}
			
			if(threadItem.getThreadLikeList().isEmpty())
			{
				viewHolder.like_img2.setVisibility(View.GONE);
			}
			else
			{
				viewHolder.like_img2.setVisibility(View.VISIBLE);
				//display liker list view
			}
			
			return convertView;
		}

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
	}

	private class DuoliaoCommentAdapter extends BaseAdapter {
		
		private ArrayList<ThreadCmtModel> commentList;
		public DuoliaoCommentAdapter(ArrayList<ThreadCmtModel> list)
		{
			super();
			commentList = list;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ThreadCmtModel commentItem = (ThreadCmtModel) getItem(position);
			ViewHolder viewHolder = null;
			if (convertView != null) {
				viewHolder = (ViewHolder) convertView.getTag();
			} else {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.duoliao_listview_item_comment_listview_item,
						null);
				viewHolder.comment_accountName = (TextView) convertView
						.findViewById(R.id.account_name);
				viewHolder.comment_content = (TextView) convertView
						.findViewById(R.id.content);
				viewHolder.comment_avatar = (ImageView) convertView
						.findViewById(R.id.avatar);
				convertView.setTag(viewHolder);
			}
			imageLoader.displayImage(commentItem.getMemberImage(),
					viewHolder.comment_avatar, otp);

			viewHolder.comment_accountName
					.setText(commentItem.getAccountName());
			viewHolder.comment_content.setText(commentItem.getCommentContent());
			
			// change size of comment list view
//			int commentHeight;
//			commentHeight = viewHolder.comment_content.getHeight()+viewHolder.comment_accountName.getHeight();
//			LayoutParams params2 = (LayoutParams) ((ListView)convertView
//					.findViewById(R.id.comment_listview)).getLayoutParams();
//			params2.height += commentHeight;
//			viewHolder.comment_listview.setLayoutParams(params2);
			//

			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return commentList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return commentList.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	class ViewHolder {
		TextView account_name;
		TextView content;
		TextView time;
		TextView comment_num;
		TextView like_num;
		ImageView avatar;
		ImageView content_img;
		ImageView liker_img1;
		ImageView liker_img2;
		ImageView liker_img3;
		ImageView line;
		ExpandedListView comment_listview;
		ImageView comment_avatar;
		TextView comment_accountName;
		TextView comment_content;
		ImageView comment_img1;
		ImageView comment_img2;
		ImageView like_img1;
		ImageView like_img2;
		
	}
	
	public OnRefreshListener<ListView> onRefreshListener = new OnRefreshListener<ListView>() {
		@Override
		public void onRefresh(PullToRefreshBase<ListView> refreshView) {
			if (AppUtil.networkAvailable(mContext)) {
				onRefresh = true;
				new GetData(MainDuoliaoActivity.this, 1).execute("");
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
				// new GetData(MainMemActivity.this, 1).execute("");
			} else {
				ToastUtils.showToast(mContext, "到头了");
			}
		}
	};

	OnClickListener reloadClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			onRefresh = true;

			new GetData(MainDuoliaoActivity.this, 1).execute("");
		}
	};


	private class LikeOrUnLike extends AsyncTask<String, String, String> {
		// private Context mContext;
		private int mType;
		private ThreadModel threadItem;
		private LikeOrUnLike(Context context, int type,ThreadModel threadItemPara) {
			// this.mContext = context;
			this.mType = type;
			dialog = new DialogActivity(context, type);
			threadItem = threadItemPara;
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
			String  memberID="34"; //for test
	    	String accountName="NeoWu"; // for test
			String result = null;
	    	int isLike=1;
	    	for(int i =0; i<threadItem.getThreadLikeList().size(); i++)
	    	{
	    		String likerID = threadItem.getThreadLikeList().get(i).getMemberID();
	    		if(likerID.equals(memberID))
	    		{
	    			isLike = 0;
	    		}
	    	}
			ThreadsRequest request = new ThreadsRequest(
					MainDuoliaoActivity.this);
			try {
				Log.d("doInBackground", "start request");
				result = request.likeOrUnLikeAThread(memberID, threadItem.getThreadID(), accountName, isLike);
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
			} 
			else {
				    //showAlert(new ThreadsParse().likeOrUnLikeAThreadParse(result).getResult());
				
			}
			}
		}

	/**
	 * Method to show alert dialog
	 * */
	private void showAlert(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message).setCancelable(false)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// do nothing
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
}
