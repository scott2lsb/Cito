package com.app.tomore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import com.app.tomore.beans.GeneralBLModel;
import com.app.tomore.net.ThreadsRequest;
import com.app.tomore.net.YellowPageParse;
import com.app.tomore.net.YellowPageRequest;
import com.app.tomore.ui.threads.DialogActivity;
import com.app.tomore.ui.yellowpage.GeneralBLActivity;
import com.google.gson.JsonSyntaxException;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class MyCameraActivity extends Activity implements OnClickListener {
	private ImageView posting_image;
	private Bitmap image;
	private static String path="/sdcard/postingimage/";//sd路径
	private View layout;
	private boolean webchatOnclick = false;
	private boolean qqOnclick = false;
	private Context mcontext;
	private boolean send = false;
	final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	private DialogActivity dialog;
	File shareimage;;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
						"postimage.jpg")));
		startActivityForResult(intent1, 2);//采用ForResult打
		mcontext = this;
		setContentView(R.layout.posting_activity_layout);
		getWindow().getDecorView().setBackgroundColor(Color.WHITE);
		posting_image = (ImageView) findViewById(R.id.posting_image);
		layout = findViewById(R.id.posting_layout);
		posting_image.setOnClickListener(this);
		final Button btnBack = (Button) layout.findViewById(R.id.bar_title_league_go_back);
		TextView title = (TextView) layout.findViewById(R.id.btLeague);
		title.setText(getString(R.string.posting));
		TextView send = (TextView) layout.findViewById(R.id.submit_button);
		send.setText(getString(R.string.sendthread));
		btnBack.setVisibility(View.GONE);
		BindData();
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.posting_image){
			MakePostingimage();
		}

	}
	
	private void MakePostingimage() {
		// TODO Auto-generated method stub
		Bitmap bt = BitmapFactory.decodeFile(path + "postimage.jpg");//从Sd中找头像，转换成Bitmap
		if(bt!=null){
			@SuppressWarnings("deprecation")
			Drawable drawable = new BitmapDrawable(bt);//转换成drawable
			//posting_image.setImageDrawable(drawable);
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
									"postimage.jpg")));
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
				send = true;
			}

			break;
		case 2:
			if (resultCode == RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/postimage.jpg");
				cropPhoto(Uri.fromFile(temp));//裁剪图片
				send = true;
			}

			break;
		case 3:
			if (data != null) {
				Bundle extras = data.getExtras();
				image = extras.getParcelable("data");
				if(image!=null){
					/**
					 * 上传服务器代码
					 */
					setPicToView(image);//保存在SD卡中
					posting_image.setImageBitmap(image);//用ImageView显示出来
				}
				send = true;
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
		 String sdStatus = Environment.getExternalStorageState();  
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { 
               return;  
           }
		FileOutputStream b = null;
		//File file = new File(path);
		//shareimage = new File(Environment.getExternalStorageDirectory(),"postimage.jpg");
		shareimage = new File(path);
		shareimage.mkdirs();// 创建文件夹
		String fileName =path + "postimage.jpg";//图片名字
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
	private void BindData()
	{
		final ImageView qq_platform = (ImageView) getWindow().getDecorView().findViewById(R.id.qq_platform);
		final ImageView webchat_platform = (ImageView) getWindow().getDecorView().findViewById(R.id.webchat_platform);
		final ImageView removeIcon = (ImageView) getWindow().getDecorView().findViewById(R.id.RemoveIcon);
		final EditText messageText = (EditText) getWindow().getDecorView().findViewById(R.id.messageText);
		TextView sendButton = (TextView) getWindow().getDecorView().findViewById(R.id.submit_button);
		/*if(image != null){
			posting_image.setImageBitmap(image);
		}*/
		//shareimage = new File(Environment.getExternalStorageDirectory(),"postimage.jpg");
		removeIcon.setOnClickListener(new View.OnClickListener() {
			@Override
		    public void onClick(View v) {
				posting_image.setImageDrawable(null);
				send = false;
		    }
		});;
		qq_platform.setOnClickListener(new View.OnClickListener() {
			@Override
		    public void onClick(View v) {
		    	if (qqOnclick == true){
		    		qq_platform.setBackground(getResources().getDrawable(R.drawable.friendsg));
		    		qqOnclick = false;
		    	}
		    	else{
		    		qq_platform.setBackground(getResources().getDrawable(R.drawable.friends));
		    		qqOnclick = true;
		    	}
		    }
		});;
		webchat_platform.setOnClickListener(new View.OnClickListener() {
			@Override
		    public void onClick(View v) {
		    	if (webchatOnclick == true){
		    		webchat_platform.setBackground(getResources().getDrawable(R.drawable.wxg));
		    		webchatOnclick = false;
		    	}
		    	else{
		    		webchat_platform.setBackground(getResources().getDrawable(R.drawable.wx));
		    		webchatOnclick = true;
		    	}
		    }
		});;

		sendButton.setOnClickListener(new View.OnClickListener() {
				@Override
			    public void onClick(View v) {
					if(send == true && (webchatOnclick == true || qqOnclick == true)){
						InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
						inputMethodManager.hideSoftInputFromWindow(((Activity) mcontext).getCurrentFocus().getWindowToken(), 
						InputMethodManager.HIDE_NOT_ALWAYS);
						
						mController.setShareContent(getString(R.string.mcontrollerContext));
						mController.setShareMedia(new UMImage(mcontext, R.drawable.tomorelogo));
						
						String appID = "wxc9197d3be76aca03";
						String appSecret = "9c253edcab52fdb8458c99ec798c3c91";
						
						shareimage = new File(Environment.getExternalStorageDirectory(),"postimage.jpg");
						UMWXHandler wxHandler = new UMWXHandler(mcontext,appID,appSecret);
						wxHandler.addToSocialSDK();
						WeiXinShareContent weixinContent = new WeiXinShareContent();
						weixinContent.setShareContent(messageText.getText().toString());
						weixinContent.setTitle("");
						//weixinContent.setTargetUrl("http://tomoreapp.com/");
						weixinContent.setShareImage(new UMImage(mcontext, shareimage));
						UMWXHandler wxCircleHandler = new UMWXHandler(mcontext, appID, appSecret);
						CircleShareContent circleMedia = new CircleShareContent();
						circleMedia.setShareImage(new UMImage(mcontext, shareimage));
						//circleMedia.setTargetUrl("http://tomoreapp.com/");
						circleMedia.setShareContent(messageText.getText().toString());
						mController.setShareMedia(circleMedia);
						mController.setShareMedia(weixinContent);
						wxCircleHandler.setToCircle(true);
						wxCircleHandler.addToSocialSDK();
						mController.getConfig().removePlatform( SHARE_MEDIA.QQ,SHARE_MEDIA.SINA,SHARE_MEDIA.QZONE,SHARE_MEDIA.TENCENT);
						mController.openShare((Activity) mcontext, false);
					}
			}
		});
	}
	
	private class Post extends AsyncTask<String, String, String> {
		// private Context mContext;
		private int mType;
		private String Content;
		private int memberID;
		private int ImageWidth;
		private int ImageHeight;
		private Post(Context context, int type,String Content,int memberID,int Imagewidth,int Imageheight) {
			// this.mContext = context;
			this.mType = type;
			this.Content = Content;
			this.memberID = memberID;
			this.ImageWidth = Imagewidth;
			this.ImageHeight = Imageheight;
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
			ThreadsRequest request = new ThreadsRequest(MyCameraActivity.this); // BLRequest
			try {
				Log.d("doInBackground", "start request");
				result = request.PostThread("title", Content, memberID, ImageWidth, ImageHeight);
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
				
			}
		}
	}
}
