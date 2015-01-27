package com.app.tomore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;


public class MyCameraActivity extends Activity {
	private Button btn_camera_capture = null;
	private Button btn_camera_cancel = null;
	private Button btn_camera_ok = null;
	private ImageButton albums=null;
	
	private Camera camera = null;
	private MySurfaceView mySurfaceView = null;
	
	private byte[] buffer = null;
	
	private final int TYPE_FILE_IMAGE = 1;
	private final int TYPE_FILE_VEDIO = 2;
	
	private PictureCallback pictureCallback = new PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			if (data == null){
				Log.i("MyPicture", "picture taken data: null");
			}else{
				Log.i("MyPicture", "picture taken data: " + data.length);
			}
			
			buffer = new byte[data.length];
			buffer = data.clone();
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycamera_layout);
		
		btn_camera_capture = (Button) findViewById(R.id.camera_capture);

		albums = (ImageButton) findViewById(R.id.albums);
		
		btn_camera_capture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				camera.takePicture(null, null, pictureCallback);
				
				btn_camera_capture.setVisibility(View.INVISIBLE);
				saveImageToFile();

			}
		});
		
		albums.setOnClickListener(new Button.OnClickListener() { //��׼ȷ��Ӧ����View.OnClickListener
		    public void onClick(View v)
		    {
		        /* �½�һ��Intent���� */
		        Intent intent = new Intent();
		        //intent.putExtra("name","LeiPei");    
		        /* ָ��intentҪ�������� */
		        intent.setClass(MyCameraActivity.this, ViewAlbums.class);
		        /* ����һ���µ�Activity */
		        MyCameraActivity.this.startActivity(intent);
		        /* �رյ�ǰ��Activity */
		        MyCameraActivity.this.finish();
		    }
		});

	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		camera.release();
		camera = null;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (camera == null){
			camera = getCameraInstance();
		}
		//�������onResume�У���Ȼ�����Home��֮���ٻص���APP������
		mySurfaceView = new MySurfaceView(getApplicationContext(), camera);
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mySurfaceView);
	}
	
	/*�õ�һ�������*/
	private Camera getCameraInstance(){
		Camera camera = null;
		try{
			camera = camera.open();
		}catch(Exception e){
			e.printStackTrace();
		}
		return camera;
	}
	
	
	//-----------------------����ͼƬ---------------------------------------
	private void saveImageToFile(){
		File file = getOutFile(TYPE_FILE_IMAGE);
		if (file == null){
			Toast.makeText(getApplicationContext(), "�ļ�����ʧ��,����SD����дȨ��", Toast.LENGTH_SHORT).show();
			return ;
		}
		Log.i("MyPicture", "�Զ������ͼƬ·��:" + file.getPath());
		Toast.makeText(getApplicationContext(), "ͼƬ����·����" + file.getPath(), Toast.LENGTH_SHORT).show();
		if (buffer == null){
			Log.i("MyPicture", "�Զ������Buffer: null");
		}else{
			try{
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(buffer);
				fos.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	//-----------------------����Uri---------------------------------------
	//�õ�����ļ���URI
	private Uri getOutFileUri(int fileType) {
		return Uri.fromFile(getOutFile(fileType));
	}
	
	//��������ļ�
	private File getOutFile(int fileType) {
		
		String storageState = Environment.getExternalStorageState();
		if (Environment.MEDIA_REMOVED.equals(storageState)){
			Toast.makeText(getApplicationContext(), "oh,no, SD��������", Toast.LENGTH_SHORT).show();
			return null;
		}
		
		File mediaStorageDir = new File (Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
				,"MyPictures");
		if (!mediaStorageDir.exists()){
			if (!mediaStorageDir.mkdirs()){
				Log.i("MyPictures", "����ͼƬ�洢·��Ŀ¼ʧ��");
				Log.i("MyPictures", "mediaStorageDir : " + mediaStorageDir.getPath());
				return null;
			}
		}
		
		File file = new File(getFilePath(mediaStorageDir,fileType));
		
		return file;
	}
	//��������ļ�·��
	private String getFilePath(File mediaStorageDir, int fileType){
		String timeStamp =new SimpleDateFormat("yyyyMMdd_HHmmss")
							.format(new Date());
		String filePath = mediaStorageDir.getPath() + File.separator;
		if (fileType == TYPE_FILE_IMAGE){
			filePath += ("IMG_" + timeStamp + ".jpg");
		}else if (fileType == TYPE_FILE_VEDIO){
			filePath += ("VIDEO_" + timeStamp + ".mp4");
		}else{
			return null;
		}
		return filePath;
	}
	
	
}
