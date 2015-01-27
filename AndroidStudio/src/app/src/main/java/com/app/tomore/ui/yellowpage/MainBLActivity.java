package com.app.tomore.ui.yellowpage;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.app.tomore.MyCameraActivity;
import com.app.tomore.R;
import com.app.tomore.ViewCache;
import com.app.tomore.net.YellowPageParse;
import com.app.tomore.net.YellowPageRequest;
import com.app.tomore.ui.threads.DialogActivity;
import com.app.tomore.ui.yellowpage.GeneralBLActivity.ViewHolder;

import java.util.concurrent.TimeoutException;

import com.google.gson.JsonSyntaxException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.app.tomore.R.id;
import com.app.tomore.R.layout;
import com.app.tomore.beans.CategoryModel;
import com.app.tomore.beans.GeneralBLModel;

public class MainBLActivity extends Activity {
	private DialogActivity dialog;
	private ArrayList<CategoryModel> cateList;
	private TextView league;
	private View layout;
	private GridView gridView;
	private DisplayImageOptions otp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_bianli_activity);
		getWindow().getDecorView().setBackgroundColor(Color.WHITE);
		new GetData(MainBLActivity.this, 1).execute("");
		league = (TextView) findViewById(R.id.join_league);
		league.setVisibility(View.VISIBLE);
		gridView = (GridView) findViewById(R.id.gridView);
		otp = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).showImageForEmptyUri(R.drawable.ic_launcher)
				.build();
		
		league.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View viewIn) {
				Intent League_intent;
				League_intent = new Intent(MainBLActivity.this,
						LeagueActivity.class);
				startActivityForResult(League_intent, 100);
			}
		});
		layout = findViewById(R.id.mainbllayout);
		final Button btnBack = (Button) layout
				.findViewById(R.id.bar_title_bl_go_back);
		btnBack.setVisibility(View.INVISIBLE);

	}
	

	class ViewHolder {
		private TextView textView;
		private ImageView ImageView;
	}
	
	public class MainBLAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return cateList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return cateList.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			final CategoryModel item = (CategoryModel) getItem(position);
			ViewHolder viewHolder = null;
			if (convertView != null) {
				viewHolder = (ViewHolder) convertView.getTag();
			} else {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(MainBLActivity.this).inflate(
						R.layout.bianlidatalayout, null);
				
				viewHolder.textView = (TextView) convertView.findViewById(R.id.ItemText);
				viewHolder.ImageView = (ImageView) convertView.findViewById(R.id.ItemImage);
				
				convertView.setTag(viewHolder);
			}
			
			ImageLoader.getInstance().displayImage(item.getImage(),
					viewHolder.ImageView, otp);
			
			viewHolder.textView.setText(item.getName());
			return convertView;
		}
	}


	private void BindDataToGridView() {
		gridView.setAdapter(new MainBLAdapter());
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CategoryModel item = cateList.get(position);
				Open_Activity(item.getIconID(),
						Integer.parseInt(item.getType()), item.getName());
			}
		});
	}

	private void Open_Activity(String BLID, int BLType, String name) {
		Intent intent;
		if (BLType == 1) {
			intent = new Intent(MainBLActivity.this, GeneralBLActivity.class);
			intent.putExtra("BLID", BLID);
			intent.putExtra("name", name);
			startActivityForResult(intent, 100);
		} else if (BLType == 0) {
			intent = new Intent(MainBLActivity.this, RestaurantBLActivity.class);
			startActivityForResult(intent, 100);
		} else if (BLType == 2) {
			intent = new Intent(MainBLActivity.this, OrderActivity.class);
			intent.putExtra("BLID", BLID);
			startActivityForResult(intent, 100);
		} else if (BLType == 3) {
			intent = new Intent(MainBLActivity.this, WebViewActivity.class);
			intent.putExtra("URL", "http://m.dianping.com/shop/18766729");
			intent.putExtra("name", name);
			startActivityForResult(intent, 100);
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
			YellowPageRequest request = new YellowPageRequest(
					MainBLActivity.this);
			try {
				Log.d("doInBackground", "start request");
				result = request.getAllBLCategories();
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
				cateList = new ArrayList<CategoryModel>();
				try {
					cateList = new YellowPageParse()
							.parseCtegoryResponse(result);
					BindDataToGridView();
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
				if (cateList != null) {
					Intent intent = new Intent(MainBLActivity.this,
							MyCameraActivity.class); // fake redirect..
					intent.putExtra("menuList", (Serializable) cateList);
					// startActivity(intent);
				} else {
					// show empty alert
				}
			}
		}
	}
}
