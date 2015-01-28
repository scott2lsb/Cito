package com.app.tomore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MyCameraActivity extends Activity implements OnClickListener {
	private ImageView posting_image;
	private Bitmap image;
	private static String path="/sdcard/postingimage/";//sd路径
	private View layout;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
						"postimage.jpg")));
		startActivityForResult(intent1, 2);//采用ForResult打
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
			posting_image.setImageDrawable(drawable);
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
				image = extras.getParcelable("data");
				if(image!=null){
					/**
					 * 上传服务器代码
					 */
					setPicToView(image);//保存在SD卡中
					posting_image.setImageBitmap(image);//用ImageView显示出来
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
}
