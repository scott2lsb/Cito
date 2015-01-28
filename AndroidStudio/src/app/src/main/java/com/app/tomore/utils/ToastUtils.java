package com.app.tomore.utils;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

/**

 * @author AlexHuang
 *
 */
public class ToastUtils {

	private static Toast toast = null;
	
	public static void showToast(Context context, String msg){
		if(toast == null){
			toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		}
		toast.setText(msg);
		toast.show();
	}
}
