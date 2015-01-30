﻿package com.app.tomore.ui.mag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import com.app.tomore.R.drawable;
import com.app.tomore.R.id;
import com.app.tomore.R.layout;
import com.app.tomore.beans.ArticleCommentModel;
import com.app.tomore.beans.ArticleModel;
import com.app.tomore.beans.CommonModel;
import com.app.tomore.beans.GeneralBLModel;
import com.app.tomore.net.MagParse;
import com.app.tomore.net.MagRequest;
import com.app.tomore.net.ToMoreParse;
import com.app.tomore.net.YellowPageParse;
import com.google.gson.JsonSyntaxException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.app.tomore.R;
import com.app.tomore.ui.threads.DialogActivity;
import com.app.tomore.utils.AppUtil;
import com.app.tomore.utils.SpUtils;
import com.app.tomore.utils.ToastUtils;
import com.app.tomore.utils.PullToRefreshListView;
import com.app.tomore.utils.PullToRefreshBase;
import com.app.tomore.utils.PullToRefreshBase.OnLastItemVisibleListener;
import com.app.tomore.utils.PullToRefreshBase.OnRefreshListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MagCommentActivity extends Activity {
	private DialogActivity dialog;
	private ArrayList<ArticleCommentModel> articleComment;
	private ArticleCommentModel articleComentModel;
	private DisplayImageOptions otp;
	private PullToRefreshListView mListView;
	private Activity mContext;
	private TextView noneData;
	private View no_net_lay;
	private Button submit;
	private EditText content;
	ArticleAdapter articleListAdapter;
	private boolean onRefresh = false;
	private boolean headerRefresh = false;
	private String articleId;
	private String memberId = "34";
	private int page;
	private int limit;

	private String finalResult = null;
	private String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mag_comment_listview);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		getWindow().getDecorView().setBackgroundColor(Color.WHITE);

		otp = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).showImageForEmptyUri(R.drawable.ic_launcher)
				.build();
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(this));

		mListView = (PullToRefreshListView) findViewById(R.id.mag_comment_listviews);
		mListView.setOnRefreshListener(onRefreshListener);
		noneData = (TextView) findViewById(R.id.noneData);
		no_net_lay = findViewById(R.id.no_net_lay);
		submit = (Button) findViewById(R.id.commentSubmit);
		content = (EditText) findViewById(R.id.commentContent);
		content.setText("");

		Intent intent = getIntent();
		articleId = intent.getStringExtra("articleid");

		mContext = this;
		page = 1;
		limit = 10;

		RelativeLayout rl = (RelativeLayout) getWindow().getDecorView()
				.findViewById(R.id.bar_title_commentlistbar);
		final Button btnBack = (Button) rl
				.findViewById(R.id.bar_title_bt_detail);

		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View viewIn) {
				Intent intent = new Intent();
				userId = SpUtils.getUserId(MagCommentActivity.this);
				if (content.getText().toString() == null
						|| content.getText().toString().length() == 0) {
					Toast.makeText(getApplicationContext(), "请输入内容",
							Toast.LENGTH_SHORT).show();
				} else {
					if (userId != null) {
						new GetData1(MagCommentActivity.this, 1).execute("");
						// content.setText("");
						intent = new Intent(MagCommentActivity.this,
								MagCommentActivity.class);
						intent.putExtra("articleid", articleId);
						finish();

						startActivity(intent);
					} else {
						AppUtil.startLoginPage(mContext);
						// articleListAdapter = new ArticleAdapter();
						// articleListAdapter.notifyDataSetChanged();
						// new GetData(MagCommentActivity.this, 1).execute("");
						// intent.setClass(MagCommentActivity.this,
						// LoginActivity.class);
						// startActivity(intent);
					}
				}

			}
		});

		// mListView.setAdapter(articleListAdapter);
		headerRefresh = true;
		new GetData(MagCommentActivity.this, 1).execute("");
	}

	private void BindDataToListView() {
		if (onRefresh) {
			onRefresh = false;
		}
		if (articleListAdapter == null) {
			articleListAdapter = new ArticleAdapter();
			mListView.setAdapter(articleListAdapter);
		} else {
			articleListAdapter.notifyDataSetChanged();
		}
		if (articleComment != null && articleComment.size() > 0) {
			showDataUi();
		} else {
			showNoDataUi();
		}
	}

	void showDataUi() {
		mListView.setVisibility(View.VISIBLE);
		noneData.setVisibility(View.GONE);
		no_net_lay.setVisibility(View.GONE);
	}

	void showNoDataUi() {
		mListView.setVisibility(View.GONE);
		noneData.setVisibility(View.VISIBLE);
		no_net_lay.setVisibility(View.GONE);
	}

	protected void showNoNetUi() {
		no_net_lay.setVisibility(View.VISIBLE);
		noneData.setVisibility(View.GONE);
		mListView.setVisibility(View.GONE);
	}

	private class GetData extends AsyncTask<String, String, String> {
		private int mType;

		private GetData(Context context, int type) {
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

		protected String doInBackground(String... params) {
			String result = null;
			MagRequest request = new MagRequest(MagCommentActivity.this);
			try {

				Log.d("doInBackground", "start request");
				result = request.getCommentByArticleId(articleId,
						Integer.toString(page), Integer.toString(limit));
				Log.d("doInBackground", "returned");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}

			return result;
		}

		protected void onPostExecute(String result) {
			if (null != dialog) {
				dialog.dismiss();
			}
			mListView.onRefreshComplete();
			Log.d("onPostExecute", "postExec state");
			if (result == null || result.equals("")) {
				ToastUtils.showToast(mContext, "列表为空");
			} else {

				if (articleComment != null && articleComment.size() != 0) {
					if (headerRefresh)
						articleComment = new ArrayList<ArticleCommentModel>();
				} else
					articleComment = new ArrayList<ArticleCommentModel>();
				try {
					if (headerRefresh)
						articleComment = new MagParse()
								.parseArticleComment(result);
					else {
						articleComment.addAll(new MagParse()
								.parseArticleComment(result));
					}

					BindDataToListView();

				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
			}
		}

	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			mListView.onRefreshComplete();
		}
	};

	public OnRefreshListener<ListView> onRefreshListener = new OnRefreshListener<ListView>() {
		@Override
		public void onRefresh(PullToRefreshBase<ListView> refreshView) {
			if (AppUtil.networkAvailable(mContext)) {
				headerRefresh = true;
				onRefresh = true;
				page = 1;
				new GetData(MagCommentActivity.this, 1).execute("");
			} else {
				ToastUtils.showToast(mContext, "没有网络");
				mListView.onRefreshComplete();
			}
		}

	};

	class ViewHolder {
		TextView textViewTitle;
		TextView textViewComment;
		TextView TimeDiff;
		ImageView imageView;
	}

	private class ArticleAdapter extends BaseAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = new ViewHolder();
			articleComentModel = (ArticleCommentModel) getItem(position);
			final String speakerName = articleComentModel.getAccountName();
			final String content = articleComentModel.getCommentContent();
			final String time = articleComentModel.getTimeDiff();
			final String imageUrl = articleComentModel.getMemberImage();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.comment_list_item, null);
			viewHolder.textViewTitle = (TextView) convertView
					.findViewById(R.id.speakerName);
			viewHolder.textViewTitle.setText(speakerName);

			viewHolder.textViewComment = (TextView) convertView
					.findViewById(R.id.content);
			viewHolder.textViewComment.setText(content);

			viewHolder.TimeDiff = (TextView) convertView
					.findViewById(R.id.time);
			viewHolder.TimeDiff.setText(time);

			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.memberImage);

			ImageLoader.getInstance().displayImage(imageUrl,
					viewHolder.imageView, otp);

			return convertView;
		}

		public void refresh(ArrayList<ArticleCommentModel> list) {
			articleComment = list;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return articleComment.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return articleComment.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	private class GetData1 extends AsyncTask<String, String, String> {
		// private Context mContext;
		private int mType;

		private GetData1(Context context, int type) {
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
			MagRequest request = new MagRequest(MagCommentActivity.this);
			try {
				Log.d("doInBackground", "start request");
				result = request.PostCommentByMemberId(articleId, userId,
						content.getText().toString());

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

				CommonModel returnResult = new CommonModel();
				try {
					ToMoreParse getrequest = new ToMoreParse();
					returnResult = getrequest.CommonPares(result);
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
				finalResult = returnResult.getResult();
				if (finalResult.equals("succ")) {
					Toast.makeText(getApplicationContext(), "发送成功",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "请重新发送",
							Toast.LENGTH_SHORT).show();
				}

				if (articleComment != null && articleComment.size() != 0) {
					if (headerRefresh)
						articleComment = new ArrayList<ArticleCommentModel>();
				} else {
					articleComment = new ArrayList<ArticleCommentModel>();
					try {
						if (headerRefresh)
							articleComment = new MagParse()
									.parseArticleComment(result);
						else {
							articleComment.addAll(new MagParse()
									.parseArticleComment(result));
						}
						// new GetData1(MagCommentActivity.this, 1).execute("");
						BindDataToListView();
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
					}

				}
			}
		}

	}
}
