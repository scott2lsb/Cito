package com.app.tomore.ui.usercenter;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.app.tomore.R;
import com.app.tomore.beans.UserModel;
import com.app.tomore.net.UserCenterParse;
import com.app.tomore.net.UserCenterRequest;
import com.app.tomore.ui.threads.DialogActivity;
import com.app.tomore.utils.SpUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class FindPasswordActivity extends Activity{
	
	private DialogActivity dialog;
	EditText etFindPasswordEmail;
	String email;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_password);
		etFindPasswordEmail = (EditText) findViewById(R.id.etFindPasswordEmail);
	}
	
	public void onBackLoginClick(View view){				
    	Intent intent = new Intent(FindPasswordActivity.this, LoginActivity.class);
		startActivity(intent);   		
	}
	
	public void onFindPasswordClick(View view){	
		email = etFindPasswordEmail.getText().toString();
		if(email.equals("")){
			Toast.makeText(getApplicationContext(), "请输入邮箱",
					Toast.LENGTH_SHORT).show();
		}else{
			new FindPassword(FindPasswordActivity.this, 1).execute("");
		}
	}
	
	private class FindPassword extends AsyncTask<String, String, String> {
		private int mType;

		private FindPassword(Context context, int type) {
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
					FindPasswordActivity.this);
			try {
				result = request.findPasswordRequest(email);
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
			if (result == null || result.equals("") || result.equals("\"email not exists\"")) {
		    	Toast.makeText(getApplicationContext(), "请输入有效的邮箱",
						Toast.LENGTH_SHORT).show();
			}else{
				UserCenterParse ucParse = new UserCenterParse();
				boolean usermodel = ucParse.parseFindPasswordResponse(result);
				if(usermodel == true){
					Toast.makeText(getApplicationContext(), "请查看邮箱",
							Toast.LENGTH_SHORT).show();					
				}
				else{
					Toast.makeText(getApplicationContext(), "请输入有效的邮箱",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
}
