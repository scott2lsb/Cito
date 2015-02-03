package com.app.tomore;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.app.tomore.beans.UserModel;
import com.app.tomore.net.ThreadsRequest;
import com.app.tomore.net.ToMoreParse;
import com.app.tomore.ui.threads.DialogActivity;
import com.app.tomore.utils.SpUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
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
import android.widget.Toast;


public class MyCameraActivity extends Activity implements OnClickListener {
	private ImageView posting_image;
	private Bitmap image;
	private static String path="/sdcard/postingimage/";//sd路径
	private View layout;
	private boolean webchatOnclick = false;
	private boolean qqOnclick = false;
	private Context mcontext;
	private boolean send = false;
	private String finalResult = null;
	final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	private DialogActivity dialog;
	private File shareimage;;
	private UserModel usermodel;
	private String memberid;
		
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.posting_activity_layout);
		usermodel=SpUtils.getUserInformation(MyCameraActivity.this);
		mcontext = this;
		posting_image = (ImageView) findViewById(R.id.posting_image);
		layout = findViewById(R.id.posting_layout);
		posting_image.setOnClickListener(this);
		final Button btnBack = (Button) layout.findViewById(R.id.bar_title_league_go_back);
		TextView title = (TextView) layout.findViewById(R.id.btLeague);
		title.setText(getString(R.string.posting));
		TextView send = (TextView) layout.findViewById(R.id.submit_button);
		send.setText(getString(R.string.sendthread));
		btnBack.setVisibility(View.GONE);
		try{
			memberid = usermodel.getMemberID();
        
			Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
						"temporary_holder.jpg")));
			startActivityForResult(intent1, 2);//采用ForResult打*/
			getWindow().getDecorView().setBackgroundColor(Color.WHITE);
			BindData();
		}
		catch(NullPointerException e)
        {
			memberid = "0";
			Toast.makeText(mcontext, getString(R.string.MustLogin), Toast.LENGTH_SHORT).show();
			TextView sendButton = (TextView) getWindow().getDecorView().findViewById(R.id.submit_button);
			sendButton.setOnClickListener(new View.OnClickListener() {
				@Override
			    public void onClick(View v) {
					Toast.makeText(mcontext, getString(R.string.MustLogin), Toast.LENGTH_SHORT).show();
					}
				});
        }
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
		builder.setTitle(getString(R.string.GetPhoto));
		builder.setItems(options, new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface Optiondialog, int which) {
		        if (which == 0){
					Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
									"temporary_holder.jpg")));
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
						+ "/temporary_holder.jpg");
				cropPhoto(Uri.fromFile(temp));//裁剪图片
				send = true;
			}

			break;
		case 3:
			if (data != null) {
				String filePath = Environment.getExternalStorageDirectory()
	                    + "/temporary_holder.jpg";
				image = BitmapFactory.decodeFile(filePath);
				posting_image.setImageBitmap(image);
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
		intent.putExtra("outputX", 600);
		intent.putExtra("outputY", 600);
		intent.putExtra("return-data", true);
		File f = new File(Environment.getExternalStorageDirectory(),
		        "/temporary_holder.jpg");

		uri = Uri.fromFile(f);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

		startActivityForResult(intent, 3);
	}

	private void BindData()
	{
		final ImageView qq_platform = (ImageView) getWindow().getDecorView().findViewById(R.id.qq_platform);
		final ImageView webchat_platform = (ImageView) getWindow().getDecorView().findViewById(R.id.webchat_platform);
		final ImageView removeIcon = (ImageView) getWindow().getDecorView().findViewById(R.id.RemoveIcon);
		final EditText messageText = (EditText) getWindow().getDecorView().findViewById(R.id.messageText);
		TextView posting_title = (TextView) getWindow().getDecorView().findViewById(R.id.posting_title);
		TextView posting_sub_title = (TextView) getWindow().getDecorView().findViewById(R.id.posting_sub_title);
		TextView sendButton = (TextView) getWindow().getDecorView().findViewById(R.id.submit_button);
		posting_title.setText(usermodel.getAccountName());
		posting_sub_title.setText(usermodel.getSchool());
		ImageView head_view = (ImageView) getWindow().getDecorView().findViewById(R.id.head_view );
		ImageLoader.getInstance().displayImage(usermodel.getProfileImage(),
				 head_view);
		
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
					if(Integer.parseInt(memberid) != 0){
						if(send == true && (webchatOnclick == true || qqOnclick == true)){
							//image = ((BitmapDrawable)posting_image.getDrawable()).getBitmap();
							InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
							inputMethodManager.hideSoftInputFromWindow(((Activity) mcontext).getCurrentFocus().getWindowToken(), 
							InputMethodManager.HIDE_NOT_ALWAYS);
							
							mController.setShareContent(getString(R.string.mcontrollerContext));
							mController.setShareMedia(new UMImage(mcontext, R.drawable.tomorelogo));
							
							String appID = "wxc9197d3be76aca03";
							String appSecret = "9c253edcab52fdb8458c99ec798c3c91";
							
							shareimage = new File(Environment.getExternalStorageDirectory(),"postimage.jpg");
							
							new Post(mcontext,1,messageText.getText().toString(),Integer.parseInt(memberid),image.getWidth(),image.getHeight()).execute("");;
							
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
						else if(send == true){
							//image = ((BitmapDrawable)posting_image.getDrawable()).getBitmap();
							new Post(mcontext,1,messageText.getText().toString(),Integer.parseInt(memberid),image.getWidth(),image.getHeight()).execute("");;
						}
						else{
							Toast.makeText(mcontext, getString(R.string.imageCannotBeEmpty), Toast.LENGTH_SHORT).show();
						}
					}
					else{
						Toast.makeText(mcontext, getString(R.string.MustLogin), Toast.LENGTH_SHORT).show();
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
			ThreadsRequest request = new ThreadsRequest(MyCameraActivity.this);
			try {
				result = request.PostThread(image,"",Content,memberID,ImageWidth,ImageHeight);
				ToMoreParse toMoreParse = new ToMoreParse();
				result = toMoreParse.CommonPares(result).getResult();
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
				Log.d("onPostExecute", "postExec state");
	        	if(result.equals("succ"))
	        	{
	        		Toast.makeText(mcontext, getString(R.string.successMessage), Toast.LENGTH_SHORT).show();
	
	        	}
	        	else if(result.equals("upload image fail"))
	        	{
	        		Toast.makeText(mcontext, getString(R.string.failtosend), Toast.LENGTH_SHORT).show();
	        	}
			}
		}
	}

